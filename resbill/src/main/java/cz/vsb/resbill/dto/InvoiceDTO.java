/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.dto;

import cz.vsb.resbill.model.Invoice;
import cz.vsb.resbill.util.ToStringBuilder;


/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */
public class InvoiceDTO extends TransactionDTO {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  
  private boolean noPriceList;

  /**
   * 
   * @param invoice
   */
  public void fill(Invoice invoice) {
    super.fill(invoice);
    
    noPriceList = invoice.getNoPriceList();
  }
  
  /**
   * @return the noPriceList
   */
  public boolean isNoPriceList() {
    return noPriceList;
  }

  
  /**
   * @param noPriceList the noPriceList to set
   */
  public void setNoPriceList(boolean noPriceList) {
    this.noPriceList = noPriceList;
  }


  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    ToStringBuilder builder = new ToStringBuilder(this);
    builder.append("noPriceList", noPriceList);
    builder.append("toString()", super.toString());
    return builder.toString();
  }
  
  

}
