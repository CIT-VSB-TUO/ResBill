package cz.vsb.resbill.web.tariffs;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cz.vsb.resbill.exception.PriceListServiceException;
import cz.vsb.resbill.model.PriceList;
import cz.vsb.resbill.service.PriceListService;
import cz.vsb.resbill.service.TariffService;
import cz.vsb.resbill.util.PeriodLimitedEntityValidator;
import cz.vsb.resbill.util.WebUtils;

/**
 * A controller for handling requests for/from tariffs/priceListEdit.html page template.
 * 
 * @author HAL191
 *
 */
@Controller
@RequestMapping("/tariffs/prices/edit")
@SessionAttributes("priceList")
public class PriceListEditController {

	private static final Logger log = LoggerFactory.getLogger(PriceListEditController.class);

	private static final String PRICE_LIST_MODEL_KEY = "priceList";

	@Inject
	private PriceListService priceListService;

	@Inject
	private TariffService tariffService;

	@InitBinder
	public void initBinder(WebDataBinder binder, Locale locale) {
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
		binder.addValidators(new PeriodLimitedEntityValidator());
	}

	private void loadPriceList(Integer priceListId, Integer tariffId, ModelMap model) {
		if (log.isDebugEnabled()) {
			log.debug("Requested priceListId=" + priceListId);
		}
		PriceList priceList = null;
		try {
			if (priceListId != null) {
				priceList = priceListService.findPriceList(priceListId);
			} else {
				priceList = new PriceList();
			}
			if (tariffId != null) {
				priceList.setTariff(tariffService.findTariff(tariffId));
			}
			model.addAttribute(PRICE_LIST_MODEL_KEY, priceList);
		} catch (Exception e) {
			log.error("Cannot load priceList with id: " + priceListId, e);

			model.addAttribute(PRICE_LIST_MODEL_KEY, priceList);
			WebUtils.addGlobalError(model, PRICE_LIST_MODEL_KEY, "error.load.priceList");
		}
		if (log.isDebugEnabled()) {
			log.debug("Loaded priceList: " + priceList);
		}
	}

	/**
	 * Handles all GET requests. Binds loaded {@link PriceList} entity with the key "priceList" into a model.
	 * 
	 * @param priceListId
	 *          key of a {@link PriceList} to view/edit
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String view(@RequestParam(value = "priceListId", required = false) Integer priceListId, @RequestParam(value = "tariffId", required = true) Integer tariffId, ModelMap model) {
		loadPriceList(priceListId, tariffId, model);

		return "tariffs/priceListEdit";
	}

	/**
	 * Handles POST requests for saving edited {@link PriceList} entity.
	 * 
	 * @param priceList
	 * @param bindingResult
	 * @param redirectAttributes
	 * @param sessionStatus
	 * @return
	 */
	@RequestMapping(value = "", method = RequestMethod.POST, params = "save")
	public String save(@Valid @ModelAttribute(PRICE_LIST_MODEL_KEY) PriceList priceList, BindingResult bindingResult, SessionStatus sessionStatus, RedirectAttributes redirectAttributes) {
		if (log.isDebugEnabled()) {
			log.debug("PriceList to save: " + priceList);
		}
		if (!bindingResult.hasErrors()) {
			try {
				priceList = priceListService.savePriceList(priceList);
				if (log.isDebugEnabled()) {
					log.debug("Saved priceList: " + priceList);
				}
			} catch (Exception e) {
				log.error("Cannot save priceList: " + priceList, e);
				bindingResult.reject("error.save.priceList");
				return "tariffs/priceListEdit";
			}
		} else {
			bindingResult.reject("error.save.priceList.validation");
			return "tariffs/priceListEdit";
		}
		sessionStatus.setComplete();
		redirectAttributes.addAttribute("tariffId", priceList.getTariff().getId());
		return "redirect:/tariffs/edit";
	}

	/**
	 * Handle POST requests for deleting {@link PriceList} entity.
	 * 
	 * @param priceList
	 * @param bindingResult
	 * @param sessionStatus
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "", method = RequestMethod.POST, params = "delete")
	public String delete(@ModelAttribute(PRICE_LIST_MODEL_KEY) PriceList priceList, BindingResult bindingResult, SessionStatus sessionStatus, RedirectAttributes redirectAttributes) {
		if (log.isDebugEnabled()) {
			log.debug("PriceList to delete: " + priceList);
		}
		try {
			priceList = priceListService.deletePriceList(priceList.getId());
			if (log.isDebugEnabled()) {
				log.debug("Deleted priceList: " + priceList);
			}
		} catch (PriceListServiceException e) {
			// TODO implement
			bindingResult.reject("error.delete.priceList.constraint.relations");
			return "tariffs/priceListEdit";
		} catch (Exception e) {
			log.error("Cannot delete priceList: " + priceList, e);
			bindingResult.reject("error.delete.priceList");
			return "tariffs/priceListEdit";
		}
		sessionStatus.setComplete();
		redirectAttributes.addAttribute("tariffId", priceList.getTariff().getId());
		return "redirect:/tariffs/edit";
	}
}
