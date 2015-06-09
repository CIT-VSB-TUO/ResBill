/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.service;

import java.util.List;

import cz.vsb.resbill.criteria.TransactionTypeCriteria;
import cz.vsb.resbill.exception.ResBillException;
import cz.vsb.resbill.model.TransactionType;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */
public interface TransactionTypeService {

  TransactionType findTransactionType(Integer transactionId) throws ResBillException;

  List<TransactionType> findTransactionTypes(TransactionTypeCriteria criteria, Integer offset, Integer limit) throws ResBillException;
}
