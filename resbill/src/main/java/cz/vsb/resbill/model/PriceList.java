package cz.vsb.resbill.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "PRICE_LIST")
public class PriceList extends BaseVersionedEntity {

	private static final long serialVersionUID = 4369861089171899807L;

	@Column(name = "cpu_price", nullable = false, precision = 10, scale = 2)
	private BigDecimal cpuPrice;

	@Column(name = "memory_mb_price", nullable = false, precision = 10, scale = 2)
	private BigDecimal memoryMBPrice;

	@Column(name = "space_gb_price", nullable = false, precision = 10, scale = 2)
	private BigDecimal spaceGBPrice;

	@Column(name = "backup_gb_price", nullable = false, precision = 10, scale = 2)
	private BigDecimal backupGBPrice;

	@Embedded
	private Period period;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "tariff_id", nullable = false)
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
		StringBuilder builder = new StringBuilder();
		builder.append("PriceList [");
		builder.append(super.toString());
		builder.append(", cpuPrice=");
		builder.append(cpuPrice);
		builder.append(", memoryMBPrice=");
		builder.append(memoryMBPrice);
		builder.append(", spaceGBPrice=");
		builder.append(spaceGBPrice);
		builder.append(", backupGBPrice=");
		builder.append(backupGBPrice);
		builder.append(", period=");
		builder.append(period);
		builder.append(", tariff.id=");
		builder.append(tariff != null ? tariff.getId() : null);
		builder.append("]");
		return builder.toString();
	}

}
