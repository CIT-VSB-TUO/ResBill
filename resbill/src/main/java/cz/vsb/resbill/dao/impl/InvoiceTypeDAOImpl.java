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

import cz.vsb.resbill.criteria.InvoiceTypeCriteria;
import cz.vsb.resbill.dao.InvoiceTypeDAO;
import cz.vsb.resbill.model.InvoiceType;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */

@Repository
public class InvoiceTypeDAOImpl implements InvoiceTypeDAO {

  @PersistenceContext
  private EntityManager em;

  /**
   * 
   */
  @Override
  public InvoiceType findInvoiceType(Integer id) {
    return em.find(InvoiceType.class, id);
  }

  /**
   * 
   */
  @Override
  public List<InvoiceType> findInvoiceTypes(InvoiceTypeCriteria criteria, Integer offset, Integer limit) {
    StringBuilder jpql = new StringBuilder();
    jpql.append(" SELECT invoiceType ");
    jpql.append(" FROM InvoiceType AS invoiceType ");
    jpql.append(" WHERE 1 = 1 ");
    if (criteria.getZeroModuloDividend() != null) {
      jpql.append(" AND MOD(:zeroModuloDividend, invoiceType.divisor) = 0 ");
    }
    if (criteria.getOrderBy() != null) {
      jpql.append(" ORDER BY ");
      if (criteria.getOrderBy() == InvoiceTypeCriteria.OrderBy.ID) {
        jpql.append(" invoiceType.id ");
      }
      if (criteria.isOrderDesc()) {
        jpql.append(" DESC ");
      }
    }

    TypedQuery<InvoiceType> query = em.createQuery(jpql.toString(), InvoiceType.class);
    if (offset != null) {
      query.setFirstResult(offset);
    }
    if (limit != null) {
      query.setMaxResults(limit);
    }

    if (criteria.getZeroModuloDividend() != null) {
      query.setParameter("zeroModuloDividend", criteria.getZeroModuloDividend());
    }

    return query.getResultList();
  }

}
