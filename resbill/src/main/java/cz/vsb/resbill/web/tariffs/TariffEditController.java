package cz.vsb.resbill.web.tariffs;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import cz.vsb.resbill.criteria.PriceListCriteria;
import cz.vsb.resbill.exception.TariffServiceException;
import cz.vsb.resbill.model.PriceList;
import cz.vsb.resbill.model.Tariff;
import cz.vsb.resbill.service.PriceListService;
import cz.vsb.resbill.service.TariffService;
import cz.vsb.resbill.util.WebUtils;

/**
 * A controller for handling requests for/from tariffs/tariffEdit.html page template.
 * 
 * @author HAL191
 *
 */
@Controller
@RequestMapping("tariffs/edit")
@SessionAttributes("tariff")
public class TariffEditController {

	private static final Logger log = LoggerFactory.getLogger(TariffEditController.class);

	private static final String TARIFF_MODEL_KEY = "tariff";

	private static final String PRICE_LISTS_MODEL_KEY = "priceLists";

	private static final String SHOW_PRICES_MODEL_KEY = "showPrices";

	@Inject
	private TariffService tariffService;

	@Inject
	private PriceListService priceListService;

	@InitBinder
	public void initBinder(WebDataBinder binder, Locale locale) {
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
	}

	private void loadTariff(Integer tariffId, ModelMap model) {
		if (log.isDebugEnabled()) {
			log.debug("Requested tariffId=" + tariffId);
		}
		Tariff tariff = null;
		try {
			if (tariffId != null) {
				tariff = tariffService.findTariff(tariffId);
			} else {
				tariff = new Tariff();
				tariff.setValid(Boolean.TRUE);
			}
			model.addAttribute(TARIFF_MODEL_KEY, tariff);
		} catch (Exception e) {
			log.error("Cannot load tariff with id: " + tariffId, e);

			model.addAttribute(TARIFF_MODEL_KEY, tariff);
			WebUtils.addGlobalError(model, TARIFF_MODEL_KEY, "error.load.tariff");
		}
		if (log.isDebugEnabled()) {
			log.debug("Loaded tariff: " + tariff);
		}
	}

	private void loadPriceLists(Integer tariffId, ModelMap model) {
		if (tariffId != null) {
			model.addAttribute(SHOW_PRICES_MODEL_KEY, Boolean.TRUE);
			List<PriceList> priceLists = null;
			try {
				PriceListCriteria criteria = new PriceListCriteria();
				criteria.setTariffId(tariffId);
				priceLists = priceListService.findPriceLists(criteria, null, null);
				model.addAttribute(PRICE_LISTS_MODEL_KEY, priceLists);
			} catch (Exception e) {
				log.error("Cannot load priceLists for tariff.id: " + tariffId, e);

				model.addAttribute(PRICE_LISTS_MODEL_KEY, priceLists);
				WebUtils.addGlobalError(model, PRICE_LISTS_MODEL_KEY, "error.load.priceLists");
			}
			if (log.isDebugEnabled()) {
				log.debug("Loaded list of priceLists size: " + (priceLists != null ? priceLists.size() : null));
			}
		} else {
			model.addAttribute(SHOW_PRICES_MODEL_KEY, Boolean.FALSE);
		}
	}

	/**
	 * Handles all GET requests. Binds loaded {@link Tariff} entity with the key "tariff" into a model and optionally "priceLists" with a list of tariff's price lists.
	 * 
	 * @param tariffId
	 *          key of a {@link Tariff} to view/edit
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String view(@RequestParam(value = "tariffId", required = false) Integer tariffId, ModelMap model) {
		// nacteni tarifu
		loadTariff(tariffId, model);
		// nacteni ceniku
		loadPriceLists(tariffId, model);

		return "tariffs/tariffEdit";
	}

	/**
	 * Handles POST requests for saving edited {@link Tariff} entity.
	 * 
	 * @param tariff
	 * @param bindingResult
	 * @return
	 */
	@RequestMapping(value = "", method = RequestMethod.POST, params = "save")
	public String save(@Valid @ModelAttribute(TARIFF_MODEL_KEY) Tariff tariff, BindingResult bindingResult, SessionStatus sessionStatus) {
		if (log.isDebugEnabled()) {
			log.debug("Tariff to save: " + tariff);
		}
		if (!bindingResult.hasErrors()) {
			try {
				tariff = tariffService.saveTariff(tariff);
				if (log.isDebugEnabled()) {
					log.debug("Saved tariff: " + tariff);
				}
			} catch (Exception e) {
				log.error("Cannot save tariff: " + tariff, e);
				bindingResult.reject("error.save.tariff");
				return "tariffs/tariffEdit";
			}
		} else {
			bindingResult.reject("error.save.tariff.validation");
			return "tariffs/tariffEdit";
		}
		sessionStatus.setComplete();
		return "redirect:/tariffs";
	}

	/**
	 * Handle POST requests for deleting {@link Tariff} entity.
	 * 
	 * @param tariff
	 * @param bindingResult
	 * @return
	 */
	@RequestMapping(value = "", method = RequestMethod.POST, params = "delete")
	public String delete(@ModelAttribute(TARIFF_MODEL_KEY) Tariff tariff, BindingResult bindingResult, SessionStatus sessionStatus) {
		if (log.isDebugEnabled()) {
			log.debug("Tariff to delete: " + tariff);
		}
		try {
			tariff = tariffService.deleteTariff(tariff.getId());
			if (log.isDebugEnabled()) {
				log.debug("Deleted tariff: " + tariff);
			}
		} catch (TariffServiceException e) {
			// TODO implement
			bindingResult.reject("error.delete.tariff.constraint.relations");
			return "tariffs/tariffEdit";
		} catch (Exception e) {
			log.error("Cannot delete tariff: " + tariff, e);
			bindingResult.reject("error.delete.tariff");
			return "tariffs/tariffEdit";
		}
		sessionStatus.setComplete();
		return "redirect:/tariffs";
	}
}
