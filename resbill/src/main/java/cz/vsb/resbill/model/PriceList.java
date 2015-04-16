package cz.vsb.resbill.model;

import java.math.BigDecimal;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class PriceList {

	private BigDecimal cpuPrice;
	private BigDecimal memoryMBPrice;
	private BigDecimal spaceGBPrice;
	private BigDecimal backupGBPrice;
	private Period period;
	private Tariff tariff;

	public BigDecimal getCpuPrice() {
		return cpuPrice;
	}

	public void setCpuPrice(BigDecimal cpuPrice) {
		this.cpuPrice = cpuPrice;
	}

	public BigDecimal getMemoryMBPrice() {
		return memoryMBPrice;
	}

	public void setMemoryMBPrice(BigDecimal memoryMBPrice) {
		this.memoryMBPrice = memoryMBPrice;
	}

	public BigDecimal getSpaceGBPrice() {
		return spaceGBPrice;
	}

	public void setSpaceGBPrice(BigDecimal spaceGBPrice) {
		this.spaceGBPrice = spaceGBPrice;
	}

	public BigDecimal getBackupGBPrice() {
		return backupGBPrice;
	}

	public void setBackupGBPrice(BigDecimal backupGBPrice) {
		this.backupGBPrice = backupGBPrice;
	}

	public Period getPeriod() {
		return period;
	}

	public void setPeriod(Period period) {
		this.period = period;
	}

	public Tariff getTariff() {
		return tariff;
	}

	public void setTariff(Tariff tariff) {
		this.tariff = tariff;
	}

	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("cpuPrice", cpuPrice);
		builder.append("memoryMBPrice", memoryMBPrice);
		builder.append("spaceGBPrice", spaceGBPrice);
		builder.append("backupGBPrice", backupGBPrice);
		builder.append("period", period);
		builder.append("tariff", tariff);
		return builder.toString();
	}

}
