/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cz.vsb.resbill.criteria.InvoiceCreateCriteria;
import cz.vsb.resbill.criteria.InvoiceCriteria;
import cz.vsb.resbill.criteria.InvoiceTypeCriteria;
import cz.vsb.resbill.dao.ContractDAO;
import cz.vsb.resbill.dao.DailyUsageDAO;
import cz.vsb.resbill.dao.InvoiceDAO;
import cz.vsb.resbill.dao.InvoiceTypeDAO;
import cz.vsb.resbill.dao.PriceListDAO;
import cz.vsb.resbill.dao.TransactionDAO;
import cz.vsb.resbill.dao.TransactionTypeDAO;
import cz.vsb.resbill.dto.InvoiceCreateResultDTO;
import cz.vsb.resbill.dto.InvoiceDTO;
import cz.vsb.resbill.exception.DailyImportException;
import cz.vsb.resbill.exception.ResBillException;
import cz.vsb.resbill.model.Contract;
import cz.vsb.resbill.model.DailyUsage;
import cz.vsb.resbill.model.File;
import cz.vsb.resbill.model.Invoice;
import cz.vsb.resbill.model.InvoicePriceList;
import cz.vsb.resbill.model.InvoiceType;
import cz.vsb.resbill.model.Period;
import cz.vsb.resbill.model.PriceList;
import cz.vsb.resbill.model.Server;
import cz.vsb.resbill.model.Transaction;
import cz.vsb.resbill.model.TransactionType;
import cz.vsb.resbill.service.InvoiceService;
import cz.vsb.resbill.service.ResBillService;
import cz.vsb.resbill.service.TransactionService;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */

@ResBillService
public class InvoiceServiceImpl implements InvoiceService {

	private static final Logger log = LoggerFactory.getLogger(InvoiceServiceImpl.class);

	/**
   * 
   */
	@PersistenceContext
	private EntityManager em;

	@Inject
	private InvoiceDAO invoiceDAO;

	@Inject
	private InvoiceTypeDAO invoiceTypeDAO;

	@Inject
	private TransactionDAO transactionDAO;

	@Inject
	private TransactionTypeDAO transactionTypeDAO;

	@Inject
	private ContractDAO contractDAO;

	@Inject
	private DailyUsageDAO dailyUsageDAO;

	@Inject
	private PriceListDAO priceListDAO;

	@Inject
	private InvoiceService invoiceService;

	@Inject
	private TransactionService transactionService;

	/**
	 * 
	 */
	@Override
	public Invoice findInvoice(Integer invoiceId) throws ResBillException {
		return findInvoice(invoiceId, false, false);
	}

	/**
	 * 
	 */
	@Override
	public Invoice findInvoice(Integer invoiceId, boolean initializeSummary, boolean initializeDetail) throws ResBillException {
		try {
			Invoice invoice = invoiceDAO.findInvoice(invoiceId);

			if (invoice != null) {
				if (initializeSummary) {
					invoice.getSummary();
				}

				if (initializeDetail) {
					invoice.getDetail();
				}
			}

			return invoice;
		} catch (Exception exc) {
			log.error("An unexpected error occured while finding Invoice by id=" + invoiceId, exc);
			throw new ResBillException(exc);
		}
	}

	/**
	 * 
	 */
	@Override
	public List<Invoice> findInvoices(InvoiceCriteria criteria, Integer offset, Integer limit) throws ResBillException {
		try {
			return invoiceDAO.findInvoices(criteria, offset, limit);
		} catch (Exception exc) {
			log.error("An unexpected error occured while finding Invoices.", exc);
			throw new ResBillException(exc);
		}
	}

	/**
   * 
   */
	@Override
	public List<InvoiceDTO> findInvoiceDTOs(InvoiceCriteria criteria, Integer offset, Integer limit) throws ResBillException {
		try {
			List<Invoice> invoices = findInvoices(criteria, offset, limit);
			List<InvoiceDTO> dtos = new ArrayList<InvoiceDTO>();

			for (Invoice invoice : invoices) {
				InvoiceDTO dto = new InvoiceDTO();
				dto.fill(invoice);
				dtos.add(dto);
			}

			return dtos;
		} catch (Exception exc) {
			log.error("An unexpected error occured while finding InvoiceDTOs.", exc);
			throw new ResBillException(exc);
		}
	}

	/**
   * 
   */
	@Override
	public Invoice deleteInvoice(Integer invoiceId) throws ResBillException {
		try {
			Invoice invoice = (Invoice) transactionService.deleteTransaction(invoiceId);

			return invoice;

		} catch (Exception exc) {
			log.error("An unexpected error occured while deleting Invoice with id=" + invoiceId, exc);
			throw new ResBillException(exc);
		}
	}

	/**
	 * 
	 * @param criteria
	 * @return
	 * @throws ResBillException
	 */
	@Override
	public InvoiceCreateResultDTO createInvoices(InvoiceCreateCriteria criteria) throws ResBillException {
		log.info("Zacinam fakturaci.");

		InvoiceCreateResultDTO resultDTO = new InvoiceCreateResultDTO();
		resultDTO.setBeginTimestamp(new Date());

		// Ziskam datum posledniho dne v pozadovanem mesici
		Date month = criteria.getMonth();
		Date lastDay = DateUtils.addDays(DateUtils.addMonths(DateUtils.truncate(month, Calendar.MONTH), 1), -1);

		// Ziskam poradi mesice v roce
		Calendar lastDayCal = Calendar.getInstance();
		lastDayCal.setTime(lastDay);
		Integer monthOrder = lastDayCal.get(Calendar.MONTH) + 1;

		// Ziskam ID typu uctovani platnych pro tento mesic
		List<Integer> invoiceTypeIds = new ArrayList<Integer>();
		InvoiceTypeCriteria invoiceTypeCriteria = new InvoiceTypeCriteria();
		invoiceTypeCriteria.setZeroModuloDividend(monthOrder);
		List<InvoiceType> invoiceTypes = invoiceTypeDAO.findInvoiceTypes(invoiceTypeCriteria, null, null);
		for (InvoiceType invoiceType : invoiceTypes) {
			invoiceTypeIds.add(invoiceType.getId());
		}

		// Ziskat vsechny kontrakty, jejichz servery maji alespon jedno nevyfakturovane DailyUsage v pozadovanem mesici NEBO DRIVE
		// Server musi byt kontraktu prirazen take nektery den v pozadovanem mesici NEBO DRIVE
		List<Object[]> contractsWithInvTypes = contractDAO.findUninvoicedContracts(lastDay, invoiceTypeIds);
		resultDTO.setContractsNumberAll(contractsWithInvTypes.size());

		// Vytvareni faktur pro jednotlive kontrakty
		for (Object[] contractWithInvType : contractsWithInvTypes) {
			Contract contract = (Contract) contractWithInvType[0];
			InvoiceType invoiceType = (InvoiceType) contractWithInvType[1];

			try {
				em.detach(contract);
				em.detach(invoiceType);
				Invoice invoice = invoiceService.createContractInvoice(contract, invoiceType, month);

				if (invoice.getNoPriceList().booleanValue()) {
					resultDTO.setContractsNumberNoPriceList(resultDTO.getContractsNumberNoPriceList() + 1);
				} else {
					resultDTO.setContractsNumberOk(resultDTO.getContractsNumberOk() + 1);
				}
			} catch (Exception exc) {
				log.error("An unexpected error occured while createContractInvoice for contractId: " + contract.getId(), exc);
				resultDTO.setContractsNumberError(resultDTO.getContractsNumberError() + 1);
			}
		}

		resultDTO.setEndTimestamp(new Date());

		log.info("Fakturace dokoncena: " + resultDTO);

		return resultDTO;
	}

	/**
	 * Vytvori fakturu pro pozadovany kontrakt v pozadovanem mesici
	 * 
	 * @param contract
	 * @return
	 * @throws DailyImportException
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public Invoice createContractInvoice(Contract contract, InvoiceType invoiceType, Date month) {
		log.info("Zacinam fakturovat pro kontrakt s ID: " + contract.getId());

		Invoice invoice = new Invoice();
		invoice.setTransactionType(transactionTypeDAO.findTransactionType(TransactionType.INVOICE));
		invoice.setInvoiceType(invoiceType);
		invoice.setContract(contract);
		invoice.setDecisiveDate(new Date());
		invoice.setOrder(transactionDAO.getNextOrder(contract.getId()));

		// TODO: fakturu naplnit smysluplnymi udaji
		File file = new File();
		file.setName("Foo file");
		file.setSize(new Long(0));
		file.setContentType("Foo content type");
		invoice.setAttachment(file);

		StringBuilder summary = new StringBuilder();

		Date firstMonthDay = DateUtils.truncate(month, Calendar.MONTH);
		Date firstDay = DateUtils.addMonths(firstMonthDay, 1 - invoiceType.getDivisor());
		Date lastDay = DateUtils.addDays(DateUtils.addMonths(firstMonthDay, 1), -1);

		invoice.setPeriod(new Period());
		invoice.getPeriod().setBeginDate(firstDay);
		invoice.getPeriod().setEndDate(lastDay);

		// Nazev faktury
		DateFormat titleDateFormat = new SimpleDateFormat("d.M.yyyy");
		StringBuilder title = new StringBuilder();
		title.append("Vyúčtování služeb datového centra za období: ");
		title.append(titleDateFormat.format(firstDay));
		title.append("-");
		title.append(titleDateFormat.format(lastDay));
		invoice.setTitle(title.toString());

		// Hlavickove informace do vypisu
		summary.append(invoice.getTitle());
		summary.append("\n");
		summary.append("Zákazník: ");
		summary.append(contract.getCustomer().getName());
		summary.append("\n");
		summary.append("Kontrakt: ");
		summary.append(contract.getName());

		// Ziskat vsechny DailyUsages, ktere maji byt fakturovany
		List<DailyUsage> dailyUsages = dailyUsageDAO.findUninvoicedDailyUsages(lastDay, contract.getId());
		log.info("Pocet DailyUsage k fakturaci: " + dailyUsages.size());

		// Pro kazdy DailyUsage dohledat cenik
		List<TmpDailyUsagePriceList> usagePrices = new ArrayList<TmpDailyUsagePriceList>();
		for (DailyUsage dailyUsage : dailyUsages) {
			TmpDailyUsagePriceList usagePrice = new TmpDailyUsagePriceList();
			usagePrices.add(usagePrice);

			usagePrice.dailyUsage = dailyUsage;

			PriceList priceList = priceListDAO.findContractDailyUsagePriceList(contract.getId(), dailyUsage.getId());
			usagePrice.priceList = priceList;

			if (priceList == null) {
				invoice.setNoPriceList(true);
			}
		}

		// Pripraveni vypisu po serverech, polozkach, cenicich a mesicich
		List<TmpServerUsagePricing> serverPricings = prepareServerPricing(usagePrices);

		// Souhrny vypis uctovanych castek po serverech
		BigDecimal contractPrice = BigDecimal.ZERO;
		Set<Integer> priceListIds = new HashSet<Integer>();
		StringBuilder groupPricing = new StringBuilder();
		for (TmpServerUsagePricing serverPricing : serverPricings) {
			contractPrice = contractPrice.add(serverPricing.price);
			priceListIds.addAll(serverPricing.priceListIds);

			// Odradkovani
			if (groupPricing.length() > 0) {
				groupPricing.append("\n");
			}
			groupPricing.append("\n");

			groupPricing.append(serverPricing.server.getName());
			groupPricing.append("\t");
			groupPricing.append(serverPricing.price.setScale(2, RoundingMode.HALF_UP));
			groupPricing.append(" Kč");

			groupPricing.append(buildGroupPricing(serverPricing));

		}
		summary.append("\n\n\n");
		summary.append("Rozpis po serverech:\n");
		summary.append(groupPricing);

		// Celkova cena za kontrakt
		invoice.setAmount(contractPrice.setScale(2, RoundingMode.HALF_UP).negate());
		summary.append("\n\n");
		summary.append("Celkem: ");
		summary.append(invoice.getAmount().negate());

		// Zaznamenat seskupene vyuctovani
		invoice.setSummary(summary.toString());

		// Kompletni vypis nalezenych DailyUsage s dohledanymi ceniky, roztrideno po jednotlivych serverech kontraktu
		StringBuilder detail = new StringBuilder();
		Integer sId = null;
		StringBuilder detailPricing = new StringBuilder();
		for (TmpDailyUsagePriceList usagePrice : usagePrices) {
			// Odradkovani
			if (detailPricing.length() > 0) {
				detailPricing.append("\n");
			}

			// Hlavicka serveru
			if (sId == null || !sId.equals(usagePrice.dailyUsage.getServer().getId())) {
				sId = usagePrice.dailyUsage.getServer().getId();

				detailPricing.append("\n");
				detailPricing.append("Server - ID: ");
				detailPricing.append(sId);
				detailPricing.append("; ServerId: ");
				detailPricing.append(usagePrice.dailyUsage.getServer().getServerId());
				detailPricing.append("; Jméno: ");
				detailPricing.append(usagePrice.dailyUsage.getServer().getName());
				detailPricing.append("\n");
			}

			detailPricing.append(buildDetailPricing(usagePrice));
		}
		detail.append("Detailní rozpis (po serverech):\n");
		detail.append(detailPricing);

		log.debug(detail.toString());

		// Zaznamenat detailni rozpis
		invoice.setDetail(detail.toString());

		// Doplnit odkaz na pouzite ceniky
		for (Integer priceListId : priceListIds) {
			PriceList priceList = priceListDAO.findPriceList(priceListId);
			InvoicePriceList invoicePriceList = new InvoicePriceList();
			invoicePriceList.setInvoice(invoice);
			invoicePriceList.setPriceList(priceList);
			invoice.getInvoicePriceLists().add(invoicePriceList);
		}

		// Ulozit
		invoice = (Invoice) transactionDAO.saveTransaction(invoice);

		// Doplnit evidencni cislo na zaklade ID a znovu ulozit
		invoice.setEvidenceNumber(Transaction.INVOICE_EVIDENCE_NUMBER_BASE + invoice.getId());
		invoice = (Invoice) transactionDAO.saveTransaction(invoice);

		// Aktualizovat stav uctu kontraktu
		contract.setBalance(contract.getBalance().add(invoice.getAmount()));
		contractDAO.saveContract(contract);

		log.info("Fakturace pro kontrakt s ID: " + contract.getId() + " ukoncena.");

		return invoice;
	}

	/**
	 * 
	 * @param serverPricing
	 * @return
	 */
	protected String buildGroupPricing(TmpServerUsagePricing serverPricing) {
		StringBuilder groupPricing = new StringBuilder();

		groupPricing.append("\n");
		groupPricing.append("\tCPU: ");
		groupPricing.append("\n");
		groupPricing.append(buildGroupOneResourcePricing(serverPricing.cpus, "\t\t"));

		groupPricing.append("\n");
		groupPricing.append("\tRAM: ");
		groupPricing.append("\n");
		groupPricing.append(buildGroupOneResourcePricing(serverPricing.memories, "\t\t"));

		groupPricing.append("\n");
		groupPricing.append("\tStorage: ");
		groupPricing.append("\n");
		groupPricing.append(buildGroupOneResourcePricing(serverPricing.spaces, "\t\t"));

		groupPricing.append("\n");
		groupPricing.append("\tBackup: ");
		groupPricing.append("\n");
		groupPricing.append(buildGroupOneResourcePricing(serverPricing.backups, "\t\t"));

		return groupPricing.toString();
	}

	/**
	 * 
	 * @param cpus
	 * @param linePrefix
	 * @return
	 */
	protected String buildGroupOneResourcePricing(List<TmpResourcePricing> resPricings, String linePrefix) {
		StringBuilder pricing = new StringBuilder();

		DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

		for (TmpResourcePricing resPricing : resPricings) {
			long pieces = 1 + DateUtils.getFragmentInDays(resPricing.endDate, Calendar.MONTH) - DateUtils.getFragmentInDays(resPricing.beginDate, Calendar.MONTH);

			// Odradkovani
			if (pricing.length() > 0) {
				pricing.append("\n");
			}

			pricing.append(linePrefix);
			pricing.append(dateFormat.format(resPricing.beginDate));
			pricing.append("-");
			pricing.append(dateFormat.format(resPricing.endDate));
			pricing.append(" - ");
			pricing.append(pieces);
			pricing.append(" x ");
			pricing.append(resPricing.amount);
			pricing.append(" á ");
			pricing.append(resPricing.unitPrice);
			pricing.append(" Kč/měsíc");
			pricing.append(" = ");
			pricing.append(resPricing.price.setScale(2, RoundingMode.HALF_UP));
			pricing.append(" Kč");
		}

		return pricing.toString();
	}

	/**
	 * 
	 * @param usagePrice
	 * @return
	 */
	protected String buildDetailPricing(TmpDailyUsagePriceList usagePrice) {
		// TODO: pokud toto zachovame, tak by bylo vhodne text nekam vytahnout

		StringBuilder pricing = new StringBuilder();

		pricing.append(usagePrice.dailyUsage.getDailyImport().getDate());

		// CPU
		pricing.append(": CPU: ");
		pricing.append(usagePrice.dailyUsage.getCpu());
		pricing.append(" á ");
		pricing.append(usagePrice.priceList.getCpuPrice());
		pricing.append(" Kč/měsíc");

		// RAM
		pricing.append("; RAM: ");
		pricing.append(usagePrice.dailyUsage.getMemoryGB());
		pricing.append(" á ");
		pricing.append(usagePrice.priceList.getMemoryGBPrice());
		pricing.append(" Kč/měsíc");

		// Storage
		pricing.append("; Storage: ");
		pricing.append(usagePrice.dailyUsage.getProvisionedSpaceGB());
		pricing.append(" á ");
		pricing.append(usagePrice.priceList.getSpaceGBPrice());
		pricing.append(" Kč/měsíc");

		// Backup
		pricing.append("; Backup: ");
		pricing.append(usagePrice.dailyUsage.getBackupGB());
		pricing.append(" á ");
		pricing.append(usagePrice.priceList.getBackupGBPrice());
		pricing.append(" Kč/měsíc");

		return pricing.toString();
	}

	/**
	 * 
	 * @return
	 */
	protected List<TmpServerUsagePricing> prepareServerPricing(List<TmpDailyUsagePriceList> usagePrices) {
		List<TmpServerUsagePricing> serverPricings = new ArrayList<TmpServerUsagePricing>();

		TmpServerUsagePricing curServerPricing = null;
		for (TmpDailyUsagePriceList curUsagePrice : usagePrices) {
			if (curServerPricing == null || !curServerPricing.server.getId().equals(curUsagePrice.dailyUsage.getServer().getId())) {
				curServerPricing = new TmpServerUsagePricing();
				curServerPricing.server = curUsagePrice.dailyUsage.getServer();

				serverPricings.add(curServerPricing);
			}

			curServerPricing.cpus.add(prepareResourcePricing(curUsagePrice, new BigDecimal(curUsagePrice.dailyUsage.getCpu()), curUsagePrice.priceList.getCpuPrice()));
			curServerPricing.memories.add(prepareResourcePricing(curUsagePrice, curUsagePrice.dailyUsage.getMemoryGB(), curUsagePrice.priceList.getMemoryGBPrice()));
			curServerPricing.spaces.add(prepareResourcePricing(curUsagePrice, curUsagePrice.dailyUsage.getProvisionedSpaceGB(), curUsagePrice.priceList.getSpaceGBPrice()));
			curServerPricing.backups.add(prepareResourcePricing(curUsagePrice, curUsagePrice.dailyUsage.getBackupGB(), curUsagePrice.priceList.getBackupGBPrice()));
		}

		// Seskupit radky, kde se shoduje: mesic, cenik a mnozstvi
		for (TmpServerUsagePricing serverPricing : serverPricings) {
			serverPricing.cpus = groupResourcePricings(serverPricing.cpus);
			serverPricing.memories = groupResourcePricings(serverPricing.memories);
			serverPricing.spaces = groupResourcePricings(serverPricing.spaces);
			serverPricing.backups = groupResourcePricings(serverPricing.backups);
		}

		// Spocitat celkovou cenu za server a posbirat ID vsech pouzitych ceniku
		for (TmpServerUsagePricing serverPricing : serverPricings) {
			BigDecimal sum = BigDecimal.ZERO;
			sum = sum.add(sumPrices(serverPricing.cpus));
			sum = sum.add(sumPrices(serverPricing.memories));
			sum = sum.add(sumPrices(serverPricing.spaces));
			sum = sum.add(sumPrices(serverPricing.backups));

			serverPricing.price = sum;

			serverPricing.priceListIds.addAll(sumPriceListIds(serverPricing.cpus));
			serverPricing.priceListIds.addAll(sumPriceListIds(serverPricing.memories));
			serverPricing.priceListIds.addAll(sumPriceListIds(serverPricing.spaces));
			serverPricing.priceListIds.addAll(sumPriceListIds(serverPricing.backups));
		}

		return serverPricings;
	}

	/**
	 * Secte castky ze vsech jednotlivych dilcich polozek
	 * 
	 * @param pricings
	 * @return
	 */
	protected BigDecimal sumPrices(List<TmpResourcePricing> pricings) {
		BigDecimal sum = BigDecimal.ZERO;

		for (TmpResourcePricing pricing : pricings) {
			sum = sum.add(pricing.price);
		}

		return sum;
	}

	/**
	 * Posbira vsechny pouzite ceniky
	 * 
	 * @param pricings
	 * @return
	 */
	protected Set<Integer> sumPriceListIds(List<TmpResourcePricing> pricings) {
		Set<Integer> ids = new HashSet<Integer>();

		for (TmpResourcePricing pricing : pricings) {
			ids.add(pricing.priceListId);
		}

		return ids;
	}

	/**
	 * Seskupi radky, kde se shoduje: mesic, cenik a mnozstvi
	 * 
	 * Predpokladem je, ze vstupni List je setrideny dle data
	 */
	protected List<TmpResourcePricing> groupResourcePricings(List<TmpResourcePricing> in) {

		List<TmpResourcePricing> out = new ArrayList<TmpResourcePricing>();

		TmpResourcePricing curPricing = null;
		for (TmpResourcePricing inPricing : in) {
			if (curPricing == null || !curPricing.month.equals(inPricing.month) || !curPricing.priceListId.equals(inPricing.priceListId) || !curPricing.amount.equals(inPricing.amount)) {
				curPricing = inPricing;
				out.add(curPricing);
			} else {
				curPricing.endDate = inPricing.endDate;
				curPricing.price = curPricing.price.add(inPricing.price);
			}
		}

		return out;
	}

	/**
	 * 
	 * @return
	 */
	protected TmpResourcePricing prepareResourcePricing(TmpDailyUsagePriceList usagePrice, BigDecimal amount, BigDecimal unitPrice) {
		TmpResourcePricing resourcePricing = new TmpResourcePricing();
		resourcePricing.unitPrice = unitPrice;

		resourcePricing.beginDate = usagePrice.dailyUsage.getDailyImport().getDate();
		resourcePricing.endDate = usagePrice.dailyUsage.getDailyImport().getDate();
		resourcePricing.amount = amount;

		resourcePricing.month = getMonth(usagePrice.dailyUsage.getDailyImport().getDate());
		resourcePricing.priceListId = usagePrice.priceList.getId();

		int days = getMonthDaysCount(resourcePricing.month);
		resourcePricing.price = resourcePricing.unitPrice.divide(new BigDecimal(days), 10, RoundingMode.HALF_UP).multiply(resourcePricing.amount);

		return resourcePricing;
	}

	/**
	 * Vrati prvni den mesice, ve kterem se vyskytuje predany den
	 * 
	 * @param day
	 * @return
	 */
	protected Date getMonth(Date day) {
		return DateUtils.truncate(day, Calendar.MONTH);
	}

	/**
	 * Vrati pocet dnu mesice, ve kterem se vyskytuje predany den
	 * 
	 * @return
	 */
	protected int getMonthDaysCount(Date day) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(day);

		return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 
	 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
	 *
	 */
	private static class TmpDailyUsagePriceList {

		public DailyUsage dailyUsage;

		public PriceList priceList;
	}

	/**
   * 
   */
	private static class TmpServerUsagePricing {

		public Server server;

		// Cena za cely server
		public BigDecimal price;

		// Ceniky, ktere byly pro vypocet ceny pouzity
		public Set<Integer> priceListIds = new HashSet<Integer>();

		public List<TmpResourcePricing> cpus = new ArrayList<TmpResourcePricing>();

		public List<TmpResourcePricing> memories = new ArrayList<TmpResourcePricing>();

		public List<TmpResourcePricing> spaces = new ArrayList<TmpResourcePricing>();

		public List<TmpResourcePricing> backups = new ArrayList<TmpResourcePricing>();
	}

	/**
   * 
   */
	private static class TmpResourcePricing {

		// Zobrzovane hodnoty
		public Date beginDate;

		public Date endDate;

		public BigDecimal amount;

		public BigDecimal unitPrice;

		public BigDecimal price;

		// Pomocne hodnoty

		// prvni den mesice
		public Date month;

		public Integer priceListId;
	}
}
