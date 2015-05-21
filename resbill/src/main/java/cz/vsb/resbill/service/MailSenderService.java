/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.service;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */
public interface MailSenderService {

	/**
	 * Odesle email. Vraci true, pokud byl email uspecne odeslan. Jinak vraci false (pripadna vyjimka je osetrena uvnitr metody).
	 * 
	 * @param from
	 * @param to
	 * @param subject
	 * @param text
	 */
	boolean send(String from, String to, String subject, String text);

	/**
	 * Odesle email s default odesilatelem. Vraci true, pokud byl email uspecne odeslan. Jinak vraci false (pripadna vyjimka je osetrena uvnitr metody).
	 * 
	 * @param to
	 * @param subject
	 * @param text
	 */
	boolean send(String to, String subject, String text);
}
