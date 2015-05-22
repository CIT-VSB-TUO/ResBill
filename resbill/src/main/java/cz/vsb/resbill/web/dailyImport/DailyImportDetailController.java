/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.web.dailyImport;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */

@Controller
@RequestMapping("/dailyImport/detail")
public class DailyImportDetailController {

	@RequestMapping(method = RequestMethod.GET)
	public String view() {
		return "dailyImport/dailyImportDetail";
	}
}
