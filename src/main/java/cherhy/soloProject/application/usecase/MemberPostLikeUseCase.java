package cherhy.soloProject.application.usecase;

import cherhy.soloProject.domain.member.entity.Member;
import cherhy.soloProject.domain.member.service.MemberReadService;
import cherhy.soloProject.domain.post.entity.Post;
import cherhy.soloProject.domain.post.service.PostReadService;
import cherhy.soloProject.domain.post.service.PostWriteService;
import cherhy.soloProject.domain.postLike.dto.PostLikeDto;
import cherhy.soloProject.domain.postLike.entity.PostLike;
import cherhy.soloProject.domain.postLike.service.PostLikeReadService;
import cherhy.soloProject.domain.postLike.service.PostLikeWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static cherhy.soloProject.application.key.RedisKey.POST_LIKE;


@Service
@Transactional
@RequiredArgsConstructor
public class MemberPostLikeUseCase {

    private final MemberReadService memberReadService;
    private final PostReadService postReadService;
    private final PostWriteService postWriteService;
    private final PostLikeWriteService postLikeWriteService;
    private final PostLikeReadService postLikeReadService;
    private final StringRedisTemplate redisTemplate;


    public ResponseEntity postExample(PostLikeDto postLikeDto){
        Post findPost = postReadService.getPost(postLikeDto.PostId());
        findPost.updatePostLikeCount(findPost.getLikeCount()+1);
        Post save = postWriteService.save(findPost);
        Integer likeCount = save.getLikeCount();
        return ResponseEntity.ok(200);
    }

    public ResponseEntity postLike(PostLikeDto postLikeDto){
        ValueOperations<String, String> ops = redisTemplate.opsForValue();

        Member findMember = memberReadService.getMember(postLikeDto.memberId());
        Post findPost = postReadService.getPost(postLikeDto.PostId());
        String formatPost = String.format(POST_LIKE.name() + findPost.getId()); //format ?????? ??????
        Optional<PostLike> postLike = postLikeReadService.getMemberIdAndPostId(findMember, findPost);
        postLikeWriteService.likeOrLikeCancel(ops, findMember, findPost, formatPost, postLike);

//        //??????????????? ????????? ?????? ??? ???????????? ?????? : ????????? ????????? ?????? ?????? ?????? ?????? (Bulk insert??? ??????)
//        String findPostLikeFromRedis = ops.get(formatPost);
//        String path = String.valueOf(post.getId());

        return ResponseEntity.ok(200);
    }

}
