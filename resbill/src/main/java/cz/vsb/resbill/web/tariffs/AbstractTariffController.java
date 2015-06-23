package cz.vsb.resbill.web.tariffs;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ModelAttribute;

import cz.vsb.resbill.dto.tariff.TariffHeaderDTO;
import cz.vsb.resbill.service.TariffService;

public abstract class AbstractTariffController {

	private static final Logger log = LoggerFactory.getLogger(AbstractTariffController.class);

	protected static final String TARIFF_HEADER_DTO_MODEL_KEY = "tariffHeaderDTO";

	@Inject
	protected TariffService tariffService;

	@ModelAttribute(TARIFF_HEADER_DTO_MODEL_KEY)
	public TariffHeaderDTO getTariffHeaderDTO(Integer tariffId) {
		if (tariffId != null) {
			try {
				return tariffService.findTariffHeaderDTO(tariffId);
			} catch (Exception e) {
				log.error("Cannot load TariffHeaderDTO with id: " + tariffId, e);
			}
		}
		return null;
	}
}
