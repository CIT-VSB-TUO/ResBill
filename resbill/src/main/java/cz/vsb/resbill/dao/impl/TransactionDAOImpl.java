/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.dao.impl;

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

import cz.vsb.resbill.criteria.TransactionCriteria;
import cz.vsb.resbill.dao.TransactionDAO;
import cz.vsb.resbill.model.Transaction;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */

@Repository
public class TransactionDAOImpl implements TransactionDAO {

	private static final Logger log = LoggerFactory.getLogger(TransactionDAOImpl.class);

	@PersistenceContext
	private EntityManager em;

	/**
   * 
   */
	@Override
	public Transaction findTransaction(Integer id) {
		return em.find(Transaction.class, id);
	}

	/**
	 * 
	 * @param criteria
	 * @param offset
	 * @param limit
	 * @return
	 */
	@Override
	public List<Transaction> findTransactions(TransactionCriteria criteria, Integer offset, Integer limit) {
		StringBuilder jpql = new StringBuilder();
		jpql.append(" SELECT transaction ");
		jpql.append(" FROM Transaction AS transaction ");

		// join a where
		Set<String> join = new LinkedHashSet<String>();
		Set<String> where = new LinkedHashSet<String>();

		if (criteria.getContractId() != null) {
			where.add("contract.id = :contractId");
		}

		// Order by
		Set<String> order = new LinkedHashSet<String>();
		if (criteria.getOrderBy() != null && !criteria.getOrderBy().isEmpty()) {

			for (TransactionCriteria.OrderBy orderBy : criteria.getOrderBy()) {
				switch (orderBy) {
				case ORDER_ASC:
					order.add(" transaction.order ASC ");
					break;
				case ORDER_DESC:
					order.add(" transaction.order DESC ");
					break;
				case DECISIVE_DATE_ASC:
					order.add(" transaction.decisiveDate ASC ");
					break;
				case DECISIVE_DATE_DESC:
					order.add(" transaction.decisiveDate DESC ");
					break;

				default:
					log.warn("Unsupported order by option: " + orderBy);
					break;
				}
			}

		}

		if (!join.isEmpty()) {
			jpql.append(" JOIN ");
			jpql.append(StringUtils.join(join, " JOIN "));
		}

		if (!where.isEmpty()) {
			jpql.append(" WHERE ");
			jpql.append(StringUtils.join(where, " AND "));
		}

		if (!order.isEmpty()) {
			jpql.append(" ORDER BY ");
			jpql.append(StringUtils.join(order, ","));
		}

		// Vytvoreni dotazu
		TypedQuery<Transaction> query = em.createQuery(jpql.toString(), Transaction.class);
		if (offset != null) {
			query.setFirstResult(offset.intValue());
		}
		if (limit != null) {
			query.setMaxResults(limit.intValue());
		}

		if (criteria.getContractId() != null) {
			query.setParameter("contractId", criteria.getContractId());
		}

		return query.getResultList();
	}

	/**
	 * Pro pozadovany kontrakt najde nejblizsi volne poradove cislo trasnakce v ramci tohoto kontraktu
	 */
	@Override
	public Integer getNextOrder(Integer contractId) {
		StringBuilder jpql = new StringBuilder();
		jpql.append(" SELECT MAX(transaction.order) ");
		jpql.append(" FROM Transaction AS transaction ");
		jpql.append(" JOIN transaction.contract AS contract ");
		jpql.append(" WHERE contract.id = :contractId ");

		TypedQuery<Integer> query = em.createQuery(jpql.toString(), Integer.class);

		query.setParameter("contractId", contractId);

		Integer maxOrder = DataAccessUtils.uniqueResult(query.getResultList());
		if (maxOrder == null) {
			maxOrder = 0;
		}

		return maxOrder + 1;
	}

	/**
   * 
   */
	@Override
	public Transaction saveTransaction(Transaction transaction) {
		if (transaction.getId() == null) {
			em.persist(transaction);
		} else {
			transaction = em.merge(transaction);
		}

		em.flush();

		return transaction;
	}

	/**
   * 
   */
	@Override
	public Transaction deleteTransaction(Transaction transaction) {
		em.remove(transaction);

		em.flush();

		return transaction;
	}
}
