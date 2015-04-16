package cz.vsb.resbill.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class ContractTariff {

	private Period period;
	private Contract contract;
	private Tariff tariff;

	public Period getPeriod() {
		return period;
	}

	public void setPeriod(Period period) {
		this.period = period;
	}

	public Contract getContract() {
		return contract;
	}

	public void setContract(Contract contract) {
		this.contract = contract;
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
		builder.append("period", period);
		builder.append("contract", contract);
		builder.append("tariff", tariff);
		return builder.toString();
	}
}
