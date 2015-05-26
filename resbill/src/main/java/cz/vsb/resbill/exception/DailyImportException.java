/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.exception;


/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */
public class DailyImportException extends ResBillException {

	/**
   * 
   */
	private static final long serialVersionUID = 1L;



	private Reason reason;

	public DailyImportException(Reason reason) {
		super();
		setReason(reason);
	}

	public DailyImportException(Reason reason, String message, Throwable cause) {
		super(message, cause);
		setReason(reason);
	}

	public DailyImportException(Reason reason, String message) {
		super(message);
		setReason(reason);
	}

	public DailyImportException(Reason reason, Throwable cause) {
		super(cause);
		setReason(reason);
	}

	public Reason getReason() {
		return reason;
	}

	public void setReason(Reason reason) {
		this.reason = reason;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PersonServiceException [");
		builder.append(super.toString());
		builder.append(", reason=");
		builder.append(reason);
		builder.append("]");
		return builder.toString();
	}

	/**
	 * 
	 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
	 *
	 */
	public static enum Reason {
		/**
		 * Nepodarilo se ziskat datum reportu z nazvu souboru - chyba parsovani datumu.
		 */
		IMPORT_REPORT_DATE_PARSE_ERROR,
		
		/**
		 * Pro toto datum jiz denni import existuje
		 */
		IMPORT_REPORT_DATE_EXISTS,
		
		/**
		 * Nepodarilo se precist data z reportu (vstupniho souboru).
		 */
		IMPORT_REPORT_DATA_UNREADABLE,
	}
}
