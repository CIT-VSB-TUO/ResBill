package cz.vsb.resbill.model;

/**
 * Marks entity with date limits as a {@link Period} embedded entity
 * 
 * @author HAL191
 *
 */
public interface PeriodLimitedEntity {

	/**
	 * Returns date limits as {@link Period}
	 * 
	 * @return
	 */
	Period getPeriod();

	/**
	 * Sets date limits
	 * 
	 * @param period
	 */
	void setPeriod(Period period);
}
