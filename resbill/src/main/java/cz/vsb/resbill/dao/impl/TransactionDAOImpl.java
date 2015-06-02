/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;

import cz.vsb.resbill.dao.TransactionDAO;
import cz.vsb.resbill.model.Transaction;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */

@Repository
public class TransactionDAOImpl implements TransactionDAO {

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
