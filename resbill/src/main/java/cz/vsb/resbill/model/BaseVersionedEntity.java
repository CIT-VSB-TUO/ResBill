package cz.vsb.resbill.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

/**
 * A common ancestor of entities with automatically generated primary key that
 * are ready for optimistic locking.
 * 
 * @author HAL191
 *
 */
@MappedSuperclass
public abstract class BaseVersionedEntity extends BaseGeneratedIdEntity implements VersionedEntity {

	private static final long serialVersionUID = 4891678972378019341L;

	@Version
	@Column(name = "lock_version", nullable = false)
	private int lockVersion;

	@Override
	public int getLockVersion() {
		return lockVersion;
	}

	@Override
	public void setLockVersion(int lockVersion) {
		this.lockVersion = lockVersion;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("BaseVersionedEntity [");
		builder.append(super.toString());
		builder.append(", lockVersion=");
		builder.append(lockVersion);
		builder.append("]");
		return builder.toString();
	}

}
