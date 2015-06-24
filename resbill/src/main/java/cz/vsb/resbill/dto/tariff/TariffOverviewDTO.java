package cz.vsb.resbill.dto.tariff;

import cz.vsb.resbill.model.PriceList;
import cz.vsb.resbill.model.Tariff;

public class TariffOverviewDTO extends TariffListDTO {

	private static final long serialVersionUID = 3245565992919540443L;

	public TariffOverviewDTO(Tariff t, PriceList pl) {
		super(t, pl);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TariffOverviewDTO [");
		builder.append(super.toString());
		builder.append(", ]");
		return builder.toString();
	}

}
