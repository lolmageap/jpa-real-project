package cherhy.soloProject.application.controller;

import cherhy.soloProject.Util.scrollDto.ScrollRequest;
import cherhy.soloProject.Util.scrollDto.ScrollResponse;
import cherhy.soloProject.application.usecase.MemberPostReplyUseCase;
import cherhy.soloProject.application.utilService.SessionReadService;
import cherhy.soloProject.domain.reply.dto.RequestReplyDto;
import cherhy.soloProject.domain.reply.dto.response.ResponseReplyDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Tag(name = "댓글")
@RestController
@RequiredArgsConstructor
@RequestMapping("/reply")
public class ReplyController {
    private final HttpSession session;
    private final MemberPostReplyUseCase memberPostReplyUseCase;
    private final SessionReadService sessionReadService;

    @PostMapping
    @Operation(summary = "댓글 등록")
    public ResponseEntity setReply(@RequestBody @Valid RequestReplyDto reply){
        Long memberId = sessionReadService.getUserData(session);
        return memberPostReplyUseCase.setReply(reply, memberId);
    }

    @GetMapping("/{postId}")
    @Operation(summary = "댓글 전체")
    public List<ResponseReplyDto> getReply(@PathVariable Long postId){
        return memberPostReplyUseCase.getReply(postId);
    }

    @GetMapping("/{postId}/scroll")
    @Operation(summary = "댓글 무한 스크롤 // 날짜순 정렬 to redis")
    public ScrollResponse<ResponseReplyDto> getReplyScrollInRedis(@PathVariable Long postId, ScrollRequest scrollRequest){
        return memberPostReplyUseCase.getReplyScrollInRedis(postId, scrollRequest);
    }


}
