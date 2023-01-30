package cherhy.soloProject.application.domain.member.entity;

import cherhy.soloProject.Util.BaseTimeEntity;
import cherhy.soloProject.application.domain.follow.entity.Follow;
import cherhy.soloProject.application.domain.post.entity.Post;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;
import static org.springframework.util.Assert.isTrue;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;
    @Column(unique = true)
    private String user_id;
    @Column(unique = true)
    private String email;
    @Column(unique = true)
    private String name;
    private String password;

    @OneToMany(mappedBy = "member")
    private List<Post> posts = new ArrayList<>();

    //member -> follow 단방향 참조
    @OneToMany(mappedBy = "follower")
    private List<Follow> followers = new ArrayList<>();

    @OneToMany(mappedBy = "following")
    private List<Follow> follows = new ArrayList<>();

    @Builder //회원가입
    public Member(String user_id, String name, String email, String password, LocalDate birthday) {
        this.user_id = requireNonNull(user_id);
        this.name = requireNonNull(name);
        this.password = requireNonNull(password);
        this.email = requireNonNull(email);
    }

    public void changeName(String changeName) {
        this.name = changeName;
    }

    public void changeUserId(String changeUserId) {
        this.user_id = changeUserId;
    }

    public void changePassword(String changePassword) {
        this.password = changePassword;
    }


}