/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.service.impl;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.vsb.resbill.criteria.TransactionTypeCriteria;
import cz.vsb.resbill.dao.TransactionTypeDAO;
import cz.vsb.resbill.exception.ResBillException;
import cz.vsb.resbill.model.TransactionType;
import cz.vsb.resbill.service.ResBillService;
import cz.vsb.resbill.service.TransactionTypeService;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */

@ResBillService
public class TransactionTypeServiceImpl implements TransactionTypeService {

  private static final Logger log = LoggerFactory.getLogger(TransactionTypeServiceImpl.class);

  @Inject
  private TransactionTypeDAO  transactionTypeDAO;

  /**
   * 
   */
  @Override
  public TransactionType findTransactionType(Integer transactionTypeId) throws ResBillException {
    try {
      return transactionTypeDAO.findTransactionType(transactionTypeId);
    } catch (Exception exc) {
      log.error("An unexpected error occured while finding TransactionType by id=" + transactionTypeId, exc);
      throw new ResBillException(exc);
    }
  }

  /**
   * 
   */
  @Override
  public List<TransactionType> findTransactionTypes(TransactionTypeCriteria criteria, Integer offset, Integer limit) throws ResBillException {
    try {
      return transactionTypeDAO.findTransactionTypes(criteria, offset, limit);
    } catch (Exception exc) {
      log.error("An unexpected error occured while finding TransactionTypes.", exc);
      throw new ResBillException(exc);
    }
  }

}
