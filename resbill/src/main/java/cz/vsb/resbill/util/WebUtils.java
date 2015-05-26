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
	 * 
	 * @param model
	 * @param objectName
	 * @param msgKey
	 */
	public static void addGlobalError(ModelMap model, String objectName, String msgKey) {
		BindingResult bindingResult = (BindingResult) model.get(BindingResult.MODEL_KEY_PREFIX + "dailyImport");
		if (bindingResult == null) {
			bindingResult = new BeanPropertyBindingResult(model.get(objectName), "dailyImport");
			model.put(BindingResult.MODEL_KEY_PREFIX + "dailyImport", bindingResult);
		}
		bindingResult.reject(msgKey);
	}
}
