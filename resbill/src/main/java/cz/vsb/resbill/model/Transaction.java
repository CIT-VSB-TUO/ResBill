package cz.vsb.resbill.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "disc", discriminatorType = DiscriminatorType.STRING, length = 3)
@DiscriminatorValue(value = "tra")
@Table(name = "TRANSACTION", uniqueConstraints = @UniqueConstraint(name = "UK_transaction__tx_order__contract_id", columnNames = { "tx_order", "contract_id" }))
public class Transaction extends BaseVersionedEntity {

  private static final long serialVersionUID             = 8668454272440856542L;

  public static final long  INVOICE_EVIDENCE_NUMBER_BASE = 2000000000;

  @Column(name = "tx_order", nullable = false)
  private Integer           order;

  @Column(name = "evidence_number")
  @Digits(integer = 10, fraction = 0)
  private Long              evidenceNumber;

  @Column(name = "decisive_date")
  @Temporal(TemporalType.DATE)
  @NotNull
  private Date              decisiveDate;

  @Column(name = "amount")
  @NotNull
  @Digits(integer = 14, fraction = 2)
  private BigDecimal        amount;

  @Column(name = "title")
  @Size(max = 250)
  private String            title;

  @Column(name = "note")
  @Size(max = 1000)
  private String            note;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "transaction_type_id", nullable = false, foreignKey = @ForeignKey(name = "FK_transaction__transaction_type"))
  private TransactionType   transactionType;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "contract_id", nullable = false, foreignKey = @ForeignKey(name = "FK_transaction__contract"))
  private Contract          contract;

  public Integer getOrder() {
    return order;
  }

  public void setOrder(Integer order) {
    this.order = order;
  }

  public Long getEvidenceNumber() {
    return evidenceNumber;
  }

  public void setEvidenceNumber(Long evidenceNumber) {
    this.evidenceNumber = evidenceNumber;
  }

  public Date getDecisiveDate() {
    return decisiveDate;
  }

  public void setDecisiveDate(Date decisiveDate) {
    this.decisiveDate = decisiveDate;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }

  public TransactionType getTransactionType() {
    return transactionType;
  }

  public void setTransactionType(TransactionType transactionType) {
    this.transactionType = transactionType;
  }

  public Contract getContract() {
    return contract;
  }

  public void setContract(Contract contract) {
    this.contract = contract;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("Transaction [");
    builder.append(super.toString());
    builder.append(", order=");
    builder.append(order);
    builder.append(", evidenceNumber=");
    builder.append(evidenceNumber);
    builder.append(", decisiveDate=");
    builder.append(decisiveDate);
    builder.append(", amount=");
    builder.append(amount);
    builder.append(", title=");
    builder.append(title);
    builder.append(", note=");
    builder.append(note);
    builder.append(", transactionType.id=");
    builder.append(transactionType != null ? transactionType.getId() : null);
    builder.append(", contract.id=");
    builder.append(contract != null ? contract.getId() : null);
    builder.append("]");
    return builder.toString();
  }

}
