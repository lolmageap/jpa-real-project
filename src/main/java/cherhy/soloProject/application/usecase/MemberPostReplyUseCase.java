package cherhy.soloProject.application.usecase;

import cherhy.soloProject.Util.scrollDto.ScrollResponse;
import cherhy.soloProject.Util.scrollDto.ScrollRequest;
import cherhy.soloProject.application.key.RedisKey;
import cherhy.soloProject.domain.member.entity.Member;
import cherhy.soloProject.domain.member.service.MemberReadService;
import cherhy.soloProject.domain.post.entity.Post;
import cherhy.soloProject.domain.post.service.PostReadService;
import cherhy.soloProject.domain.reply.dto.RequestReplyDto;
import cherhy.soloProject.domain.reply.dto.response.ResponseReplyDto;
import cherhy.soloProject.domain.reply.entity.Reply;
import cherhy.soloProject.domain.reply.service.ReplyReadService;
import cherhy.soloProject.domain.reply.service.ReplyWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static cherhy.soloProject.application.key.RedisKey.*;


@Service
@Transactional
@RequiredArgsConstructor
public class MemberPostReplyUseCase {

    private final MemberReadService memberReadService;
    private final PostReadService postReadService;
    private final ReplyReadService replyReadService;
    private final ReplyWriteService replyWriteService;
    private final StringRedisTemplate redisTemplate;

    public ResponseEntity setReply(RequestReplyDto reply, Long memberId){
        ZSetOperations zSetOps = redisTemplate.opsForZSet(); //Sorted Set 선언
        Member findMember = memberReadService.getMember(memberId);
        Post findPost = postReadService.getPost(reply.postId());
        Reply build = replyWriteService.buildReply(reply, findMember, findPost);
        Reply save = replyWriteService.save(build);
        replyWriteService.addRedis(zSetOps, findPost, save);
        return ResponseEntity.ok(200);
    }

    public List<ResponseReplyDto> getReply(Long postId) {
        Post findPost = postReadService.getPost(postId);
        List<Reply> replies = findPost.getReplies();
        return replyReadService.changeResponseReplyDto(postId, replies);
    }

    // 레디스를 사용한 무한 스크롤
    public ScrollResponse<ResponseReplyDto> getReplyScrollInRedis(Long postId, ScrollRequest scrollRequest) {
        ZSetOperations zSetOps = redisTemplate.opsForZSet();
        Post findPost = postReadService.getPost(postId);
        String postRedis = String.format(REPLY_MODIFY_DESC.name() + findPost.getId()); // redis key 값
        List<Long> sortedKeys = replyReadService.getSortedKeys(scrollRequest, zSetOps, postRedis); // scrollRequest 정렬 되어있는 키 가져오기
        Long getNextKey = replyReadService.NextKeyCheck(scrollRequest, sortedKeys); // 다음 키값을 가져옴
        List<Reply> repliesScroll = replyReadService.getPostIdScroll(postId, sortedKeys); // 정렬된 값을 기준으로 값 가져오기
        List<ResponseReplyDto> responseReplyDtos = replyReadService.changeResponseReplyDto(postId, repliesScroll); //dto로 변환
//        long getNextKey = nextKey(scrollRequest, repliesScroll);
        return new ScrollResponse<>(scrollRequest.next(getNextKey),responseReplyDtos);
    }

}
