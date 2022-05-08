package hellojpa;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.Hibernate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.ToString.Exclude;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Parent {

	@Id
	@GeneratedValue
	private Long id;
	private String name;

	@OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
	@Exclude
	private List<Child> children = new ArrayList<>();

	public void addChildren(List<Child> children) {
		this.children.addAll(children);
		this.children.forEach(child -> child.setParent(this));
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
			return false;
		}
		Parent parent = (Parent)o;
		return id != null && Objects.equals(id, parent.id);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}

/*
옵션에 따라 부모가 관리하는 자식의 라이프 사이클을 다르게 가져갈 수 있다
CascadeType.ALL - 전부 따라감
CascadeType.REMOVE - 삭제만 따라감
CascadeType.PERSIST - 저장만 따라감

생명주기를 관리하는 것이기 때문에 부모쪽에서 전부 관리하는 것이다
따라서 여러 엔티티에 속한 것에는 적용할 수 없다
orphanRemoval = true 로 줬을 때는 CascadeType.REMOVE 와 마찬가지로 동작한다
부모 객체가 삭제됐을 때나 부모 객체에서 컬렉션 요소를 삭제했을 때 자식 엔티티의 삭제 쿼리 날라감

Cascade 사용 시 개발 편의성은 올라가지만 고려해봐야 하는 점은 부모 엔티티가 꼭 관리해야 하는지다
 */