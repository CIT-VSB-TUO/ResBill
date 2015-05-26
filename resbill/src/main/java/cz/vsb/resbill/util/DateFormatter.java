package cz.vsb.resbill.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.format.Formatter;

public class DateFormatter implements Formatter<Date> {

	public static final String PATTERN_KEY = "date.format.detail";

	@Inject
	private MessageSource messageSource;

	private DateFormat getDateFormat(Locale locale) {
		String pattern = messageSource.getMessage(PATTERN_KEY, null, locale);
		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
		dateFormat.setLenient(false);
		return dateFormat;
	}

	@Override
	public String print(Date date, Locale locale) {
		return getDateFormat(locale).format(date);
	}

	@Override
	public Date parse(String text, Locale locale) throws ParseException {
		return StringUtils.isNotEmpty(text) ? getDateFormat(locale).parse(text) : null;
	}

}
