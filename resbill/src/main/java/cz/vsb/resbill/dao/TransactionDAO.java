/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.dao;

import java.util.List;

import cz.vsb.resbill.criteria.TransactionCriteria;
import cz.vsb.resbill.model.Transaction;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */
public interface TransactionDAO {

  Transaction findTransaction(Integer id);

  List<Transaction> findTransactions(TransactionCriteria criteria, Integer offset, Integer limit);

  Transaction saveTransaction(Transaction transaction);

  Transaction deleteTransaction(Transaction transaction);

  /**
   * Pro pozadovany kontrakt najde nejblizsi volne poradove cislo trasnakce v ramci tohoto kontraktu
   * 
   * @param contractId
   * @return
   */
  Integer getNextOrder(Integer contractId);

}
