package cz.vsb.resbill.dto.tariff;

import cz.vsb.resbill.model.PriceList;
import cz.vsb.resbill.model.Tariff;

public class TariffOverviewDTO extends TariffDTO {

	private static final long serialVersionUID = 3245565992919540443L;

	private Boolean valid;

	private PriceListDTO lastPriceList;

	public TariffOverviewDTO(Tariff t) {
		super(t);
	}

	@Override
	protected void fill(Tariff t) {
		super.fill(t);
		if (t != null) {
			this.valid = t.getValid();
		}
	}

	public void fillPriceList(PriceList pl) {
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
		builder.append("TariffOverviewDTO [");
		builder.append(super.toString());
		builder.append(", valid=");
		builder.append(valid);
		builder.append(", lastPriceList=");
		builder.append(lastPriceList);
		builder.append("]");
		return builder.toString();
	}

}
