package cz.vsb.resbill.model;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * A common ancestor of entities with an enumerated primary key.
 * 
 * @author HAL191
 *
 */
@MappedSuperclass
public abstract class BaseEnumeratedIdEntity extends BaseDomainEntity {

	private static final long serialVersionUID = 1810771445429480334L;

	@Id
	@Column(name = "id", nullable = false)
	@Access(AccessType.PROPERTY)
	private Integer id;

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("BaseEnumeratedIdEntity [");
		builder.append(super.toString());
		builder.append(", id=");
		builder.append(id);
		builder.append("]");
		return builder.toString();
	}

}
