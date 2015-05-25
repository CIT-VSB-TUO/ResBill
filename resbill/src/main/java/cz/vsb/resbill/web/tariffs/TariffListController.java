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
import cz.vsb.resbill.exception.ResBillException;
import cz.vsb.resbill.model.Tariff;
import cz.vsb.resbill.service.TariffService;

@Controller
@RequestMapping("/tariffs")
public class TariffListController {

	private static final Logger log = LoggerFactory.getLogger(TariffListController.class);

	@Inject
	private TariffService tariffService;

	private List<Tariff> getTariffs() throws ResBillException {
		return tariffService.findTariffs(new TariffCriteria(), null, null);
	}

	@RequestMapping(method = RequestMethod.GET)
	public String view(ModelMap model) {
		try {
			model.addAttribute("tariffs", getTariffs());
		} catch (ResBillException e) {
			log.error("Cannot load list of tariffs.", e);
			// TODO error handle
		}
		return "tariffs/tariffList";
	}
}
