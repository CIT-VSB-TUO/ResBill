/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.dao;

import cz.vsb.resbill.model.Transaction;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */
public interface TransactionDAO {

  Transaction findTransaction(Integer id);

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
