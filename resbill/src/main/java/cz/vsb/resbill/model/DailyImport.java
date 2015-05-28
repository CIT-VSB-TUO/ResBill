/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import cz.vsb.resbill.util.ToStringBuilder;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */
@Entity
@Table(name = "DAILY_IMPORT", uniqueConstraints = @UniqueConstraint(name = "UK_daily_import__date", columnNames = { "import_date" }))
public class DailyImport extends BaseVersionedEntity {

	private static final long serialVersionUID = 1L;

	@Column(name = "import_date")
	@Temporal(TemporalType.DATE)
	@NotNull
	private Date date;

	@Column(name = "report_name")
	@NotEmpty
	@Size(max = 100)
	private String reportName;

	@Column(name = "report")
	@Lob
	@Basic(fetch = FetchType.LAZY)
	private String report;

	@Column(name = "import_begin_timestamp")
	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	private Date importBeginTimestamp;

	@Column(name = "import_end_timestamp")
	@Temporal(TemporalType.TIMESTAMP)
	private Date importEndTimestamp;

	@Column(name = "success")
	private Boolean success;

	@Column(name = "all_lines")
	private Integer allLines;

	@Column(name = "ok_lines")
	private Integer okLines;

	@Column(name = "warn_lines")
	private Integer warnLines;

	@Column(name = "error_lines")
	private Integer errorLines;

	@Column(name = "protocol")
	@Lob
	@Basic(fetch = FetchType.LAZY)
	private String protocol;

	public Date getDate() {
		return date;
	}

	@OneToMany(mappedBy = "dailyImport", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<DailyUsage> dailyUsages = new HashSet<>();

	public void setDate(Date date) {
		this.date = date;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public String getReport() {
		return report;
	}

	public void setReport(String report) {
		this.report = report;
	}

	public Date getImportBeginTimestamp() {
		return importBeginTimestamp;
	}

	public void setImportBeginTimestamp(Date importBeginTimestamp) {
		this.importBeginTimestamp = importBeginTimestamp;
	}

	public Date getImportEndTimestamp() {
		return importEndTimestamp;
	}

	public void setImportEndTimestamp(Date importEndTimestamp) {
		this.importEndTimestamp = importEndTimestamp;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public Integer getAllLines() {
		return allLines;
	}

	public void setAllLines(Integer allLines) {
		this.allLines = allLines;
	}

	public Integer getOkLines() {
		return okLines;
	}

	public void setOkLines(Integer okLines) {
		this.okLines = okLines;
	}

	public Integer getWarnLines() {
		return warnLines;
	}

	public void setWarnLines(Integer warnLines) {
		this.warnLines = warnLines;
	}

	public Integer getErrorLines() {
		return errorLines;
	}

	public void setErrorLines(Integer errorLines) {
		this.errorLines = errorLines;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("date", date);
		builder.append("reportName", reportName);
		builder.append("importBeginTimestamp", importBeginTimestamp);
		builder.append("importEndTimestamp", importEndTimestamp);
		builder.append("success", success);
		builder.append("allLines", allLines);
		builder.append("okLines", okLines);
		builder.append("warnLines", warnLines);
		builder.append("errorLines", errorLines);
		builder.append("toString()", super.toString());
		return builder.toString();
	}

}
