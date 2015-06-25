/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.dto.server;

import cz.vsb.resbill.dto.DailyUsageDTO;
import cz.vsb.resbill.dto.contract.ContractDTO;
import cz.vsb.resbill.model.Contract;
import cz.vsb.resbill.model.DailyUsage;
import cz.vsb.resbill.model.Server;
import cz.vsb.resbill.util.ToStringBuilder;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */
public class ServerListDTO extends ServerDTO {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  private DailyUsageDTO     dailyUsageDTO    = null;

  private ContractDTO       contractDTO      = null;

  /**
   * 
   */
  public ServerListDTO() {
    super();
  }

  /**
   * 
   * @param server
   */
  public ServerListDTO(Server server) {
    super(server);
  }

  /**
   * 
   * @param fields
   */
  public ServerListDTO(Object[] fields) {
    this();
    fill(fields);
  }

  /**
   * 
   * @param fields
   */
  public void fill(Object[] fields) {
    Server server = (Server) fields[0];
    DailyUsage dailyUsage = (DailyUsage) fields[1];
    Contract contract = (Contract) fields[2];

    fill(server);
    if (dailyUsage != null) {
      dailyUsageDTO = new DailyUsageDTO(dailyUsage);
    }
    if (contract != null) {
      contractDTO = new ContractDTO(contract);
    }
  }

  /**
   * @return the dailyUsageDTO
   */
  public DailyUsageDTO getDailyUsageDTO() {
    return dailyUsageDTO;
  }

  /**
   * @param dailyUsageDTO
   *          the dailyUsageDTO to set
   */
  public void setDailyUsageDTO(DailyUsageDTO dailyUsageDTO) {
    this.dailyUsageDTO = dailyUsageDTO;
  }

  /**
   * @return the contractDTO
   */
  public ContractDTO getContractDTO() {
    return contractDTO;
  }

  /**
   * @param contractDTO
   *          the contractDTO to set
   */
  public void setContractDTO(ContractDTO contractDTO) {
    this.contractDTO = contractDTO;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    ToStringBuilder builder = new ToStringBuilder(this);
    builder.append("dailyUsageDTO", dailyUsageDTO);
    builder.append("contractDTO", contractDTO);
    builder.append("toString()", super.toString());
    return builder.toString();
  }

}
