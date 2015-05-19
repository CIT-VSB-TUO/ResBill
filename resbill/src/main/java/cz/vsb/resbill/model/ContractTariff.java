package cz.vsb.resbill.model;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "CONTRACT_TARIFF", uniqueConstraints = @UniqueConstraint(name = "UK_contract_tariff__contract_id__tariff_id__begin_date", columnNames = { "contract_id", "tariff_id", "begin_date" }))
public class ContractTariff extends BaseVersionedEntity {

	private static final long serialVersionUID = 1481292473824718485L;

	@Embedded
	@NotNull
	@Valid
	private Period period;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "contract_id", foreignKey = @ForeignKey(name = "FK_contract_tariff__contract"))
	@NotNull
	private Contract contract;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "tariff_id", foreignKey = @ForeignKey(name = "FK_contract_tariff__tariff"))
	@NotNull
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
		StringBuilder builder = new StringBuilder();
		builder.append("ContractTariff [");
		builder.append(super.toString());
		builder.append(", period=");
		builder.append(period);
		builder.append(", contract.id=");
		builder.append(contract != null ? contract.getId() : null);
		builder.append(", tariff.id=");
		builder.append(tariff != null ? tariff.getId() : null);
		builder.append("]");
		return builder.toString();
	}
}
