package org.esupportail.smsuapiadmin.dto;

import java.util.ArrayList;
import java.util.List;

import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.smsuapiadmin.dao.DaoService;
import org.esupportail.smsuapiadmin.dao.beans.Account;
import org.esupportail.smsuapiadmin.dao.beans.Application;
import org.esupportail.smsuapiadmin.dao.beans.Fonction;
import org.esupportail.smsuapiadmin.dao.beans.Institution;
import org.esupportail.smsuapiadmin.dao.beans.Role;
import org.esupportail.smsuapiadmin.dao.beans.Sms;
import org.esupportail.smsuapiadmin.dao.beans.SmsStatus;
import org.esupportail.smsuapiadmin.dao.beans.Statistic;
import org.esupportail.smsuapiadmin.dao.beans.StatisticPK;
import org.esupportail.smsuapiadmin.dao.beans.UserBoSmsu;
import org.esupportail.smsuapiadmin.domain.beans.EnumeratedFunction;
import org.esupportail.smsuapiadmin.domain.beans.EnumeratedRole;
import org.esupportail.smsuapiadmin.dto.beans.UIAccount;
import org.esupportail.smsuapiadmin.dto.beans.UIApplication;
import org.esupportail.smsuapiadmin.dto.beans.UIFonction;
import org.esupportail.smsuapiadmin.dto.beans.UIInstitution;
import org.esupportail.smsuapiadmin.dto.beans.UIRole;
import org.esupportail.smsuapiadmin.dto.beans.UISms;
import org.esupportail.smsuapiadmin.dto.beans.UIStatistic;
import org.esupportail.smsuapiadmin.dto.beans.UIUser;

/**
 * Implementation of the interface 'DTOConverterService'.
 * 
 * @author MZRL3760
 * 
 */
public class DTOConverterServiceImpl implements DTOConverterService {

	/**
	 * Log4j logger.
	 */
	private final Logger logger = new LoggerImpl(getClass());

	/**
	 * {@link DaoService}.
	 */
	private DaoService daoService;

	/**
	 * Default constructor.
	 */
	public DTOConverterServiceImpl() {
		// nothing to do
	}

	
	public UIAccount convertToUI(final Account acc) {
		UIAccount result = new UIAccount();

		result.setId(String.valueOf(acc.getId()));
		result.setName(acc.getLabel());
		result.setQuota(acc.getQuota() + "");
		result.setConsumedSms(acc.getConsumedSms() + "");

		return result;
	}

	
	public UIApplication convertToUI(final Application app) {
		UIApplication result = new UIApplication();

		result.setId(String.valueOf(app.getId()));
		result.setName(app.getName());
		result.setCertificateOrPassword(app.getCertificate());

		UIAccount uiAcc = convertToUI(app.getAccount());
		result.setAccount(uiAcc);
		UIInstitution uiInst = convertToUI(app.getInstitution());
		result.setAccount(uiAcc);
		result.setInstitution(uiInst);
		result.setQuota(app.getQuota() + "");
		boolean deletable = isDeletable(app);
		result.setDeletable(deletable);

		return result;
	}

	
	public UIInstitution convertToUI(final Institution inst) {
		UIInstitution result = new UIInstitution();

		result.setId(String.valueOf(inst.getId()));
		result.setName(inst.getLabel());

		return result;
	}

	/**
	 * Returns true the application is not referenced by other objects in
	 * database. False otherwise.
	 * 
	 * @param app
	 * @return
	 */
	private boolean isDeletable(final Application app) {
		boolean result = false;

		List<Sms> listSMS = daoService.getSMSByApplication(app);

		// aucun sms ne reference cette application
		if ((listSMS == null) || (listSMS.size() == 0)) {
			List<Statistic> listStatistic = daoService
					.getStatisticByApplication(app);
			// aucun sms ne reference cette application
			if ((listStatistic == null) || (listStatistic.size() == 0)) {
				result = true;
			}
		}

		return result;
	}

	
	public UIStatistic convertToUI(final Statistic stat) {
		UIStatistic result = new UIStatistic();

		StatisticPK id = stat.getId();
		result.setMonth(id.getMonth());

		Account acc = id.getAcc();
		UIAccount uiAcc = convertToUI(acc);
		result.setAccount(uiAcc);

		Application app = id.getApp();
		UIApplication uiApp = convertToUI(app);
		result.setApplication(uiApp);

		result.setNbSendedSMS(stat.getNbSms());
		result.setNbSMSInError(stat.getNbSmsInError());

		return result;
	}

	
	public UIUser convertToUI(final UserBoSmsu user) {
		UIUser result = new UIUser();

		result.setId(user.getId() + "");
		result.setLogin(user.getLogin());

		Role role = user.getRole();
		UIRole uiRole = convertToUI(role);
		result.setRole(uiRole);

		return result;
	}

	
	public UIRole convertToUI(final Role role) {
		UIRole result = new UIRole();

		result.setId(role.getId() + "");
		
		EnumeratedRole enumRole = EnumeratedRole.valueOf(role.getName());
		result.setRole(enumRole);

		List<UIFonction> uiFcts = new ArrayList<UIFonction>();

		for (Fonction fct : role.getFonctions()) {
			UIFonction uiFct = convertToUI(fct);
			uiFcts.add(uiFct);
		}
		result.setFonctions(uiFcts);

		return result;
	}

	
	public UIFonction convertToUI(final Fonction fct) {
		UIFonction result = new UIFonction();

		result.setId(fct.getId() + "");
		EnumeratedFunction function = EnumeratedFunction.valueOf(fct.getName());
		result.setFunction(function);

		return result;
	}

	/**
	 * Setter for 'daoService'.
	 * 
	 * @param daoService
	 */
	public void setDaoService(final DaoService daoService) {
		this.daoService = daoService;
	}

	
	public Application convertFromUI(final UIApplication uiApp) {
		final Application result = new Application();

		if (!uiApp.isAddMode()) {
			// l'id
			result.setId(Integer.valueOf(uiApp.getId()));
		}

		// le nom
		result.setName(uiApp.getName());

		// le certificat
		result.setCertificate(uiApp.getCertificateOrPassword());

		// le quota
		String quotaStr = uiApp.getQuota();
		Long quota = Long.valueOf(quotaStr);
		result.setQuota(quota);

		// le compte d'imputation
		Account account = daoService.getAccountByName(uiApp.getAccount()
				.getName());
		if (account == null) {
			account = new Account();
			account.setLabel(uiApp.getAccount().getName());
			account.setQuota(quota);
			daoService.addAccount(account);
		}
		result.setAccount(account);

		// l'etablissement
		Institution institution = daoService.getInstitutionByName(uiApp
				.getInstitution().getName());
		if (institution == null) {
			institution = new Institution();
			institution.setLabel(uiApp.getInstitution().getName());
			daoService.addInstitution(institution);
		}
		result.setInstitution(institution);

		// le nombre de sms consomme est nul;
		result.setConsumedSms(new Long(0));

		return result;
	}

	
	public UISms convertToUI(final Sms sms) {
		UISms result = new UISms();
		// id
		result.setId(sms.getId() + "");
		// account
		Account acc = sms.getAcc();
		UIAccount uiAcc = convertToUI(acc);
		result.setAccount(uiAcc);
		// application
		Application app = sms.getApp();
		UIApplication uiApp = convertToUI(app);
		result.setApplication(uiApp);
		// date
		result.setDate(sms.getDate());
		// initialId
		result.setInitialId(sms.getInitialId() + "");
		// senderId
		result.setSenderId(sms.getSenderId() + "");
		// state
		SmsStatus smsStatus = SmsStatus.valueOf(sms.getState());
		result.setState(smsStatus);
		// phone
		result.setPhone(sms.getPhone());

		return result;
	}

	
	public UserBoSmsu convertFromUI(final UIUser uiUser) {
		final UserBoSmsu result = new UserBoSmsu();

		if (!uiUser.isAddMode()) {
			// l'id
			result.setId(Integer.valueOf(uiUser.getId()));
		}

		// le nom
		result.setLogin(uiUser.getLogin());

		// le role
		String roleId = uiUser.getRole().getId();
		Role role = daoService.getRoleById(roleId);
		if (role == null) {
			logger.error("Aucun role d'identifiant " + roleId
					+ " n'existe en base.");
		}
		result.setRole(role);

		return result;
	}

}
