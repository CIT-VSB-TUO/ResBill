package cz.vsb.resbill.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.apache.commons.lang3.builder.ToStringBuilder;

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
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("id", id);
		return builder.toString();
	}

}
