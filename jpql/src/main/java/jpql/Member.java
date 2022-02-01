package jpql;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.ToString.Exclude;

@Entity
@Getter
@Setter
@ToString
@NamedQuery(name = "Member.findByUsername",
  query = "select m from Member m where m.username = :username")
public class Member {

  @Id
  @GeneratedValue
  private Long id;
  private String username;
  private int age;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "TEAM_ID")
  @Exclude
  private Team team;

  public void changeTeam(Team team) {
    this.team = team;
    team.getMembers().add(this);
  }
}
