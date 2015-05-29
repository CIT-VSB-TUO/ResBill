/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.dao.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import cz.vsb.resbill.dao.ContractDAO;
import cz.vsb.resbill.model.Contract;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */

@Repository
public class ContractDAOImpl implements ContractDAO {

	@PersistenceContext
	private EntityManager em;

	/**
	 * Najde vsechny kontrakty, jejichz servery maji alespon jedno nevyfakturovane DailyUsage v pozadovanem dni.
	 * 
	 * Server musi byt kontraktu prirazen take nejpozdeji v pozadovanem dni.
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
