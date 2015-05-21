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

import cz.vsb.resbill.service.DailyImportService;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */
@Component(value = "DailyImportServlet")
public class DailyImportHandler implements HttpRequestHandler {

	@Inject
	private DailyImportService dailyImportService;

	/**
	 * 
	 */
	@Override
	public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();

//		Date date = new java.sql.Date(2014, 02, 01);

//		out.println("Importuji pro den: " + date);
		out.println("Zacinam import...");
		out.flush();

//		dailyImportService.importDailyReport(date);
		dailyImportService.importAllReports();
		
		

		out.println("DOKONCENO");
	}

}
