package cherhy.soloProject.application.usecase;

import cherhy.soloProject.Util.scrollDto.ScrollResponse;
import cherhy.soloProject.Util.scrollDto.ScrollRequest;
import cherhy.soloProject.domain.follow.dto.response.ResponseFollowMemberDto;
import cherhy.soloProject.domain.follow.entity.Follow;
import cherhy.soloProject.domain.follow.service.FollowReadService;
import cherhy.soloProject.domain.follow.service.FollowWriteService;
import cherhy.soloProject.domain.member.entity.Member;
import cherhy.soloProject.domain.member.service.MemberReadService;
import cherhy.soloProject.domain.memberBlock.service.MemberBlockReadService;
import cherhy.soloProject.domain.memberBlock.service.MemberBlockWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@Transactional
@RequiredArgsConstructor
public class MemberFollowUseCase {

    private final MemberReadService memberReadService;
    private final FollowWriteService followWriteService;
    private final FollowReadService followReadService;
    private final MemberBlockReadService memberBlockReadService;
    private final MemberBlockWriteService memberBlockWriteService;

    public ResponseEntity followMember(Long memberId, Long FollowingId){
        memberReadService.SameUserCheck(memberId, FollowingId);
        Member myMember = memberReadService.getMember(memberId);
        Member followMember = memberReadService.getMember(FollowingId);
        Optional<Follow> follow = followReadService.getFollowExist(myMember, followMember);

        follow.ifPresentOrElse(f -> followWriteService.unfollow(f),
                () -> { memberBlockReadService.getBlockMember(myMember,followMember)
                            .ifPresent(m -> memberBlockWriteService.unblock(m));
                        followWriteService.follow(myMember, followMember);
        });

        return ResponseEntity.ok(200);
    }

    public ScrollResponse<ResponseFollowMemberDto> followList(Long id, ScrollRequest scrollRequest) {
        Member member = memberReadService.getMember(id);
        List<ResponseFollowMemberDto> getFollowing = followReadService.getFollowing(scrollRequest, member);
        return followReadService.getResponseFollowerMemberDtoScroll(getFollowing, scrollRequest);
    }

    public ScrollResponse<ResponseFollowMemberDto> followerList(Long id, ScrollRequest scrollRequest) {
        Member member = memberReadService.getMember(id);
        List<ResponseFollowMemberDto> getFollow = followReadService.getFollower(scrollRequest, member);
        return followReadService.getResponseFollowerMemberDtoScroll(getFollow, scrollRequest);
    }

}
