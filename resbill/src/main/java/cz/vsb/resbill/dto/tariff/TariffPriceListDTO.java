package cz.vsb.resbill.dto.tariff;

import java.io.Serializable;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import cz.vsb.resbill.model.PriceList;
import cz.vsb.resbill.model.Tariff;

public class TariffPriceListDTO implements Serializable {

	private static final long serialVersionUID = -3012025575066216238L;

	@Valid
	@NotNull
	private Tariff tariff;

	@Valid
	@NotNull
	private PriceList lastPriceList;

	private boolean lastPriceListEditable = true;

	public TariffPriceListDTO(Tariff tariff, PriceList pl) {
		this.tariff = tariff;
		this.lastPriceList = pl;
		if (pl != null) {
			lastPriceListEditable = pl.getInvoicePriceLists().isEmpty();
		}
	}

	public Tariff getTariff() {
		return tariff;
	}

	public PriceList getLastPriceList() {
		return lastPriceList;
	}

	public boolean isLastPriceListEditable() {
		return lastPriceListEditable;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TariffPriceListDTO [");
		builder.append(super.toString());
		builder.append(", tariff=");
		builder.append(tariff);
		builder.append(", lastPriceList=");
		builder.append(lastPriceList);
		builder.append(", lastPriceListEditable=");
		builder.append(lastPriceListEditable);
		builder.append("]");
		return builder.toString();
	}

}
