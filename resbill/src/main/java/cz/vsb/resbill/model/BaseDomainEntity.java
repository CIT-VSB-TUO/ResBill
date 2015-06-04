package cz.vsb.resbill.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * A common ancestor of domain model entities. Each entity has its primary identifier.
 * 
 * @author HAL191
 *
 */
public abstract class BaseDomainEntity implements IdentifiableEntity, Serializable {

	private static final long serialVersionUID = 9045791835477269437L;

	/**
	 * Default implementation using primary identifier. Should be overridden in subclasses.
	 */
	@Override
	public int hashCode() {
		if (this.getId() == null) {
			return super.hashCode();
		} else {
			return new HashCodeBuilder(13, 17).append(this.getId()).toHashCode();
		}
	}

	/**
	 * Default implementation using primary identifier. Should be overridden in subclasses.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this.getId() == null) {
			return super.equals(obj);
		} else {
			if (this == obj) return true;

			if (!(obj instanceof BaseDomainEntity)) return false;
			BaseDomainEntity other = (BaseDomainEntity) obj;
			return new EqualsBuilder().append(this.getId(), other.getId()).isEquals();
		}
	}
}
