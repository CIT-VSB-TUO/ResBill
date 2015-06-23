package cz.vsb.resbill.dto.tariff;

import cz.vsb.resbill.model.Tariff;

public class TariffHeaderDTO extends TariffDTO {

	private static final long serialVersionUID = -7716780994886260440L;

	public TariffHeaderDTO(Tariff t) {
		super(t);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TariffHeaderDTO [");
		builder.append(super.toString());
		builder.append(", ]");
		return builder.toString();
	}

}
