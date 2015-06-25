package cz.vsb.resbill.dto.tariff;

import cz.vsb.resbill.dto.PriceListDTO;
import cz.vsb.resbill.model.PriceList;
import cz.vsb.resbill.model.Tariff;

public class TariffListDTO extends TariffDTO {

	private static final long serialVersionUID = -6793992829113983397L;

	private Boolean valid;

	private PriceListDTO lastPriceList;

	public TariffListDTO(Tariff t, PriceList pl) {
		super(t);
		fill(pl);
	}

	@Override
	protected void fill(Tariff t) {
		super.fill(t);
		if (t != null) {
			this.valid = t.getValid();
		}
	}

	protected void fill(PriceList pl) {
		if (pl != null) {
			lastPriceList = new PriceListDTO(pl);
		}
	}

	public Boolean getValid() {
		return valid;
	}

	public PriceListDTO getLastPriceList() {
		return lastPriceList;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TariffListDTO [");
		builder.append(super.toString());
		builder.append(", valid=");
		builder.append(valid);
		builder.append(", lastPriceList=");
		builder.append(lastPriceList);
		builder.append("]");
		return builder.toString();
	}

}
