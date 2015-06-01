/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.dao;

import java.util.List;

import cz.vsb.resbill.criteria.InvoiceTypeCriteria;
import cz.vsb.resbill.model.InvoiceType;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */
public interface InvoiceTypeDAO {

  InvoiceType findInvoiceType(Integer id);

  List<InvoiceType> findInvoiceTypes(InvoiceTypeCriteria criteria, Integer offset, Integer limit);
}
