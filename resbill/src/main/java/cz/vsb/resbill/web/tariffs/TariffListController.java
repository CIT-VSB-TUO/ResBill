package cz.vsb.resbill.web.tariffs;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cz.vsb.resbill.criteria.TariffCriteria;
import cz.vsb.resbill.model.Tariff;
import cz.vsb.resbill.service.TariffService;
import cz.vsb.resbill.util.WebUtils;

/**
 * A controller for handling requests for/from tariffs/tariffList.html page template.
 * 
 * @author HAL191
 *
 */
@Controller
@RequestMapping("/tariffs")
public class TariffListController {

	private static final Logger log = LoggerFactory.getLogger(TariffListController.class);

	private static final String TARIFFS_MODEL_KEY = "tariffs";

	@Inject
	private TariffService tariffService;

	private void loadTariffs(ModelMap model) {
		List<Tariff> tariffs = null;
		try {
			tariffs = tariffService.findTariffs(new TariffCriteria(), null, null);
			model.addAttribute(TARIFFS_MODEL_KEY, tariffs);
		} catch (Exception e) {
			log.error("Cannot load list of tariffs.", e);

			model.addAttribute(TARIFFS_MODEL_KEY, tariffs);
			WebUtils.addGlobalError(model, TARIFFS_MODEL_KEY, "error.load.tariffs");
		}
		if (log.isDebugEnabled()) {
			log.debug("Loaded list of tariffs size: " + (tariffs != null ? tariffs.size() : null));
		}
	}

	/**
	 * Handles all GET requests. Loads a list of all tariffs and binds it with the key "tariffs" into a model.
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String view(ModelMap model) {
		loadTariffs(model);

		return "tariffs/tariffList";
	}
}
