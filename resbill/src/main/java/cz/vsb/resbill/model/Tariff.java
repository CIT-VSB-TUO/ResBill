package cz.vsb.resbill.model;

import java.util.Set;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Tariff {

	private String name;
	private Boolean valid;
	private PriceList currentPriceList;
	private Set<PriceList> prices;
	private Set<ContractTariff> contractTariffs;
	private Set<Contract> currentContracts;

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

	public PriceList getCurrentPriceList() {
		return currentPriceList;
	}

	public void setCurrentPriceList(PriceList currentPriceList) {
		this.currentPriceList = currentPriceList;
	}

	public Set<ContractTariff> getContractTariffs() {
		return contractTariffs;
	}

	public void setContractTariffs(Set<ContractTariff> contractTariffs) {
		this.contractTariffs = contractTariffs;
	}

	public Set<Contract> getCurrentContracts() {
		return currentContracts;
	}

	public void setCurrentContracts(Set<Contract> currentContracts) {
		this.currentContracts = currentContracts;
	}

	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("name", name);
		builder.append("valid", valid);
		builder.append("currentPriceList", currentPriceList);
		builder.append("prices", prices);
		builder.append("contractTariffs", contractTariffs);
		builder.append("currentContracts", currentContracts);
		return builder.toString();
	}

}
