package cz.vsb.resbill.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.format.Formatter;

public class BigDecimalFormatter implements Formatter<BigDecimal> {

	public static final String PATTERN_KEY = "decimal.format.detail";

	@Inject
	private MessageSource messageSource;

	private NumberFormat getNumberFormat(Locale locale) {
		String pattern = messageSource.getMessage(PATTERN_KEY, null, locale);
		DecimalFormat format = (DecimalFormat) NumberFormat.getNumberInstance(locale);
		format.applyPattern(pattern);
		format.setParseBigDecimal(true);
		format.setRoundingMode(NumberUtils.STANDARD_ROUNDING_MODE);
		return format;
	}

	@Override
	public String print(BigDecimal object, Locale locale) {
		return getNumberFormat(locale).format(object);
	}

	@Override
	public BigDecimal parse(String text, Locale locale) throws ParseException {
		return StringUtils.isNotEmpty(text) ? (BigDecimal) getNumberFormat(locale).parse(text) : null;
	}
}
