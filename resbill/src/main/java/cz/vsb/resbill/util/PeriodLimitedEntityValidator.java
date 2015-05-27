package cz.vsb.resbill.util;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import cz.vsb.resbill.model.Period;
import cz.vsb.resbill.model.PeriodLimitedEntity;

public class PeriodLimitedEntityValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return PeriodLimitedEntity.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		PeriodLimitedEntity entity = (PeriodLimitedEntity) target;

		Period period = entity.getPeriod();
		if (period == null) {
			errors.rejectValue("period", "validation.field.null");
		}
		errors.pushNestedPath("period");
		if (period.getBeginDate() != null && period.getEndDate() != null && period.getBeginDate().compareTo(period.getEndDate()) > 0) {
			errors.rejectValue("endDate", "validation.period.incorrect");
		}
		errors.popNestedPath();
	}

}
