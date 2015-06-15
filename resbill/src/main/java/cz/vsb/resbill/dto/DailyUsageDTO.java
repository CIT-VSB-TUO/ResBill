/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import cz.vsb.resbill.model.DailyUsage;
import cz.vsb.resbill.util.ToStringBuilder;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */
public class DailyUsageDTO implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID   = 1L;

  private Integer           dailyUsageId       = null;

  private Date              date               = null;

  private Integer           cpu                = null;

  private BigDecimal        memoryGB           = null;

  private BigDecimal        provisionedSpaceGB = null;

  private BigDecimal        usedSpaceGB        = null;

  private BigDecimal        backupSpaceGB      = null;

  private boolean           powerState         = false;

  /**
   * 
   */
  public DailyUsageDTO() {
    super();
  }

  /**
   * 
   */
  public DailyUsageDTO(DailyUsage dailyUsage) {
    this();
    fill(dailyUsage);
  }

  /**
   * 
   * @param dailyUsage
   */
  public void fill(DailyUsage dailyUsage) {
    dailyUsageId = dailyUsage.getId();
    date = dailyUsage.getDailyImport().getDate();

    cpu = dailyUsage.getCpu();
    memoryGB = dailyUsage.getMemoryGB();
    provisionedSpaceGB = dailyUsage.getProvisionedSpaceGB();
    usedSpaceGB = dailyUsage.getUsedSpaceGB();
    backupSpaceGB = dailyUsage.getBackupGB();
    powerState = dailyUsage.getPowerState();
  }

  /**
   * @return the dailyUsageId
   */
  public Integer getDailyUsageId() {
    return dailyUsageId;
  }

  /**
   * @param dailyUsageId
   *          the dailyUsageId to set
   */
  public void setDailyUsageId(Integer dailyUsageId) {
    this.dailyUsageId = dailyUsageId;
  }

  /**
   * @return the date
   */
  public Date getDate() {
    return date;
  }

  /**
   * @param date
   *          the date to set
   */
  public void setDate(Date date) {
    this.date = date;
  }

  /**
   * @return the cpu
   */
  public Integer getCpu() {
    return cpu;
  }

  /**
   * @param cpu
   *          the cpu to set
   */
  public void setCpu(Integer cpu) {
    this.cpu = cpu;
  }

  /**
   * @return the memoryGB
   */
  public BigDecimal getMemoryGB() {
    return memoryGB;
  }

  /**
   * @param memoryGB
   *          the memoryGB to set
   */
  public void setMemoryGB(BigDecimal memoryGB) {
    this.memoryGB = memoryGB;
  }

  /**
   * @return the provisionedSpaceGB
   */
  public BigDecimal getProvisionedSpaceGB() {
    return provisionedSpaceGB;
  }

  /**
   * @param provisionedSpaceGB
   *          the provisionedSpaceGB to set
   */
  public void setProvisionedSpaceGB(BigDecimal provisionedSpaceGB) {
    this.provisionedSpaceGB = provisionedSpaceGB;
  }

  /**
   * @return the usedSpaceGB
   */
  public BigDecimal getUsedSpaceGB() {
    return usedSpaceGB;
  }

  /**
   * @param usedSpaceGB
   *          the usedSpaceGB to set
   */
  public void setUsedSpaceGB(BigDecimal usedSpaceGB) {
    this.usedSpaceGB = usedSpaceGB;
  }

  /**
   * @return the backupSpaceGB
   */
  public BigDecimal getBackupSpaceGB() {
    return backupSpaceGB;
  }

  /**
   * @param backupSpaceGB
   *          the backupSpaceGB to set
   */
  public void setBackupSpaceGB(BigDecimal backupSpaceGB) {
    this.backupSpaceGB = backupSpaceGB;
  }

  /**
   * @return the powerState
   */
  public boolean isPowerState() {
    return powerState;
  }

  /**
   * @param powerState
   *          the powerState to set
   */
  public void setPowerState(boolean powerState) {
    this.powerState = powerState;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    ToStringBuilder builder = new ToStringBuilder(this);
    builder.append("dailyUsageId", dailyUsageId);
    builder.append("date", date);
    builder.append("cpu", cpu);
    builder.append("memoryGB", memoryGB);
    builder.append("provisionedSpaceGB", provisionedSpaceGB);
    builder.append("usedSpaceGB", usedSpaceGB);
    builder.append("backupSpaceGB", backupSpaceGB);
    builder.append("powerState", powerState);
    builder.append("toString()", super.toString());
    return builder.toString();
  }

}
