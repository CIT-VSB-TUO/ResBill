/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.dto.invoice;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;

import cz.vsb.resbill.util.ToStringBuilder;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */
public class InvoiceExportServerDTO implements Serializable {

  /**
   * 
   */
  private static final long           serialVersionUID         = 1L;

  private String                      name                     = null;

  private Float                       price                    = null;

  private Date                        invoiceEndDate           = null;

  private List<InvoiceExportUsageDTO> cpuUsages                = null;

  private List<InvoiceExportUsageDTO> memoryGbUsages           = null;

  private List<InvoiceExportUsageDTO> provisionedSpaceGbUsages = null;

  private List<InvoiceExportUsageDTO> backupGbUsages           = null;

  /**
   * Vrati true, pokud alespon jedna polozka zalohovani ma nenulovou platbu
   * 
   * @return
   */
  public boolean isBackupWithPrice() {
    List<InvoiceExportUsageDTO> backupGbUsages = getBackupGbUsages();

    if (backupGbUsages != null) {
      for (InvoiceExportUsageDTO invoiceExportUsageDTO : backupGbUsages) {
        if (invoiceExportUsageDTO.getPrice() != null && invoiceExportUsageDTO.getPrice().floatValue() != 0) {
          return true;
        }
      }
    }

    return false;
  }

  /**
   * Vrati posledni den spotreby, ale pouze v pripade, ze tento den je ostre mensi nez koncove datum faktury.
   * 
   * @return
   */
  public Date getUsageTerminationDate() {
    Date invoiceEndDate = getInvoiceEndDate();
    TreeSet<Date> dates = new TreeSet<Date>();

    List<InvoiceExportUsageDTO> cpuUsages = getCpuUsages();
    if (cpuUsages != null) {
      for (InvoiceExportUsageDTO usage : cpuUsages) {
        dates.add(usage.getEndDate());
      }
    }

    List<InvoiceExportUsageDTO> memoryGbUsages = getMemoryGbUsages();
    if (memoryGbUsages != null) {
      for (InvoiceExportUsageDTO usage : memoryGbUsages) {
        dates.add(usage.getEndDate());
      }
    }

    List<InvoiceExportUsageDTO> provisionedSpaceGbUsages = getProvisionedSpaceGbUsages();
    if (provisionedSpaceGbUsages != null) {
      for (InvoiceExportUsageDTO usage : provisionedSpaceGbUsages) {
        dates.add(usage.getEndDate());
      }
    }
    List<InvoiceExportUsageDTO> backupGbUsages = getBackupGbUsages();
    if (backupGbUsages != null) {
      for (InvoiceExportUsageDTO usage : backupGbUsages) {
        dates.add(usage.getEndDate());
      }
    }

    Date maxDate = dates.last();
    if (maxDate.before(invoiceEndDate)) {
      return maxDate;
    } else {
      return null;
    }
  }

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * @param name
   *          the name to set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * @return the price
   */
  public Float getPrice() {
    return price;
  }

  /**
   * @param price
   *          the price to set
   */
  public void setPrice(Float price) {
    this.price = price;
  }

  /**
   * @return the cpuUsages
   */
  public List<InvoiceExportUsageDTO> getCpuUsages() {
    return cpuUsages;
  }

  /**
   * @param cpuUsages
   *          the cpuUsages to set
   */
  public void setCpuUsages(List<InvoiceExportUsageDTO> cpuUsages) {
    this.cpuUsages = cpuUsages;
  }

  /**
   * @return the memoryGbUsages
   */
  public List<InvoiceExportUsageDTO> getMemoryGbUsages() {
    return memoryGbUsages;
  }

  /**
   * @param memoryGbUsages
   *          the memoryGbUsages to set
   */
  public void setMemoryGbUsages(List<InvoiceExportUsageDTO> memoryGbUsages) {
    this.memoryGbUsages = memoryGbUsages;
  }

  /**
   * @return the provisionedSpaceGbUsages
   */
  public List<InvoiceExportUsageDTO> getProvisionedSpaceGbUsages() {
    return provisionedSpaceGbUsages;
  }

  /**
   * @param provisionedSpaceGbUsages
   *          the provisionedSpaceGbUsages to set
   */
  public void setProvisionedSpaceGbUsages(List<InvoiceExportUsageDTO> provisionedSpaceGbUsages) {
    this.provisionedSpaceGbUsages = provisionedSpaceGbUsages;
  }

  /**
   * @return the backupGbUsages
   */
  public List<InvoiceExportUsageDTO> getBackupGbUsages() {
    return backupGbUsages;
  }

  /**
   * @param backupGbUsages
   *          the backupGbUsages to set
   */
  public void setBackupGbUsages(List<InvoiceExportUsageDTO> backupGbUsages) {
    this.backupGbUsages = backupGbUsages;
  }

  /**
   * @return the invoiceEndDate
   */
  public Date getInvoiceEndDate() {
    return invoiceEndDate;
  }

  /**
   * @param invoiceEndDate
   *          the invoiceEndDate to set
   */
  public void setInvoiceEndDate(Date invoiceEndDate) {
    this.invoiceEndDate = invoiceEndDate;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    ToStringBuilder builder = new ToStringBuilder(this);
    builder.append("name", name);
    builder.append("price", price);
    builder.append("invoiceEndDate", invoiceEndDate);
    builder.append("cpuUsages", cpuUsages);
    builder.append("memoryGbUsages", memoryGbUsages);
    builder.append("provisionedSpaceGbUsages", provisionedSpaceGbUsages);
    builder.append("backupGbUsages", backupGbUsages);
    builder.append("toString()", super.toString());
    return builder.toString();
  }

}
