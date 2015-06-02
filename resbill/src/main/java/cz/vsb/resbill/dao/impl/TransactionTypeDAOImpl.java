/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import cz.vsb.resbill.dao.TransactionTypeDAO;
import cz.vsb.resbill.model.TransactionType;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */

@Repository
public class TransactionTypeDAOImpl implements TransactionTypeDAO {

  @PersistenceContext
  private EntityManager em;

  @Override
  public TransactionType findTransactionType(Integer id) {
    return em.find(TransactionType.class, id);
  }

}
