package cz.vsb.resbill.web.contracts;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;
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

import cz.vsb.resbill.criteria.TariffCriteria;
import cz.vsb.resbill.criteria.TariffCriteria.OrderBy;
import cz.vsb.resbill.dto.ContractTariffEditDTO;
import cz.vsb.resbill.exception.ContractTariffServiceException;
import cz.vsb.resbill.model.ContractTariff;
import cz.vsb.resbill.model.Period;
import cz.vsb.resbill.model.Tariff;
import cz.vsb.resbill.service.ContractTariffService;
import cz.vsb.resbill.service.TariffService;
import cz.vsb.resbill.util.WebUtils;

/**
 * A controller for handling requests for/from contracts/contractTariffEdit.html page template.
 * 
 * @author HAL191
 *
 */
@Controller
@RequestMapping("/contracts/tariffs/edit")
@SessionAttributes("contractTariffEditDTO")
public class ContractTariffEditController extends AbstractContractController {

	private static final Logger log = LoggerFactory.getLogger(ContractTariffEditController.class);

	private static final String CONTRACT_TARIFF_EDIT_DTO_MODEL_KEY = "contractTariffEditDTO";

	@Inject
	private ContractTariffService contractTariffService;

	@Inject
	private TariffService tariffService;

	@InitBinder
	public void initBinder(WebDataBinder binder, Locale locale) {
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
	}

	@ModelAttribute("tariffs")
	public List<Tariff> getTariffs() {
		try {
			TariffCriteria criteria = new TariffCriteria();
			criteria.setValid(Boolean.TRUE);
			criteria.setOrderBy(OrderBy.NAME);
			return tariffService.findTariffs(criteria, null, null);
		} catch (Exception e) {
			log.error("Cannot load tariffs", e);
			return null;
		}
	}

	private void loadContractTariffEditDTO(Integer contractTariffId, Integer contractId, ModelMap model) {
		if (log.isDebugEnabled()) {
			log.debug("Requested contractTariff.id=" + contractTariffId);
		}
		ContractTariffEditDTO ctEditDTO = null;
		try {
			if (contractTariffId != null) {
				ctEditDTO = new ContractTariffEditDTO(contractTariffService.findContractTariff(contractTariffId));
			} else {
				ContractTariff ct = new ContractTariff();
				ct.setPeriod(new Period());
				if (contractId != null) {
					ct.setContract(contractService.findContract(contractId));
					ct.getPeriod().setBeginDate(ct.getContract().getPeriod().getBeginDate());
				} else {
					ct.getPeriod().setBeginDate(DateUtils.truncate(new Date(), Calendar.DATE));
				}
				ctEditDTO = new ContractTariffEditDTO(ct);
			}
			model.addAttribute(CONTRACT_TARIFF_EDIT_DTO_MODEL_KEY, ctEditDTO);
		} catch (Exception e) {
			log.error("Cannot load contract tariff with id: " + contractTariffId, e);

			model.addAttribute(CONTRACT_TARIFF_EDIT_DTO_MODEL_KEY, ctEditDTO);
			WebUtils.addGlobalError(model, CONTRACT_TARIFF_EDIT_DTO_MODEL_KEY, "error.load.contract.tariff");
		}
		if (log.isDebugEnabled()) {
			log.debug("Loaded contractTariffEditDTO: " + ctEditDTO);
		}
	}

	/**
	 * Handles all GET requests. Binds loaded {@link ContractTariffEditDTO} entity with the key "contractTariffEditDTO" into a model.
	 * 
	 * @param contractTariffId
	 *          key of a {@link ContractTariffEditDTO} to view/edit
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String view(@RequestParam(value = "contractTariffId", required = false) Integer contractTariffId, @RequestParam(value = CONTRACT_ID_PARAM_KEY, required = false) Integer contractId,
			ModelMap model) {
		loadContractTariffEditDTO(contractTariffId, contractId, model);

		return "contracts/contractTariffEdit";
	}

	/**
	 * Handles POST requests for saving edited {@link ContractTariffEditDTO} instance.
	 * 
	 * @param contractTariffEditDTO
	 * @param bindingResult
	 * @return
	 */
	@RequestMapping(value = "", method = RequestMethod.POST, params = "save")
	public String save(@Valid @ModelAttribute(CONTRACT_TARIFF_EDIT_DTO_MODEL_KEY) ContractTariffEditDTO contractTariffEditDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		if (log.isDebugEnabled()) {
			log.debug("ContractTariffEditDTO to save: " + contractTariffEditDTO);
		}
		if (!bindingResult.hasErrors()) {
			try {
				ContractTariff ct = contractTariffEditDTO.getContractTariff();
				if (ct.getId() == null) {
					ct.setTariff(tariffService.findTariff(contractTariffEditDTO.getTariffId()));
				}
				ct = contractTariffService.saveContractTariff(ct);
				if (log.isDebugEnabled()) {
					log.debug("Saved contract tariff: " + ct);
				}
				redirectAttributes.addAttribute(CONTRACT_ID_PARAM_KEY, ct.getContract().getId());
				return "redirect:/contracts/tariffs";
			} catch (ContractTariffServiceException e) {
				switch (e.getReason()) {
				case NOT_LAST_CONTRACT_TARIFF:
					bindingResult.reject("error.save.contract.tariff.not.last");
					break;
				case OUT_OF_CONTRACT_DURATION:
					bindingResult.reject("error.save.contract.tariff.out.of.duration");
					break;
				case INVALID_PERIOD:
					bindingResult.reject("error.save.contract.tariff.period");
					break;
				case CONTRACT_TARIFF_MODIFICATION:
					bindingResult.reject("error.save.contract.tariff.modified");
					break;
				case FIRST_CONTRACT_TARIFF_BEGIN_DATE_MODIFICATION:
					bindingResult.reject("error.save.contract.tariff.first.beginDate.modified");
					break;
				case NOT_COVERED_BY_PRICE_LISTS:
					bindingResult.reject("error.save.contract.tariff.priceList.coverage");
					break;
				case INVOICE_DAILY_USAGE_UNCOVERED:
					bindingResult.reject("error.save.contract.tariff.invoice.dailyUsage.coverage");
					break;
				default:
					log.warn("Unsupported reason: " + e);
					bindingResult.reject("error.save.contract.tariff");
					break;
				}
			} catch (Exception e) {
				log.error("Cannot save ContractTariffEditDTO: " + contractTariffEditDTO, e);
				bindingResult.reject("error.save.contract.tariff");
			}
		} else {
			bindingResult.reject("error.save.contract.tariff.validation");
		}
		return "contracts/contractTariffEdit";
	}

	/**
	 * Handle POST requests for deleting {@link ContractTariffEditDTO} instance.
	 * 
	 * @param contractTariffEditDTO
	 * @param bindingResult
	 * @return
	 */
	@RequestMapping(value = "", method = RequestMethod.POST, params = "delete")
	public String delete(@ModelAttribute(CONTRACT_TARIFF_EDIT_DTO_MODEL_KEY) ContractTariffEditDTO contractTariffEditDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		if (log.isDebugEnabled()) {
			log.debug("ContractTariffEditDTO to delete: " + contractTariffEditDTO);
		}
		try {
			ContractTariff ct = contractTariffService.deleteContractTariff(contractTariffEditDTO.getContractTariff().getId());
			if (log.isDebugEnabled()) {
				log.debug("Deleted ContractTariff: " + ct);
			}
			redirectAttributes.addAttribute(CONTRACT_ID_PARAM_KEY, ct.getContract().getId());
			return "redirect:/contracts/tariffs";
		} catch (ContractTariffServiceException e) {
			switch (e.getReason()) {
			case NOT_LAST_CONTRACT_TARIFF:
				bindingResult.reject("error.delete.contract.tariff.not.last");
				break;
			case CONTRACT_SERVER_ASSOCIATED:
				bindingResult.reject("error.delete.contract.tariff.server.associated");
				break;
			case DAILY_USAGE_INVOICED:
				bindingResult.reject("error.delete.contract.tariff.invoiced.dailyUsage");
				break;
			case INVOICE_DAILY_USAGE_UNCOVERED:
				bindingResult.reject("error.delete.contract.tariff.invoice.dailyUsage.coverage");
				break;
			default:
				log.warn("Unsupported reason: " + e);
				bindingResult.reject("error.delete.contract.tariff");
				break;
			}
		} catch (Exception e) {
			log.error("Cannot delete ContractTariffEditDTO: " + contractTariffEditDTO, e);
			bindingResult.reject("error.delete.contract.tariff");
		}
		return "contracts/contractTariffEdit";
	}
}
