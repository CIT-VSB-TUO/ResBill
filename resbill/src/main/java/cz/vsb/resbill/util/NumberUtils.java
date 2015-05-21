/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */
public class NumberUtils {

	public static final int STANDARD_SCALE = 2;

	public static final RoundingMode STANDARD_ROUNDING_MODE = RoundingMode.HALF_UP;

	/**
	 * Nastavi standardni zaokrouhlovani.
	 * 
	 * @param bigDecimal
	 */
	public static BigDecimal normalize(BigDecimal bigDecimal) {
		return bigDecimal.setScale(STANDARD_SCALE, STANDARD_ROUNDING_MODE);
	}

	/**
	 * Vytvori BigDecimal ze Stringu a nastavi mu standardni chovani.
	 * 
	 * @param numString
	 * @return
	 */
	public static BigDecimal createBigDecimal(String numString) {
		BigDecimal bigDecimal = org.apache.commons.lang3.math.NumberUtils.createBigDecimal(numString);

		if (bigDecimal != null) {
			bigDecimal = normalize(bigDecimal);
		}

		return bigDecimal;
	}

	/**
	 * Vytvori BigDecimal ze Stringu a nastavi mu standardni chovani. Pokud by byl vysledkem null, vrati default hodnotu
	 * 
	 * @param numString
	 * @param defaultBigDecimal
	 * @return
	 */
	public static BigDecimal createBigDecimal(String numString, BigDecimal defaultBigDecimal) {
		BigDecimal bigDecimal = createBigDecimal(numString);

		if (bigDecimal == null) {
			bigDecimal = defaultBigDecimal;
		}

		return bigDecimal;
	}

	/**
	 * Vytvori Integer ze Stringu
	 * 
	 * @param numString
	 * @return
	 */
	public static Integer createInteger(String numString) {
		return org.apache.commons.lang3.math.NumberUtils.createInteger(numString);
	}

	/**
	 * Vytvori Integer ze Stringu. Pokud by byl vysledkem null, vrati default hodnotu
	 * 
	 * @param numString
	 * @return
	 */
	public static Integer createInteger(String numString, Integer defaultInteger) {
		Integer integer = createInteger(numString);

		if (integer == null) {
			integer = defaultInteger;
		}

		return integer;
	}
}
