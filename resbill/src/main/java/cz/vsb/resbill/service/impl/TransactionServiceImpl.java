/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.vsb.resbill.criteria.TransactionCriteria;
import cz.vsb.resbill.dao.ContractDAO;
import cz.vsb.resbill.dao.TransactionDAO;
import cz.vsb.resbill.dto.InvoiceDTO;
import cz.vsb.resbill.dto.TransactionDTO;
import cz.vsb.resbill.exception.ResBillException;
import cz.vsb.resbill.model.Contract;
import cz.vsb.resbill.model.Invoice;
import cz.vsb.resbill.model.Transaction;
import cz.vsb.resbill.service.ResBillService;
import cz.vsb.resbill.service.TransactionService;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */

@ResBillService
public class TransactionServiceImpl implements TransactionService {

  private static final Logger log = LoggerFactory.getLogger(TransactionServiceImpl.class);

  @Inject
  private TransactionDAO      transactionDAO;

  @Inject
  private ContractDAO         contractDAO;

  /**
   * 
   * @param transactionId
   * @return
   */
  @Override
  public Transaction findTransaction(Integer transactionId) throws ResBillException {
    try {
      Transaction transaction = transactionDAO.findTransaction(transactionId);

      return transaction;
    } catch (Exception exc) {
      log.error("An unexpected error occured while finding Transaction by id=" + transactionId, exc);
      throw new ResBillException(exc);
    }
  }

  /**
   * 
   */
  @Override
  public List<Transaction> findTransactions(TransactionCriteria criteria, Integer offset, Integer limit) throws ResBillException {
    try {
      return transactionDAO.findTransactions(criteria, offset, limit);
    } catch (Exception exc) {
      log.error("An unexpected error occured while finding Transactions.", exc);
      throw new ResBillException(exc);
    }
  }

  /**
   * 
   */
  @Override
  public List<TransactionDTO> findTransactionDTOs(TransactionCriteria criteria, Integer offset, Integer limit) throws ResBillException {
    try {
      List<Transaction> transactions = findTransactions(criteria, offset, limit);
      List<TransactionDTO> dtos = new ArrayList<TransactionDTO>();

      for (Transaction transaction : transactions) {
        TransactionDTO dto = new TransactionDTO();
        dto.fill(transaction);
        dtos.add(dto);
      }

      return dtos;
    } catch (Exception exc) {
      log.error("An unexpected error occured while finding TransactionDTOs.", exc);
      throw new ResBillException(exc);
    }
  }

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
