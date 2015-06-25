package cz.vsb.resbill.web.tariffs;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cz.vsb.resbill.criteria.PriceListCriteria;
import cz.vsb.resbill.criteria.PriceListCriteria.OrderBy;
import cz.vsb.resbill.dto.PriceListDTO;
import cz.vsb.resbill.service.PriceListService;
import cz.vsb.resbill.util.WebUtils;

/**
 * A controller for handling requests for/from tariffs/priceListList.html page template.
 * 
 * @author HAL191
 *
 */
@Controller
@RequestMapping("/tariffs/prices")
public class PriceListListController extends AbstractTariffController {

	private static final Logger log = LoggerFactory.getLogger(PriceListListController.class);

	private static final String PRICE_LIST_DTOS_MODEL_KEY = "priceListDTOs";

	@Inject
	private PriceListService priceListService;

	private void loadPriceLists(Integer tariffId, ModelMap model) {
		List<PriceListDTO> dtos = null;
		try {
			PriceListCriteria criteria = new PriceListCriteria();
			criteria.setTariffId(tariffId);
			criteria.setOrderBy(OrderBy.PERIOD);
			dtos = priceListService.findPriceListDTOs(criteria, null, null);
			model.addAttribute(PRICE_LIST_DTOS_MODEL_KEY, dtos);
		} catch (Exception e) {
			log.error("Cannot load list of tariff price lists.", e);

			model.addAttribute(PRICE_LIST_DTOS_MODEL_KEY, dtos);
			WebUtils.addGlobalError(model, PRICE_LIST_DTOS_MODEL_KEY, "error.load.priceLists");
		}
		if (log.isDebugEnabled()) {
			log.debug("Loaded list of tariff prices size: " + (dtos != null ? dtos.size() : null));
		}
	}

	/**
	 * Handles all GET requests. Loads a list of all price lists of tariff and binds it with the key "priceLists" into a model.
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String view(@RequestParam(value = "tariffId", required = true) Integer tariffId, ModelMap model) {
		loadPriceLists(tariffId, model);

		return "tariffs/priceListList";
	}

}
