/**
 * 
 */
package com.revature.validation;

import java.sql.SQLException;

import com.revature.dal.AccountStatusDAO;
import com.revature.dal.DAOUtilities;
import com.revature.exceptions.InvalidAccountStatusParamsException;

/**
 * @author John Dawkins jackdawkins1298@outlook.com
 *
 */
public class AccountStatusValidator {
	
	public static void validateAccountStatusParams(String status) throws SQLException, InvalidAccountStatusParamsException {
		// Get DAO
		AccountStatusDAO dao = DAOUtilities.getAccountStatusDAO();

		// Make sure the status is unique
		if (dao.accountStatusExists(status)) {
			throw new InvalidAccountStatusParamsException();
		}
	}
}
