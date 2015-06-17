package cz.vsb.resbill.model;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "PRICE_LIST")
public class PriceList extends BaseVersionedEntity implements PeriodLimitedEntity {

	private static final long serialVersionUID = 4369861089171899807L;

	@Column(name = "cpu_price")
	@NotNull
	@Digits(integer = 8, fraction = 2)
	private BigDecimal cpuPrice;

	@Column(name = "memory_gb_price")
	@NotNull
	@Digits(integer = 8, fraction = 2)
	private BigDecimal memoryGBPrice;

	@Column(name = "space_gb_price")
	@NotNull
	@Digits(integer = 8, fraction = 2)
	private BigDecimal spaceGBPrice;

	@Column(name = "backup_gb_price")
	@NotNull
	@Digits(integer = 8, fraction = 2)
	private BigDecimal backupGBPrice;

	@Embedded
	@NotNull
	@Valid
	private Period period;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "tariff_id", nullable = false, foreignKey = @ForeignKey(name = "FK_price_list__tariff"))
	private Tariff tariff;

	@OneToMany(mappedBy = "priceList", fetch = FetchType.LAZY)
	private Set<InvoicePriceList> invoicePriceLists = new HashSet<InvoicePriceList>();

	@OneToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "previous_id", foreignKey = @ForeignKey(name = "FK_price_list__previous"))
	private PriceList previous;

	public BigDecimal getCpuPrice() {
		return cpuPrice;
	}

	public void setCpuPrice(BigDecimal cpuPrice) {
		this.cpuPrice = cpuPrice;
	}

	public BigDecimal getMemoryGBPrice() {
		return memoryGBPrice;
	}

	public void setMemoryGBPrice(BigDecimal memoryGBPrice) {
		this.memoryGBPrice = memoryGBPrice;
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

	@Override
	public Period getPeriod() {
		return period;
	}

	@Override
	public void setPeriod(Period period) {
		this.period = period;
	}

	public Tariff getTariff() {
		return tariff;
	}

	public void setTariff(Tariff tariff) {
		this.tariff = tariff;
	}

	public Set<InvoicePriceList> getInvoicePriceLists() {
		return invoicePriceLists;
	}

	public void setInvoicePriceLists(Set<InvoicePriceList> invoicePriceLists) {
		this.invoicePriceLists = invoicePriceLists;
	}

	public PriceList getPrevious() {
		return previous;
	}

	public void setPrevious(PriceList previous) {
		this.previous = previous;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PriceList [");
		builder.append(super.toString());
		builder.append(", cpuPrice=");
		builder.append(cpuPrice);
		builder.append(", memoryGBPrice=");
		builder.append(memoryGBPrice);
		builder.append(", spaceGBPrice=");
		builder.append(spaceGBPrice);
		builder.append(", backupGBPrice=");
		builder.append(backupGBPrice);
		builder.append(", period=");
		builder.append(period);
		builder.append(", previous.id=");
		builder.append(previous != null ? previous.getId() : null);
		builder.append(", tariff.id=");
		builder.append(tariff != null ? tariff.getId() : null);
		builder.append("]");
		return builder.toString();
	}

}
