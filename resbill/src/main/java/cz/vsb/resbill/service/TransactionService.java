/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.service;

import cz.vsb.resbill.exception.ResBillException;
import cz.vsb.resbill.model.Transaction;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */
public interface TransactionService {

  Transaction deleteTransaction(Integer transactionId) throws ResBillException;
}
