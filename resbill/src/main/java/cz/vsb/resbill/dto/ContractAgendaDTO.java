/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.dto;

import cz.vsb.resbill.util.ToStringBuilder;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */
public class ContractAgendaDTO extends ContractDTO {

  /**
   * 
   */
  private static final long serialVersionUID            = 1L;

  private boolean           dailyUsagesAfterContractEnd = false;

  private boolean           dailyUsagesOutOfTariff      = false;

  private boolean           noInvoiceType               = false;

  private boolean           noServer                    = false;

  private boolean           noTariff                    = false;

  private boolean           negativeBalance             = false;

  private boolean           positiveBalance             = false;

  /**
   * @return the dailyUsagesAfterContractEnd
   */
  public boolean isDailyUsagesAfterContractEnd() {
    return dailyUsagesAfterContractEnd;
  }

  /**
   * @param dailyUsagesAfterContractEnd
   *          the dailyUsagesAfterContractEnd to set
   */
  public void setDailyUsagesAfterContractEnd(boolean dailyUsagesAfterContractEnd) {
    this.dailyUsagesAfterContractEnd = dailyUsagesAfterContractEnd;
  }

  /**
   * @return the dailyUsagesOutOfTariff
   */
  public boolean isDailyUsagesOutOfTariff() {
    return dailyUsagesOutOfTariff;
  }

  /**
   * @param dailyUsagesOutOfTariff
   *          the dailyUsagesOutOfTariff to set
   */
  public void setDailyUsagesOutOfTariff(boolean dailyUsagesOutOfTariff) {
    this.dailyUsagesOutOfTariff = dailyUsagesOutOfTariff;
  }

  /**
   * @return the noInvoiceType
   */
  public boolean isNoInvoiceType() {
    return noInvoiceType;
  }

  /**
   * @param noInvoiceType
   *          the noInvoiceType to set
   */
  public void setNoInvoiceType(boolean noInvoiceType) {
    this.noInvoiceType = noInvoiceType;
  }

  /**
   * @return the noServer
   */
  public boolean isNoServer() {
    return noServer;
  }

  /**
   * @param noServer
   *          the noServer to set
   */
  public void setNoServer(boolean noServer) {
    this.noServer = noServer;
  }

  /**
   * @return the noTariff
   */
  public boolean isNoTariff() {
    return noTariff;
  }

  /**
   * @param noTariff
   *          the noTariff to set
   */
  public void setNoTariff(boolean noTariff) {
    this.noTariff = noTariff;
  }

  /**
   * @return the negativeBalance
   */
  public boolean isNegativeBalance() {
    return negativeBalance;
  }

  /**
   * @param negativeBalance
   *          the negativeBalance to set
   */
  public void setNegativeBalance(boolean negativeBalance) {
    this.negativeBalance = negativeBalance;
  }

  /**
   * @return the positiveBalance
   */
  public boolean isPositiveBalance() {
    return positiveBalance;
  }

  /**
   * @param positiveBalance
   *          the positiveBalance to set
   */
  public void setPositiveBalance(boolean positiveBalance) {
    this.positiveBalance = positiveBalance;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    ToStringBuilder builder = new ToStringBuilder(this);
    builder.append("dailyUsagesAfterContractEnd", dailyUsagesAfterContractEnd);
    builder.append("dailyUsagesOutOfTariff", dailyUsagesOutOfTariff);
    builder.append("noInvoiceType", noInvoiceType);
    builder.append("noServer", noServer);
    builder.append("noTariff", noTariff);
    builder.append("negativeBalance", negativeBalance);
    builder.append("positiveBalance", positiveBalance);
    builder.append("toString()", super.toString());
    return builder.toString();
  }

}
