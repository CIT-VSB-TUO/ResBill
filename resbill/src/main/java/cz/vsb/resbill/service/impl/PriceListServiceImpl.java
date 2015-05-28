package cz.vsb.resbill.service.impl;

import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cz.vsb.resbill.criteria.PriceListCriteria;
import cz.vsb.resbill.dao.PriceListDAO;
import cz.vsb.resbill.exception.PriceListServiceException;
import cz.vsb.resbill.exception.ResBillException;
import cz.vsb.resbill.model.PriceList;
import cz.vsb.resbill.service.PriceListService;

/**
 * An implementation of {@link PriceListService} interface.
 * 
 * @author HAL191
 *
 */
@Service
@Transactional
public class PriceListServiceImpl implements PriceListService {

	private static final Logger log = LoggerFactory.getLogger(PriceListServiceImpl.class);

	@Inject
	private PriceListDAO priceListDAO;

	@Override
	public PriceList findPriceList(Integer priceListId) throws ResBillException {
		try {
			return priceListDAO.findPriceList(priceListId);
		} catch (Exception e) {
			log.error("An unexpected error occured while finding PriceList by id=" + priceListId, e);
			throw new ResBillException(e);
		}
	}

	@Override
	public List<PriceList> findPriceLists(PriceListCriteria criteria, Integer offset, Integer limit) throws ResBillException {
		try {
			return priceListDAO.findPriceLists(criteria, offset, limit);
		} catch (Exception e) {
			log.error("An unexpected error occured while searching for PriceList entities by criteria: " + criteria, e);
			throw new ResBillException(e);
		}
	}

	@Override
	public PriceList savePriceList(PriceList priceList) throws ResBillException {
		try {
			return priceListDAO.savePriceList(priceList);
		} catch (Exception e) {
			log.error("An unexpected error occured while saving PriceList entity: " + priceList, e);
			throw new ResBillException(e);
		}
	}

	@Override
	public PriceList deletePriceList(Integer priceListId) throws PriceListServiceException, ResBillException {
		try {
			// TODO chybi kontrola zavislosti
			PriceList priceList = priceListDAO.findPriceList(priceListId);
			return priceListDAO.deletePriceList(priceList);
		} catch (Exception e) {
			log.error("An unexpected error occured while deleting PriceList with id=" + priceListId, e);
			throw new ResBillException(e);
		}
	}
}
