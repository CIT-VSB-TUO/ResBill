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
@Table(name = "CONTRACT_SERVER", uniqueConstraints = @UniqueConstraint(name = "UK_contract_server__contract_id__server_id__begin_date", columnNames = { "contract_id", "server_id", "begin_date" }))
public class ContractServer extends BaseVersionedEntity implements PeriodLimitedEntity {

	private static final long serialVersionUID = -5513761411228439161L;

	@Embedded
	@NotNull
	@Valid
	private Period period;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "contract_id", nullable = false, foreignKey = @ForeignKey(name = "FK_contract_server__contract"))
	private Contract contract;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "server_id", nullable = false, foreignKey = @ForeignKey(name = "FK_contract_server__server"))
	private Server server;

	@Override
	public Period getPeriod() {
		return period;
	}

	@Override
	public void setPeriod(Period period) {
		this.period = period;
	}

	public Contract getContract() {
		return contract;
	}

	public void setContract(Contract contract) {
		this.contract = contract;
	}

	public Server getServer() {
		return server;
	}

	public void setServer(Server server) {
		this.server = server;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ContractServer [");
		builder.append(super.toString());
		builder.append(", period=");
		builder.append(period);
		builder.append(", contract.id=");
		builder.append(contract != null ? contract.getId() : null);
		builder.append(", server.id=");
		builder.append(server != null ? server.getId() : null);
		builder.append("]");
		return builder.toString();
	}
}
