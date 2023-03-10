package cherhy.soloProject.domain.entity;

import cherhy.soloProject.domain.member.repository.jpa.MemberRepository;
import cherhy.soloProject.domain.member.service.MemberWriteService;
import cherhy.soloProject.domain.photo.repository.jpa.PhotoRepository;
import cherhy.soloProject.domain.post.repository.jpa.PostRepository;
import cherhy.soloProject.domain.post.service.PostReadService;
import cherhy.soloProject.domain.post.service.PostWriteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


@SpringBootTest
@Transactional
@Rollback(value = false)
class PostTest {
    @PersistenceContext
    EntityManager em;
    @Autowired
    PostWriteService postWriteService;
    @Autowired
    PostReadService postReadService;
    @Autowired
    PostRepository postRepository;
    @Autowired
    MemberWriteService memberWriteService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PhotoRepository photoRepository;

//    @BeforeEach
//    public void bef(){
////        MemberDto member = new MemberDto("aaaa","홍길동","abcd@naver.com","abcd", LocalDate.now());
//        MemberDto member = new MemberDto("aaaa","홍길동","abcd@naver.com","abcd");
//        memberWriteService.signUp(member);
//    }

//    @Test
//    public void postTest(){
////      사진 입력받음
//
    @Test
    public void postTest() {
//      사진 입력받음

//        ArrayList<String> photoList = new ArrayList<>();
//        for (int i = 0; i < 52; i++) {
//        photoList.add("test number one" + i);
//        photoList.add("test number two" + i);
//        photoList.add("test number three" + i);
//
//        PostRequestDto postRequestDto = new PostRequestDto(2L,i+i + "개",i+i  + "장", photoList);
//        postWriteService.save(postRequestDto);
//        }

//        em.flush();
//        em.clear();
//
//        List<PostPhotoDto> findByPosts = postReadService.findPostByMemberId(postDto.memberId());
//        List<Photo> photos = photoRepository.findByPostId(findByPosts.get(0).getId());
//
//        assertThat(photos.size()).isEqualTo(3);

//    }
    }
}