package cz.vsb.resbill.dao;

import java.util.List;

import cz.vsb.resbill.criteria.CustomerCriteria;
import cz.vsb.resbill.model.Customer;

public interface CustomerDAO {

	Customer findCustomer(Integer id);

	List<Customer> findCustomers(CustomerCriteria criteria, Integer offset, Integer limit);
}
