package cz.vsb.resbill.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * A common ancestor of entities with automatically generated primary key that
 * are ready for optimistic locking.
 * 
 * @author HAL191
 *
 */
@MappedSuperclass
public abstract class BaseVersionedEntity extends BaseGeneratedIdEntity
		implements VersionedEntity {

	private static final long serialVersionUID = 4891678972378019341L;

	@Version
	@Column(name = "lock_version", nullable = false)
	private int lockVersion;

	@Override
	public int getLockVersion() {
		return lockVersion;
	}

	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("lockVersion", lockVersion);
		return builder.toString();
	}

}
