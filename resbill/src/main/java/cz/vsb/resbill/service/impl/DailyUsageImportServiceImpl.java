/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.service.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cz.vsb.resbill.dao.DailyUsageDAO;
import cz.vsb.resbill.dao.ProductionLevelDAO;
import cz.vsb.resbill.dao.ServerDAO;
import cz.vsb.resbill.model.DailyUsage;
import cz.vsb.resbill.model.ProductionLevel;
import cz.vsb.resbill.model.Server;
import cz.vsb.resbill.service.DailyUsageImportService;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */

@Service
@Transactional
public class DailyUsageImportServiceImpl implements DailyUsageImportService {

	private Logger log = LoggerFactory.getLogger(DailyUsageImportServiceImpl.class);

	@Inject
	private ApplicationContext appContext;

	@PersistenceContext
	private EntityManager em;

	@Inject
	private DailyUsageImportService dailyUsageImportService;

	@Inject
	private ServerDAO serverDAO;

	@Inject
	private ProductionLevelDAO productionLevelDAO;

	@Inject
	private DailyUsageDAO dailyUsageDAO;

	/**
	 * Hlavni importni metoda
	 * 
	 */
	@Override
	public void importDailyUsage() {
		// ApplicationContext appContext = new ClassPathXmlApplicationContext("WEB-INF/spring.xml");
		try {

			System.out.println("JSEM V SERVICE: " + "DailyUsageImportServiceImpl");
			log.debug("NECO DEBUGUJI");

			System.out.println(appContext.getBeanDefinitionCount());

			Date date = new java.sql.Date(2014, 02, 01);
			Resource reportResource = appContext.getResource("file:///d:/RessBill/dailyUsage/report_2014-02-01");
			log.debug("Importuji soubor: " + reportResource.getFilename());

			System.out.println(reportResource.getFilename());

			InputStreamReader reader = new InputStreamReader(reportResource.getInputStream());
			BufferedReader buffReader = new BufferedReader(reader);

			List<LineImportData> lineImportDatas = new ArrayList<LineImportData>();

			String line = buffReader.readLine();
			int lineNumber = 0;
			while (line != null) {
				lineNumber++;

				System.out.println(line);

				line = StringUtils.stripToNull(line);

				if (line != null && !line.startsWith("serverId")) {
					LineImportData lineImportData = new LineImportData();
					lineImportData.line = line;
					lineImportData.lineNumber = lineNumber;

					dailyUsageImportService.importLine(date, lineImportData);

					lineImportDatas.add(lineImportData);
				}

				line = buffReader.readLine();
			}

			System.out.println("KONEC");

			log.debug("Import souboru: " + reportResource.getFilename() + " dokoncen.");

		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}

	/**
	 * 
	 * @param line
	 * @return
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void importLine(Date date, LineImportData lineImportData) {
		try {
			log.debug(lineImportData.lineNumber + ": " + lineImportData.line);

			// Nastaveni kodu vysledku pro pripad, ze vse dopadne v poradku (v pripade chyb nebo warningu se bude menit)
			lineImportData.resultCode = DailyUsageImportService.LineImportResultCode.OK;

			// Nacteni dat radku
			String[] lineElements = lineImportData.line.split(";");

			String serverId = StringUtils.stripToNull(lineElements[0]);
			String name = StringUtils.stripToNull(lineElements[1]);
			String production = StringUtils.stripToNull(lineElements[2]);
			String powerState = StringUtils.stripToNull(lineElements[3]);
			String cpu = StringUtils.stripToNull(lineElements[4]);
			String memoryMb = StringUtils.stripToNull(lineElements[5]);
			String provSpaceGb = StringUtils.stripToNull(lineElements[6]);
			String usedSpaceGb = StringUtils.stripToNull(lineElements[7]);
			String backupGb = StringUtils.stripToNull(lineElements[8]);

			// Najit server dle serverId. Pokud neexistuje, zalozit.
			Server server = serverDAO.findServer(serverId);
			if (server == null) {
				server = new Server();
				server.setServerId(serverId);
				server.setName(name);

				serverDAO.saveServer(server);

				lineImportData.resultCode = DailyUsageImportService.LineImportResultCode.OK_NEW_SERVER;
			} else {
				server.setName(name);
				serverDAO.saveServer(server);

				// TODO: proverit, zda je server prirazen ke kontraktu (k danemu dni). Pokud neni, nastavit warning.
			}

			// Najit Uroven produkce
			ProductionLevel productionLevel = null;
			if (production != null) {
				productionLevel = productionLevelDAO.findProductionLevel(production);
			} else {
				productionLevel = productionLevelDAO.findProductionLevel(ProductionLevel.INIT);
			}
			if (productionLevel == null) {
				lineImportData.resultCode = DailyUsageImportService.LineImportResultCode.ERROR_PRODUCTION_LEVEL_NOT_EXISTS;
				return;
			}

			// Test, ze pro dany den a server zaznam doposud neexistuje
			StringBuilder existsJpql = new StringBuilder();
			existsJpql.append(" SELECT dailyUsage ");
			existsJpql.append(" FROM DailyUsage AS dailyUsage ");
			existsJpql.append(" JOIN dailyUsage.server AS server ");
			existsJpql.append(" WHERE dailyUsage.date = :date ");
			existsJpql.append(" AND server.id = :serverId ");
			TypedQuery<DailyUsage> existsQuery = em.createQuery(existsJpql.toString(), DailyUsage.class);
			existsQuery.setParameter("date", date);
			existsQuery.setParameter("serverId", server.getId());
			DailyUsage existingDailyUsage = DataAccessUtils.uniqueResult(existsQuery.getResultList());
			if (existingDailyUsage != null) {
				lineImportData.resultCode = DailyUsageImportService.LineImportResultCode.ERROR_LINE_EXISTS;
				return;
			}

			// Vytvoreni a naplneni objektu dennich zaznamu
			DailyUsage dailyUsage = new DailyUsage();
			dailyUsage.setServer(server);
			dailyUsage.setProductionLevel(productionLevel);
			dailyUsage.setServerName(name);
			Integer cpuInt = NumberUtils.createInteger(cpu);
			dailyUsage.setCpu(cpuInt != null ? cpuInt : 0);
			Integer memoryMbInt = NumberUtils.createInteger(memoryMb);
			dailyUsage.setMemoryMB(memoryMbInt != null ? memoryMbInt : 0);
			BigDecimal provSpaceGbBd = NumberUtils.createBigDecimal(provSpaceGb);
			dailyUsage.setProvisionedSpaceGB(provSpaceGbBd != null ? provSpaceGbBd : new BigDecimal(0));
			BigDecimal usedSpaceGbBd = NumberUtils.createBigDecimal(usedSpaceGb);
			dailyUsage.setUsedSpaceGB(usedSpaceGbBd != null ? usedSpaceGbBd : new BigDecimal(0));
			BigDecimal backupGbBd = NumberUtils.createBigDecimal(backupGb);
			dailyUsage.setBackupGB(backupGbBd != null ? backupGbBd : new BigDecimal(0));
			Integer powerStateInt = NumberUtils.createInteger(powerState);
			dailyUsage.setPowerState(powerStateInt != null ? BooleanUtils.toBooleanObject(powerStateInt) : false);

			// Ulozeni noveho objektu
			dailyUsageDAO.saveDailyUsage(dailyUsage);

		} catch (Exception exc) {
			// Vyjimku pri zpracovani jednoho radku zaznamenam, ale nezastavuji zpracovani celeho souboru
			log.warn(exc.getMessage(), exc);
			lineImportData.exception = exc;
		}
	}
}
