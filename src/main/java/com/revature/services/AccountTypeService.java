/**
 * 
 */
package com.revature.services;

import java.sql.SQLException;
import java.util.ArrayList;

import com.revature.dal.AccountTypeDAO;
import com.revature.dal.DAOUtilities;
import com.revature.exceptions.InvalidAccountTypeParamsException;
import com.revature.exceptions.NoSuchAccountTypeException;
import com.revature.model.AccountType;
import com.revature.validation.AccountTypeValidator;

/**
 * @author John Dawkins jackdawkins1298@outlook.com
 *
 */
public class AccountTypeService {

	public static AccountType insertAccountType(String type) throws SQLException, InvalidAccountTypeParamsException {
		// Validate the type trying to be inserted
		AccountTypeValidator.validateAccountTypeParams(type);

		// Get DAO
		AccountTypeDAO dao = DAOUtilities.getAccountTypeDAO();

		// Insert the type and get the id
		int id = dao.insertAccountType(type);

		// Return the generated type
		try {
			return dao.getAccountTypeById(id);
		} catch (NoSuchAccountTypeException e) {
			// We know this id is valid, so just throw SQLException
			throw new SQLException(e);
		}
	}

	public static AccountType getAccountTypeById(int id) throws SQLException, NoSuchAccountTypeException {
		// Get dao
		AccountTypeDAO dao = DAOUtilities.getAccountTypeDAO();

		// Return the type with the id, propagating exceptions
		return dao.getAccountTypeById(id);
	}

	public static ArrayList<AccountType> getAllAccountTypes() throws SQLException {
		// Get dao
		AccountTypeDAO dao = DAOUtilities.getAccountTypeDAO();

		// Return all the types
		return dao.getAllAccountTypes();
	}
}
