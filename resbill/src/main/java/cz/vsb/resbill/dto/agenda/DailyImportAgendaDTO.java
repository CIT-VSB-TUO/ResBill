/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.dto.agenda;

import cz.vsb.resbill.dto.DailyImportDTO;
import cz.vsb.resbill.util.ToStringBuilder;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */
public class DailyImportAgendaDTO extends DailyImportDTO {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  private boolean           hasErrors        = false;

  private boolean           hasWarns         = false;

  /**
   * @return the hasErrors
   */
  public boolean isHasErrors() {
    return hasErrors;
  }

  /**
   * @param hasErrors
   *          the hasErrors to set
   */
  public void setHasErrors(boolean hasErrors) {
    this.hasErrors = hasErrors;
  }

  /**
   * @return the hasWarns
   */
  public boolean isHasWarns() {
    return hasWarns;
  }

  /**
   * @param hasWarns
   *          the hasWarns to set
   */
  public void setHasWarns(boolean hasWarns) {
    this.hasWarns = hasWarns;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    ToStringBuilder builder = new ToStringBuilder(this);
    builder.append("hasErrors", hasErrors);
    builder.append("hasWarns", hasWarns);
    builder.append("toString()", super.toString());
    return builder.toString();
  }

}
