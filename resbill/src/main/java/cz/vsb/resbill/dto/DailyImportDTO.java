/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.dto;

import java.io.Serializable;
import java.util.Date;

import cz.vsb.resbill.model.DailyImport;
import cz.vsb.resbill.util.ToStringBuilder;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */
public class DailyImportDTO implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID     = 1L;

  private Integer           dailyImportId        = null;

  private Date              date                 = null;

  private Date              importBeginTimestamp = null;

  private Date              importEndTimestamp   = null;

  /**
   * 
   * @param dailyImport
   */
  public void fill(DailyImport dailyImport) {
    dailyImportId = dailyImport.getId();
    date = dailyImport.getDate();
    importBeginTimestamp = dailyImport.getImportBeginTimestamp();
    importEndTimestamp = dailyImport.getImportEndTimestamp();
  }

  /**
   * @return the dailyImportId
   */
  public Integer getDailyImportId() {
    return dailyImportId;
  }

  /**
   * @param dailyImportId
   *          the dailyImportId to set
   */
  public void setDailyImportId(Integer dailyImportId) {
    this.dailyImportId = dailyImportId;
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
   * @return the importBeginTimestamp
   */
  public Date getImportBeginTimestamp() {
    return importBeginTimestamp;
  }

  /**
   * @param importBeginTimestamp
   *          the importBeginTimestamp to set
   */
  public void setImportBeginTimestamp(Date importBeginTimestamp) {
    this.importBeginTimestamp = importBeginTimestamp;
  }

  /**
   * @return the importEndTimestamp
   */
  public Date getImportEndTimestamp() {
    return importEndTimestamp;
  }

  /**
   * @param importEndTimestamp
   *          the importEndTimestamp to set
   */
  public void setImportEndTimestamp(Date importEndTimestamp) {
    this.importEndTimestamp = importEndTimestamp;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    ToStringBuilder builder = new ToStringBuilder(this);
    builder.append("dailyImportId", dailyImportId);
    builder.append("date", date);
    builder.append("importBeginTimestamp", importBeginTimestamp);
    builder.append("importEndTimestamp", importEndTimestamp);
    builder.append("toString()", super.toString());
    return builder.toString();
  }

}
