package cherhy.soloProject.application.domain.postBlock.service;

import cherhy.soloProject.application.domain.member.entity.Member;
import cherhy.soloProject.application.domain.member.repository.jpa.MemberRepository;
import cherhy.soloProject.application.domain.post.entity.Post;
import cherhy.soloProject.application.domain.post.repository.jpa.PostRepository;
import cherhy.soloProject.application.domain.postBlock.entity.PostBlock;
import cherhy.soloProject.application.domain.postBlock.repository.jpa.PostBlockRepository;
import cherhy.soloProject.application.exception.MemberNotFoundException;
import cherhy.soloProject.application.exception.PostNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostBlockWriteService {
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final PostBlockRepository postBlockRepository;

    public Optional<PostBlock> getPostBlockByMemberIdAndPostId(Long memberId, Long postId) {
        return postBlockRepository.findByMemberIdAndPostId(memberId, postId);
    }

    public void blockOrUnblock(Member member, Post post, Optional<PostBlock> postBlock) {
        postBlock.ifPresentOrElse(p -> unblock(p),
                () -> save(member, post));
    }

    private String save(Member member, Post post) {
        PostBlock buildPostBlock = PostBlock.builder()
                .member(member)
                .post(post)
                .build();

        postBlockRepository.save(buildPostBlock);
        return "차단 성공";
    }

    private String unblock(PostBlock postBlock) {
        postBlockRepository.delete(postBlock);
        return "차단 해제";
    }

    private Post getPost(Long postId) {
        return postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
    }

    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
    }
}
