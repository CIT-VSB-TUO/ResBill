package cz.vsb.resbill.dao;

import java.util.List;

import cz.vsb.resbill.criteria.PriceListCriteria;
import cz.vsb.resbill.model.PriceList;

public interface PriceListDAO {

	PriceList findPriceList(Integer id);

	List<PriceList> findPriceLists(PriceListCriteria criteria);
}
