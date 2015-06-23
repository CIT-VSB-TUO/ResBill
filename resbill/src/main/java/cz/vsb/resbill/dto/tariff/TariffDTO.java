package cz.vsb.resbill.dto.tariff;

import java.io.Serializable;

import cz.vsb.resbill.model.Tariff;

public class TariffDTO implements Serializable {

	private static final long serialVersionUID = 6839724013274249996L;

	private Integer tariffId;

	private String name;

	public TariffDTO(Tariff t) {
		fill(t);
	}

	protected void fill(Tariff t) {
		if (t != null) {
			this.tariffId = t.getId();
			this.name = t.getName();
		}
	}

	public Integer getTariffId() {
		return tariffId;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TariffDTO [");
		builder.append(super.toString());
		builder.append(", tariffId=");
		builder.append(tariffId);
		builder.append(", name=");
		builder.append(name);
		builder.append("]");
		return builder.toString();
	}

}
