/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.dto;

import java.io.Serializable;
import java.util.Date;

import cz.vsb.resbill.model.ContractServer;
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
  private static final long serialVersionUID               = 1L;

  private DailyUsageDTO     lastDailyUsageDTO              = null;

  private Integer           currentContractServerId        = null;

  private Date              currentContractServerBeginDate = null;

  private Date              currentContractServerEndDate   = null;

  private ContractDTO       currentContractDTO             = null;

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
   * 
   * @param contractServer
   */
  public void fill(ContractServer contractServer) {
    currentContractServerId = contractServer.getId();
    currentContractServerBeginDate = contractServer.getPeriod().getBeginDate();
    currentContractServerEndDate = contractServer.getPeriod().getEndDate();
  }

  /**
   * @return the lastDailyUsageDTO
   */
  public DailyUsageDTO getLastDailyUsageDTO() {
    return lastDailyUsageDTO;
  }

  /**
   * @param lastDailyUsageDTO
   *          the lastDailyUsageDTO to set
   */
  public void setLastDailyUsageDTO(DailyUsageDTO lastDailyUsageDTO) {
    this.lastDailyUsageDTO = lastDailyUsageDTO;
  }

  /**
   * @return the currentContractServerId
   */
  public Integer getCurrentContractServerId() {
    return currentContractServerId;
  }

  /**
   * @param currentContractServerId
   *          the currentContractServerId to set
   */
  public void setCurrentContractServerId(Integer currentContractServerId) {
    this.currentContractServerId = currentContractServerId;
  }

  /**
   * @return the currentContractDTO
   */
  public ContractDTO getCurrentContractDTO() {
    return currentContractDTO;
  }

  /**
   * @param currentContractDTO
   *          the currentContractDTO to set
   */
  public void setCurrentContractDTO(ContractDTO currentContractDTO) {
    this.currentContractDTO = currentContractDTO;
  }

  /**
   * @return the currentContractServerBeginDate
   */
  public Date getCurrentContractServerBeginDate() {
    return currentContractServerBeginDate;
  }

  /**
   * @param currentContractServerBeginDate
   *          the currentContractServerBeginDate to set
   */
  public void setCurrentContractServerBeginDate(Date currentContractServerBeginDate) {
    this.currentContractServerBeginDate = currentContractServerBeginDate;
  }

  /**
   * @return the currentContractServerEndDate
   */
  public Date getCurrentContractServerEndDate() {
    return currentContractServerEndDate;
  }

  /**
   * @param currentContractServerEndDate
   *          the currentContractServerEndDate to set
   */
  public void setCurrentContractServerEndDate(Date currentContractServerEndDate) {
    this.currentContractServerEndDate = currentContractServerEndDate;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    ToStringBuilder builder = new ToStringBuilder(this);
    builder.append("lastDailyUsageDTO", lastDailyUsageDTO);
    builder.append("currentContractServerId", currentContractServerId);
    builder.append("currentContractServerBeginDate", currentContractServerBeginDate);
    builder.append("currentContractServerEndDate", currentContractServerEndDate);
    builder.append("currentContractDTO", currentContractDTO);
    builder.append("toString()", super.toString());
    return builder.toString();
  }

}
