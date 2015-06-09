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
import org.springframework.stereotype.Repository;

import cz.vsb.resbill.criteria.TransactionTypeCriteria;
import cz.vsb.resbill.dao.TransactionTypeDAO;
import cz.vsb.resbill.model.TransactionType;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */

@Repository
public class TransactionTypeDAOImpl implements TransactionTypeDAO {

  private static final Logger log = LoggerFactory.getLogger(TransactionTypeDAOImpl.class);

  @PersistenceContext
  private EntityManager       em;

  /**
   * 
   */
  @Override
  public TransactionType findTransactionType(Integer id) {
    return em.find(TransactionType.class, id);
  }

  /**
   * 
   */
  @Override
  public List<TransactionType> findTransactionTypes(TransactionTypeCriteria criteria, Integer offset, Integer limit) {
    StringBuilder jpql = new StringBuilder();
    jpql.append(" SELECT transactionType ");
    jpql.append(" FROM TransactionType AS transactionType ");

    // join a where
    Set<String> join = new LinkedHashSet<String>();
    Set<String> where = new LinkedHashSet<String>();

    // Order by
    Set<String> order = new LinkedHashSet<String>();
    if (criteria.getOrderBy() != null && !criteria.getOrderBy().isEmpty()) {

      for (TransactionTypeCriteria.OrderBy orderBy : criteria.getOrderBy()) {
        switch (orderBy) {
        case ID_ASC:
          order.add(" transactionType.id ASC ");
          break;
        case ID_DESC:
          order.add(" transactionType.id DESC ");
          break;
        case TITLE_ASC:
          order.add(" transactionType.title ASC ");
          break;
        case TITLE_DESC:
          order.add(" transactionType.title DESC ");
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
    TypedQuery<TransactionType> query = em.createQuery(jpql.toString(), TransactionType.class);
    if (offset != null) {
      query.setFirstResult(offset.intValue());
    }
    if (limit != null) {
      query.setMaxResults(limit.intValue());
    }

    return query.getResultList();
  }
}
