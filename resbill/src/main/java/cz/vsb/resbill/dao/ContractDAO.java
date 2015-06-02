/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.dao;

import java.util.Date;
import java.util.List;

import cz.vsb.resbill.model.Contract;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */
public interface ContractDAO {

  /**
   * Najde vsechny kontrakty, jejichz servery maji alespon jedno nevyfakturovane DailyUsage nejpozdeji v pozadovanem dni.
   * 
   * Server musi byt kontraktu prirazen take nejpozdeji v pozadovanem dni.
   * 
   * Pro kontrakt nesmi existovat faktura se stejnym pozadovanym dnem.
   * 
   * Vraceny budou pouze ty kontrakty, ktere maji typ fakturace (v pozadovanem dni) odpovidajici predanemu parametru invoiceTypeIds.
   * 
   * Ke kazdemu kontraktu pripoji Typ uctovani, ktery ma byt pouzit.
   */
  List<Object[]> findUninvoicedContracts(Date lastDay, List<Integer> invoiceTypeIds);

  Contract saveContract(Contract contract);
}
