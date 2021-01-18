/**
 * 
 */
package com.revature.services;

import java.sql.SQLException;
import java.util.ArrayList;

import com.revature.dal.AccountDAO;
import com.revature.dal.DAOUtilities;
import com.revature.dal.UserAccountDAO;
import com.revature.exceptions.InvalidAccountParamsException;
import com.revature.exceptions.NoSuchAccountException;
import com.revature.exceptions.NotAuthorizedException;
import com.revature.model.Account;
import com.revature.model.AccountStatus;
import com.revature.model.AccountType;
import com.revature.model.User;
import com.revature.validation.AccountValidator;

/**
 * @author John Dawkins jackdawkins1298@outlook.com
 *
 */
public class AccountService {

	public static Account insertAccount(User user, double amount, User accountUser, AccountType type, AccountStatus status)
			throws SQLException, NotAuthorizedException, InvalidAccountParamsException {
		// Check if user is authorized to create the given account
		if (!(user.getRole().getRole().equals("Admin") || user.getRole().getRole().equals("Employee"))) {
			// A regular user is only authorized to add an account for themself
			if (user.getId() != accountUser.getId()) {
				throw new NotAuthorizedException();
			}
		}

		// Validate the amount
		AccountValidator.validateAccountParams(amount);

		// Get the needed daos
		AccountDAO accountDAO = DAOUtilities.getAccountDAO();
		UserAccountDAO userAccountDAO = DAOUtilities.getUserAccountDAO();

		// Insert the account, getting the id
		int id = accountDAO.insertAccount(amount, type, status);

		// Get the new account
		Account account = null;
		try {
			account = accountDAO.getAccountById(id);
		} catch (NoSuchAccountException e) {
			// We know the ID is valid, so if we get this exception something else went
			// wrong and it makes sense to throw an SQLException rather then an
			// NoSuchAccountException
			throw new SQLException(e);
		}

		// Insert the user-account relationship
		userAccountDAO.insertUserAccountRelationship(accountUser, account);

		return account;
	}

	public static Account updateAccount(User user, Account account, double amount, AccountType type, AccountStatus status)
			throws SQLException, NotAuthorizedException, InvalidAccountParamsException {
		// Check if user is authorized to create the given account
		if (!user.getRole().getRole().equals("Admin")) {
			// Non admins are unauthorized
			throw new NotAuthorizedException();
		}

		// Validate the amount
		AccountValidator.validateAccountParams(amount);

		// Get the needed dao
		AccountDAO accountDAO = DAOUtilities.getAccountDAO();

		// Update the account
		accountDAO.updateAccount(account, amount, type, status);

		// Refresh the account
		try {
			account = accountDAO.getAccountById(account.getId());
		} catch (NoSuchAccountException e) {
			// We know the ID is valid, so if we get this exception something else went
			// wrong and it makes sense to throw an SQLException rather then an
			// NoSuchAccountException
			throw new SQLException(e);
		}

		return account;
	}

	public static Account getAccountById(int id) throws SQLException, NoSuchAccountException {
		// Get the DAO
		AccountDAO dao = DAOUtilities.getAccountDAO();

		// Return the account with the given id, propagating exceptions
		return dao.getAccountById(id);
	}

	public static Account authorizeAndGetAccountById(User user, int id)
			throws SQLException, NoSuchAccountException, NotAuthorizedException {
		// Get the DAOs needed
		AccountDAO accountDAO = DAOUtilities.getAccountDAO();
		UserAccountDAO userAccountDAO = DAOUtilities.getUserAccountDAO();

		// Get the account with the given id, propagating exceptions
		Account account = accountDAO.getAccountById(id);

		// Check if user is authorized
		if (!(user.getRole().getRole().equals("Admin") || user.getRole().getRole().equals("Employee"))) {
			// Regular user must be accessing an account that belongs to them
			User accountOwner = userAccountDAO.getAccountOwner(account);
			if (user.getId() != accountOwner.getId()) {
				throw new NotAuthorizedException();
			}
		}

		return account;
	}

	public static ArrayList<Account> getAllAccounts(User user) throws NotAuthorizedException, SQLException {
		// Check if user is authorized
		if (!(user.getRole().getRole().equals("Admin") || user.getRole().getRole().equals("Employee"))) {
			throw new NotAuthorizedException();
		}

		// Get DAO
		AccountDAO dao = DAOUtilities.getAccountDAO();

		return dao.getAllAccounts();
	}

	public static ArrayList<Account> getAccountsByStatus(User user, AccountStatus status)
			throws NotAuthorizedException, SQLException {
		// Check if user is authorized
		if (!(user.getRole().getRole().equals("Admin") || user.getRole().getRole().equals("Employee"))) {
			throw new NotAuthorizedException();
		}

		// Get DAO
		AccountDAO dao = DAOUtilities.getAccountDAO();

		return dao.getAccountsByStatus(status);
	}

	public static ArrayList<Account> getAccountsByUser(User user, User accountUser) throws NotAuthorizedException, SQLException {
		// Check if user is authorized
		if (!(user.getRole().getRole().equals("Admin") || user.getRole().getRole().equals("Employee"))) {
			// Regular users can only get accounts belonging to them
			if (user.getId() != accountUser.getId()) {
				throw new NotAuthorizedException();
			}
		}

		// Get DAO
		UserAccountDAO dao = DAOUtilities.getUserAccountDAO();

		return dao.getAccountsByUser(accountUser);
	}

	public static void withdraw(User user, Account account, double amount) throws SQLException, NotAuthorizedException {
		// Get the DAOs needed
		AccountDAO accountDAO = DAOUtilities.getAccountDAO();
		UserAccountDAO userAccountDAO = DAOUtilities.getUserAccountDAO();

		// Check if user is authorized
		if (!user.getRole().getRole().equals("Admin")) {
			// Non-admin must be accessing an account that belongs to them
			User accountOwner = userAccountDAO.getAccountOwner(account);
			if (user.getId() != accountOwner.getId()) {
				throw new NotAuthorizedException();
			}
		}

		// Withdraw from account
		accountDAO.addAmount(account, -amount);
	}

	public static void deposit(User user, Account account, double amount) throws SQLException, NotAuthorizedException {
		// Get the DAOs needed
		AccountDAO accountDAO = DAOUtilities.getAccountDAO();
		UserAccountDAO userAccountDAO = DAOUtilities.getUserAccountDAO();

		// Check if user is authorized
		if (!user.getRole().getRole().equals("Admin")) {
			// Non-admin must be accessing an account that belongs to them
			User accountOwner = userAccountDAO.getAccountOwner(account);
			if (user.getId() != accountOwner.getId()) {
				throw new NotAuthorizedException();
			}
		}

		// Deposit to account
		accountDAO.addAmount(account, amount);
	}

	public static void transfer(User user, Account account1, Account account2, double amount)
			throws SQLException, NotAuthorizedException {
		// Get the DAOs needed
		AccountDAO accountDAO = DAOUtilities.getAccountDAO();
		UserAccountDAO userAccountDAO = DAOUtilities.getUserAccountDAO();

		// Check if user is authorized
		if (!user.getRole().getRole().equals("Admin")) {
			// Non-admin must be withdrawing from an account that belongs to them
			User accountOwner = userAccountDAO.getAccountOwner(account1);
			if (user.getId() != accountOwner.getId()) {
				throw new NotAuthorizedException();
			}
		}

		// Withdraw from account1
		accountDAO.addAmount(account1, -amount);
		
		//Deposit to account2
		accountDAO.addAmount(account2, amount);
	}
}
