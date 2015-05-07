package cz.vsb.resbill.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "TARIFF")
public class Tariff extends BaseVersionedEntity {

	private static final long serialVersionUID = -8694449318602384617L;

	@Column(name = "name", nullable = false, length = 250)
	private String name;

	@Column(name = "valid", nullable = false)
	private Boolean valid;

	@OneToMany(mappedBy = "tariff", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<PriceList> prices = new HashSet<>();

	@OneToMany(mappedBy = "tariff", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<ContractTariff> contractTariffs = new HashSet<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getValid() {
		return valid;
	}

	public void setValid(Boolean valid) {
		this.valid = valid;
	}

	public Set<PriceList> getPrices() {
		return prices;
	}

	public void setPrices(Set<PriceList> prices) {
		this.prices = prices;
	}

	public Set<ContractTariff> getContractTariffs() {
		return contractTariffs;
	}

	public void setContractTariffs(Set<ContractTariff> contractTariffs) {
		this.contractTariffs = contractTariffs;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Tariff [");
		builder.append(super.toString());
		builder.append(", name=");
		builder.append(name);
		builder.append(", valid=");
		builder.append(valid);
		builder.append("]");
		return builder.toString();
	}

}
