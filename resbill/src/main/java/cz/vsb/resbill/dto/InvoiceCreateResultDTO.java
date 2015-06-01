/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.dto;

import java.io.Serializable;
import java.util.Date;

import cz.vsb.resbill.util.ToStringBuilder;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */
public class InvoiceCreateResultDTO implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID           = 1L;

  /**
   * Cas spusteni fakturace
   */
  private Date              beginTimestamp             = null;

  /**
   * Cas ukonceni fakturace
   */
  private Date              endTimestamp               = null;

  /**
   * Pocet zpracovavanych kontraktu, tj. pocet kontraktu, ktere maji za zpracovavane obdobi alespon jedno doposud nefakturovane DailyUsage
   */
  private int               contractsNumberAll         = 0;

  /**
   * Pocet uspesne vyfakturovanych kontraktu
   */
  private int               contractsNumberOk          = 0;

  /**
   * Pocet zpracovanych kontraktu, kde nedoslo k fakturaci z duvodu neexistujiciho ceniku
   */
  private int               contractsNumberNoPriceList = 0;

  /**
   * Pocet zpracovanych kontraktu, kde doslo k nejake jine chybe
   */
  private int               contractsNumberError       = 0;

  /**
   * @return the beginTimestamp
   */
  public Date getBeginTimestamp() {
    return beginTimestamp;
  }

  /**
   * @param beginTimestamp
   *          the beginTimestamp to set
   */
  public void setBeginTimestamp(Date beginTimestamp) {
    this.beginTimestamp = beginTimestamp;
  }

  /**
   * @return the endTimestamp
   */
  public Date getEndTimestamp() {
    return endTimestamp;
  }

  /**
   * @param endTimestamp
   *          the endTimestamp to set
   */
  public void setEndTimestamp(Date endTimestamp) {
    this.endTimestamp = endTimestamp;
  }

  /**
   * @return the contractsNumberAll
   */
  public int getContractsNumberAll() {
    return contractsNumberAll;
  }

  /**
   * @param contractsNumberAll
   *          the contractsNumberAll to set
   */
  public void setContractsNumberAll(int contractsNumberAll) {
    this.contractsNumberAll = contractsNumberAll;
  }

  /**
   * @return the contractsNumberNoPriceList
   */
  public int getContractsNumberNoPriceList() {
    return contractsNumberNoPriceList;
  }

  /**
   * @param contractsNumberNoPriceList
   *          the contractsNumberNoPriceList to set
   */
  public void setContractsNumberNoPriceList(int contractsNumberNoPriceList) {
    this.contractsNumberNoPriceList = contractsNumberNoPriceList;
  }

  /**
   * @return the contractsNumberError
   */
  public int getContractsNumberError() {
    return contractsNumberError;
  }

  /**
   * @param contractsNumberError
   *          the contractsNumberError to set
   */
  public void setContractsNumberError(int contractsNumberError) {
    this.contractsNumberError = contractsNumberError;
  }

  /**
   * @return the contractsNumberOk
   */
  public int getContractsNumberOk() {
    return contractsNumberOk;
  }

  /**
   * @param contractsNumberOk
   *          the contractsNumberOk to set
   */
  public void setContractsNumberOk(int contractsNumberOk) {
    this.contractsNumberOk = contractsNumberOk;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    ToStringBuilder builder = new ToStringBuilder(this);
    builder.append("beginTimestamp", beginTimestamp);
    builder.append("endTimestamp", endTimestamp);
    builder.append("contractsNumberAll", contractsNumberAll);
    builder.append("contractsNumberOk", contractsNumberOk);
    builder.append("contractsNumberNoPriceList", contractsNumberNoPriceList);
    builder.append("contractsNumberError", contractsNumberError);
    builder.append("toString()", super.toString());
    return builder.toString();
  }

}
