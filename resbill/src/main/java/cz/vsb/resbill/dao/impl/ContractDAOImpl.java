/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.dao.impl;

import java.util.Date;
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

import cz.vsb.resbill.criteria.ContractCriteria;
import cz.vsb.resbill.dao.ContractDAO;
import cz.vsb.resbill.model.Contract;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */

@Repository
public class ContractDAOImpl implements ContractDAO {

	private static final Logger log = LoggerFactory.getLogger(ContractDAOImpl.class);

	@PersistenceContext
	private EntityManager em;

	@Override
	public Contract findContract(Integer contractId) {
		return em.find(Contract.class, contractId);
	}

	@Override
	public List<Contract> findContracts(ContractCriteria criteria, Integer offset, Integer limit) {
		StringBuilder jpql = new StringBuilder("SELECT c FROM Contract AS c");
		// building query
		if (criteria != null) {
			// where
			Set<String> where = new LinkedHashSet<String>();
			if (criteria.getCustomerId() != null) {
				where.add("c.customer.id = :customerId");
			}
			if (!where.isEmpty()) {
				jpql.append(" WHERE ");
				jpql.append(StringUtils.join(where, " AND "));
			}

			// order by
			if (criteria.getOrderBy() != null) {
				switch (criteria.getOrderBy()) {
				case EVIDENCE_NUMBER:
					jpql.append(" ORDER BY c.evidenceNumber");
					break;
				case NAME:
					jpql.append(" ORDER BY c.name");
					break;
				default:
					log.warn("Unsupported order by option: " + criteria.getOrderBy());
					break;
				}
			}
		}
		TypedQuery<Contract> query = em.createQuery(jpql.toString(), Contract.class);
		// parameters
		if (criteria != null) {
			if (criteria.getCustomerId() != null) {
				query.setParameter("customerId", criteria.getCustomerId());
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

	/**
	 * Najde vsechny kontrakty, jejichz servery maji alespon jedno nevyfakturovane DailyUsage nejpozdeji v pozadovanem dni.
	 * 
	 * Server musi byt kontraktu prirazen take nejpozdeji v pozadovanem dni.
	 * 
	 * Pro kontrakt nesmi existovat faktura se stejnym pozadovanym dnem.
	 * 
	 * Vraceny budou pouze ty kontrakty, ktere maji typ fakturace (v pozadovanem dni) odpovidajici predanemu parametru invoiceTypeIds.
	 */
	@Override
	public List<Contract> findUninvoicedContracts(Date lastDay, List<Integer> invoiceTypeIds) {
		StringBuilder jpql = new StringBuilder();
		jpql.append(" SELECT DISTINCT contract ");
		jpql.append(" FROM Contract AS contract ");
		jpql.append(" JOIN contract.contractInvoiceTypes AS contractInvoiceType ");
		jpql.append(" JOIN contractInvoiceType.invoiceType AS invoiceType ");
		jpql.append(" JOIN contract.contractServers AS contractServer ");
		jpql.append(" JOIN contractServer.server AS server ");
		jpql.append(" JOIN server.dailyUsages AS dailyUsage ");
		jpql.append(" JOIN dailyUsage.dailyImport AS dailyImport ");
		jpql.append(" WHERE invoiceType.id IN (:invoiceTypeIds) ");
		jpql.append(" AND contractInvoiceType.period.beginDate <= :lastDay ");
		jpql.append(" AND (contractInvoiceType.period.endDate IS NULL OR contractInvoiceType.period.endDate >= :lastDay) ");
		jpql.append(" AND contractServer.period.beginDate <= :lastDay ");
		jpql.append(" AND contractServer.period.beginDate <= dailyImport.date ");
		jpql.append(" AND (contractServer.period.endDate IS NULL OR contractServer.period.endDate >= dailyImport.date) ");
		jpql.append(" AND dailyImport.date <= :lastDay ");
		jpql.append(" AND NOT EXISTS ( ");
		jpql.append("   SELECT invoice ");
		jpql.append("   FROM Invoice AS invoice ");
		jpql.append("   WHERE invoice.contract = contract ");
		jpql.append("   AND invoice.period.endDate = :lastDay ");
		jpql.append(" ) ");
		jpql.append(" AND dailyUsage NOT IN ( ");
		jpql.append("   SELECT invoiceDailyUsage.dailyUsage ");
		jpql.append("   FROM InvoiceDailyUsage AS invoiceDailyUsage ");
		jpql.append(" ) ");

		TypedQuery<Contract> query = em.createQuery(jpql.toString(), Contract.class);
		query.setParameter("lastDay", lastDay);
		query.setParameter("invoiceTypeIds", invoiceTypeIds);

		return query.getResultList();
	}

}
