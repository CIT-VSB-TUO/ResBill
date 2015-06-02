package cz.vsb.resbill.dao;

import java.util.List;

import cz.vsb.resbill.criteria.CustomerCriteria;
import cz.vsb.resbill.model.Customer;

/**
 * Data access interface for {@link Customer} model entity.
 * 
 * @author HAL191
 */
public interface CustomerDAO {

	/**
	 * Finds a {@link Customer} with given primary key.
	 * 
	 * @param id
	 *          primary key
	 * @return found {@link Customer} entity
	 */
	Customer findCustomer(Integer id);

	/**
	 * Finds {@link Customer} entities by specified criteria
	 * 
	 * @param criteria
	 *          filtering criteria
	 * @param offset
	 *          order number of first result to return
	 * @param limit
	 *          maximum number of results to return
	 * @return list of {@link Customer} entities
	 */
	List<Customer> findCustomers(CustomerCriteria criteria, Integer offset, Integer limit);

	/**
	 * Saves current state of given {@link Customer} entity.
	 * 
	 * @param customer
	 *          entity to save
	 * @return saved entity
	 */
	Customer saveCustomer(Customer customer);

	/**
	 * Deletes given {@link Customer} entity.
	 * 
	 * @param customer
	 *          entity to delete
	 * @return deleted entity
	 */
	Customer deleteCustomer(Customer customer);
}
