package cz.vsb.resbill.web.tariffs;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.validation.Valid;

import org.apache.commons.lang3.time.DateUtils;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cz.vsb.resbill.dto.tariff.TariffPriceListDTO;
import cz.vsb.resbill.exception.PriceListServiceException;
import cz.vsb.resbill.model.Period;
import cz.vsb.resbill.model.PriceList;
import cz.vsb.resbill.model.Tariff;
import cz.vsb.resbill.util.WebUtils;

/**
 * A controller for handling requests for/from tariffs/tariffEdit.html page template.
 * 
 * @author HAL191
 *
 */
@Controller
@RequestMapping("tariffs/edit")
@SessionAttributes("tariffPriceListDTO")
public class TariffEditController extends AbstractTariffController {

	private static final Logger log = LoggerFactory.getLogger(TariffEditController.class);

	private static final String TARIFF_PRICE_LIST_DTO_MODEL_KEY = "tariffPriceListDTO";

	@InitBinder
	public void initBinder(WebDataBinder binder, Locale locale) {
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
	}

	private void loadTariffPriceListDTO(Integer tariffId, ModelMap model) {
		if (log.isDebugEnabled()) {
			log.debug("Requested tariffId=" + tariffId);
		}
		TariffPriceListDTO dto = null;
		try {
			if (tariffId != null) {
				dto = tariffService.findTariffPriceListDTO(tariffId);
			} else {
				dto = new TariffPriceListDTO(new Tariff(), new PriceList());
				dto.getTariff().setValid(Boolean.TRUE);
				dto.getLastPriceList().setTariff(dto.getTariff());
				dto.getLastPriceList().setPeriod(new Period());
				dto.getLastPriceList().getPeriod().setBeginDate(DateUtils.truncate(new Date(), Calendar.DATE));
			}
			model.addAttribute(TARIFF_PRICE_LIST_DTO_MODEL_KEY, dto);
		} catch (Exception e) {
			log.error("Cannot load tariffPriceListDTO by id: " + tariffId, e);

			model.addAttribute(TARIFF_PRICE_LIST_DTO_MODEL_KEY, dto);
			WebUtils.addGlobalError(model, TARIFF_PRICE_LIST_DTO_MODEL_KEY, "error.load.tariff");
		}
		if (log.isDebugEnabled()) {
			log.debug("Loaded tariffPriceListDTO: " + dto);
		}
	}

	/**
	 * Handles all GET requests. Binds loaded {@link TariffPriceListDTO} entity with the key "tariffPriceListDTO" into a model and optionally "priceLists" with a list of tariff's price lists.
	 * 
	 * @param tariffId
	 *          key of a {@link TariffPriceListDTO} to view/edit
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String view(@RequestParam(value = TARIFF_ID_PARAM_KEY, required = false) Integer tariffId, ModelMap model) {
		// nacteni tarifu
		loadTariffPriceListDTO(tariffId, model);

		return "tariffs/tariffEdit";
	}

	/**
	 * Handles POST requests for saving edited {@link TariffPriceListDTO} instance.
	 * 
	 * @param tariff
	 * @param bindingResult
	 * @return
	 */
	@RequestMapping(value = "", method = RequestMethod.POST, params = "save")
	public String save(@Valid @ModelAttribute(TARIFF_PRICE_LIST_DTO_MODEL_KEY) TariffPriceListDTO dto, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		if (log.isDebugEnabled()) {
			log.debug("Tariff to save: " + dto.getTariff());
			log.debug("PriceList to save: " + dto.getLastPriceList());
		}
		if (!bindingResult.hasErrors()) {
			try {
				Tariff tariff = tariffService.saveTariffPriceListDTO(dto);
				if (log.isDebugEnabled()) {
					log.debug("Saved tariffPriceListDTO: " + dto);
				}
				redirectAttributes.addAttribute(TARIFF_ID_PARAM_KEY, tariff.getId());
				return "redirect:/tariffs/overview";
			} catch (PriceListServiceException e) {
				switch (e.getReason()) {
				case NOT_LAST_PRICE_LIST:
					bindingResult.reject("error.save.priceList.not.last");
					break;
				case INVOICE_EXISTENCE:
					bindingResult.reject("error.save.priceList.invoice.exists");
					break;
				case INVOICE_DATE_COLLISION:
					bindingResult.reject("error.save.priceList.invoice.date");
					break;
				case CONTRACT_PERIOD_UNCOVERED:
					bindingResult.reject("error.save.priceList.contract.uncovered");
					break;
				case INVALID_PERIOD:
					bindingResult.reject("error.save.priceList.period");
					break;
				case TARIFF_MODIFICATION:
					bindingResult.reject("error.save.priceList.tariff.modification");
					break;
				default:
					log.warn("Unsupported reason: " + e);
					bindingResult.reject("error.save.priceList");
					break;
				}
			} catch (Exception e) {
				log.error("Cannot save tariffPriceListDTO: " + dto, e);
				bindingResult.reject("error.save.tariff");
			}
		} else {
			bindingResult.reject("error.save.tariff.validation");
		}
		return "tariffs/tariffEdit";
	}

}
