/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.dto.statistics;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import cz.vsb.resbill.util.ToStringBuilder;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */
public class StatisticReportDTO<K extends StatisticDTO> implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  /**
   * Prvni den pocitani statistiky
   */
  private Date              beginDate        = null;

  /**
   * Posledni den pozitani statistiky
   */
  private Date              endDate          = null;

  /**
   * Souhrne hodnoty za cely soubor
   */
  private StatisticUsageDTO overallDTO       = null;

  /**
   * Jednotlive dilci hodnoty za kazdou komponentu souboru
   */
  private List<K>           componentDTOs    = null;

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
   * @return the overallDTO
   */
  public StatisticUsageDTO getOverallDTO() {
    return overallDTO;
  }

  /**
   * @param overallDTO
   *          the overallDTO to set
   */
  public void setOverallDTO(StatisticUsageDTO overallDTO) {
    this.overallDTO = overallDTO;
  }

  /**
   * @return the componentDTOs
   */
  public List<K> getComponentDTOs() {
    return componentDTOs;
  }

  /**
   * @param componentDTOs
   *          the componentDTOs to set
   */
  public void setComponentDTOs(List<K> componentDTOs) {
    this.componentDTOs = componentDTOs;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    ToStringBuilder builder = new ToStringBuilder(this);
    builder.append("beginDate", beginDate);
    builder.append("endDate", endDate);
    builder.append("overallDTO", overallDTO);
    builder.append("componentDTOs", componentDTOs);
    builder.append("toString()", super.toString());
    return builder.toString();
  }

}
