package cz.vsb.resbill.model;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * A common ancestor of entities with an automatically generated primary key.
 * 
 * @author HAL191
 *
 */
@MappedSuperclass
public abstract class BaseGeneratedIdEntity extends BaseDomainEntity {

	private static final long serialVersionUID = 3015626934429617562L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
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
		builder.append("BaseGeneratedIdEntity [");
		builder.append(super.toString());
		builder.append(", id=");
		builder.append(id);
		builder.append("]");
		return builder.toString();
	}

}
