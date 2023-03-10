package cherhy.soloProject.application.controller;

import cherhy.soloProject.application.usecase.MemberSearchUsecase;
import cherhy.soloProject.application.utilService.SessionReadService;
import cherhy.soloProject.domain.member.dto.response.MemberSearchResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Set;

@Tag(name = "회원 검색")
@RestController
@RequiredArgsConstructor
@RequestMapping("/memberSearch")
public class MemberSearchController {
    private final HttpSession session;
    private final MemberSearchUsecase memberSearchUsecase;
    private final SessionReadService sessionReadService;


    @Operation(summary = "회원 검색")
    @GetMapping("/{name}")
    public List<MemberSearchResponseDto> modifyMember(@PathVariable @Valid @NotBlank(message = "검색어를 입력해주세요") String name) {
        Long memberId = sessionReadService.getUserData(session);
        return memberSearchUsecase.searchMember(name, memberId);
    }

    @Operation(summary = "회원 검색 기록")
    @GetMapping("/log")
    public Set<String> memberHistoryLog() {
        Long memberId = sessionReadService.getUserData(session);
        return memberSearchUsecase.getSearchHistoryLog(memberId);
    }

    @Operation(summary = "검색에서 타자 칠 때 검색어로 시작하는 검색 순위가 가장 높은 데이터 5개 Get")
    @GetMapping("/searchRank")
    public Set<String> highScoreSearchWord(String searchName) {
        return memberSearchUsecase.getHighScoreSearchWord(searchName);
    }

}
