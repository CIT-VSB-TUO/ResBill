package cz.vsb.resbill.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import cz.vsb.resbill.model.PriceList;
import cz.vsb.resbill.model.Tariff;
import cz.vsb.resbill.util.PeriodLimitedEntityComparator;

public class TariffPriceListDTO implements Serializable {

	private static final long serialVersionUID = -3012025575066216238L;

	@Valid
	@NotNull
	private Tariff tariff;

	@Valid
	@NotNull
	private PriceList lastPriceList;

	private List<PriceList> previousPriceLists;

	private boolean priceListEditable = true;

	public TariffPriceListDTO() {
	}

	public void fill(Tariff tariff) {
		this.tariff = tariff;
		if (tariff != null) {
			previousPriceLists = new ArrayList<PriceList>();
			for (PriceList priceList : tariff.getPrices()) {
				if (priceList.getPeriod().getEndDate() == null) {
					lastPriceList = priceList;
					priceListEditable = priceList.getInvoicePriceLists().isEmpty();
				} else {
					previousPriceLists.add(priceList);
				}
			}
			if (!previousPriceLists.isEmpty()) {
				Collections.sort(previousPriceLists, PeriodLimitedEntityComparator.INSTANCE);
			}
		}
	}

	public Tariff getTariff() {
		return tariff;
	}

	public void setTariff(Tariff tariff) {
		this.tariff = tariff;
	}

	public PriceList getLastPriceList() {
		return lastPriceList;
	}

	public void setLastPriceList(PriceList lastPriceList) {
		this.lastPriceList = lastPriceList;
	}

	public List<PriceList> getPreviousPriceLists() {
		return previousPriceLists;
	}

	public void setPreviousPriceLists(List<PriceList> previousPriceLists) {
		this.previousPriceLists = previousPriceLists;
	}

	public boolean isPriceListEditable() {
		return priceListEditable;
	}

	public void setPriceListEditable(boolean priceListEditable) {
		this.priceListEditable = priceListEditable;
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
		builder.append(", previousPriceLists.size=");
		builder.append(previousPriceLists != null ? previousPriceLists.size() : null);
		builder.append(", priceListEditable=");
		builder.append(priceListEditable);
		builder.append("]");
		return builder.toString();
	}

}
