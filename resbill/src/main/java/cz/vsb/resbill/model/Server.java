package cz.vsb.resbill.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "SERVER", uniqueConstraints = @UniqueConstraint(name = "UK_server__server_id", columnNames = "server_id"))
public class Server extends BaseVersionedEntity {

	private static final long serialVersionUID = -4103528635705673991L;

	@Column(name = "server_id")
	@NotEmpty
	@Size(max = 50)
	private String serverId;

	@Column(name = "name")
	@NotEmpty
	@Size(max = 100)
	private String name;

	@OneToMany(mappedBy = "server", fetch = FetchType.LAZY)
	private Set<DailyUsage> dailyUsages = new HashSet<>();

	@OneToMany(mappedBy = "server", fetch = FetchType.LAZY)
	private Set<ContractServer> contractServers = new HashSet<>();

	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<DailyUsage> getDailyUsages() {
		return dailyUsages;
	}

	public void setDailyUsages(Set<DailyUsage> dailyUsages) {
		this.dailyUsages = dailyUsages;
	}

	public Set<ContractServer> getContractServers() {
		return contractServers;
	}

	public void setContractServers(Set<ContractServer> contractServers) {
		this.contractServers = contractServers;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Server [");
		builder.append(super.toString());
		builder.append(", serverId=");
		builder.append(serverId);
		builder.append(", name=");
		builder.append(name);
		builder.append("]");
		return builder.toString();
	}
}
