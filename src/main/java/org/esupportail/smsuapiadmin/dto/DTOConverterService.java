package org.esupportail.smsuapiadmin.dto;

import java.util.Set;

import org.esupportail.smsuapi.dao.beans.Account;
import org.esupportail.smsuapi.dao.beans.Application;
import org.esupportail.smsuapiadmin.dao.beans.Fonction;
import org.esupportail.smsuapi.dao.beans.Institution;
import org.esupportail.smsuapiadmin.dao.beans.Role;
import org.esupportail.smsuapi.dao.beans.Sms;
import org.esupportail.smsuapi.dao.beans.Statistic;
import org.esupportail.smsuapiadmin.dao.beans.UserBoSmsu;
import org.esupportail.smsuapiadmin.domain.beans.EnumeratedFunction;
import org.esupportail.smsuapiadmin.domain.beans.EnumeratedRole;
import org.esupportail.smsuapiadmin.dto.beans.UIAccount;
import org.esupportail.smsuapiadmin.dto.beans.UIApplication;
import org.esupportail.smsuapiadmin.dto.beans.UIRole;
import org.esupportail.smsuapiadmin.dto.beans.UISms;
import org.esupportail.smsuapiadmin.dto.beans.UIStatistic;
import org.esupportail.smsuapiadmin.dto.beans.UIUser;

/**
 * Interface 'DTOConverterService' purposes services to convert business objects
 * to UI objects and inversely.
 * 
 * @author MZRL3760
 * 
 */
public interface DTOConverterService {

	/**
	 * Makes an UIAccount from an Account.
	 * 
	 * @param acc
	 * @return
	 */
	UIAccount convertToUI(Account acc);

	/**
	 * Makes an UIApplication from an Application.
	 * 
	 * @param app
	 * @return
	 */
	UIApplication convertToUI(Application app);

	/**
	 * Makes an UIAccount from an Account.
	 * 
	 * @param acc
	 * @return
	 */
	UIStatistic convertToUI(Statistic stat);

	/**
	 * Makes an Application from an UIApplication.
	 * 
	 * @param uiApp
	 * @return
	 */
	Application convertFromUI(UIApplication uiApp, boolean isAddMode);

	Account convertFromUI(UIAccount acc, boolean isAddMode);

	/**
	 * Makes a string from an Institution.
	 * 
	 * @param inst
	 * @return
	 */
	String convertToUI(Institution inst);

	/**
	 * Makes an UIUser from a UserBoSmsu.
	 * 
	 * @param user
	 * @return
	 */
	UIUser convertToUI(UserBoSmsu user);

	/**
	 * Makes an UIFonction from a Fonction.
	 * 
	 * @param user
	 * @return
	 */
	EnumeratedFunction convertToEnum(Fonction fct);

	Set<EnumeratedFunction> convertToEnum(Set<Fonction> fcts);

	Set<String> convert(Set<Fonction> fcts);

	/**
	 * Makes an UIRole from a Role.
	 * 
	 * @param role
	 * @return
	 */
	UIRole convertToUI(Role role);

	/**
	 * Makes an EnumeratedRole from a Role.
	 * 
	 * @param role
	 * @return
	 */
	EnumeratedRole convertToEnum(Role role);

	/**
	 * Makes an UISms from a Sms.
	 * 
	 * @param user
	 * @return
	 */
	UISms convertToUI(Sms sms);

	/**
	 * Makes a UserBoSmsu from a UIUser.
	 * 
	 * @param uiUser
	 * @return
	 */
	UserBoSmsu convertFromUI(UIUser uiUser, boolean isAddMode);

}
