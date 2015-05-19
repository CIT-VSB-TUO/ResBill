/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.dao;

import cz.vsb.resbill.model.ProductionLevel;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */
public interface ProductionLevelDAO {

	ProductionLevel findProductionLevel(Integer productionLevelId);

	ProductionLevel findProductionLevel(String productionLevelCode);
}
