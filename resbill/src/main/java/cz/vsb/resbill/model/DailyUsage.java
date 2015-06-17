package cz.vsb.resbill.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "DAILY_USAGE", uniqueConstraints = @UniqueConstraint(name = "UK_daily_usage__daily_import_id__server_id", columnNames = { "daily_import_id", "server_id" }))
public class DailyUsage extends BaseVersionedEntity {

	private static final long serialVersionUID = 144836678591268182L;

	@Column(name = "server_name")
	@NotEmpty
	@Size(max = 100)
	private String serverName;

	@Column(name = "cpu")
	@NotNull
	private Integer cpu;

	@Column(name = "memory_gb")
	@NotNull
	@Digits(integer = 8, fraction = 2)
	private BigDecimal memoryGB;

	@Column(name = "prov_space_gb")
	@NotNull
	@Digits(integer = 8, fraction = 2)
	private BigDecimal provisionedSpaceGB;

	@Column(name = "used_space_gb")
	@NotNull
	@Digits(integer = 8, fraction = 2)
	private BigDecimal usedSpaceGB;

	@Column(name = "backup_gb")
	@NotNull
	@Digits(integer = 8, fraction = 2)
	private BigDecimal backupGB;

	@Column(name = "power_state")
	@NotNull
	private Boolean powerState;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "prod_level_id", foreignKey = @ForeignKey(name = "FK_daily_usage__production_level"))
	@NotNull
	private ProductionLevel productionLevel;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "server_id", foreignKey = @ForeignKey(name = "FK_daily_usage__server"))
	@NotNull
	private Server server;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "daily_import_id", foreignKey = @ForeignKey(name = "FK_daily_usage__daily_import"))
	@NotNull
	private DailyImport dailyImport;

	/**
	 * @return the dailyImport
	 */
	public DailyImport getDailyImport() {
		return dailyImport;
	}

	/**
	 * @param dailyImport
	 *          the dailyImport to set
	 */
	public void setDailyImport(DailyImport dailyImport) {
		this.dailyImport = dailyImport;
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

	/**
	 * @return the memoryGB
	 */
	public BigDecimal getMemoryGB() {
		return memoryGB;
	}

	/**
	 * @param memoryGB
	 *          the memoryGB to set
	 */
	public void setMemoryGB(BigDecimal memoryGB) {
		this.memoryGB = memoryGB;
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
		builder.append(", dailyImport.id=");
		builder.append(dailyImport != null ? dailyImport.getId() : null);
		builder.append(", serverName=");
		builder.append(serverName);
		builder.append(", cpu=");
		builder.append(cpu);
		builder.append(", memoryGB=");
		builder.append(memoryGB);
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
		builder.append(server != null ? server.getId() : null);
		builder.append("]");
		return builder.toString();
	}

}
