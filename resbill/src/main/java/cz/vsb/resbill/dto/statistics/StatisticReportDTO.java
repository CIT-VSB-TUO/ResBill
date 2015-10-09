/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.dto.statistics;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.context.i18n.LocaleContextHolder;

import cz.vsb.resbill.util.ToStringBuilder;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */
public class StatisticReportDTO<K extends StatisticDTO> implements Serializable {

  /**
   * 
   */
  private static final long        serialVersionUID                   = 1L;

  public static final NumberFormat PIE_CHART_LEGEND_PERCENTAGE_FORMAT = new DecimalFormat("00.00");

  public static final float        PIE_CHART_TRESHOLD                 = 1.0f;

  /**
   * Prvni den pocitani statistiky
   */
  private Date                     beginDate                          = null;

  /**
   * Posledni den pozitani statistiky
   */
  private Date                     endDate                            = null;

  /**
   * Souhrne hodnoty za cely soubor
   */
  private StatisticUsageDTO        overallDTO                         = null;

  /**
   * Jednotlive dilci hodnoty za kazdou komponentu souboru
   */
  private List<K>                  componentDTOs                      = null;

  /**
   * 
   * @param components
   * @return
   */
  public static List<PieChartDTO> filtrComponentsByPieChartTreshold(List<PieChartDTO> pieChartDTOs) {
    List<PieChartDTO> result = new ArrayList<PieChartDTO>();
    float otherSum = 0;

    for (PieChartDTO pieChartDTO : pieChartDTOs) {
      if (pieChartDTO.getData().floatValue() >= PIE_CHART_TRESHOLD) {
        result.add(pieChartDTO);
      } else {
        otherSum = otherSum + pieChartDTO.getData().floatValue();
      }
    }

    if (otherSum > 0) {
      ResourceBundle rb = ResourceBundle.getBundle("messages", LocaleContextHolder.getLocale());
      result.add(new PieChartDTO(PIE_CHART_LEGEND_PERCENTAGE_FORMAT.format(otherSum) + "% " + rb.getString("text.statistics.other"), otherSum));
    }

    return result;
  }

  /**
   * 
   * @return
   */
  public List<PieChartDTO> getServerPieChartDTOs() {
    List<PieChartDTO> dtos = new ArrayList<PieChartDTO>();

    List<K> componentDTOs = getComponentDTOs();
    for (K component : componentDTOs) {
      dtos.add(new PieChartDTO(PIE_CHART_LEGEND_PERCENTAGE_FORMAT.format(component.getUsageDTO().getServerPercentage()) + "% " + component.getTitle(), component.getUsageDTO().getServerPercentage()));
    }

    return filtrComponentsByPieChartTreshold(dtos);
  }

  /**
   * 
   * @return
   */
  public List<PieChartDTO> getCpuPieChartDTOs() {
    List<PieChartDTO> dtos = new ArrayList<PieChartDTO>();

    List<K> componentDTOs = getComponentDTOs();
    for (K component : componentDTOs) {
      dtos.add(new PieChartDTO(PIE_CHART_LEGEND_PERCENTAGE_FORMAT.format(component.getUsageDTO().getCpuPercentage()) + "% " + component.getTitle(), component.getUsageDTO().getCpuPercentage()));
    }

    return filtrComponentsByPieChartTreshold(dtos);
  }

  /**
   * 
   * @return
   */
  public List<PieChartDTO> getMemoryPieChartDTOs() {
    List<PieChartDTO> dtos = new ArrayList<PieChartDTO>();

    List<K> componentDTOs = getComponentDTOs();
    for (K component : componentDTOs) {
      dtos.add(new PieChartDTO(PIE_CHART_LEGEND_PERCENTAGE_FORMAT.format(component.getUsageDTO().getMemoryGbPercentage()) + "% " + component.getTitle(), component.getUsageDTO()
          .getMemoryGbPercentage()));
    }

    return filtrComponentsByPieChartTreshold(dtos);
  }

  /**
   * 
   * @return
   */
  public List<PieChartDTO> getProvisionedSpacePieChartDTOs() {
    List<PieChartDTO> dtos = new ArrayList<PieChartDTO>();

    List<K> componentDTOs = getComponentDTOs();
    for (K component : componentDTOs) {
      dtos.add(new PieChartDTO(PIE_CHART_LEGEND_PERCENTAGE_FORMAT.format(component.getUsageDTO().getProvisionedSpaceGbPercentage()) + "% " + component.getTitle(), component.getUsageDTO()
          .getProvisionedSpaceGbPercentage()));
    }

    return filtrComponentsByPieChartTreshold(dtos);
  }

  /**
   * 
   * @return
   */
  public List<PieChartDTO> getUsedSpacePieChartDTOs() {
    List<PieChartDTO> dtos = new ArrayList<PieChartDTO>();

    List<K> componentDTOs = getComponentDTOs();
    for (K component : componentDTOs) {
      dtos.add(new PieChartDTO(PIE_CHART_LEGEND_PERCENTAGE_FORMAT.format(component.getUsageDTO().getUsedSpaceGbPercentage()) + "% " + component.getTitle(), component.getUsageDTO()
          .getUsedSpaceGbPercentage()));
    }

    return filtrComponentsByPieChartTreshold(dtos);
  }

  /**
   * 
   * @return
   */
  public List<PieChartDTO> getBackupPieChartDTOs() {
    List<PieChartDTO> dtos = new ArrayList<PieChartDTO>();

    List<K> componentDTOs = getComponentDTOs();
    for (K component : componentDTOs) {
      dtos.add(new PieChartDTO(PIE_CHART_LEGEND_PERCENTAGE_FORMAT.format(component.getUsageDTO().getBackupGbPercentage()) + "% " + component.getTitle(), component.getUsageDTO()
          .getBackupGbPercentage()));
    }

    return filtrComponentsByPieChartTreshold(dtos);
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
