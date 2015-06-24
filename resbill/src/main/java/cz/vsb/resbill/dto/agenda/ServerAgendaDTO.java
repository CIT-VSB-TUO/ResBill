/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.dto.agenda;

import cz.vsb.resbill.dto.server.ServerDTO;
import cz.vsb.resbill.util.ToStringBuilder;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */
public class ServerAgendaDTO extends ServerDTO {

  /**
   * 
   */
  private static final long serialVersionUID         = 1L;

  private boolean           dailyUsagesOutOfContract = false;

  private boolean           noContract               = false;

  private boolean           noDailyUsage             = false;

  /**
   * @return the dailyUsagesOutOfContract
   */
  public boolean isDailyUsagesOutOfContract() {
    return dailyUsagesOutOfContract;
  }

  /**
   * @param dailyUsagesOutOfContract
   *          the dailyUsagesOutOfContract to set
   */
  public void setDailyUsagesOutOfContract(boolean dailyUsagesOutOfContract) {
    this.dailyUsagesOutOfContract = dailyUsagesOutOfContract;
  }

  /**
   * @return the noContract
   */
  public boolean isNoContract() {
    return noContract;
  }

  /**
   * @param noContract
   *          the noContract to set
   */
  public void setNoContract(boolean noContract) {
    this.noContract = noContract;
  }

  /**
   * @return the noDailyUsage
   */
  public boolean isNoDailyUsage() {
    return noDailyUsage;
  }

  /**
   * @param noDailyUsage
   *          the noDailyUsage to set
   */
  public void setNoDailyUsage(boolean noDailyUsage) {
    this.noDailyUsage = noDailyUsage;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    ToStringBuilder builder = new ToStringBuilder(this);
    builder.append("dailyUsagesOutOfContract", dailyUsagesOutOfContract);
    builder.append("noContract", noContract);
    builder.append("noDailyUsage", noDailyUsage);
    builder.append("toString()", super.toString());
    return builder.toString();
  }

}
