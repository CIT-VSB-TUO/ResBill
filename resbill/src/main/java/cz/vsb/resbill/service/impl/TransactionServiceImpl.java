/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.service.impl;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.vsb.resbill.dao.ContractDAO;
import cz.vsb.resbill.dao.TransactionDAO;
import cz.vsb.resbill.exception.ResBillException;
import cz.vsb.resbill.model.Contract;
import cz.vsb.resbill.model.Transaction;
import cz.vsb.resbill.service.TransactionService;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */
public class TransactionServiceImpl implements TransactionService {

  private static final Logger log = LoggerFactory.getLogger(TransactionServiceImpl.class);

  @Inject
  private TransactionDAO      transactionDAO;

  @Inject
  private ContractDAO         contractDAO;

  /**
   * 
   */
  @Override
  public Transaction deleteTransaction(Integer transactionId) throws ResBillException {
    try {

      Transaction transaction = transactionDAO.findTransaction(transactionId);
      if (transaction != null) {
        // Odectu ucetni polozku ze stavu uctu kontraktu
        Contract contract = transaction.getContract();
        contract.setBalance(contract.getBalance().subtract(transaction.getAmount()));
        contractDAO.saveContract(contract);

        // Smazu ucetni polozku
        transaction = transactionDAO.deleteTransaction(transaction);
      }

      return transaction;

    } catch (Exception exc) {
      log.error("An unexpected error occured while deleting Transaction with id=" + transactionId, exc);
      throw new ResBillException(exc);
    }
  }

}
