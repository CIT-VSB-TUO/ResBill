package cz.vsb.resbill.service;

import java.util.List;

import cz.vsb.resbill.criteria.CustomerCriteria;
import cz.vsb.resbill.dto.customer.CustomerHeaderDTO;
import cz.vsb.resbill.dto.customer.CustomerListDTO;
import cz.vsb.resbill.exception.CustomerServiceException;
import cz.vsb.resbill.exception.ResBillException;
import cz.vsb.resbill.model.Customer;

/**
 * A service for processing {@link Customer} entities.
 * 
 * @author HAL191
 *
 */
public interface CustomerService {

	/**
	 * Finds a {@link Customer} with given key.
	 * 
	 * @param customerId
	 *          primary key of a {@link Customer}
	 * @return found {@link Customer}, otherwise <code>null</code>
	 * @throws ResBillException
	 *           if unexpected error occurs
	 */
	Customer findCustomer(Integer customerId) throws ResBillException;

	/**
	 * Finds {@link CustomerHeaderDTO}
	 * 
	 * @param customerId
	 * @return
	 * @throws ResBillException
	 */
	CustomerHeaderDTO findCustomerHeaderDTO(Integer customerId) throws ResBillException;

	/**
	 * Finds a list of {@link Customer} entities according to given criteria.
	 * 
	 * @param criteria
	 *          filtering criteria
	 * @param offset
	 * @param limit
	 * @return list of found {@link Customer} entities
	 * @throws ResBillException
	 *           if unexpected error occurs
	 */
	List<Customer> findCustomers(CustomerCriteria criteria, Integer offset, Integer limit) throws ResBillException;

	/**
	 * Finds a list of {@link CustomerListDTO} instances according to given criteria.
	 * 
	 * @param criteria
	 *          filtering criteria
	 * @param offset
	 * @param limit
	 * @return list of found {@link CustomerListDTO} instances
	 * @throws ResBillException
	 *           if unexpected error occurs
	 */
	List<CustomerListDTO> findCustomerListDTOs(CustomerCriteria criteria, Integer offset, Integer limit) throws ResBillException;

	/**
	 * Persists given {@link Customer} entity.
	 * 
	 * @param customer
	 *          entity to save
	 * @return saved {@link Customer} entity (with generated primary key)
	 * @throws CustomerServiceException
	 *           if specific error occurs
	 * @throws ResBillException
	 *           if unexpected error occurs
	 */
	Customer saveCustomer(Customer customer) throws CustomerServiceException, ResBillException;

	/**
	 * Deletes {@link Customer} entity with given primary key.
	 * 
	 * @param customerId
	 *          key of deleted {@link Customer} entity
	 * @return deleted {@link Customer} entity
	 * @throws CustomerServiceException
	 *           if specific error occurs
	 * @throws ResBillException
	 *           if unexpected error occurs
	 */
	Customer deleteCustomer(Integer customerId) throws CustomerServiceException, ResBillException;
}
