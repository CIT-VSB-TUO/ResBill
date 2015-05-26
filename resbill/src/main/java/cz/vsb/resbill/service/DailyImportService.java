/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.service;

import java.io.File;
import java.util.List;

import cz.vsb.resbill.criteria.DailyImportCriteria;
import cz.vsb.resbill.exception.DailyImportException;
import cz.vsb.resbill.exception.ResBillException;
import cz.vsb.resbill.model.DailyImport;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */
public interface DailyImportService {

	public static final String REPORT_FILE_NAME_PREFIX = "report_";

	public static final String REPORT_FILE_NAME_DATE_PATERN = "yyyy-MM-dd";

	DailyImport findDailyImport(Integer dailyImportId) throws ResBillException;

	DailyImport findDailyImport(Integer dailyImportId, boolean initializeReport, boolean initializeProtocol) throws ResBillException;

	List<DailyImport> findDailyImports(DailyImportCriteria criteria, Integer offset, Integer limit) throws ResBillException;

	DailyImport deleteDailyImport(Integer dailyImportId) throws ResBillException;

	void importAllReports() throws ResBillException;

	void importDailyReport(File file) throws DailyImportException;

	DailyImport beginDailyImport(DailyImport dailyImport);

	void endDailyImport(DailyImport dailyImport, List<LineImportData> lineImportDatas);

	void importLine(DailyImport dailyImport, LineImportData lineImportData);

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
		 * ID serveru, tak jak bylo precteno z radku
		 */
		public String serverId;

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
		OK(LineImportResultCodeGroup.OK),

		/**
		 * Nacteno v poradku, pribyl novy server
		 */
		OK_NEW_SERVER(LineImportResultCodeGroup.WARN),

		/**
		 * Nasteno v poradku, ale existujici server neni prirazen zadnemu kontraktu
		 */
		OK_NO_CONTRACT(LineImportResultCodeGroup.WARN),

		/**
		 * Chyba - nepodarilo se rozparsovat udaje na radku
		 */
		ERROR_LINE_FORMAT(LineImportResultCodeGroup.ERROR),

		/**
		 * Chyba - importovany radek v DB jiz existuje (tj. existuje kombinace Server(serverID) a datumu)
		 */
		ERROR_LINE_EXISTS(LineImportResultCodeGroup.ERROR),

		/**
		 * Uvedeny kod urovne produkce nebyla nalezena v ciselniku
		 */
		ERROR_PRODUCTION_LEVEL_NOT_EXISTS(LineImportResultCodeGroup.ERROR),

		/**
		 * Chyba - nepodarilo se ulozit nacteny radek
		 */
		ERROR_SAVE(LineImportResultCodeGroup.ERROR),

		/**
		 * Neznama chyba
		 */
		ERROR_OTHER(LineImportResultCodeGroup.ERROR),

		;

		private LineImportResultCodeGroup codeGroup;

		/**
		 * 
		 * @param codeGroup
		 */
		private LineImportResultCode(LineImportResultCodeGroup codeGroup) {
			this.codeGroup = codeGroup;
		}

		/**
		 * @return the codeGroup
		 */
		public LineImportResultCodeGroup getCodeGroup() {
			return codeGroup;
		}

	}

	/**
	 * 
	 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
	 *
	 */
	public enum LineImportResultCodeGroup {
		/**
		 * Vse v poradku bez vyhrad
		 */
		OK,

		/**
		 * Vysledek v poradku, ale administrator by se na to mel podivat
		 */
		WARN,

		/**
		 * Chyba
		 */
		ERROR,
	}
}
