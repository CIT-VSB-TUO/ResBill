/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.dto;

import java.io.Serializable;
import java.util.Date;

import cz.vsb.resbill.model.DailyUsage;
import cz.vsb.resbill.model.Server;
import cz.vsb.resbill.util.ToStringBuilder;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */
public class ServerOverviewDTO extends ServerDTO implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  private DailyUsageDTO        lastDailyUsageDTO   = null;

  /**
   * 
   */
  public ServerOverviewDTO() {
    super();
  }

  /**
   * 
   * @param server
   */
  public ServerOverviewDTO(Server server) {
    super(server);
  }

  /**
   * 
   */
  @Override
  public void fill(Server server) {
    super.fill(server);
  }



  
  /**
   * @return the lastDailyUsageDTO
   */
  public DailyUsageDTO getLastDailyUsageDTO() {
    return lastDailyUsageDTO;
  }

  
  /**
   * @param lastDailyUsageDTO the lastDailyUsageDTO to set
   */
  public void setLastDailyUsageDTO(DailyUsageDTO lastDailyUsageDTO) {
    this.lastDailyUsageDTO = lastDailyUsageDTO;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    ToStringBuilder builder = new ToStringBuilder(this);
    builder.append("lastDailyUsageDTO", lastDailyUsageDTO);
    builder.append("toString()", super.toString());
    return builder.toString();
  }

}
