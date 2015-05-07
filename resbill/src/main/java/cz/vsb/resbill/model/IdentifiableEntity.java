package cz.vsb.resbill.model;

/**
 * An interface marking all domain objects. Each domain object has its unique
 * identifier.
 * 
 * @author HAL191
 *
 */
public interface IdentifiableEntity {

	/**
	 * Returns the unique identifier.
	 * 
	 * @return unique identifier
	 */
	Integer getId();

	/**
	 * Sets the unique identifier.
	 * 
	 * @param id
	 *          unique identifier to set
	 */
	void setId(Integer id);
}
