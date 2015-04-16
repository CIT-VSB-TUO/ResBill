package cz.vsb.resbill.model;

import java.util.Set;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Server {

	private String serverId;
	private String name;
	private Set<DailyUsage> dailyUsages;
	private Set<ContractServer> contractServers;
	private Set<Contract> currentContracts;

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

	public Set<Contract> getCurrentContracts() {
		return currentContracts;
	}

	public void setCurrentContracts(Set<Contract> currentContracts) {
		this.currentContracts = currentContracts;
	}

	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("serverId", serverId);
		builder.append("name", name);
		builder.append("dailyUsages", dailyUsages);
		builder.append("contractServers", contractServers);
		builder.append("currentContracts", currentContracts);
		return builder.toString();
	}
}
