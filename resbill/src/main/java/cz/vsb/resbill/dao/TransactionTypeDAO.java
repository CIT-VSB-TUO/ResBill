/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.dao;

import java.util.List;

import cz.vsb.resbill.criteria.TransactionTypeCriteria;
import cz.vsb.resbill.model.TransactionType;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */
public interface TransactionTypeDAO {

  TransactionType findTransactionType(Integer id);

  List<TransactionType> findTransactionTypes(TransactionTypeCriteria criteria, Integer offset, Integer limit);
}
