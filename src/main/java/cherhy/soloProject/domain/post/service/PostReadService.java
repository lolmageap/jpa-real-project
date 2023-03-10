package cherhy.soloProject.domain.post.service;

import cherhy.soloProject.Util.scrollDto.ScrollRequest;
import cherhy.soloProject.domain.member.entity.Member;
import cherhy.soloProject.domain.post.dto.PostPhotoDto;
import cherhy.soloProject.domain.post.entity.Post;
import cherhy.soloProject.domain.post.repository.jpa.PostRepository;
import cherhy.soloProject.application.exception.PostNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostReadService {

    private final PostRepository postRepository;

    public List<Post> getPostByMemberId(Member member) {
        return postRepository.findAllByMemberId(member.getId());
    }
    public List<Post> getPostByMemberId(Member member, Member myMember) {
        return postRepository.findPostByMemberId(member.getId(), myMember.getId());
    }

    public Post getPost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);
    }

    public Post getPost(Long postId, Member member) {
        return postRepository.findByIdAndMember(postId, member)
                .orElseThrow(PostNotFoundException::new);
    }

    public Long getPostCountPage(Long memberId) {
        return postRepository.findAllByMemberIdCount(memberId);
    }

    public Long getPostCountPage(Long memberId, Long memberSessionId) {
        return postRepository.findAllByMemberIdCount(memberId, memberSessionId);
    }

    public List<Post> getPostByMemberIdPage(Member member, Pageable pageable) {
        return postRepository.findAllByMemberId(member.getId(), pageable);
    }

    public List<Post> getPostByMemberIdPage(Member member, Member myMember, Pageable pageable) {
        return postRepository.findAllByMemberId(member.getId(), myMember.getId(), pageable);
    }

    public List<Post> getPostByMemberIdCursor(Member member, ScrollRequest scrollRequest) {
        return postRepository.findByMemberIdPostIdDesc(member.getId(), scrollRequest);
    }

    public List<Post> getPostByMemberIdCursor(Member member, Member myMember, ScrollRequest scrollRequest) {
        return postRepository.findByMemberIdPostIdDesc(member.getId(), myMember.getId(), scrollRequest);
    }

    public long getNextKey(List<PostPhotoDto> findPosts) {
        return findPosts.stream().mapToLong(v -> v.getId())
                .min().orElse(ScrollRequest.NONE_KEY);
    }

    public List<PostPhotoDto> changePostPhotoDto(List<Post> findPosts) {
        return findPosts.stream().map(post ->
                new PostPhotoDto(
                post.getId(), post.getMember().getId(), post.getTitle(), post.getContent(), post.getLikeCount(), post.getPhotos())
        ).collect(Collectors.toList());
    }

}
