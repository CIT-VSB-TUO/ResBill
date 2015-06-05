/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.criteria;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cz.vsb.resbill.util.ToStringBuilder;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */
public class DailyImportCriteria implements Serializable, Cloneable {

  /**
   * 
   */
  private static final long serialVersionUID   = 1L;

  private List<OrderBy>     orderBy            = Arrays.asList(new OrderBy[] { OrderBy.DATE_DESC });

  private Set<Integer>      dailyImportIds     = null;

  private Date              beginDate          = null;

  private Date              endDate            = null;

  private Date              beginImportEndDate = null;

  private Date              endImportEndDate   = null;

  private EnumSet<Feature>  features           = null;

  /**
   * 
   */
  @Override
  public DailyImportCriteria clone() throws CloneNotSupportedException {
    DailyImportCriteria clone = (DailyImportCriteria) super.clone();

    clone.setOrderBy(orderBy != null ? new ArrayList<OrderBy>(orderBy) : null);
    clone.setDailyImportIds(dailyImportIds != null ? new HashSet<Integer>(dailyImportIds) : null);
    clone.setFeatures(features != null ? features.clone() : null);

    return clone;
  }

  /**
   * @return the dailyImportIds
   */
  public Set<Integer> getDailyImportIds() {
    return dailyImportIds;
  }

  /**
   * @param dailyImportIds
   *          the dailyImportIds to set
   */
  public void setDailyImportIds(Set<Integer> dailyImportIds) {
    this.dailyImportIds = dailyImportIds;
  }

  /**
   * @return the beginDate
   */
  public Date getBeginDate() {
    return beginDate;
  }

  /**
   * @param beginDate
   *          the beginDate to set
   */
  public void setBeginDate(Date beginDate) {
    this.beginDate = beginDate;
  }

  /**
   * @return the endDate
   */
  public Date getEndDate() {
    return endDate;
  }

  /**
   * @param endDate
   *          the endDate to set
   */
  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  /**
   * @return the beginImportEndDate
   */
  public Date getBeginImportEndDate() {
    return beginImportEndDate;
  }

  /**
   * @param beginImportEndDate
   *          the beginImportEndDate to set
   */
  public void setBeginImportEndDate(Date beginImportEndDate) {
    this.beginImportEndDate = beginImportEndDate;
  }

  /**
   * @return the endImportEndDate
   */
  public Date getEndImportEndDate() {
    return endImportEndDate;
  }

  /**
   * @param endImportEndDate
   *          the endImportEndDate to set
   */
  public void setEndImportEndDate(Date endImportEndDate) {
    this.endImportEndDate = endImportEndDate;
  }

  /**
   * @return the features
   */
  public EnumSet<Feature> getFeatures() {
    return features;
  }

  /**
   * @param features
   *          the features to set
   */
  public void setFeatures(EnumSet<Feature> features) {
    this.features = features;
  }

  /**
   * @return the orderBy
   */
  public List<OrderBy> getOrderBy() {
    return orderBy;
  }

  /**
   * @param orderBy
   *          the orderBy to set
   */
  public void setOrderBy(List<OrderBy> orderBy) {
    this.orderBy = orderBy;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    ToStringBuilder builder = new ToStringBuilder(this);
    builder.append("orderBy", orderBy);
    builder.append("dailyImportIds", dailyImportIds);
    builder.append("beginDate", beginDate);
    builder.append("endDate", endDate);
    builder.append("beginImportEndDate", beginImportEndDate);
    builder.append("endImportEndDate", endImportEndDate);
    builder.append("features", features);
    builder.append("toString()", super.toString());
    return builder.toString();
  }

  /**
   * Pri hledani se vrati pouze ty importy, ktere splnuji pozadovanou vlastnost
   * 
   * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
   *
   */
  public static enum Feature {
    /**
     * Import ma alespon jeden radek s chybou
     */
    HAS_ERRORS,

    /**
     * Import ma alespon jeden radek s varovanim
     */
    HAS_WARNS,

  }

  /**
   * 
   * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
   *
   */
  public static enum OrderBy {
    DATE_ASC,

    DATE_DESC,
  }
}
