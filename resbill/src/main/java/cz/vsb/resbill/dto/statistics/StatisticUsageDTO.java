/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.dto.statistics;

import java.io.Serializable;
import java.math.BigDecimal;

import cz.vsb.resbill.util.ToStringBuilder;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */
public class StatisticUsageDTO implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  /**
   * Soucet CPU (absolutni)
   */
  private Integer           cpu;

  /**
   * CPU relativne k celku
   */
  private float             cpuPercentage;

  /**
   * Soucet RAM (absolutni)
   */
  private BigDecimal        memoryGB;

  /**
   * RAM relativne k celku
   */
  private float             memoryGbPercentage;

  /**
   * Soucet HDD (absolutni)
   */
  private BigDecimal        provisionedSpaceGB;

  /**
   * HDD relativne k celku
   */
  private float             provisionedSpaceGbPercentage;

  /**
   * Soucet vyuziteho HDD (absolutni)
   */
  private BigDecimal        usedSpaceGB;

  /**
   * Vyuzity HDD relativne k celku
   */
  private float             usedSpaceGbPercentage;

  /**
   * Soucet zalohovani (absolutni)
   */
  private BigDecimal        backupGB;

  /**
   * Zalohovani relativne k celku
   */
  private float             backupGbPercentage;

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
   * @return the cpuPercentage
   */
  public float getCpuPercentage() {
    return cpuPercentage;
  }

  /**
   * @param cpuPercentage
   *          the cpuPercentage to set
   */
  public void setCpuPercentage(float cpuPercentage) {
    this.cpuPercentage = cpuPercentage;
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
   * @return the memoryGbPercentage
   */
  public float getMemoryGbPercentage() {
    return memoryGbPercentage;
  }

  /**
   * @param memoryGbPercentage
   *          the memoryGbPercentage to set
   */
  public void setMemoryGbPercentage(float memoryGbPercentage) {
    this.memoryGbPercentage = memoryGbPercentage;
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
   * @return the provisionedSpaceGbPercentage
   */
  public float getProvisionedSpaceGbPercentage() {
    return provisionedSpaceGbPercentage;
  }

  /**
   * @param provisionedSpaceGbPercentage
   *          the provisionedSpaceGbPercentage to set
   */
  public void setProvisionedSpaceGbPercentage(float provisionedSpaceGbPercentage) {
    this.provisionedSpaceGbPercentage = provisionedSpaceGbPercentage;
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
   * @return the usedSpaceGbPercentage
   */
  public float getUsedSpaceGbPercentage() {
    return usedSpaceGbPercentage;
  }

  /**
   * @param usedSpaceGbPercentage
   *          the usedSpaceGbPercentage to set
   */
  public void setUsedSpaceGbPercentage(float usedSpaceGbPercentage) {
    this.usedSpaceGbPercentage = usedSpaceGbPercentage;
  }

  /**
   * @return the backupGB
   */
  public BigDecimal getBackupGB() {
    return backupGB;
  }

  /**
   * @param backupGB
   *          the backupGB to set
   */
  public void setBackupGB(BigDecimal backupGB) {
    this.backupGB = backupGB;
  }

  /**
   * @return the backupGbPercentage
   */
  public float getBackupGbPercentage() {
    return backupGbPercentage;
  }

  /**
   * @param backupGbPercentage
   *          the backupGbPercentage to set
   */
  public void setBackupGbPercentage(float backupGbPercentage) {
    this.backupGbPercentage = backupGbPercentage;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    ToStringBuilder builder = new ToStringBuilder(this);
    builder.append("cpu", cpu);
    builder.append("cpuPercentage", cpuPercentage);
    builder.append("memoryGB", memoryGB);
    builder.append("memoryGbPercentage", memoryGbPercentage);
    builder.append("provisionedSpaceGB", provisionedSpaceGB);
    builder.append("provisionedSpaceGbPercentage", provisionedSpaceGbPercentage);
    builder.append("usedSpaceGB", usedSpaceGB);
    builder.append("usedSpaceGbPercentage", usedSpaceGbPercentage);
    builder.append("backupGB", backupGB);
    builder.append("backupGbPercentage", backupGbPercentage);
    builder.append("toString()", super.toString());
    return builder.toString();
  }

}
