package cz.vsb.resbill.dto.tariff;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import cz.vsb.resbill.model.PriceList;

public class PriceListDTO implements Serializable {

	private static final long serialVersionUID = 370807145190008033L;

	private Integer priceListId;

	private Date beginDate;

	private Date endDate;

	private BigDecimal cpuPrice;

	private BigDecimal memoryGBPrice;

	private BigDecimal spaceGBPrice;

	private BigDecimal backupGBPrice;

	private Integer previousPriceListId;

	public PriceListDTO(PriceList pl) {
		if (pl != null) {
			this.priceListId = pl.getId();
			this.beginDate = pl.getPeriod().getBeginDate();
			this.endDate = pl.getPeriod().getEndDate();
			this.cpuPrice = pl.getCpuPrice();
			this.memoryGBPrice = pl.getMemoryGBPrice();
			this.spaceGBPrice = pl.getSpaceGBPrice();
			this.backupGBPrice = pl.getBackupGBPrice();
			this.previousPriceListId = pl.getPrevious() != null ? pl.getPrevious().getId() : null;
		}
	}

	public Integer getPriceListId() {
		return priceListId;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public BigDecimal getCpuPrice() {
		return cpuPrice;
	}

	public BigDecimal getMemoryGBPrice() {
		return memoryGBPrice;
	}

	public BigDecimal getSpaceGBPrice() {
		return spaceGBPrice;
	}

	public BigDecimal getBackupGBPrice() {
		return backupGBPrice;
	}

	public Integer getPreviousPriceListId() {
		return previousPriceListId;
	}

	public boolean isFirst() {
		return getPreviousPriceListId() == null;
	}

	public boolean isLast() {
		return getEndDate() == null;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PriceListDTO [");
		builder.append(super.toString());
		builder.append(", priceListId=");
		builder.append(priceListId);
		builder.append(", beginDate=");
		builder.append(beginDate);
		builder.append(", endDate=");
		builder.append(endDate);
		builder.append(", cpuPrice=");
		builder.append(cpuPrice);
		builder.append(", memoryGBPrice=");
		builder.append(memoryGBPrice);
		builder.append(", spaceGBPrice=");
		builder.append(spaceGBPrice);
		builder.append(", backupGBPrice=");
		builder.append(backupGBPrice);
		builder.append(", previousPriceListId=");
		builder.append(previousPriceListId);
		builder.append("]");
		return builder.toString();
	}

}
