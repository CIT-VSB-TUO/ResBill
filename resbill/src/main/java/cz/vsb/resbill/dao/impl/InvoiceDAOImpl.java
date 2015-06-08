/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import cz.vsb.resbill.criteria.InvoiceCriteria;
import cz.vsb.resbill.dao.InvoiceDAO;
import cz.vsb.resbill.model.Invoice;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */

@Repository
public class InvoiceDAOImpl implements InvoiceDAO {

  private static final Logger log = LoggerFactory.getLogger(InvoiceDAOImpl.class);

  @PersistenceContext
  private EntityManager       em;

  /**
	 * 
	 */
  @Override
  public Invoice findInvoice(Integer id) {
    return em.find(Invoice.class, id);
  }

  /**
	 * 
	 */
  @Override
  public List<Invoice> findInvoices(InvoiceCriteria criteria, Integer offset, Integer limit) {
    StringBuilder jpql = new StringBuilder();
    jpql.append(" SELECT invoice ");
    jpql.append(" FROM Invoice AS invoice ");
    if (criteria.needsContract()) {
      jpql.append(" JOIN invoice.contract AS contract ");
    }
    if (criteria.needsCustomer()) {
      jpql.append(" JOIN contract.customer AS customer ");
    }
    jpql.append(" WHERE 1 = 1 ");
    if (criteria.getBeginEndDate() != null) {
      jpql.append(" AND invoice.period.endDate >= :beginEndDate ");
    }
    if (criteria.getEndEndDate() != null) {
      jpql.append(" AND invoice.period.endDate <= :endEndDate ");
    }

    // Order by
    if (criteria.getOrderBy() != null && !criteria.getOrderBy().isEmpty()) {
      List<String> order = new ArrayList<String>();
      for (InvoiceCriteria.OrderBy orderBy : criteria.getOrderBy()) {
        switch (orderBy) {
        case ORDER_ASC:
          order.add(" invoice.order ASC ");
          break;
        case ORDER_DESC:
          order.add(" invoice.order DESC ");
          break;
        case DECISIVE_DATE_ASC:
          order.add(" invoice.decisiveDate ASC ");
          break;
        case DECISIVE_DATE_DESC:
          order.add(" invoice.decisiveDate DESC ");
          break;
        case CONTRACT_NAME_ASC:
          order.add(" contract.name ASC ");
          break;
        case CONTRACT_NAME_DESC:
          order.add(" contract.name DESC ");
          break;
        case CUSTOMER_NAME_ASC:
          order.add(" customer.name ASC ");
          break;
        case CUSTOMER_NAME_DESC:
          order.add(" customer.name DESC ");
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

    // Vytvoreni dotazu
    TypedQuery<Invoice> query = em.createQuery(jpql.toString(), Invoice.class);
    if (offset != null) {
      query.setFirstResult(offset);
    }
    if (limit != null) {
      query.setMaxResults(limit);
    }

    if (criteria.getBeginEndDate() != null) {
      query.setParameter("beginEndDate", criteria.getBeginEndDate());
    }
    if (criteria.getEndEndDate() != null) {
      query.setParameter("endEndDate", criteria.getEndEndDate());
    }

    return query.getResultList();
  }

}
