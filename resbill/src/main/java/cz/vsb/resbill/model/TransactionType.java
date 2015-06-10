package cz.vsb.resbill.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "TRANSACTION_TYPE")
public class TransactionType extends BaseEnumeratedIdEntity {

  private static final long   serialVersionUID   = -502862889044300245L;

  /** Invoice */
  public static final Integer INVOICE            = 1;

  /** Incoming payment */
  public static final Integer INCOMING_PAYMENT   = 2;

  /** Additional payment */
  public static final Integer ADDITIONAL_PAYMENT = 3;

  @Column(name = "title")
  @NotEmpty
  @Size(max = 100)
  private String              title;

  @Column(name = "system_managed")
  @NotNull
  private Boolean             systemManaged      = Boolean.FALSE;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * @return the systemManaged
   */
  public Boolean getSystemManaged() {
    return systemManaged;
  }

  /**
   * @param systemManaged
   *          the systemManaged to set
   */
  public void setSystemManaged(Boolean systemManaged) {
    this.systemManaged = systemManaged;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("TransactionType [");
    builder.append(super.toString());
    builder.append(", title=");
    builder.append(title);
    builder.append(", systemManaged=");
    builder.append(systemManaged);
    builder.append("]");
    return builder.toString();
  }

}
