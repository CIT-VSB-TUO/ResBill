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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cz.vsb.resbill.criteria.DailyImportCriteria;
import cz.vsb.resbill.dao.DailyImportDAO;
import cz.vsb.resbill.dao.DailyUsageDAO;
import cz.vsb.resbill.dao.ProductionLevelDAO;
import cz.vsb.resbill.dao.ServerDAO;
import cz.vsb.resbill.dto.DailyImportAgendaDTO;
import cz.vsb.resbill.dto.DailyImportAllReportsResultDTO;
import cz.vsb.resbill.dto.DailyImportDTO;
import cz.vsb.resbill.exception.DailyImportException;
import cz.vsb.resbill.exception.ResBillException;
import cz.vsb.resbill.model.ContractServer;
import cz.vsb.resbill.model.DailyImport;
import cz.vsb.resbill.model.DailyUsage;
import cz.vsb.resbill.model.InvoiceDailyUsage;
import cz.vsb.resbill.model.ProductionLevel;
import cz.vsb.resbill.model.Server;
import cz.vsb.resbill.service.DailyImportService;
import cz.vsb.resbill.service.MailSenderService;
import cz.vsb.resbill.service.ResBillService;
import cz.vsb.resbill.util.NumberUtils;
import cz.vsb.resbill.util.ResourceBundleUtils;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */

@ResBillService
public class DailyImportServiceImpl implements DailyImportService {

  private static final Logger log = LoggerFactory.getLogger(DailyImportServiceImpl.class);

  @Inject
  private ApplicationContext  appContext;

  @PersistenceContext
  private EntityManager       em;

  @Inject
  private DailyImportService  dailyImportService;

  @Inject
  private MailSenderService   mailSenderService;

  @Inject
  private ServerDAO           serverDAO;

  @Inject
  private ProductionLevelDAO  productionLevelDAO;

  @Inject
  private DailyImportDAO      dailyImportDAO;

  @Inject
  private DailyUsageDAO       dailyUsageDAO;

  /**
   * 
   * @param dailyImportId
   * @return
   * @throws ResBillException
   */
  @Override
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
  @Override
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
      log.error("An unexpected error occured while finding DailyImport by id=" + dailyImportId, exc);
      throw new ResBillException(exc);
    }
  }

  /**
   * 
   * @param criteria
   * @return
   */
  @Override
  public List<DailyImport> findDailyImports(DailyImportCriteria criteria, Integer offset, Integer limit) throws ResBillException {
    try {
      return dailyImportDAO.findDailyImports(criteria, offset, limit);
    } catch (Exception exc) {
      log.error("An unexpected error occured while finding DailyImports.", exc);
      throw new ResBillException(exc);
    }
  }

  /**
   * 
   */
  @Override
  public List<DailyImportDTO> findDailyImportDTOs(DailyImportCriteria criteria, Integer offset, Integer limit) throws ResBillException {
    try {
      List<DailyImport> dailyImports = findDailyImports(criteria, offset, limit);
      List<DailyImportDTO> dtos = new ArrayList<DailyImportDTO>();

      for (DailyImport dailyImport : dailyImports) {
        DailyImportDTO dto = new DailyImportDTO();
        dto.fill(dailyImport);
        dtos.add(dto);
      }

      return dtos;
    } catch (Exception exc) {
      log.error("An unexpected error occured while finding DailyImportDTOs.", exc);
      throw new ResBillException(exc);
    }
  }

  /**
   * Nalezne denni importy pro pouziti na Dashboardu (Agenda).
   * 
   * Tj. nalezne vsechny denni importy, ktere splnuji alespon jednu pozadovanou vlastnost (Feature) - tim se lisi od standardni metody findDailyImports.
   * 
   * @param criteria
   * @param offset
   * @param limit
   * @return
   */
  @Override
  public List<DailyImportAgendaDTO> findDailyImportAgendaDTOs(DailyImportCriteria criteria, Integer offset, Integer limit) throws ResBillException {
    try {
      DailyImportCriteria crit = null;
      List<DailyImport> dailyImports = null;
      Set<Integer> dailyImportIds = new HashSet<Integer>();

      // Denni importy s chybou
      crit = criteria.clone();
      crit.setFeatures(EnumSet.of(DailyImportCriteria.Feature.HAS_ERRORS));
      dailyImports = findDailyImports(crit, null, null);
      Set<Integer> hasErrorsDailyImportIds = new HashSet<Integer>();
      for (DailyImport dailyImport : dailyImports) {
        hasErrorsDailyImportIds.add(dailyImport.getId());
      }
      dailyImportIds.addAll(hasErrorsDailyImportIds);

      // Denni importy s varovanim
      crit = criteria.clone();
      crit.setFeatures(EnumSet.of(DailyImportCriteria.Feature.HAS_WARNS));
      dailyImports = findDailyImports(crit, null, null);
      Set<Integer> hasWarnsDailyImportIds = new HashSet<Integer>();
      for (DailyImport dailyImport : dailyImports) {
        hasWarnsDailyImportIds.add(dailyImport.getId());
      }
      dailyImportIds.addAll(hasWarnsDailyImportIds);

      // Pokud neexistuje zadny "zajimavy" denni import, pak vratim prazdny seznam
      if (dailyImportIds.isEmpty()) {
        return new ArrayList<DailyImportAgendaDTO>();
      }

      // Ziskam vsechny "zajimave" denni importy setridene dle puvodniho pozadavku
      crit = new DailyImportCriteria();
      crit.setDailyImportIds(dailyImportIds);
      crit.setOrderBy(criteria.getOrderBy());

      dailyImports = findDailyImports(crit, offset, limit);
      List<DailyImportAgendaDTO> dtos = new ArrayList<DailyImportAgendaDTO>();

      for (DailyImport dailyImport : dailyImports) {
        DailyImportAgendaDTO dto = new DailyImportAgendaDTO();
        dto.fill(dailyImport);
        dtos.add(dto);

        if (hasErrorsDailyImportIds.contains(dailyImport.getId())) {
          dto.setHasErrors(true);
        }
        if (hasWarnsDailyImportIds.contains(dailyImport.getId())) {
          dto.setHasWarns(true);
        }
      }

      return dtos;
    } catch (Exception exc) {
      log.error("An unexpected error occured while finding DailyImportAgendaDTOs.", exc);
      throw new ResBillException(exc);
    }
  }

  /**
	 * 
	 */
  @Override
  public DailyImport deleteDailyImport(Integer dailyImportId) throws DailyImportException, ResBillException {
    try {
      // test, ze DailyImport (resp. nektera z jeho DailyUsage) neni nikde pouzit:
      // - neni pouzit na fakture
      StringBuilder existsJpql = new StringBuilder();
      existsJpql.append(" SELECT invoiceDailyUsage ");
      existsJpql.append(" FROM InvoiceDailyUsage AS invoiceDailyUsage ");
      existsJpql.append(" JOIN invoiceDailyUsage.dailyUsage AS dailyUsage ");
      existsJpql.append(" JOIN dailyUsage.dailyImport AS dailyImport ");
      existsJpql.append(" WHERE dailyImport.id = :dailyImportId ");

      TypedQuery<InvoiceDailyUsage> existsQuery = em.createQuery(existsJpql.toString(), InvoiceDailyUsage.class);
      existsQuery.setMaxResults(1);
      existsQuery.setParameter("dailyImportId", dailyImportId);
      InvoiceDailyUsage existingInvoiceDailyUsage = DataAccessUtils.uniqueResult(existsQuery.getResultList());
      if (existingInvoiceDailyUsage != null) {
        throw new DailyImportException(DailyImportException.Reason.USED_ON_INVOICE, "An unexpected error occured while deleteDailyImport() for dailyImportId: " + dailyImportId + " - used on invoice.");
      }

      DailyImport dailyImport = dailyImportDAO.findDailyImport(dailyImportId);
      if (dailyImport != null) {
        dailyImport = dailyImportDAO.deleteDailyImport(dailyImport);
      }
      return dailyImport;

    } catch (DailyImportException exc) {
      log.error(exc.getMessage(), exc);
      throw exc;
    } catch (Exception exc) {
      log.error("An unexpected error occured while deleting DailyImport with id=" + dailyImportId, exc);
      throw new ResBillException(exc);
    }
  }

  /**
   * Hlavni importni metoda
   * 
   */
  @Override
  public DailyImportAllReportsResultDTO importAllReports() throws ResBillException {
    log.info("Zacinam import celeho adresare.");

    DailyImportAllReportsResultDTO resultDTO = new DailyImportAllReportsResultDTO();
    resultDTO.setBeginTimestamp(new Date());

    try {
      ResourceBundle rb = ResourceBundleUtils.getConfigBundle();
      String dirSrcName = rb.getString("dir.import.reports");
      String dirDstName = rb.getString("dir.import.reports.imported");
      log.info("Jmeno zdrojoveho adresare: " + dirSrcName);
      log.info("Jmeno ciloveho adresare: " + dirDstName);

      // Ziskani zdrojoveho adresare
      Resource dirSrcRes = appContext.getResource("file:///" + dirSrcName);
      File dirSrc = dirSrcRes.getFile();

      // Ziskani adresare, kam budou presunuty naimportovane soubory
      Resource dirDstRes = appContext.getResource("file:///" + dirDstName);
      File dirDst = dirDstRes.getFile();

      // Filtruji jen soubory, jejichz nazev zacina na "report_"
      FilenameFilter filter = new FilenameFilter() {

        @Override
        public boolean accept(File dir, String name) {
          return name.startsWith(REPORT_FILE_NAME_PREFIX);
        }
      };

      // Postupne budu zpracovavat vsechny nalezene soubory
      File[] files = dirSrc.listFiles(filter);
      resultDTO.setAllReports(files.length);
      for (File file : files) {
        try {
          DailyImport dailyImport = dailyImportService.importDailyReport(file, dirDst);

          if (dailyImport.getErrorLines() > 0) {
            resultDTO.setErrorReports(resultDTO.getErrorReports() + 1);
          } else if (dailyImport.getWarnLines() > 0) {
            resultDTO.setWarnReports(resultDTO.getWarnReports() + 1);
          } else {
            resultDTO.setOkReports(resultDTO.getOkReports() + 1);
          }
        } catch (DailyImportException exc) {
          switch (exc.getReason()) {
          case IMPORT_REPORT_DATE_EXISTS:
            resultDTO.setExistsReports(resultDTO.getExistsReports() + 1);
            break;

          case IMPORT_REPORT_DATE_PARSE_ERROR:
          case IMPORT_REPORT_DATA_UNREADABLE:
          case IMPORT_REPORT_UNMOVABLE:
          default:
            resultDTO.setCriticalErrorReports(resultDTO.getCriticalErrorReports() + 1);
            log.error(exc.getMessage(), exc);
            break;
          }
        }

        // break; // pro ucely ladeni
      }

    } catch (Exception exc) {
      log.error("An unexpected error occured while importAllReports().", exc);
      throw new ResBillException(exc);
    }

    resultDTO.setEndTimestamp(new Date());

    log.info("Import celeho adresare dokoncen: " + resultDTO);

    return resultDTO;
  }

  /**
   * Hlavni importni metoda pro jeden report (jeden den)
   * 
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public DailyImport importDailyReport(File file, File dstDir) throws DailyImportException {
    String fileName = file.getName();
    log.info("Zacinam importovat soubor: " + fileName);
    DailyImport dailyImport = null;

    try {
      // Ziskani datumu
      Date date = null;
      try {
        String dateString = StringUtils.remove(fileName, REPORT_FILE_NAME_PREFIX);
        dateString = StringUtils.remove(dateString, REPORT_FILE_NAME_SUFIX);
        date = DateUtils.parseDateStrictly(dateString, REPORT_FILE_NAME_DATE_PATERNS);
      } catch (ParseException exc) {
        throw new DailyImportException(DailyImportException.Reason.IMPORT_REPORT_DATE_PARSE_ERROR, "An unexpected error occured while importDailyReport() for file: " + fileName
            + " - file date parse error.", exc);
      }

      // Overeni, ze pro dane datum jeste nebyl import proveden
      DailyImport existingDailyImport = dailyImportDAO.findDailyImport(date);
      if (existingDailyImport != null) {
        throw new DailyImportException(DailyImportException.Reason.IMPORT_REPORT_DATE_EXISTS, "An unexpected error occured while importDailyReport() for file: " + fileName + " - import date exists.");
      }

      // Precteni dat ze souboru do Stringu
      String report = null;
      try (BufferedReader buffReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));) {
        report = IOUtils.toString(buffReader);
      } catch (IOException exc) {
        throw new DailyImportException(DailyImportException.Reason.IMPORT_REPORT_DATA_UNREADABLE,
            "An unexpected error occured while importDailyReport() for file: " + fileName + " - data read error.", exc);
      }

      // Zaznamenani zapoceti denniho importu.
      // Ulozeni znamych udaju.
      dailyImport = new DailyImport();
      dailyImport.setDate(date);
      dailyImport.setReportName(fileName);
      dailyImport.setReport(report);
      dailyImport = dailyImportService.beginDailyImport(dailyImport);

      // Prochazeni jednotlivych radku
      List<LineImportData> lineImportDatas = new ArrayList<LineImportData>();
      String[] lines = report.split("\n");

      String headerLine = StringUtils.stripToNull(lines[0]);
      String[] headerLineNames = headerLine.split(";");
      Map<ReportItemName, Integer> lineItemIndices = new EnumMap<ReportItemName, Integer>(ReportItemName.class);
      for (int i = 0; i < headerLineNames.length; i++) {
        String itemName = headerLineNames[i];

        ReportItemName[] itemNameEnums = ReportItemName.values();
        for (ReportItemName reportItemName : itemNameEnums) {
          if (reportItemName.getName().equals(itemName)) {
            lineItemIndices.put(reportItemName, i);
          }
        }
      }

      for (int lineNumber = 1; lineNumber < lines.length; lineNumber++) {
        String line = lines[lineNumber];

        line = StringUtils.stripToNull(line);

        if (line != null && !line.startsWith("serverId")) {
          LineImportData lineImportData = new LineImportData();
          lineImportData.line = line;
          lineImportData.lineNumber = lineNumber + 1; // v poli se pocita od 0, ale radky v souboru jsou od 1

          dailyImportService.importLine(dailyImport, lineImportData, lineItemIndices);

          lineImportDatas.add(lineImportData);
        }
      }

      // Presunuti naimportovaneho souboru do ciloveho adresare
      try {
        Path filePath = file.toPath();
        Path dirPath = dstDir.toPath();
        Files.move(filePath, dirPath.resolve(filePath.getFileName()), StandardCopyOption.REPLACE_EXISTING);
      } catch (IOException exc) {
        throw new DailyImportException(DailyImportException.Reason.IMPORT_REPORT_UNMOVABLE, "An unexpected error occured while importDailyReport() for file: " + fileName + " - file move error.", exc);
      }

      // Zaznamenani ukonceni denniho importu
      dailyImportService.endDailyImport(dailyImport, lineImportDatas);
    } catch (DailyImportException exc) {
      // log.error(exc.getMessage(), exc); // Chyba je dle druhu zalogovana ve volajici metode
      log.info("NEdokoncen import souboru: " + fileName + " - " + exc.getReason());
      throw exc;
    }

    log.info("Dokoncen import souboru: " + fileName);

    return dailyImport;
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
      protocol.append(lineImportData.serverName);
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

    ResourceBundle rb = ResourceBundleUtils.getConfigBundle();
    mailSenderService.send(rb.getString("email.group.admins"), rb.getString("email.subject.dailyImport") + " - " + dailyImport.getReportName(), dailyImport.getProtocol());
  }

  /**
   * 
   * @param line
   * @return
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void importLine(DailyImport dailyImport, LineImportData lineImportData, Map<DailyImportService.ReportItemName, Integer> lineItemIndices) {
    try {
      log.info(lineImportData.lineNumber + ": " + lineImportData.line);

      // Nastaveni kodu vysledku pro pripad, ze vse dopadne v poradku (v pripade chyb nebo warningu se bude menit)
      lineImportData.resultCode = DailyImportService.LineImportResultCode.OK;

      // Nacteni dat radku
      String[] lineElements = lineImportData.line.split(";");

      String serverId = StringUtils.stripToNull(lineElements[lineItemIndices.get(DailyImportService.ReportItemName.SERVER_ID)]);
      String name = StringUtils.stripToNull(lineElements[lineItemIndices.get(DailyImportService.ReportItemName.NAME)]);
      String production = StringUtils.stripToNull(lineElements[lineItemIndices.get(DailyImportService.ReportItemName.PRODUCTION)]);
      String powerState = StringUtils.stripToNull(lineElements[lineItemIndices.get(DailyImportService.ReportItemName.POWER_STATE)]);
      String cpu = StringUtils.stripToNull(lineElements[lineItemIndices.get(DailyImportService.ReportItemName.CPU)]);
      String memoryGb = StringUtils.stripToNull(lineElements[lineItemIndices.get(DailyImportService.ReportItemName.MEMORY_GB)]);
      String provSpaceGb = StringUtils.stripToNull(lineElements[lineItemIndices.get(DailyImportService.ReportItemName.PROV_SPACE_GB)]);
      String usedSpaceGb = StringUtils.stripToNull(lineElements[lineItemIndices.get(DailyImportService.ReportItemName.USED_SPACE_GB)]);
      String backupGb = StringUtils.stripToNull(lineElements[lineItemIndices.get(DailyImportService.ReportItemName.BACKUP_GB)]);

      lineImportData.serverId = serverId;
      lineImportData.serverName = name;

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
        existsQuery.setMaxResults(1);
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
      existsQuery.setMaxResults(1);
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
      dailyUsage.setCpu(NumberUtils.createInteger(cpu, Integer.valueOf(0)));
      dailyUsage.setMemoryGB(NumberUtils.createBigDecimal(memoryGb, NumberUtils.STANDARD_ZERO));
      dailyUsage.setProvisionedSpaceGB(NumberUtils.createBigDecimal(provSpaceGb, NumberUtils.STANDARD_ZERO));
      dailyUsage.setUsedSpaceGB(NumberUtils.createBigDecimal(usedSpaceGb, NumberUtils.STANDARD_ZERO));
      dailyUsage.setBackupGB(NumberUtils.createBigDecimal(backupGb, NumberUtils.STANDARD_ZERO));

      // Hodnoty spotreby nesmi byt zaporne
      if (dailyUsage.getCpu() < 0 || dailyUsage.getMemoryGB().compareTo(BigDecimal.ZERO) < 0 || dailyUsage.getProvisionedSpaceGB().compareTo(BigDecimal.ZERO) < 0
          || dailyUsage.getUsedSpaceGB().compareTo(BigDecimal.ZERO) < 0 || dailyUsage.getBackupGB().compareTo(BigDecimal.ZERO) < 0) {
        
        lineImportData.resultCode = DailyImportService.LineImportResultCode.ERROR_USAGE_NEGATIVE_VALUE;
        return;
      }

      if (powerState != null) {
        if (EnumUtils.isValidEnum(PowerStateOn.class, powerState)) {
          dailyUsage.setPowerState(true);
        } else if (EnumUtils.isValidEnum(PowerStateOff.class, powerState)) {
          dailyUsage.setPowerState(false);
        } else {
          throw new IllegalArgumentException("The String '" + powerState + "' did not match any specified value of PowerState.");
        }

        // dailyUsage.setPowerState(powerState != null ? BooleanUtils.toBooleanObject(powerState, "PoweredOn", "PoweredOff", null) : false);
      } else {
        dailyUsage.setPowerState(false);
      }

      // Ulozeni noveho objektu
      dailyUsageDAO.saveDailyUsage(dailyUsage);

    } catch (Exception exc) {
      // Vyjimku pri zpracovani jednoho radku zaznamenam, ale nezastavuji zpracovani celeho souboru
      log.warn(exc.getMessage(), exc);
      lineImportData.resultCode = DailyImportService.LineImportResultCode.ERROR_OTHER;
      lineImportData.exception = exc;
    }
  }
}
