package org.esupportail.smsuapiadmin.dto;

import java.util.List;
import java.util.Set;

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
import org.esupportail.smsuapiadmin.dto.beans.UIRole;
import org.esupportail.smsuapiadmin.dto.beans.UISms;
import org.esupportail.smsuapiadmin.dto.beans.UIStatistic;
import org.esupportail.smsuapiadmin.dto.beans.UIUser;
import org.esupportail.smsuapiadmin.business.NotFoundException;

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

		result.setId(acc.getId());
		result.setName(acc.getLabel());
		result.setQuota(acc.getQuota());
		result.setConsumedSms(acc.getConsumedSms());

		return result;
	}

	
    public Account convertFromUI(final UIAccount acc, boolean isAddMode) {
		Account result = new Account();

		if (!isAddMode) {
			result.setId(Integer.valueOf(acc.getId()));
		}
		result.setLabel(acc.getName());
		result.setQuota(acc.getQuota());

		return result;
	}

	
	public UIApplication convertToUI(final Application app) {
		UIApplication result = new UIApplication();

		result.setId(app.getId());
		result.setName(app.getName());
		result.setPassword(app.getPassword());

		result.setAccountName(app.getAccount().getLabel());
		String uiInst = convertToUI(app.getInstitution());
		result.setInstitution(uiInst);
		result.setQuota(app.getQuota());
		result.setConsumedSms(app.getConsumedSms());
		boolean deletable = isDeletable(app);
		result.setDeletable(deletable);

		return result;
	}

	
	public String convertToUI(final Institution inst) {
		return inst.getLabel();
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

		result.setAccountName(id.getAcc().getLabel());
		result.setAppName(id.getApp().getName());
		result.setInstitution(id.getApp().getInstitution().getLabel());

		result.setNbSendedSMS(stat.getNbSms());
		result.setNbSMSInError(stat.getNbSmsInError());

		return result;
	}

	
	public UIUser convertToUI(final UserBoSmsu user) {
		UIUser result = new UIUser();

		result.setId(user.getId() + "");
		result.setLogin(user.getLogin());
		result.setRole(convertToEnum(user.getRole()));

		return result;
	}

	
	public UIRole convertToUI(final Role role) {
		UIRole result = new UIRole();

		result.setId(role.getId() + "");	
		result.setRole(EnumeratedRole.valueOf(role.getName()));
		result.setFonctions(convertToEnum(role.getFonctions()));

		return result;
	}
	
	public EnumeratedRole convertToEnum(final Role role) {
		return EnumeratedRole.valueOf(role.getName());
	}

	
	public EnumeratedFunction convertToEnum(final Fonction fct) {
		return EnumeratedFunction.valueOf(fct.getName());
	}
	
	public Set<EnumeratedFunction> convertToEnum(final Set<Fonction> fcts) {
		Set<EnumeratedFunction> r = new java.util.TreeSet<EnumeratedFunction>();
		for (Fonction fct : fcts) {
			r.add(convertToEnum(fct));
		}
		return r;
	}

	/**
	 * Setter for 'daoService'.
	 * 
	 * @param daoService
	 */
	public void setDaoService(final DaoService daoService) {
		this.daoService = daoService;
	}

	
    public Application convertFromUI(final UIApplication uiApp, boolean isAddMode) {
		final Application result = new Application();

		if (!isAddMode) {
			// l'id
			result.setId(Integer.valueOf(uiApp.getId()));
		}

		// le nom
		result.setName(uiApp.getName());

		result.setPassword(uiApp.getPassword());

		result.setQuota(uiApp.getQuota());

		// le compte d'imputation
		Account account = daoService.getAccountByName(uiApp.getAccountName());
		if (account == null) throw new NotFoundException("invalid account " + uiApp.getAccountName());
		result.setAccount(account);

		// l'etablissement
		Institution institution = daoService.getInstitutionByName(uiApp
				.getInstitution());
		if (institution == null) {
			institution = new Institution();
			institution.setLabel(uiApp.getInstitution());
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

	
	public UserBoSmsu convertFromUI(final UIUser uiUser, boolean isAddMode) {
		final UserBoSmsu result = new UserBoSmsu();

		if (!isAddMode) {
			// l'id
			result.setId(Integer.valueOf(uiUser.getId()));
		}

		// le nom
		result.setLogin(uiUser.getLogin());

		// le role
		EnumeratedRole roleName = uiUser.getRole();
		Role role = daoService.getRoleByName(roleName.toString());
		if (role == null) {
			logger.error("Aucun role d'identifiant " + roleName + " n'existe en base.");
		}
		result.setRole(role);

		return result;
	}

}
