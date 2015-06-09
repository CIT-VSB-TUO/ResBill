/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.exception;

import cz.vsb.resbill.util.ToStringBuilder;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */
public class TransactionServiceException extends ResBillException {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  private Reason            reason;

  /**
   * 
   * @param reason
   */
  public TransactionServiceException(Reason reason) {
    super();
    setReason(reason);
  }

  /**
   * 
   * @param reason
   * @param message
   * @param cause
   */
  public TransactionServiceException(Reason reason, String message, Throwable cause) {
    super(message, cause);
    setReason(reason);
  }

  /**
   * 
   * @param reason
   * @param message
   */
  public TransactionServiceException(Reason reason, String message) {
    super(message);
    setReason(reason);
  }

  /**
   * 
   * @param reason
   * @param cause
   */
  public TransactionServiceException(Reason reason, Throwable cause) {
    super(cause);
    setReason(reason);
  }

  public Reason getReason() {
    return reason;
  }

  public void setReason(Reason reason) {
    this.reason = reason;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    ToStringBuilder builder = new ToStringBuilder(this);
    builder.append("reason", reason);
    builder.append("toString()", super.toString());
    return builder.toString();
  }

  /**
   * 
   * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
   *
   */
  public static enum Reason {
    /**
     * Editace ucetnich polozek neni povolena (mohou se pouze zakladat)
     */
    EDIT_NOT_ALLOWED,
    
    /**
     * Vytvareni ani editace ucetnich polozek typu "faktura" neni povolena (vytvari se jinde)
     */
    INVOICE_NOT_ALLOWED,
    
    /**
     * Ucetni polozka typu "dosla platba" muze mit pouze kladnou hodnotu
     */
    INCOMING_PAYMENT_NO_POSITIVE,
  }
}
