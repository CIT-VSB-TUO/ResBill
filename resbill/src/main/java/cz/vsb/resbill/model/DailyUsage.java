package cz.vsb.resbill.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "DAILY_USAGE")
public class DailyUsage extends BaseGeneratedIdEntity {

	private static final long serialVersionUID = 144836678591268182L;

	@Column(name = "date", nullable = false)
	@Temporal(TemporalType.DATE)
	private Date date;

	@Column(name = "server_name", nullable = false, length = 100)
	private String serverName;

	@Column(name = "cpu")
	private Integer cpu;

	@Column(name = "memory_mb")
	private Integer memoryMB;

	@Column(name = "prov_space_gb", precision = 10, scale = 2)
	private BigDecimal provisionedSpaceGB;

	@Column(name = "used_space_gb", precision = 10, scale = 2)
	private BigDecimal usedSpaceGB;

	@Column(name = "backup_gb", precision = 10, scale = 2)
	private BigDecimal backupGB;

	@Column(name = "power_state")
	private Boolean powerState;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "prod_level_id")
	private ProductionLevel productionLevel;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "server_id", nullable = false)
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

	public BigDecimal getProvisionedSpaceGB() {
		return provisionedSpaceGB;
	}

	public void setProvisionedSpaceGB(BigDecimal provisionedSpaceGB) {
		this.provisionedSpaceGB = provisionedSpaceGB;
	}

	public BigDecimal getUsedSpaceGB() {
		return usedSpaceGB;
	}

	public void setUsedSpaceGB(BigDecimal usedSpaceGB) {
		this.usedSpaceGB = usedSpaceGB;
	}

	public BigDecimal getBackupGB() {
		return backupGB;
	}

	public void setBackupGB(BigDecimal backupGB) {
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
		StringBuilder builder = new StringBuilder();
		builder.append("DailyUsage [");
		builder.append(super.toString());
		builder.append(", date=");
		builder.append(date);
		builder.append(", serverName=");
		builder.append(serverName);
		builder.append(", cpu=");
		builder.append(cpu);
		builder.append(", memoryMB=");
		builder.append(memoryMB);
		builder.append(", provisionedSpaceGB=");
		builder.append(provisionedSpaceGB);
		builder.append(", usedSpaceGB=");
		builder.append(usedSpaceGB);
		builder.append(", backupGB=");
		builder.append(backupGB);
		builder.append(", powerState=");
		builder.append(powerState);
		builder.append(", productionLevel.id=");
		builder.append(productionLevel != null ? productionLevel.getId() : null);
		builder.append(", server.id=");
		builder.append(server != null ? server : null);
		builder.append("]");
		return builder.toString();
	}

}
