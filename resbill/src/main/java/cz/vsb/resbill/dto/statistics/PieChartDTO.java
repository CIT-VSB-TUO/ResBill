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
public class PieChartDTO implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  private String            label            = null;

  private Number            data             = null;

  /**
   * 
   * @param label
   * @param data
   */
  public PieChartDTO(String label, Number data) {
    super();
    this.label = label;
    this.data = data;
  }

  /**
   * @return the label
   */
  public String getLabel() {
    return label;
  }

  /**
   * @param label
   *          the label to set
   */
  public void setLabel(String label) {
    this.label = label;
  }

  /**
   * @return the data
   */
  public Number getData() {
    return data;
  }

  /**
   * @param data
   *          the data to set
   */
  public void setData(Number data) {
    this.data = data;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    ToStringBuilder builder = new ToStringBuilder(this);
    builder.append("label", label);
    builder.append("data", data);
    builder.append("toString()", super.toString());
    return builder.toString();
  }

}
