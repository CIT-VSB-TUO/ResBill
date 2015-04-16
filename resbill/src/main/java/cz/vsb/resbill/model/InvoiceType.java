package cz.vsb.resbill.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class InvoiceType {

	private String title;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("title", title);
		return builder.toString();
	}

}
