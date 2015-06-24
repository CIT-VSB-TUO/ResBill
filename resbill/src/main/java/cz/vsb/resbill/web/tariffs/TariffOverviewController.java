package cz.vsb.resbill.web.tariffs;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cz.vsb.resbill.dto.tariff.TariffOverviewDTO;
import cz.vsb.resbill.exception.PriceListServiceException;
import cz.vsb.resbill.exception.TariffServiceException;
import cz.vsb.resbill.model.PriceList;
import cz.vsb.resbill.model.Tariff;
import cz.vsb.resbill.service.PriceListService;
import cz.vsb.resbill.util.WebUtils;

@Controller
@RequestMapping("/tariffs/overview")
@SessionAttributes("tariffOverviewDTO")
public class TariffOverviewController extends AbstractTariffController {

	private static final Logger log = LoggerFactory.getLogger(TariffOverviewController.class);

	private static final String TARIFF_OVERVIEW_DTO_MODEL_KEY = "tariffOverviewDTO";

	@Inject
	private PriceListService priceListService;

	private void loadTariffOverviewDTO(Integer tariffId, ModelMap model) {
		if (log.isDebugEnabled()) {
			log.debug("Requested tariffId=" + tariffId);
		}
		TariffOverviewDTO dto = null;
		try {
			dto = tariffService.findTariffOverviewDTO(tariffId);
			model.addAttribute(TARIFF_OVERVIEW_DTO_MODEL_KEY, dto);
		} catch (Exception e) {
			log.error("Cannot load TariffOverviewDTO with id: " + tariffId, e);

			model.addAttribute(TARIFF_OVERVIEW_DTO_MODEL_KEY, dto);
			WebUtils.addGlobalError(model, TARIFF_OVERVIEW_DTO_MODEL_KEY, "error.load.tariff");
		}
		if (log.isDebugEnabled()) {
			log.debug("Loaded tariff: " + dto);
		}
	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String view(@RequestParam(value = "tariffId", required = true) Integer tariffId, ModelMap model) {
		loadTariffOverviewDTO(tariffId, model);

		return "tariffs/tariffOverview";
	}

	/**
	 * Handle GET requests for deleting {@link Tariff} instance.
	 * 
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public String deleteTariff(@RequestParam(value = "tariffId", required = true) Integer tariffId, ModelMap model) {
		if (log.isDebugEnabled()) {
			log.debug("Tariff.id to delete: " + tariffId);
		}
		try {
			Tariff tariff = tariffService.deleteTariff(tariffId);
			if (log.isDebugEnabled()) {
				log.debug("Deleted tariff: " + tariff);
			}

			return "redirect:/tariffs";
		} catch (TariffServiceException e) {
			switch (e.getReason()) {
			case CONTRACT_ASSOCIATED:
				WebUtils.addGlobalError(model, TARIFF_OVERVIEW_DTO_MODEL_KEY, "error.delete.tariff.contract");
				break;
			default:
				log.warn("Unsupported reason: " + e);
				WebUtils.addGlobalError(model, TARIFF_OVERVIEW_DTO_MODEL_KEY, "error.delete.tariff");
				break;
			}
		} catch (Exception e) {
			log.error("Cannot delete tariff.id: " + tariffId, e);
			WebUtils.addGlobalError(model, TARIFF_OVERVIEW_DTO_MODEL_KEY, "error.delete.tariff");
		}
		return "tariffs/tariffOverview";
	}

	/**
	 * Handle GET requests for deleting last {@link PriceList} instance.
	 * 
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.GET, params = "priceListId")
	public String deletePriceList(@RequestParam(value = "priceListId", required = true) Integer priceListId, ModelMap model, RedirectAttributes redirectAttributes) {
		if (log.isDebugEnabled()) {
			log.debug("PriceList.id to delete: " + priceListId);
		}
		try {
			PriceList priceList = priceListService.deletePriceList(priceListId);
			if (log.isDebugEnabled()) {
				log.debug("Deleted priceList: " + priceList);
			}

			redirectAttributes.addAttribute("tariffId", priceList.getTariff().getId());
			return "redirect:/tariffs/overview";
		} catch (PriceListServiceException e) {
			switch (e.getReason()) {
			case NOT_LAST_PRICE_LIST:
				WebUtils.addGlobalError(model, TARIFF_OVERVIEW_DTO_MODEL_KEY, "error.delete.priceList.not.last");
				break;
			case FIRST_PRICE_LIST:
				WebUtils.addGlobalError(model, TARIFF_OVERVIEW_DTO_MODEL_KEY, "error.delete.priceList.first");
				break;
			case INVOICE_EXISTENCE:
				WebUtils.addGlobalError(model, TARIFF_OVERVIEW_DTO_MODEL_KEY, "error.delete.priceList.invoice");
				break;
			default:
				log.warn("Unsupported reason: " + e);
				WebUtils.addGlobalError(model, TARIFF_OVERVIEW_DTO_MODEL_KEY, "error.delete.priceList");
				break;
			}
		} catch (Exception e) {
			log.error("Cannot delete priceList with id: " + priceListId, e);
			WebUtils.addGlobalError(model, TARIFF_OVERVIEW_DTO_MODEL_KEY, "error.delete.priceList");
		}
		return "tariffs/tariffOverview";
	}

}
