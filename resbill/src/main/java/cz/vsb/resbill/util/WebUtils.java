/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.util;

import org.springframework.ui.ModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */
public class WebUtils {

	/**
	 * Prida globalni chybu
	 * 
	 * @param model mapa modelovych objektu
	 * @param object Name jmeno objektu, u nejz doslo k chybe. Klic musi byt dopredu zavedeny v modelu
	 * @param msgKey Klic zpravy z properties souboru
	 */
	public static void addGlobalError(ModelMap model, String objectName, String msgKey) {
		BindingResult bindingResult = (BindingResult) model.get(BindingResult.MODEL_KEY_PREFIX + objectName);
		if (bindingResult == null) {
			bindingResult = new BeanPropertyBindingResult(model.get(objectName), objectName);
			model.put(BindingResult.MODEL_KEY_PREFIX + objectName, bindingResult);
		}
		bindingResult.reject(msgKey);
	}
}
