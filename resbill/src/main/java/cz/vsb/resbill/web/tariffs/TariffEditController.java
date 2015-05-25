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

import cz.vsb.resbill.exception.ResBillException;
import cz.vsb.resbill.exception.TariffServiceException;
import cz.vsb.resbill.model.Tariff;
import cz.vsb.resbill.service.TariffService;

@Controller
@RequestMapping("tariffs/edit")
public class TariffEditController {

	private static final Logger log = LoggerFactory.getLogger(TariffEditController.class);

	@Inject
	private TariffService tariffService;

	@InitBinder
	public void initBinder(WebDataBinder binder, Locale locale) {
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
	}

	private Tariff getTariff(Integer tariffId) throws ResBillException {
		if (tariffId != null) {
			return tariffService.findTariff(tariffId);
		} else {
			return new Tariff();
		}
	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String view(@RequestParam(value = "tariffId", required = false) Integer tariffId, ModelMap model) {
		if (log.isDebugEnabled()) {
			log.debug("Searched tariffId=" + tariffId);
		}
		// nacteni tarifu
		try {
			model.addAttribute("tariff", getTariff(tariffId));
			if (log.isDebugEnabled()) {
				log.debug("Edited tariff: " + model.get("tariff"));
			}
		} catch (Exception e) {
			log.error("Cannot load tariff with id: " + tariffId, e);
			// TODO error notification
		}

		return "tariffs/tariffEdit";
	}

	@RequestMapping(value = "", method = RequestMethod.POST, params = "save")
	public String save(@Valid @ModelAttribute("tariff") Tariff tariff, BindingResult bindingResult) {
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
		return "redirect:/tariffs";
	}

	@RequestMapping(value = "", method = RequestMethod.POST, params = "delete")
	public String delete(@ModelAttribute("tariff") Tariff tariff, BindingResult bindingResult) {
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
		return "redirect:/tariffs";
	}
}
