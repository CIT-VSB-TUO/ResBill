package cz.vsb.resbill.dao.impl;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import cz.vsb.resbill.criteria.ContractPersonCriteria;
import cz.vsb.resbill.dao.ContractPersonDAO;
import cz.vsb.resbill.model.ContractPerson;

/**
 * Implementation class of {@link ContractPersonDAO} interface.
 * 
 * @author HAL191
 *
 */
@Repository
public class ContractPersonDAOImpl implements ContractPersonDAO {

	@PersistenceContext
	private EntityManager em;

	@Override
	public ContractPerson findContractPerson(Integer contractPersonId) {
		return em.find(ContractPerson.class, contractPersonId);
	}

	@Override
	public List<ContractPerson> findContractPersons(ContractPersonCriteria criteria, Integer offset, Integer limit) {
		StringBuilder jpql = new StringBuilder("SELECT cp FROM ContractPerson AS cp");
		// building query
		if (criteria != null) {
			// where
			Set<String> where = new LinkedHashSet<String>();
			if (criteria.getContractId() != null) {
				where.add("cp.contract.id = :contractId");
			}
			if (criteria.getPersonId() != null) {
				where.add("cp.person.id = :personId");
			}
			if (!where.isEmpty()) {
				jpql.append(" WHERE ");
				jpql.append(StringUtils.join(where, " AND "));
			}
		}

		TypedQuery<ContractPerson> query = em.createQuery(jpql.toString(), ContractPerson.class);

		// parameters
		if (criteria != null) {
			if (criteria.getContractId() != null) {
				query.setParameter("contractId", criteria.getContractId());
			}
			if (criteria.getPersonId() != null) {
				query.setParameter("personId", criteria.getPersonId());
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
