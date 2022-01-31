package hellojpa;

import static javax.persistence.FetchType.LAZY;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.ToString.Exclude;
import org.hibernate.Hibernate;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Child {

  @Id
  @GeneratedValue
  private Long id;
  private String name;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "PARENT_ID")
  @Exclude
  private Parent parent;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    Child child = (Child) o;
    return id != null && Objects.equals(id, child.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
