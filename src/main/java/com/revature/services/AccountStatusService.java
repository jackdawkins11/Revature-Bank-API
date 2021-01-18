/**
 * 
 */
package com.revature.services;

import java.sql.SQLException;
import java.util.ArrayList;

import com.revature.dal.AccountStatusDAO;
import com.revature.dal.DAOUtilities;
import com.revature.exceptions.InvalidAccountStatusParamsException;
import com.revature.exceptions.NoSuchAccountStatusException;
import com.revature.model.AccountStatus;
import com.revature.validation.AccountStatusValidator;

/**
 * @author John Dawkins jackdawkins1298@outlook.com
 *
 */
public class AccountStatusService {

	public static AccountStatus insertAccountStatus(String status) throws SQLException, InvalidAccountStatusParamsException {
		// Validate the status trying to be inserted
		AccountStatusValidator.validateAccountStatusParams(status);

		// Get DAO
		AccountStatusDAO dao = DAOUtilities.getAccountStatusDAO();

		// Insert the status and get the id
		int id = dao.insertAccountStatus(status);

		// Return the generated status
		try {
			return dao.getAccountStatusById(id);
		} catch (NoSuchAccountStatusException e) {
			// We know this id is valid, so just throw SQLException
			throw new SQLException(e);
		}
	}

	public static AccountStatus getAccountStatusById(int id) throws SQLException, NoSuchAccountStatusException {
		// Get dao
		AccountStatusDAO dao = DAOUtilities.getAccountStatusDAO();

		// Return the status with the id, propagating exceptions
		return dao.getAccountStatusById(id);
	}

	public static ArrayList<AccountStatus> getAllAccountStatuss() throws SQLException {
		// Get dao
		AccountStatusDAO dao = DAOUtilities.getAccountStatusDAO();

		// Return all the statuss
		return dao.getAllAccountStatus();
	}
}
