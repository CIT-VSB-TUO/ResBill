/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.util;

import java.util.ResourceBundle;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */
public class ResourceBundleUtils {

	private static final String CONFIG_BUNDLE = "config";

	private static final String CONFIG_TEST_BUNDLE = "config-test";

	public static ResourceBundle getConfigBundle() {
		ResourceBundle bundle = getBundle(CONFIG_TEST_BUNDLE);
		if (bundle != null) {
			return bundle;
		}
		return getBundle(CONFIG_BUNDLE);
	}

	private static ResourceBundle getBundle(String bundleName) {
		try {
			return ResourceBundle.getBundle(bundleName);
		} catch (Exception e) {
			return null;
		}
	}
}
