/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.dto.statistics;

import java.io.Serializable;

import cz.vsb.resbill.util.ToStringBuilder;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */
public abstract class StatisticDTO implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  private StatisticUsageDTO usageDTO         = null;
  
  /**
   * Vrati retezec identifikujici textove tento objekt (napr. pro pouziti v kolacovych grafech)
   * 
   * @return
   */
  public abstract String getTitle();

  /**
   * @return the usageDTO
   */
  public StatisticUsageDTO getUsageDTO() {
    return usageDTO;
  }

  /**
   * @param usageDTO
   *          the usageDTO to set
   */
  public void setUsageDTO(StatisticUsageDTO usageDTO) {
    this.usageDTO = usageDTO;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    ToStringBuilder builder = new ToStringBuilder(this);
    builder.append("usageDTO", usageDTO);
    builder.append("toString()", super.toString());
    return builder.toString();
  }

}
