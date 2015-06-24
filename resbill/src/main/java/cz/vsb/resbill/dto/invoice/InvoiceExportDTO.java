/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.dto.invoice;

import java.io.Serializable;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

import cz.vsb.resbill.model.Contract;
import cz.vsb.resbill.model.Customer;
import cz.vsb.resbill.model.Invoice;
import cz.vsb.resbill.model.Person;
import cz.vsb.resbill.util.ToStringBuilder;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */
public class InvoiceExportDTO implements Serializable {

  /**
   * 
   */
  private static final long            serialVersionUID = 1L;

  private Long                         evidenceNumber   = null;

  private String                       customerName     = null;

  private String                       contractName     = null;

  private Date                         beginDate        = null;

  private Date                         endDate          = null;

  private Float                        beginBalance     = null;

  private Float                        endBalance       = null;

  private Float                        amount           = null;

  private String                       contactName      = null;

  private String                       contactPhone     = null;

  private String                       contactEmail     = null;

  private List<InvoiceExportServerDTO> servers          = null;

  /**
   * 
   */
  public InvoiceExportDTO() {
    super();
  }

  /**
   * 
   */
  public InvoiceExportDTO(Invoice invoice) {
    this();

    fill(invoice);
  }

  /**
   * 
   * @param person
   */
  public void fill(Invoice invoice) {
    evidenceNumber = invoice.getEvidenceNumber();
    beginDate = invoice.getPeriod().getBeginDate();
    endDate = invoice.getPeriod().getEndDate();
    amount = invoice.getAmount().setScale(2, RoundingMode.HALF_UP).floatValue();

    Contract contract = invoice.getContract();
    contractName = contract.getName();
    endBalance = contract.getBalance().setScale(2, RoundingMode.HALF_UP).floatValue();
    beginBalance = endBalance - amount;

    Customer customer = contract.getCustomer();
    customerName = customer.getName();

    Person contact = customer.getContactPerson();
    contactName = contact.getFullNameWithTitles();
    contactPhone = contact.getPhone();
    contactEmail = contact.getEmail();
  }

  /**
   * @return the beginBalance
   */
  public Float getBeginBalance() {
    return beginBalance;
  }

  /**
   * @param beginBalance
   *          the beginBalance to set
   */
  public void setBeginBalance(Float beginBalance) {
    this.beginBalance = beginBalance;
  }

  /**
   * @return the endBalance
   */
  public Float getEndBalance() {
    return endBalance;
  }

  /**
   * @param endBalance
   *          the endBalance to set
   */
  public void setEndBalance(Float endBalance) {
    this.endBalance = endBalance;
  }

  /**
   * @return the amount
   */
  public Float getAmount() {
    return amount;
  }

  /**
   * @param amount
   *          the amount to set
   */
  public void setAmount(Float amount) {
    this.amount = amount;
  }

  /**
   * @return the evidenceNumber
   */
  public Long getEvidenceNumber() {
    return evidenceNumber;
  }

  /**
   * @param evidenceNumber
   *          the evidenceNumber to set
   */
  public void setEvidenceNumber(Long evidenceNumber) {
    this.evidenceNumber = evidenceNumber;
  }

  /**
   * @return the customerName
   */
  public String getCustomerName() {
    return customerName;
  }

  /**
   * @param customerName
   *          the customerName to set
   */
  public void setCustomerName(String customerName) {
    this.customerName = customerName;
  }

  /**
   * @return the contractName
   */
  public String getContractName() {
    return contractName;
  }

  /**
   * @param contractName
   *          the contractName to set
   */
  public void setContractName(String contractName) {
    this.contractName = contractName;
  }

  /**
   * @return the beginDate
   */
  public Date getBeginDate() {
    return beginDate;
  }

  /**
   * @param beginDate
   *          the beginDate to set
   */
  public void setBeginDate(Date beginDate) {
    this.beginDate = beginDate;
  }

  /**
   * @return the endDate
   */
  public Date getEndDate() {
    return endDate;
  }

  /**
   * @param endDate
   *          the endDate to set
   */
  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  /**
   * @return the contactName
   */
  public String getContactName() {
    return contactName;
  }

  /**
   * @param contactName
   *          the contactName to set
   */
  public void setContactName(String contactName) {
    this.contactName = contactName;
  }

  /**
   * @return the contactPhone
   */
  public String getContactPhone() {
    return contactPhone;
  }

  /**
   * @param contactPhone
   *          the contactPhone to set
   */
  public void setContactPhone(String contactPhone) {
    this.contactPhone = contactPhone;
  }

  /**
   * @return the contactEmail
   */
  public String getContactEmail() {
    return contactEmail;
  }

  /**
   * @param contactEmail
   *          the contactEmail to set
   */
  public void setContactEmail(String contactEmail) {
    this.contactEmail = contactEmail;
  }

  /**
   * @return the servers
   */
  public List<InvoiceExportServerDTO> getServers() {
    return servers;
  }

  /**
   * @param servers
   *          the servers to set
   */
  public void setServers(List<InvoiceExportServerDTO> servers) {
    this.servers = servers;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    ToStringBuilder builder = new ToStringBuilder(this);
    builder.append("evidenceNumber", evidenceNumber);
    builder.append("customerName", customerName);
    builder.append("contractName", contractName);
    builder.append("beginDate", beginDate);
    builder.append("endDate", endDate);
    builder.append("beginBalance", beginBalance);
    builder.append("endBalance", endBalance);
    builder.append("amount", amount);
    builder.append("contactName", contactName);
    builder.append("contactPhone", contactPhone);
    builder.append("contactEmail", contactEmail);
    builder.append("servers", servers);
    builder.append("toString()", super.toString());
    return builder.toString();
  }

}
