/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.util;

import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Třída pro vypisování hodnot v metodě toString. Je to org.apache.commons.lang.builder.ToStringBuilder jen s tím, že standardně vytváří více-řádkový výstup.
 * 
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */
public class ToStringBuilder extends org.apache.commons.lang3.builder.ToStringBuilder {

	public ToStringBuilder(Object object) {
		super(object, ToStringStyle.MULTI_LINE_STYLE);
	}
}
