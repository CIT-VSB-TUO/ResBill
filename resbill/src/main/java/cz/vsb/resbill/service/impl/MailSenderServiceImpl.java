/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.service.impl;

import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import cz.vsb.resbill.service.MailSenderService;
import cz.vsb.resbill.util.ResourceBundleUtils;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */

@Service
public class MailSenderServiceImpl implements MailSenderService {

	private Logger log = LoggerFactory.getLogger(MailSenderServiceImpl.class);

	/**
   * 
   */
	@Override
	public boolean send(String from, String to, String subject, String text) {
		boolean result = false;

		ResourceBundle rb = ResourceBundle.getBundle(ResourceBundleUtils.CONFIG_BUNDLE);
		String smtpServer = rb.getString("email.server.smtp");

		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost(smtpServer);

		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setFrom(from);
		msg.setTo(to);
		msg.setSubject(subject);
		msg.setText(text);

		try {
			mailSender.send(msg);

			result = true;
			log.info("Odeslan email. From: " + from + ", to: " + to + ", subject: " + subject + ".");
		} catch (MailException exc) {
			log.error(exc.getMessage(), exc);
			result = false;
			log.info("NEodeslan email. From: " + from + ", to: " + to + ", subject: " + subject + ".");
		}

		return result;
	}

	/**
	 * 
	 */
	@Override
	public boolean send(String to, String subject, String text) {
		ResourceBundle rb = ResourceBundle.getBundle(ResourceBundleUtils.CONFIG_BUNDLE);
		String from = rb.getString("email.sender.default");

		return send(from, to, subject, text);
	}

}
