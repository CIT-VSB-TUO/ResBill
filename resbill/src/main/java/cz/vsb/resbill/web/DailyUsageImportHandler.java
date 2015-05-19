/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.HttpRequestHandler;

import cz.vsb.resbill.service.DailyUsageImportService;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */
@Component(value = "DailyUsageImportServlet")
public class DailyUsageImportHandler implements HttpRequestHandler {

	@Inject
	private DailyUsageImportService dailyUsageImportService;

	/**
	 * 
	 */
	@Override
	public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();

		System.out.println("DOVOLAL JSEM SE");
		dailyUsageImportService.importDailyUsage();
		System.out.println("HOTOVO");

		out.println("DOKONCENO");
	}

}
