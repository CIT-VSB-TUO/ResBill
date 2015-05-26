/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cz.vsb.resbill.criteria.DailyImportCriteria;
import cz.vsb.resbill.dao.DailyImportDAO;
import cz.vsb.resbill.dao.DailyUsageDAO;
import cz.vsb.resbill.dao.ProductionLevelDAO;
import cz.vsb.resbill.dao.ServerDAO;
import cz.vsb.resbill.exception.ResBillException;
import cz.vsb.resbill.model.ContractServer;
import cz.vsb.resbill.model.DailyImport;
import cz.vsb.resbill.model.DailyUsage;
import cz.vsb.resbill.model.Person;
import cz.vsb.resbill.model.ProductionLevel;
import cz.vsb.resbill.model.Server;
import cz.vsb.resbill.service.DailyImportService;
import cz.vsb.resbill.service.MailSenderService;
import cz.vsb.resbill.util.NumberUtils;
import cz.vsb.resbill.util.ResourceBundleUtils;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */

@Service
@Transactional
public class DailyImportServiceImpl implements DailyImportService {

	private static final Logger log = LoggerFactory.getLogger(DailyImportServiceImpl.class);

	@Inject
	private ApplicationContext appContext;

	@PersistenceContext
	private EntityManager em;

	@Inject
	private DailyImportService dailyImportService;

	@Inject
	private MailSenderService mailSenderService;

	@Inject
	private ServerDAO serverDAO;

	@Inject
	private ProductionLevelDAO productionLevelDAO;

	@Inject
	private DailyImportDAO dailyImportDAO;

	@Inject
	private DailyUsageDAO dailyUsageDAO;

	/**
	 * 
	 * @param dailyImportId
	 * @return
	 * @throws ResBillException
	 */
	public DailyImport findDailyImport(Integer dailyImportId) throws ResBillException {
		return findDailyImport(dailyImportId, false, false);
	}

	/**
	 * 
	 * @param dailyImportId
	 * @param initializeReport
	 * @param initializeProtocol
	 * @return
	 * @throws ResBillException
	 */
	public DailyImport findDailyImport(Integer dailyImportId, boolean initializeReport, boolean initializeProtocol) throws ResBillException {
		try {
			DailyImport dailyImport = dailyImportDAO.findDailyImport(dailyImportId);

			if (dailyImport != null) {
				if (initializeReport) {
					dailyImport.getReport();
				}

				if (initializeProtocol) {
					dailyImport.getProtocol();
				}
			}

			return dailyImport;
		} catch (Exception exc) {
			log.error(exc.getMessage(), exc);
			throw new ResBillException(exc);
		}
	}

	/**
	 * 
	 * @param criteria
	 * @return
	 */
	public List<DailyImport> findDailyImports(DailyImportCriteria criteria, Integer offset, Integer limit) throws ResBillException {
		try {
			return dailyImportDAO.findDailyImports(criteria, offset, limit);
		} catch (Exception exc) {
			log.error(exc.getMessage(), exc);
			throw new ResBillException(exc);
		}
	}

	/**
	 * 
	 */
	@Override
	public DailyImport deleteDailyImport(Integer dailyImportId) {
		DailyImport dailyImport = dailyImportDAO.findDailyImport(dailyImportId);
		if (dailyImport != null) {
			dailyImport = dailyImportDAO.deleteDailyImport(dailyImport);
		}
		return dailyImport;
	}

	/**
	 * Hlavni importni metoda
	 * 
	 */
	@Override
	public void importAllReports() {
		log.info("Zacinam import celeho adresare.");

		try {

			ResourceBundle rb = ResourceBundle.getBundle(ResourceBundleUtils.CONFIG_BUNDLE);
			String dirName = rb.getString("dir.import.reports");
			log.info("Jmeno adresare: " + dirName);

			// Ziskani adresare
			Resource dirRes = appContext.getResource("file:///" + dirName);
			File dir = dirRes.getFile();

			// Filtruji jen soubory, jejichz nazev zacina na "report_"
			FilenameFilter filter = new FilenameFilter() {

				@Override
				public boolean accept(File dir, String name) {
					return name.startsWith(REPORT_FILE_NAME_PREFIX);
				}
			};

			// Postupne budu zpracovavat vsechny nalezene soubory
			File[] files = dir.listFiles(filter);
			for (File file : files) {
				dailyImportService.importDailyReport(file);

				// break; // pro ucely ladeni
			}

		} catch (Exception exc) {
			log.error(exc.getMessage(), exc);
		}

		log.info("Import celeho adresare dokoncen.");
	}

	/**
	 * Hlavni importni metoda pro jeden report (jeden den)
	 * 
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void importDailyReport(File file) {
		String fileName = file.getName();
		log.info("Zacinam importovat soubor: " + fileName);

		// Ziskani datumu
		Date date = null;
		try {
			String dateString = StringUtils.remove(fileName, REPORT_FILE_NAME_PREFIX);
			date = DateUtils.parseDateStrictly(dateString, REPORT_FILE_NAME_DATE_PATERN);
		} catch (ParseException exc) {
			log.error(exc.getMessage(), exc);
			log.info("Neproveden import souboru: " + fileName + " - nepodarilo se zjistit datum.");
			return;
		}

		// Overeni, ze pro dane datum jeste nebyl import proveden
		DailyImport existingDailyImport = dailyImportDAO.findDailyImport(date);
		if (existingDailyImport != null) {
			log.info("Neproveden import souboru: " + fileName + " - pro tento den byl jiz import proveden.");
			return;
		}

		// Precteni dat ze souboru do Stringu
		String report = null;
		try (BufferedReader buffReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));) {
			report = IOUtils.toString(buffReader);
		} catch (IOException exc) {
			log.error(exc.getMessage(), exc);
			log.info("Neproveden import souboru: " + fileName + " - ze souboru se nepodarilo precist zaznamy.");
			return;
		}

		// Zaznamenani zapoceti denniho importu.
		// Ulozeni znamych udaju.
		DailyImport dailyImport = new DailyImport();
		dailyImport.setDate(date);
		dailyImport.setReportName(fileName);
		dailyImport.setReport(report);
		dailyImport = dailyImportService.beginDailyImport(dailyImport);

		// Prochazeni jednotlivych radku
		List<LineImportData> lineImportDatas = new ArrayList<LineImportData>();
		String[] lines = report.split("\n");
		for (int lineNumber = 0; lineNumber < lines.length; lineNumber++) {
			String line = lines[lineNumber];

			line = StringUtils.stripToNull(line);

			if (line != null && !line.startsWith("serverId")) {
				LineImportData lineImportData = new LineImportData();
				lineImportData.line = line;
				lineImportData.lineNumber = lineNumber + 1; // v poli se pocita od 0, ale radky v souboru jsou od 1

				dailyImportService.importLine(dailyImport, lineImportData);

				lineImportDatas.add(lineImportData);
			}
		}

		// Zaznamenani ukonceni denniho importu
		dailyImportService.endDailyImport(dailyImport, lineImportDatas);

		log.info("Dokoncen import souboru: " + fileName);
	}

	/**
   * 
   */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public DailyImport beginDailyImport(DailyImport dailyImport) {
		dailyImport.setImportBeginTimestamp(new Date());

		return dailyImportDAO.saveDailyImport(dailyImport);
	}

	/**
	 * 
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void endDailyImport(DailyImport dailyImport, List<LineImportData> lineImportDatas) {
		StringBuilder protocol = new StringBuilder();
		int okLines = 0;
		int warnLines = 0;
		int errorLines = 0;

		for (LineImportData lineImportData : lineImportDatas) {
			if (protocol.length() > 0) {
				protocol.append("\n");
			}

			protocol.append(lineImportData.lineNumber);
			protocol.append(":");
			protocol.append(lineImportData.serverId);
			protocol.append(":");
			protocol.append(lineImportData.resultCode);
			if (lineImportData.exception != null) {
				protocol.append("\n");
				protocol.append(ExceptionUtils.getStackTrace(lineImportData.exception));
			}

			if (lineImportData.resultCode.getCodeGroup() == DailyImportService.LineImportResultCodeGroup.OK) {
				okLines++;
			}
			if (lineImportData.resultCode.getCodeGroup() == DailyImportService.LineImportResultCodeGroup.WARN) {
				warnLines++;
			}
			if (lineImportData.resultCode.getCodeGroup() == DailyImportService.LineImportResultCodeGroup.ERROR) {
				errorLines++;
			}
		}

		dailyImport.setAllLines(okLines + warnLines + errorLines);
		dailyImport.setOkLines(okLines);
		dailyImport.setWarnLines(warnLines);
		dailyImport.setErrorLines(errorLines);

		dailyImport.setSuccess(errorLines == 0);

		log.info("Vysledek importu: AllLines: " + dailyImport.getAllLines() + ", OkLines: " + dailyImport.getOkLines() + ", WarnLines: " + dailyImport.getWarnLines() + ", ErrorLines: "
		    + dailyImport.getErrorLines());

		// TODO: nacpat do prekladoveho souboru
		// Souhrnne informace:
		StringBuilder protocolSummary = new StringBuilder();
		protocolSummary.append("Zpracovávaný soubor: ").append(dailyImport.getReportName());
		protocolSummary.append("\n----------------------------------------------------------");
		protocolSummary.append("\nVýsledek importu: ");
		if (errorLines == 0 && warnLines == 0) {
			protocolSummary.append("Vše v pořádku. Není potřeba kontrola.");
		} else if (errorLines == 0) {
			protocolSummary.append("Import v pořádku, ale existují varování. Doporučena kontrola.");
		} else {
			protocolSummary.append("Import s chybama. Nutná kontrola.");
		}
		protocolSummary.append("\n----------------------------------------------------------");
		protocolSummary.append("\nCelkem řádků: ").append(dailyImport.getAllLines());
		protocolSummary.append("\nBezvadných řádků: ").append(dailyImport.getOkLines());
		protocolSummary.append("\nŘádků s varováním: ").append(dailyImport.getWarnLines());
		protocolSummary.append("\nChybných řádků: ").append(dailyImport.getErrorLines());
		protocolSummary.append("\n----------------------------------------------------------");
		protocolSummary.append("\n\n");

		protocol.insert(0, protocolSummary);

		dailyImport.setProtocol(protocol.toString());
		dailyImport.setImportEndTimestamp(new Date());

		dailyImportDAO.saveDailyImport(dailyImport);

		ResourceBundle rb = ResourceBundle.getBundle(ResourceBundleUtils.CONFIG_BUNDLE);
		mailSenderService.send(rb.getString("email.group.admins"), rb.getString("email.subject.dailyImport") + " - " + dailyImport.getReportName(), dailyImport.getProtocol());
	}

	/**
	 * 
	 * @param line
	 * @return
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void importLine(DailyImport dailyImport, LineImportData lineImportData) {
		try {
			log.info(lineImportData.lineNumber + ": " + lineImportData.line);

			// Nastaveni kodu vysledku pro pripad, ze vse dopadne v poradku (v pripade chyb nebo warningu se bude menit)
			lineImportData.resultCode = DailyImportService.LineImportResultCode.OK;

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

			lineImportData.serverId = serverId;

			// Najit server dle serverId. Pokud neexistuje, zalozit.
			Server server = serverDAO.findServer(serverId);
			if (server == null) {
				server = new Server();
				server.setServerId(serverId);
				server.setName(name);

				serverDAO.saveServer(server);

				lineImportData.resultCode = DailyImportService.LineImportResultCode.OK_NEW_SERVER;
			} else {
				server.setName(name);
				serverDAO.saveServer(server);

				// proverit, zda je server prirazen ke kontraktu (k danemu dni). Pokud neni, nastavit warning.
				StringBuilder contractExistsJpql = new StringBuilder();
				contractExistsJpql.append(" SELECT contractServer ");
				contractExistsJpql.append(" FROM ContractServer AS contractServer ");
				contractExistsJpql.append(" JOIN contractServer.server AS server ");
				contractExistsJpql.append(" WHERE server.id = :serverId ");
				contractExistsJpql.append(" AND contractServer.period.beginDate <= :date ");
				contractExistsJpql.append(" AND (contractServer.period.endDate IS NULL OR contractServer.period.endDate >= :date) ");
				TypedQuery<ContractServer> existsQuery = em.createQuery(contractExistsJpql.toString(), ContractServer.class);
				existsQuery.setParameter("serverId", server.getId());
				existsQuery.setParameter("date", dailyImport.getDate());
				ContractServer existingContractServer = DataAccessUtils.uniqueResult(existsQuery.getResultList());
				if (existingContractServer == null) {
					lineImportData.resultCode = DailyImportService.LineImportResultCode.OK_NO_CONTRACT;
				}
			}

			// Najit Uroven produkce
			ProductionLevel productionLevel = null;
			if (production != null) {
				productionLevel = productionLevelDAO.findProductionLevel(production);
			} else {
				productionLevel = productionLevelDAO.findProductionLevel(ProductionLevel.INIT);
			}
			if (productionLevel == null) {
				lineImportData.resultCode = DailyImportService.LineImportResultCode.ERROR_PRODUCTION_LEVEL_NOT_EXISTS;
				return;
			}

			// Test, ze pro dany den a server zaznam doposud neexistuje
			StringBuilder existsJpql = new StringBuilder();
			existsJpql.append(" SELECT dailyUsage ");
			existsJpql.append(" FROM DailyUsage AS dailyUsage ");
			existsJpql.append(" JOIN dailyUsage.server AS server ");
			existsJpql.append(" JOIN dailyUsage.dailyImport AS dailyImport ");
			existsJpql.append(" WHERE dailyImport.id = :dailyImportId ");
			existsJpql.append(" AND server.id = :serverId ");
			TypedQuery<DailyUsage> existsQuery = em.createQuery(existsJpql.toString(), DailyUsage.class);
			existsQuery.setParameter("dailyImportId", dailyImport.getId());
			existsQuery.setParameter("serverId", server.getId());
			DailyUsage existingDailyUsage = DataAccessUtils.uniqueResult(existsQuery.getResultList());
			if (existingDailyUsage != null) {
				lineImportData.resultCode = DailyImportService.LineImportResultCode.ERROR_LINE_EXISTS;
				return;
			}

			// Vytvoreni a naplneni objektu dennich zaznamu
			DailyUsage dailyUsage = new DailyUsage();
			dailyUsage.setDailyImport(dailyImport);
			dailyUsage.setServer(server);
			dailyUsage.setProductionLevel(productionLevel);
			dailyUsage.setServerName(name);
			dailyUsage.setCpu(NumberUtils.createInteger(cpu, 0));
			dailyUsage.setMemoryMB(NumberUtils.createInteger(memoryMb, 0));
			dailyUsage.setProvisionedSpaceGB(NumberUtils.createBigDecimal(provSpaceGb, BigDecimal.ZERO));
			dailyUsage.setUsedSpaceGB(NumberUtils.createBigDecimal(usedSpaceGb, BigDecimal.ZERO));
			dailyUsage.setBackupGB(NumberUtils.createBigDecimal(backupGb, BigDecimal.ZERO));
			dailyUsage.setPowerState(powerState != null ? BooleanUtils.toBooleanObject(powerState, "PoweredOn", "PoweredOff", null) : false);

			// Ulozeni noveho objektu
			dailyUsageDAO.saveDailyUsage(dailyUsage);

		} catch (Exception exc) {
			// Vyjimku pri zpracovani jednoho radku zaznamenam, ale nezastavuji zpracovani celeho souboru
			log.warn(exc.getMessage(), exc);
			lineImportData.exception = exc;
		}
	}
}