package cz.vsb.resbill.dao.impl;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private static final Logger log = LoggerFactory.getLogger(ContractPersonDAOImpl.class);

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
			// join
			Set<String> joins = new LinkedHashSet<String>();
			if (criteria.isFetchContract()) {
				joins.add("FETCH cp.contract AS contract");
			}
			if (criteria.isFetchPerson()) {
				joins.add("FETCH cp.person AS person");
			}
			// where
			Set<String> where = new LinkedHashSet<String>();
			if (criteria.getContractId() != null) {
				where.add("cp.contract.id = :contractId");
			}
			if (criteria.getPersonId() != null) {
				where.add("cp.person.id = :personId");
			}
			// order by
			List<String> order = new ArrayList<String>();
			if (criteria.getOrderBy() != null) {
				switch (criteria.getOrderBy()) {
				case PERSON_NAME_ASC:
					if (!criteria.isFetchPerson()) {
						joins.add("cp.person AS person");
					}
					order.add("person.secondName ASC");
					order.add("person.firstName ASC");
					break;
				case PERSON_NAME_DESC:
					if (!criteria.isFetchPerson()) {
						joins.add("cp.person AS person");
					}
					order.add("person.secondName DESC");
					order.add("person.firstName DESC");
					break;
				default:
					log.warn("Unsupported order by option: " + criteria.getOrderBy());
					break;
				}
			}
			if (!joins.isEmpty()) {
				jpql.append(" LEFT JOIN ");
				jpql.append(StringUtils.join(joins, " LEFT JOIN "));
			}
			if (!where.isEmpty()) {
				jpql.append(" WHERE ");
				jpql.append(StringUtils.join(where, " AND "));
			}
			if (!order.isEmpty()) {
				jpql.append(" ORDER BY ");
				jpql.append(StringUtils.join(order, ", "));
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

	@Override
	public ContractPerson saveContractPerson(ContractPerson contractPerson) {
		if (contractPerson.getId() == null) {
			em.persist(contractPerson);
		} else {
			contractPerson = em.merge(contractPerson);
		}
		em.flush();

		return contractPerson;
	}

	@Override
	public ContractPerson deleteContractPerson(ContractPerson contractPerson) {
		em.remove(contractPerson);
		em.flush();

		return contractPerson;
	}
}
