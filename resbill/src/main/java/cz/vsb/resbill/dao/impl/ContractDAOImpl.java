/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.dao.impl;

import java.util.ArrayList;
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
  private EntityManager       em;

  @Override
  public Contract findContract(Integer contractId) {
    return em.find(Contract.class, contractId);
  }

  /**
	 * 
	 */
  @Override
  public List<Contract> findContracts(ContractCriteria criteria, Integer offset, Integer limit) {
    StringBuilder jpql = new StringBuilder("SELECT contract FROM Contract AS contract");
    // building query
    if (criteria != null) {
      // where
      Set<String> where = new LinkedHashSet<String>();
      if (criteria.getCustomerId() != null) {
        where.add("contract.customer.id = :customerId");
      }
      if (criteria.getContractIds() != null) {
        where.add("contract.id IN (:contractIds)");
      }

      // "Vlastnosti" kontraktu
      if (criteria.getFeatures() != null) {
        // Kontrakty odebírající zdroje po skončení platnosti
        if (criteria.getFeatures().contains(ContractCriteria.Feature.DAILY_USAGE_AFTER_CONTRACT_END)) {
          StringBuilder subJpql = new StringBuilder();
          subJpql.append(" EXISTS ( ");
          subJpql.append("   SELECT dailyUsage ");
          subJpql.append("   FROM Contract AS duContract");
          subJpql.append("   JOIN duContract.contractServers AS contractServer ");
          subJpql.append("   JOIN contractServer.server AS server ");
          subJpql.append("   JOIN server.dailyUsages AS dailyUsage ");
          subJpql.append("   JOIN dailyUsage.dailyImport AS dailyImport ");
          subJpql.append("   WHERE duContract = contract ");
          subJpql.append("   AND duContract.period.endDate IS NOT NULL ");
          subJpql.append("   AND dailyImport.date > duContract.period.endDate ");
          subJpql.append(" ) ");

          where.add(subJpql.toString());
        }

        // Kontrakty odebírající zdroje mimo období tarifu
        if (criteria.getFeatures().contains(ContractCriteria.Feature.DAILY_USAGE_OUT_OF_TARIFF)) {
          StringBuilder subJpql = new StringBuilder();
          subJpql.append(" EXISTS ( ");
          subJpql.append("   SELECT dailyUsage ");
          subJpql.append("   FROM Contract AS duContract");
          subJpql.append("   JOIN duContract.contractServers AS contractServer ");
          subJpql.append("   JOIN contractServer.server AS server ");
          subJpql.append("   JOIN server.dailyUsages AS dailyUsage ");
          subJpql.append("   JOIN dailyUsage.dailyImport AS dailyImport ");
          subJpql.append("   WHERE duContract = contract ");
          subJpql.append("   AND NOT EXISTS ( ");
          subJpql.append("     SELECT contractTariff ");
          subJpql.append("     FROM ContractTariff AS contractTariff ");
          subJpql.append("     WHERE contractTariff.contract = duContract ");
          subJpql.append("     AND contractTariff.period.beginDate <= dailyImport.date ");
          subJpql.append("     AND (contractTariff.period.endDate IS NULL OR contractTariff.period.endDate >= dailyImport.date) ");
          subJpql.append("   ) ");
          subJpql.append(" ) ");

          where.add(subJpql.toString());
        }

        // Kontrakty bez typu účtování
        if (criteria.getFeatures().contains(ContractCriteria.Feature.NO_INVOICE_TYPE)) {
          StringBuilder subJpql = new StringBuilder();
          subJpql.append(" NOT EXISTS ( ");
          subJpql.append("   SELECT contractInvoiceType ");
          subJpql.append("   FROM ContractInvoiceType AS contractInvoiceType ");
          subJpql.append("   WHERE contractInvoiceType.contract = contract ");
          subJpql.append(" ) ");

          where.add(subJpql.toString());
        }

        // Kontrakty bez serveru
        if (criteria.getFeatures().contains(ContractCriteria.Feature.NO_SERVER)) {
          StringBuilder subJpql = new StringBuilder();
          subJpql.append(" NOT EXISTS ( ");
          subJpql.append("   SELECT contractServer ");
          subJpql.append("   FROM ContractServer AS contractServer ");
          subJpql.append("   WHERE contractServer.contract = contract ");
          subJpql.append(" ) ");

          where.add(subJpql.toString());
        }

        // Kontrakty bez tarifu
        if (criteria.getFeatures().contains(ContractCriteria.Feature.NO_TARIFF)) {
          StringBuilder subJpql = new StringBuilder();
          subJpql.append(" NOT EXISTS ( ");
          subJpql.append("   SELECT contractTariff ");
          subJpql.append("   FROM ContractTariff AS contractTariff ");
          subJpql.append("   WHERE contractTariff.contract = contract ");
          subJpql.append(" ) ");

          where.add(subJpql.toString());
        }

        // Kontrakty se záporným kreditem
        if (criteria.getFeatures().contains(ContractCriteria.Feature.NEGATIVE_BALANCE)) {
          where.add("contract.balance < 0");
        }
      }

      if (!where.isEmpty()) {
        jpql.append(" WHERE ");
        jpql.append(StringUtils.join(where, " AND "));
      }

      // order by
      if (criteria.getOrderBy() != null && !criteria.getOrderBy().isEmpty()) {
        List<String> order = new ArrayList<String>();
        for (ContractCriteria.OrderBy orderBy : criteria.getOrderBy()) {
          switch (orderBy) {
          case EVIDENCE_NUMBER_ASC:
            order.add(" contract.evidenceNumber ASC ");
            break;
          case EVIDENCE_NUMBER_DESC:
            order.add(" contract.evidenceNumber DESC ");
            break;
          case NAME_ASC:
            order.add(" contract.name ASC ");
            break;
          case NAME_DESC:
            order.add(" contract.name DESC ");
            break;
          default:
            log.warn("Unsupported order by option: " + orderBy);
            break;
          }
        }

        if (!order.isEmpty()) {
          jpql.append(" ORDER BY ");
          jpql.append(StringUtils.join(order, ","));
        }
      }

    }

    // Vytvoreni dotazu
    TypedQuery<Contract> query = em.createQuery(jpql.toString(), Contract.class);
    if (offset != null) {
      query.setFirstResult(offset.intValue());
    }
    if (limit != null) {
      query.setMaxResults(limit.intValue());
    }

    // parameters
    if (criteria != null) {
      if (criteria.getCustomerId() != null) {
        query.setParameter("customerId", criteria.getCustomerId());
      }

      if (criteria.getContractIds() != null) {
        query.setParameter("contractIds", criteria.getContractIds());
      }
    }

    return query.getResultList();
  }

  /**
   * 
   */
  @Override
  public Contract saveContract(Contract contract) {
    if (contract.getId() == null) {
      em.persist(contract);
    } else {
      contract = em.merge(contract);
    }

    em.flush();

    return contract;
  }

  /**
   * Najde vsechny kontrakty, jejichz servery maji alespon jedno nevyfakturovane DailyUsage nejpozdeji v pozadovanem dni.
   * 
   * Server musi byt kontraktu prirazen take nejpozdeji v pozadovanem dni.
   * 
   * Pro kontrakt nesmi existovat faktura se stejnym pozadovanym dnem.
   * 
   * Vraceny budou pouze ty kontrakty, ktere maji typ fakturace (v pozadovanem dni) odpovidajici predanemu parametru invoiceTypeIds.
   * 
   * Ke kazdemu kontraktu pripoji Typ uctovani, ktery ma byt pouzit.
   */
  @Override
  public List<Object[]> findUninvoicedContracts(Date lastDay, List<Integer> invoiceTypeIds) {
    StringBuilder jpql = new StringBuilder();
    jpql.append(" SELECT DISTINCT contract, invoiceType ");
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

    TypedQuery<Object[]> query = em.createQuery(jpql.toString(), Object[].class);
    query.setParameter("lastDay", lastDay);
    query.setParameter("invoiceTypeIds", invoiceTypeIds);

    return query.getResultList();
  }

}
