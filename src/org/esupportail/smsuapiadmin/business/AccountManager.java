package org.esupportail.smsuapiadmin.business;

import java.awt.Image;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRXlsAbstractExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporter;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.esupportail.commons.beans.AbstractApplicationAwareBean;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.smsuapiadmin.dao.DaoService;
import org.esupportail.smsuapiadmin.dao.beans.Account;
import org.esupportail.smsuapiadmin.dto.DTOConverterService;
import org.esupportail.smsuapiadmin.dto.beans.UIAccount;

/**
 * AccountManager is the business layer between the web and the database for
 * 'account' objects.
 * 
 * @author MZRL3760
 * 
 */
@SuppressWarnings("serial")
public class AccountManager extends AbstractApplicationAwareBean {

	/**
	 * Action "Ajouter".
	 */
	private static final String ACTION_AJOUTER = "A";

	/**
	 * Action "Supprimer".
	 */
	private static final String ACTION_SUPPRIMER = "S";

	/**
	 * Log4j logger.
	 */
	private final Logger logger = new LoggerImpl(getClass());

	/**
	 * {@link DaoService}.
	 */
	private DaoService daoService;

	/**
	 * {@link DTOConverterService}.
	 */
	private DTOConverterService dtoConverterService;

	/**
	 * constructor.
	 */
	public AccountManager() {
		super();
	}

	/**
	 * @param daoService
	 *            the daoService to set
	 */
	public void setDaoService(final DaoService daoService) {
		this.daoService = daoService;
	}

	/**
	 * Setter for 'dtoConverterService'.
	 * 
	 * @param dtoConverterService
	 */
	public void setDtoConverterService(
			final DTOConverterService dtoConverterService) {
		this.dtoConverterService = dtoConverterService;
	}

	/**
	 * Makes an Excel file that presents informations about accounts.
	 * 
	 * @return
	 */
	public byte[] makeAccountInfosXlsFile() {
		byte[] result = null;

		List<Account> accounts = getAllAccounts();

		// on construit le document
		HSSFWorkbook workbook = new HSSFWorkbook();

		// le style du header
		HSSFFont font = workbook.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		HSSFCellStyle style = workbook.createCellStyle();
		style.setFont(font);

		// on construit la feuille
		String nameSheet = getI18nService().getString("ACCOUNT.XLSFILE.SHEET");
		HSSFSheet sheet = workbook.createSheet(nameSheet);

		// on récupère les titres des colonnes
		String columnLabel = getI18nService().getString("ACCOUNT.NAME");
		String columnQuota = getI18nService().getString("ACCOUNT.QUOTA");
		String columnConsSMS = getI18nService().getString("ACCOUNT.CONS_SMS");

		// on fait le row header
		HSSFRow headerRow = sheet.createRow(0);
		HSSFCell cellLabelHeader = headerRow.createCell(0);
		cellLabelHeader.setCellStyle(style);
		cellLabelHeader.setCellValue(new HSSFRichTextString(columnLabel));
		HSSFCell cellQuotaHeader = headerRow.createCell(1);
		cellQuotaHeader.setCellStyle(style);
		cellQuotaHeader.setCellValue(new HSSFRichTextString(columnQuota));
		HSSFCell cellConSMSHeader = headerRow.createCell(2);
		cellConSMSHeader.setCellStyle(style);
		cellConSMSHeader.setCellValue(new HSSFRichTextString(columnConsSMS));

		HSSFRow valueRow = null;
		int i = 1;
		for (Account account : accounts) {
			// on recupere les valeurs du compte
			String label = account.getLabel();
			Long quota = account.getQuota();
			Long consumedSms = account.getConsumedSms();

			// on crée une nouvelle ligne de valeurs
			valueRow = sheet.createRow(i);
			HSSFCell cellLabelValue = valueRow.createCell(0);
			cellLabelValue.setCellValue(new HSSFRichTextString(label));
			HSSFCell cellQuotaValue = valueRow.createCell(1);
			cellQuotaValue.setCellValue(quota);
			HSSFCell cellConSMSValue = valueRow.createCell(2);
			cellConSMSValue.setCellValue(consumedSms);

			// on passe à la ligne suivante
			i++;
		}

		// On ouvre un flux d'écriture sur le fichier résultat
		try {
			ByteArrayOutputStream fos = new ByteArrayOutputStream();
			workbook.write(fos);
			fos.flush();
			fos.close();
			result = fos.toByteArray();
		} catch (FileNotFoundException e) {
			logger
					.error(
							"Impossible d'ouvrir un flux en écriture sur le fichier excel",
							e);
		} catch (IOException e) {
			logger.error("Impossible d'écrire dans le fichier excel", e);
		}

		return result;
	}

	/**
	 * Retrieves all the accounts defined in database.
	 * 
	 * @return
	 */
	public List<Account> getAllAccounts() {
		if (logger.isDebugEnabled()) {
			logger.debug("Retrieve the accounts from the database");
		}
		List<Account> allAccounts = daoService.getAccounts();
		return allAccounts;
	}

	/**
	 * Updates accounts quota.
	 * 
	 * @param importFile
	 */
	public void updateAccountsQuota(final InputStream importFile)
			throws UpdateAccountsQuotaException {
		try {
			// on construit le workbook à partir du fichier
			HSSFWorkbook workbook = new HSSFWorkbook(importFile);

			// on récupère la première feuille
			HSSFSheet sheet = workbook.getSheetAt(0);
			if (sheet == null) {
				String message = "Le fichier d'import ne contient pas de feuille.";
				throw new UpdateAccountsQuotaException(message);
			}

			int rowIndex = 0;
			HSSFRow row = sheet.getRow(rowIndex);

			Map<String, Integer> accountsToIncrement = new Hashtable<String, Integer>();
			List<String> accountsToDelete = new ArrayList<String>();

			// on parcourt les lignes
			while (row != null) {
				HSSFCell cellLabel = row.getCell(0);
				HSSFCell cellQuota = row.getCell(1);
				HSSFCell cellAction = row.getCell(2);

				// une action et un compte sont précisés
				if (cellLabel != null && cellAction != null) {
					HSSFRichTextString labelValue = cellLabel
							.getRichStringCellValue();

					HSSFRichTextString actionValue = cellAction
							.getRichStringCellValue();

					// le compte
					String account = labelValue.getString();

					// le compte n'a pas déjà été traité dans la
					// liste
					if (!accountsToIncrement.containsKey(account)
							&& !accountsToDelete.contains(account)) {

						// l'action
						String action = actionValue.getString();

						// action "Ajouter"
						if (ACTION_AJOUTER.equals(action)) {
							// le quota est précisé
							String errorQuotaFormat = "ACCOUNT.IMPORT.XLSFILE.ERROR.QUOTAFORMAT";
							if (cellQuota != null) {
								try {
									int quota = (int) cellQuota
											.getNumericCellValue();
									accountsToIncrement.put(account, quota);
								} catch (NumberFormatException e) {
									String message = getI18nService()
											.getString(errorQuotaFormat,
													rowIndex + 1);
									logger.error(message, e);
									throw new UpdateAccountsQuotaException(
											message, e);
								} catch (IllegalStateException e) {
									String message = getI18nService()
											.getString(errorQuotaFormat,
													rowIndex + 1);
									logger.error(message, e);
									throw new UpdateAccountsQuotaException(
											message, e);
								}
							} else {
								// le quota n'est pas précisé
								String message = getI18nService().getString(
										errorQuotaFormat, rowIndex + 1);
								throw new UpdateAccountsQuotaException(message);
							}

						} else if (ACTION_SUPPRIMER.equals(action)) {
							// action "Supprimer"

							if (cellQuota == null) {
								accountsToDelete.add(account);
							} else {
								String errorQuotaDelete = "ACCOUNT.IMPORT.XLSFILE.ERROR.QUOTADELETE";
								String message = getI18nService().getString(
										errorQuotaDelete, rowIndex + 1);
								throw new UpdateAccountsQuotaException(message);
							}

						} else {
							// action inconnue
							String errorActionUnknown = "ACCOUNT.IMPORT.XLSFILE.ERROR.ACTIONUNKNOWN";
							String message = getI18nService().getString(
									errorActionUnknown, rowIndex + 1);
							throw new UpdateAccountsQuotaException(message);
						}
					} else {
						// ce compte est déjà présent dans les lignes
						// précédentes
						String message = getI18nService().getString(
								"ACCOUNT.IMPORT.XLSFILE.ERROR.ACCOUNTEXISTS",
								rowIndex + 1);
						throw new UpdateAccountsQuotaException(message);
					}
				} else {
					String message = getI18nService().getString(
							"ACCOUNT.IMPORT.XLSFILE.ERROR.FIELDLESS",
							rowIndex + 1);
					throw new UpdateAccountsQuotaException(message);
				}

				rowIndex++;
				row = sheet.getRow(rowIndex);
			}
			// on finit par mettre à jour les comptes
			updateAccountQuota(accountsToIncrement, accountsToDelete);
		} catch (FileNotFoundException e) {
			// exception technique
			String message = "Impossible d'ouvrir le fichier d'import excel";
			logger.error(message, e);
			throw new UpdateAccountsQuotaException(message, e);
		} catch (IOException e) {
			// exception technique
			String message = "Impossible d'ouvrir en lecture le fichier d'import excel";
			logger.error(message, e);
			throw new UpdateAccountsQuotaException(message, e);
		} catch (UpdateAccountsQuotaException e) {
			// exception métier
			// on relance
			throw e;
		}
	}

	/**
	 * Updates the quotas.
	 * 
	 * @param accountsToIncrement
	 *            Accounts with quota has to be incremented
	 * @param accountsToDelete
	 *            Accounts to delete
	 */
	private void updateAccountQuota(
			final Map<String, Integer> accountsToIncrement,
			final List<String> accountsToDelete) {

		// mise à jour des comptes dont le quota ets à incrémenter
		for (String account : accountsToIncrement.keySet()) {
			int increment = accountsToIncrement.get(account);
			incrementAccountQuota(account, increment);
		}

		// La suppression d'un compte consiste à mettre son quota à 0
		for (String accountName : accountsToDelete) {
			Account account = daoService.getAccountByName(accountName);
			account.setQuota(new Long(0));
			daoService.updateAccount(account);
		}

	}

	/**
	 * Increments the quota of the account.
	 * 
	 * @param account
	 * @param increment
	 */
	private void incrementAccountQuota(final String accountName,
			final int increment) {
		// on récupère le compte de la base
		Account account = daoService.getAccountByName(accountName);
		// si aucun compte de ce nom n'existe, on le crée
		if (account == null) {
			Account newAccount = new Account();
			newAccount.setLabel(accountName);
			daoService.addAccount(newAccount);
			account = daoService.getAccountByName(accountName);
		}
		// on récupère le quota actuel
		Long currentQuota = account.getQuota();
		// on l'incrémente
		Long newQuota = currentQuota + increment;
		// si le nouveau quota est négatif, on le met à zéro
		if (newQuota < 0) {
			newQuota = new Long(0);
			logger.debug("Le nouveau quota du compte " + accountName
					+ " est négatif -> on le met à zéro.");
		}
		account.setQuota(newQuota);
		daoService.updateAccount(account);
		logger.debug("Le nouveau quota du compte " + accountName + " est : "
				+ newQuota);
	}

	/**
	 * Makes account report.
	 * 
	 * @return
	 * @throws JRException
	 * @throws IOException
	 */
	public byte[] makeAccountPdfReport() throws JRException, IOException {

		JasperPrint jasperPrint = makeJasperPrint();

		// on exporte en pdf
		byte[] bytes = JasperExportManager.exportReportToPdf(jasperPrint);

		return bytes;
	}

	/**
	 * Makes an Excel file that presents informations about accounts.
	 * 
	 * @return
	 * @throws JRException
	 * @throws IOException
	 */
	public byte[] makeAccountInfosXlsFile2() throws IOException, JRException {

		byte[] result = null;

		JasperPrint jasperPrint = makeJasperPrint();

		ByteArrayOutputStream xlsReport = new ByteArrayOutputStream();
		JRXlsExporter exporter = new JRXlsExporter();
		exporter.setParameter(JRXlsAbstractExporterParameter.JASPER_PRINT,
				jasperPrint);

		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, xlsReport);

		exporter.exportReport();

		result = xlsReport.toByteArray();

		return result;

	}

	/**
	 * Makes a jasper print.
	 * 
	 * @return
	 * @throws IOException
	 * @throws JRException
	 */
	private JasperPrint makeJasperPrint() throws IOException, JRException {

		JasperPrint result;
		// on récupère le fichier .jasper (déjà compilé)
		InputStream jasperFile = this.getClass().getResourceAsStream(
				"/properties/jasper/accounts_report.jasper");
		// on récupère le logo pour l'ajouter au report
		InputStream logoStream = this.getClass().getResourceAsStream(
				"/properties/jasper/Logo_Univ_Paris.gif");
		Image logoImage = ImageIO.read(logoStream);

		// Les paramètres
		Map<String, Object> parameters = new HashMap<String, Object>();
		// on recupere les chaines i18n
		String softName = getI18nService().getString("SOFTWARE.NAME");
		String reportTitle = getI18nService().getString("ACCOUNT.REPORT.TITLE");
		String columnLabel = getI18nService().getString("ACCOUNT.NAME");
		String columnQuota = getI18nService().getString("ACCOUNT.QUOTA");
		String columnSMS = getI18nService().getString("ACCOUNT.CONS_SMS");
		parameters.put("SOFTWARE_NAME", softName);
		parameters.put("REPORT_TITLE", reportTitle);
		parameters.put("COLUMN_LABEL", columnLabel);
		parameters.put("COLUMN_QUOTA", columnQuota);
		parameters.put("COLUMN_SMS", columnSMS);
		parameters.put("LOGO_UNIV_PARIS", logoImage);

		// la source de données
		AccountDataSource accountDataSource = new AccountDataSource(
				getAllAccounts());

		// on remplit le report avec les paramètres et la source de données
		result = JasperFillManager.fillReport(jasperFile,
				parameters, accountDataSource);

		return result;
	}

	/**
	 * Returns all applications as UIObject.
	 * 
	 * @return
	 */
	public List<UIAccount> getAllUIAccounts() {
		if (logger.isDebugEnabled()) {
			logger.debug("Retrieves the accounts from the database");
		}
		List<UIAccount> allUiAccounts = new ArrayList<UIAccount>();
		List<Account> allAccounts = daoService.getAccounts();

		for (Account acc : allAccounts) {
			UIAccount ui = dtoConverterService.convertToUI(acc);
			allUiAccounts.add(ui);
		}
		return allUiAccounts;
	}

	/**
	 * Return the account with the specified id.
	 * 
	 * @param accountId
	 * @return
	 */
	public Account getAccountById(final String accountId) {
		Account result = null;

		try {
			int id = Integer.parseInt(accountId);
			result = daoService.getAccountById(id);
		} catch (NumberFormatException e) {
			logger.warn("L'identifiant d'un compte doit etre un entier", e);
		}

		return result;
	}

}
