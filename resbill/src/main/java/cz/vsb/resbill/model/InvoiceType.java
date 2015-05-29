package cz.vsb.resbill.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import cz.vsb.resbill.util.ToStringBuilder;

@Entity
@Table(name = "INVOICE_TYPE")
public class InvoiceType extends BaseEnumeratedIdEntity {

  private static final long   serialVersionUID = -5801051324020018291L;

  /** Monthly produced invoices */
  public static final Integer MONTHLY          = 1;

  /** Quarterly produced invoices */
  public static final Integer QUARTERLY        = 2;

  /** Half-yearly produced invoices */
  public static final Integer HALF_YEARLY      = 3;

  /** Annually produced invoices */
  public static final Integer ANNUALLY         = 4;

  /** The title of the type */
  @Column(name = "title")
  @NotEmpty
  @Size(max = 100)
  private String              title;

  @Column(name = "divisor")
  @NotNull
  private Integer             divisor;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * @return the divisor
   */
  public Integer getDivisor() {
    return divisor;
  }

  /**
   * @param divisor
   *          the divisor to set
   */
  public void setDivisor(Integer divisor) {
    this.divisor = divisor;
  }

  /**
   * 
   */
  @Override
  public String toString() {
    ToStringBuilder builder = new ToStringBuilder(this);
    builder.append("title", title);
    builder.append("divisor", divisor);
    builder.append("toString()", super.toString());
    return builder.toString();
  }

}
