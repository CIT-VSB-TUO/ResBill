/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

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

  @PersistenceContext
  private EntityManager em;

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

    if (criteria.getOrderBy() != null && !criteria.getOrderBy().isEmpty()) {
      jpql.append(" ORDER BY ");

      StringBuilder order = new StringBuilder();
      for (InvoiceCriteria.OrderBy orderBy : criteria.getOrderBy()) {
        if (order.length() > 0) {
          order.append(", ");
        }

        switch (orderBy) {
        case ORDER_ASC:
          order.append(" invoice.order ASC ");
          break;
        case ORDER_DESC:
          order.append(" invoice.order DESC ");
          break;
        case DECISIVE_DATE_ASC:
          order.append(" invoice.decisiveDate ASC ");
          break;
        case DECISIVE_DATE_DESC:
          order.append(" invoice.decisiveDate DESC ");
          break;
        case CONTRACT_NAME_ASC:
          order.append(" contract.name ASC ");
          break;
        case CONTRACT_NAME_DESC:
          order.append(" contract.name DESC ");
          break;
        case CUSTOMER_NAME_ASC:
          order.append(" customer.name ASC ");
          break;
        case CUSTOMER_NAME_DESC:
          order.append(" customer.name DESC ");
          break;
        }
      }
      jpql.append(order);
    }

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
