/**
 * 
 */
package com.revature.validation;

import java.sql.SQLException;

import com.revature.dal.AccountTypeDAO;
import com.revature.dal.DAOUtilities;
import com.revature.exceptions.InvalidAccountTypeParamsException;

/**
 * @author John Dawkins jackdawkins1298@outlook.com
 *
 */
public class AccountTypeValidator {

	public static void validateAccountTypeParams(String type) throws SQLException, InvalidAccountTypeParamsException {
		// Get DAO
		AccountTypeDAO dao = DAOUtilities.getAccountTypeDAO();

		// Make sure the type is unique
		if (dao.accountTypeExists(type)) {
			throw new InvalidAccountTypeParamsException();
		}
	}
}
