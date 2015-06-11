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

import cz.vsb.resbill.criteria.ContractInvoiceTypeCriteria;
import cz.vsb.resbill.dao.ContractInvoiceTypeDAO;
import cz.vsb.resbill.model.ContractInvoiceType;

/**
 * Implementation class of {@link ContractInvoiceTypeDAO} interface.
 * 
 * @author HAL191
 *
 */
@Repository
public class ContractInvoiceTypeDAOImpl implements ContractInvoiceTypeDAO {

	private static final Logger log = LoggerFactory.getLogger(ContractInvoiceTypeDAOImpl.class);

	@PersistenceContext
	private EntityManager em;

	@Override
	public ContractInvoiceType findContractInvoiceType(Integer contractInvoiceTypeId) {
		return em.find(ContractInvoiceType.class, contractInvoiceTypeId);
	}

	@Override
	public ContractInvoiceType findFirstContractInvoiceType(Integer contractId) {
		ContractInvoiceTypeCriteria criteria = new ContractInvoiceTypeCriteria();
		criteria.setContractId(contractId);
		criteria.setFirst(Boolean.TRUE);
		List<ContractInvoiceType> results = findContractInvoiceTypes(criteria, null, null);
		return DataAccessUtils.singleResult(results);
	}

	@Override
	public ContractInvoiceType findLastContractInvoiceType(Integer contractId) {
		ContractInvoiceTypeCriteria criteria = new ContractInvoiceTypeCriteria();
		criteria.setContractId(contractId);
		criteria.setLast(Boolean.TRUE);
		List<ContractInvoiceType> results = findContractInvoiceTypes(criteria, null, null);
		return DataAccessUtils.singleResult(results);
	}

	@Override
	public List<ContractInvoiceType> findContractInvoiceTypes(ContractInvoiceTypeCriteria criteria, Integer offset, Integer limit) {
		StringBuilder jpql = new StringBuilder("SELECT cit FROM ContractInvoiceType AS cit");
		// building query
		if (criteria != null) {
			// join
			Set<String> joins = new LinkedHashSet<String>();
			if (criteria.isFetchContract()) {
				joins.add("FETCH cit.contract AS contract");
			}
			if (criteria.isFetchInvoiceType()) {
				joins.add("FETCH cit.invoiceType AS invoiceType");
			}
			// where
			Set<String> where = new LinkedHashSet<String>();
			if (criteria.getContractId() != null) {
				where.add("cit.contract.id = :contractId");
			}
			if (criteria.getInvoiceTypeId() != null) {
				where.add("cit.invoiceType.id = :invoiceTypeId");
			}
			if (criteria.getFirst() != null) {
				if (criteria.getFirst()) {
					where.add("cit.previous IS NULL");
				} else {
					where.add("cit.previous IS NOT NULL");
				}
			}
			if (criteria.getLast() != null) {
				if (criteria.getLast()) {
					where.add("cit.period.endDate IS NULL");
				} else {
					where.add("cit.period.endDate IS NOT NULL");
				}
			}
			// order by
			List<String> order = new ArrayList<String>();
			if (criteria.getOrderBy() != null) {
				switch (criteria.getOrderBy()) {
				case PERIOD_ASC:
					order.add("cit.period.beginDate ASC");
					order.add("cit.period.endDate ASC");
					break;
				case PERIOD_DESC:
					order.add("cit.period.beginDate DESC");
					order.add("cit.period.endDate DESC");
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

		TypedQuery<ContractInvoiceType> query = em.createQuery(jpql.toString(), ContractInvoiceType.class);

		// parameters
		if (criteria != null) {
			if (criteria.getContractId() != null) {
				query.setParameter("contractId", criteria.getContractId());
			}
			if (criteria.getInvoiceTypeId() != null) {
				query.setParameter("invoiceTypeId", criteria.getInvoiceTypeId());
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
	public ContractInvoiceType saveContractInvoiceType(ContractInvoiceType contractInvoiceType) {
		if (contractInvoiceType.getId() == null) {
			em.persist(contractInvoiceType);
		} else {
			contractInvoiceType = em.merge(contractInvoiceType);
		}
		em.flush();

		return contractInvoiceType;
	}

	@Override
	public ContractInvoiceType deleteContractInvoiceType(ContractInvoiceType contractInvoiceType) {
		em.remove(contractInvoiceType);
		em.flush();

		return contractInvoiceType;
	}
}
