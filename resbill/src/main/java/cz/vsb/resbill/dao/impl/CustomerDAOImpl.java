package cz.vsb.resbill.dao.impl;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import cz.vsb.resbill.criteria.CustomerCriteria;
import cz.vsb.resbill.dao.CustomerDAO;
import cz.vsb.resbill.model.Customer;

@Repository
public class CustomerDAOImpl implements CustomerDAO {

	@PersistenceContext
	private EntityManager em;

	@Override
	public Customer findCustomer(Integer id) {
		return em.find(Customer.class, id);
	}

	@Override
	public List<Customer> findCustomers(CustomerCriteria criteria, Integer offset, Integer limit) {
		StringBuilder jpql = new StringBuilder("SELECT c FROM Customer AS c");
		if (criteria != null) {
			// where
			Set<String> where = new LinkedHashSet<String>();
			if (StringUtils.isNotEmpty(criteria.getNamePrefix())) {
				where.add("c.name LIKE :namePrefix");
			}
			if (criteria.getContactPersonId() != null) {
				where.add("c.contactPerson.id = :contactPersonId");
			}
			if (!where.isEmpty()) {
				jpql.append(" WHERE ");
				jpql.append(StringUtils.join(where, " AND "));
			}
		}
		// order by
		jpql.append(" ORDER BY c.name");

		TypedQuery<Customer> query = em.createQuery(jpql.toString(), Customer.class);
		// parameters
		if (criteria != null) {
			if (StringUtils.isNotEmpty(criteria.getNamePrefix())) {
				query.setParameter("namePrefix", criteria.getNamePrefix() + "%");
			}
			if (criteria.getContactPersonId() != null) {
				query.setParameter("contactPersonId", criteria.getContactPersonId());
			}
		}

		if (offset != null) {
			query.setFirstResult(offset.intValue());
		}
		if (limit != null) {
			query.setMaxResults(limit.intValue());
		}

		return query.getResultList();
	}

}
