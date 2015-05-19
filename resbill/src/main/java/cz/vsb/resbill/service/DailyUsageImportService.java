/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.service;

import java.util.Date;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */
public interface DailyUsageImportService {

	public void importDailyUsage();

	public void importLine(Date date, LineImportData lineImportData);

	/**
	 * 
	 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
	 *
	 */
	public static class LineImportData {

		/**
		 * Cislo radku zpracovavaneho souboru
		 */
		public int lineNumber;

		/**
		 * Textovy obsah celeho radku
		 */
		public String line;

		/**
		 * Priznak vysledku zpracovani
		 */
		public LineImportResultCode resultCode;

		/**
		 * Pokud dojde k vyjimce, zde je zaznamenana
		 */
		public Exception exception;
	}

	/**
	 * 
	 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
	 *
	 */
	public enum LineImportResultCode {
		/**
		 * Vse je v poradku (bez vyhrad)
		 */
		OK,

		/**
		 * Nacteno v poradku, pribyl novy server
		 */
		OK_NEW_SERVER,

		/**
		 * Nasteno v poradku, ale existujici server neni prirazen zadnemu kontraktu
		 */
		OK_NO_CONTRACT,

		/**
		 * Chyba - nepodarilo se rozparsovat udaje na radku
		 */
		ERROR_LINE_FORMAT,

		/**
		 * Chyba - importovany radek v DB jiz existuje (tj. existuje kombinace Server(serverID) a datumu)
		 */
		ERROR_LINE_EXISTS,

		/**
		 * Uvedeny kod urovne produkce nebyla nalezena v ciselniku
		 */
		ERROR_PRODUCTION_LEVEL_NOT_EXISTS,
		
		/**
		 * Chyba - nepodarilo se ulozit nacteny radek
		 */
		ERROR_SAVE,

		/**
		 * Neznama chyba
		 */
		ERROR_OTHER
	}
}
