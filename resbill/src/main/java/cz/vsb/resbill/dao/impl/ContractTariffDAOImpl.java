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
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;

import cz.vsb.resbill.criteria.ContractTariffCriteria;
import cz.vsb.resbill.dao.ContractTariffDAO;
import cz.vsb.resbill.model.ContractTariff;

/**
 * Implementation class of {@link ContractTariffDAO} interface.
 * 
 * @author HAL191
 *
 */
@Repository
public class ContractTariffDAOImpl implements ContractTariffDAO {

	private static final Logger log = LoggerFactory.getLogger(ContractTariffDAOImpl.class);

	@PersistenceContext
	private EntityManager em;

	@Override
	public ContractTariff findContractTariff(Integer contractTariffId) {
		return em.find(ContractTariff.class, contractTariffId);
	}

	@Override
	public ContractTariff findFirstContractTariff(Integer contractId) {
		ContractTariffCriteria criteria = new ContractTariffCriteria();
		criteria.setContractId(contractId);
		criteria.setFirst(Boolean.TRUE);
		List<ContractTariff> results = findContractTariffs(criteria, null, null);
		return DataAccessUtils.singleResult(results);
	}

	@Override
	public ContractTariff findLastContractTariff(Integer contractId) {
		ContractTariffCriteria criteria = new ContractTariffCriteria();
		criteria.setContractId(contractId);
		criteria.setLast(Boolean.TRUE);
		List<ContractTariff> results = findContractTariffs(criteria, null, null);
		return DataAccessUtils.singleResult(results);
	}

	@Override
	public List<ContractTariff> findContractTariffs(ContractTariffCriteria criteria, Integer offset, Integer limit) {
		StringBuilder jpql = new StringBuilder("SELECT ct FROM ContractTariff AS ct");
		// building query
		if (criteria != null) {
			// join
			Set<String> joins = new LinkedHashSet<String>();
			if (criteria.isFetchContract()) {
				joins.add("FETCH ct.contract AS contract");
			}
			if (criteria.isFetchTariff()) {
				joins.add("FETCH ct.tariff AS tariff");
			}
			// where
			Set<String> where = new LinkedHashSet<String>();
			if (criteria.getContractId() != null) {
				where.add("ct.contract.id = :contractId");
			}
			if (criteria.getTariffId() != null) {
				where.add("ct.tariff.id = :tariffId");
			}
			if (criteria.getFirst() != null) {
				if (criteria.getFirst()) {
					where.add("ct.previous IS NULL");
				} else {
					where.add("ct.previous IS NOT NULL");
				}
			}
			if (criteria.getLast() != null) {
				if (criteria.getLast()) {
					where.add("ct.period.endDate IS NULL");
				} else {
					where.add("ct.period.endDate IS NOT NULL");
				}
			}
			// order by
			List<String> order = new ArrayList<String>();
			if (criteria.getOrderBy() != null) {
				switch (criteria.getOrderBy()) {
				case PERIOD_ASC:
					order.add("ct.period.beginDate ASC");
					order.add("ct.period.endDate ASC");
					break;
				case PERIOD_DESC:
					order.add("ct.period.beginDate DESC");
					order.add("ct.period.endDate DESC");
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

		TypedQuery<ContractTariff> query = em.createQuery(jpql.toString(), ContractTariff.class);

		// parameters
		if (criteria != null) {
			if (criteria.getContractId() != null) {
				query.setParameter("contractId", criteria.getContractId());
			}
			if (criteria.getTariffId() != null) {
				query.setParameter("tariffId", criteria.getTariffId());
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
	public ContractTariff saveContractTariff(ContractTariff contractTariff) {
		if (contractTariff.getId() == null) {
			em.persist(contractTariff);
		} else {
			contractTariff = em.merge(contractTariff);
		}
		em.flush();

		return contractTariff;
	}

	@Override
	public ContractTariff deleteContractTariff(ContractTariff contractTariff) {
		em.remove(contractTariff);
		em.flush();

		return contractTariff;
	}
}
