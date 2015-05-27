/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.dto;

import java.io.Serializable;

import cz.vsb.resbill.util.ToStringBuilder;

/**
 * Vysledek hromadneho importu.
 * 
 * Pro predani z aplikacni na prezentacni vrstvu.
 * 
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */
public class DailyImportAllReportsResultDTO implements Serializable {

	/**
   * 
   */
	private static final long serialVersionUID = 1L;

	/**
	 * Pocet vsech zpracovavanych souboru.
	 */
	private int allReports = 0;

	/**
	 * Pocet souboru, ktere obsahovaly bezvadne radky
	 */
	private int okReports = 0;

	/**
	 * Pocet souboru, ktere jiz byly zpracovany drive a nyni tedy byly preskoceny.
	 */
	private int existsReports = 0;

	/**
	 * Pocet souboru, ktere neobsahovaly chybu, ale zaroven obsahovaly radek zpracovany s varovanim
	 */
	private int warnReports = 0;

	/**
	 * Pocet souboru, ktere byly zpracovany s nejakym chybnym radkem
	 */
	private int errorReports = 0;

	/**
	 * Pocet souboru, ktere nebyly zpracovany vubec v dusledku nejake chyby
	 */
	private int criticalErrorReports = 0;

	/**
	 * @return the allReports
	 */
	public int getAllReports() {
		return allReports;
	}

	/**
	 * @param allReports
	 *          the allReports to set
	 */
	public void setAllReports(int allReports) {
		this.allReports = allReports;
	}

	/**
	 * @return the okReports
	 */
	public int getOkReports() {
		return okReports;
	}

	/**
	 * @param okReports
	 *          the okReports to set
	 */
	public void setOkReports(int okReports) {
		this.okReports = okReports;
	}

	/**
	 * @return the existsReports
	 */
	public int getExistsReports() {
		return existsReports;
	}

	/**
	 * @param existsReports
	 *          the existsReports to set
	 */
	public void setExistsReports(int existsReports) {
		this.existsReports = existsReports;
	}

	/**
	 * @return the warnReports
	 */
	public int getWarnReports() {
		return warnReports;
	}

	/**
	 * @param warnReports
	 *          the warnReports to set
	 */
	public void setWarnReports(int warnReports) {
		this.warnReports = warnReports;
	}

	/**
	 * @return the errorReports
	 */
	public int getErrorReports() {
		return errorReports;
	}

	/**
	 * @param errorReports
	 *          the errorReports to set
	 */
	public void setErrorReports(int errorReports) {
		this.errorReports = errorReports;
	}

	/**
	 * @return the criticalErrorReports
	 */
	public int getCriticalErrorReports() {
		return criticalErrorReports;
	}

	/**
	 * @param criticalErrorReports
	 *          the criticalErrorReports to set
	 */
	public void setCriticalErrorReports(int criticalErrorReports) {
		this.criticalErrorReports = criticalErrorReports;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("allReports", allReports);
		builder.append("okReports", okReports);
		builder.append("existsReports", existsReports);
		builder.append("warnReports", warnReports);
		builder.append("errorReports", errorReports);
		builder.append("criticalErrorReports", criticalErrorReports);
		return builder.toString();
	}

}
