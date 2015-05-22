package cz.vsb.resbill.web.tariffs;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cz.vsb.resbill.criteria.TariffCriteria;
import cz.vsb.resbill.model.Tariff;
import cz.vsb.resbill.service.TariffService;

@Controller
@RequestMapping("/tariffs")
public class TariffListController {

	@Inject
	private TariffService tariffService;

	@ModelAttribute("tariffs")
	public List<Tariff> getTariffs() {
		return tariffService.findTariffs(new TariffCriteria());
	}

	@RequestMapping(method = RequestMethod.GET)
	public String view() {
		return "tariffs/tariffList";
	}
}
