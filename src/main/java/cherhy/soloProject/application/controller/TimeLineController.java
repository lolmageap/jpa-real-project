package cherhy.soloProject.application.controller;

import cherhy.soloProject.Util.scrollDto.ScrollRequest;
import cherhy.soloProject.Util.scrollDto.ScrollResponse;
import cherhy.soloProject.application.usecase.MemberTimeLineUseCase;
import cherhy.soloProject.application.utilService.SessionReadService;
import cherhy.soloProject.domain.post.dto.PostPhotoDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@Tag(name = "타임라인")
@RestController
@RequiredArgsConstructor
@RequestMapping("/timeLine")
public class TimeLineController {
    private final HttpSession session;
    private final SessionReadService sessionReadService;
    private final MemberTimeLineUseCase memberPostTimeLineUseCase;

    @Operation(summary = "타임라인 조회 // only RDBMS")
    @GetMapping
    public ScrollResponse<PostPhotoDto> getTimeLine(ScrollRequest scrollRequest){
        Long memberId = sessionReadService.getUserData(session);
        return memberPostTimeLineUseCase.getTimeLine(memberId, scrollRequest);
    }

}
