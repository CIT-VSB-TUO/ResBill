package cz.vsb.resbill.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.apache.commons.lang3.builder.ToStringBuilder;

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
