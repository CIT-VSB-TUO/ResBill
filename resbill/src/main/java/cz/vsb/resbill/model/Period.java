package cz.vsb.resbill.model;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Period {

	private Date beginDate;
	private Date endDate;

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("beginDate", beginDate);
		builder.append("endDate", endDate);
		return builder.toString();
	}

}
