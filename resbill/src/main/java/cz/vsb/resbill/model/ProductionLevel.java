package cz.vsb.resbill.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class ProductionLevel {

	private String code;
	private String title;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("code", code);
		builder.append("title", title);
		return builder.toString();
	}

}
