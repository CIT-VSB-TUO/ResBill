package cz.vsb.resbill.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.support.DataAccessUtils;

import cz.vsb.resbill.criteria.ContractCriteria;
import cz.vsb.resbill.criteria.CustomerCriteria;
import cz.vsb.resbill.dao.ContractDAO;
import cz.vsb.resbill.dao.CustomerDAO;
import cz.vsb.resbill.dto.customer.CustomerHeaderDTO;
import cz.vsb.resbill.dto.customer.CustomerListDTO;
import cz.vsb.resbill.exception.CustomerServiceException;
import cz.vsb.resbill.exception.CustomerServiceException.Reason;
import cz.vsb.resbill.exception.ResBillException;
import cz.vsb.resbill.model.Contract;
import cz.vsb.resbill.model.Customer;
import cz.vsb.resbill.service.CustomerService;
import cz.vsb.resbill.service.ResBillService;

/**
 * An implementation of {@link CustomerService} interface.
 * 
 * @author HAL191
 *
 */
@ResBillService
public class CustomerServiceImpl implements CustomerService {

	private static final Logger log = LoggerFactory.getLogger(CustomerServiceImpl.class);

	@Inject
	private CustomerDAO customerDAO;

	@Inject
	private ContractDAO contractDAO;

	@Override
	public Customer findCustomer(Integer customerId) throws ResBillException {
		try {
			return customerDAO.findCustomer(customerId);
		} catch (Exception e) {
			log.error("An unexpected error occured while finding Customer by id=" + customerId, e);
			throw new ResBillException(e);
		}
	}

	@Override
	public CustomerHeaderDTO findCustomerHeaderDTO(Integer customerId) throws ResBillException {
		try {
			return new CustomerHeaderDTO(findCustomer(customerId));
		} catch (ResBillException e) {
			throw e;
		} catch (Exception e) {
			log.error("An unexpected error occured while finding CustomerHeaderDTO by id=" + customerId, e);
			throw new ResBillException(e);
		}
	}

	@Override
	public List<Customer> findCustomers(CustomerCriteria criteria, Integer offset, Integer limit) throws ResBillException {
		try {
			return customerDAO.findCustomers(criteria, offset, limit);
		} catch (Exception e) {
			log.error("An unexpected error occured while searching for Customer entities by criteria: " + criteria, e);
			throw new ResBillException(e);
		}
	}

	@Override
	public List<CustomerListDTO> findCustomerListDTOs(CustomerCriteria criteria, Integer offset, Integer limit) throws ResBillException {
		try {
			if (criteria == null) {
				criteria = new CustomerCriteria();
			}
			criteria.setFetchContactPerson(true);

			List<Customer> customers = findCustomers(criteria, offset, limit);
			List<CustomerListDTO> dtos = new ArrayList<CustomerListDTO>(customers.size());
			for (Customer customer : customers) {
				dtos.add(new CustomerListDTO(customer));
			}
			return dtos;
		} catch (ResBillException e) {
			throw e;
		} catch (Exception e) {
			log.error("An unexpected error occured while searching for Customer entities by criteria: " + criteria, e);
			throw new ResBillException(e);
		}
	}

	@Override
	public Customer saveCustomer(Customer customer) throws CustomerServiceException, ResBillException {
		try {
			// kontrola jedinecnosti nazvu
			CustomerCriteria criteria = new CustomerCriteria();
			criteria.setName(customer.getName());
			List<Customer> customers = customerDAO.findCustomers(criteria, null, null);
			Customer foundCustomer = DataAccessUtils.singleResult(customers);
			if (foundCustomer != null && !foundCustomer.getId().equals(customer.getId())) {
				throw new CustomerServiceException(Reason.NONUNIQUE_NAME);
			}
			return customerDAO.saveCustomer(customer);
		} catch (CustomerServiceException e) {
			throw e;
		} catch (Exception e) {
			log.error("An unexpected error occured while saving Customer entity: " + customer, e);
			throw new ResBillException(e);
		}
	}

	@Override
	public Customer deleteCustomer(Integer customerId) throws CustomerServiceException, ResBillException {
		try {
			Customer customer = customerDAO.findCustomer(customerId);

			// kontrola zavislosti
			// kontrakty
			ContractCriteria conCriteria = new ContractCriteria();
			conCriteria.setCustomerId(customerId);
			List<Contract> contracts = contractDAO.findContracts(conCriteria, null, null);
			if (!contracts.isEmpty()) {
				throw new CustomerServiceException(Reason.CONTRACT_EXISTENCE);
			}

			return customerDAO.deleteCustomer(customer);
		} catch (CustomerServiceException e) {
			throw e;
		} catch (Exception e) {
			log.error("An unexpected error occured while deleting Customer with id=" + customerId, e);
			throw new ResBillException(e);
		}
	}
}
