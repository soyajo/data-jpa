package study.datajpa.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "username", "age"})
// 네임드 쿼리는 실무에 사용안함.
// 장점 애플리케이션 로딩 시점에 에러가나면 잡을 수있음.
// jpql은 못잡음.
@NamedQuery(
        name = "Member.findByUsername",
        query="select m from Member m where m.username = :username"
)
@NamedEntityGraph(name = "Member.all", attributeNodes = @NamedAttributeNode("team"))
public class Member extends BaseEntity{
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String username;
    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    public Member(String userName) {
        this.username = userName;
    }

    public Member(String username, int age) {
        this.username = username;
        this.age = age;
    }

    public void changeTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }

    public Member(String userName, int age, Team team) {
        this.username = userName;
        this.age = age;
        if (team != null) {
            changeTeam(team);
        }
    }
}
