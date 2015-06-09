/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.service;

import java.util.List;

import cz.vsb.resbill.criteria.TransactionCriteria;
import cz.vsb.resbill.dto.TransactionDTO;
import cz.vsb.resbill.exception.ResBillException;
import cz.vsb.resbill.exception.TransactionServiceException;
import cz.vsb.resbill.model.Transaction;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */
public interface TransactionService {

  Transaction findTransaction(Integer transactionId) throws ResBillException;

  List<Transaction> findTransactions(TransactionCriteria criteria, Integer offset, Integer limit) throws ResBillException;

  List<TransactionDTO> findTransactionDTOs(TransactionCriteria criteria, Integer offset, Integer limit) throws ResBillException;

  Transaction saveTransaction(Transaction transaction) throws TransactionServiceException, ResBillException;

  Transaction deleteTransaction(Integer transactionId) throws ResBillException;
}
