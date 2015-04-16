package cz.vsb.resbill.model;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class DailyUsage {

	private Date date;
	private String serverName;
	private Integer cpu;
	private Integer memoryMB;
	private Float provisionedSpaceGB;
	private Float usedSpaceGB;
	private Float backupGB;
	private Boolean powerState;
	private ProductionLevel productionLevel;
	private Server server;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public Integer getCpu() {
		return cpu;
	}

	public void setCpu(Integer cpu) {
		this.cpu = cpu;
	}

	public Integer getMemoryMB() {
		return memoryMB;
	}

	public void setMemoryMB(Integer memoryMB) {
		this.memoryMB = memoryMB;
	}

	public Float getProvisionedSpaceGB() {
		return provisionedSpaceGB;
	}

	public void setProvisionedSpaceGB(Float provisionedSpaceGB) {
		this.provisionedSpaceGB = provisionedSpaceGB;
	}

	public Float getUsedSpaceGB() {
		return usedSpaceGB;
	}

	public void setUsedSpaceGB(Float usedSpaceGB) {
		this.usedSpaceGB = usedSpaceGB;
	}

	public Float getBackupGB() {
		return backupGB;
	}

	public void setBackupGB(Float backupGB) {
		this.backupGB = backupGB;
	}

	public Boolean getPowerState() {
		return powerState;
	}

	public void setPowerState(Boolean powerState) {
		this.powerState = powerState;
	}

	public ProductionLevel getProductionLevel() {
		return productionLevel;
	}

	public void setProductionLevel(ProductionLevel productionLevel) {
		this.productionLevel = productionLevel;
	}

	public Server getServer() {
		return server;
	}

	public void setServer(Server server) {
		this.server = server;
	}

	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("date", date);
		builder.append("serverName", serverName);
		builder.append("cpu", cpu);
		builder.append("memoryMB", memoryMB);
		builder.append("provisionedSpaceGB", provisionedSpaceGB);
		builder.append("usedSpaceGB", usedSpaceGB);
		builder.append("backupGB", backupGB);
		builder.append("powerState", powerState);
		builder.append("productionLevel", productionLevel);
		builder.append("server", server);
		return builder.toString();
	}

}
