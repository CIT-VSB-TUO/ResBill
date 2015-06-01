/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.dao;

import cz.vsb.resbill.model.TransactionType;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */
public interface TransactionTypeDAO {

  TransactionType findTransactionType(Integer id);
}
