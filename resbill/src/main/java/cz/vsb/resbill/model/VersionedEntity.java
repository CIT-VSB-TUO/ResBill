package cz.vsb.resbill.model;

/**
 * Marks entity classes that serves value for optimistic locking.
 * 
 * @author HAL191
 *
 */
public interface VersionedEntity {

	/**
	 * Returns the lock version number of this entity.
	 * 
	 * @return optimistic lock version number
	 */
	int getLockVersion();

}
