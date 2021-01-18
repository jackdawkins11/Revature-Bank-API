/**
 * 
 */
package com.revature.dal;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.revature.model.Account;
import com.revature.model.AccountStatus;
import com.revature.model.AccountType;
import com.revature.model.Role;
import com.revature.model.User;
import com.revature.utilities.DatabaseClearer;

/**
 * @author John Dawkins jackdawkins1298@outlook.com
 *
 */
public class TestUserAccount {

	@BeforeAll
	static void setTesting() {
		ConnectionHandler.testing = true;
	}
	
	@BeforeEach
	public void setUp() {
		assertTrue(DatabaseClearer.clearTables());
	}

	@Test
	public void testGetAccountsByUser() throws Exception {
		// The User data
		String[] username = { "Jack", "Jake", "Jerry" };
		String[] password = { "pass1", "badpass", "passw$$" };
		String[] firstName = { "John", "Jacob", "Jerry" };
		String[] lastName = { "Dawkins", "McCregor", "Sander" };
		String[] email = { "jackdawkins1298@outlook.com", "jacob@gmail.com", "jj12p@outlook.com" };
		String[] roleStr = { "Admin", "Employee", "Standard" };

		// Get DAOs
		RoleDAO roleDAO = DAOUtilities.getRoleDAO();
		UserDAO userDAO = DAOUtilities.getUserDAO();

		// Insert the above users
		for (int i = 0; i < 3; i++) {
			int roleId = roleDAO.insertRole(roleStr[i]);

			Role role = roleDAO.getRoleById(roleId);

			userDAO.insertUser(username[i], password[i], firstName[i], lastName[i], email[i], role);

		}

		// Get the DAOs
		AccountStatusDAO accountStatusDAO = DAOUtilities.getAccountStatusDAO();
		AccountTypeDAO accountTypeDAO = DAOUtilities.getAccountTypeDAO();
		AccountDAO accountDAO = DAOUtilities.getAccountDAO();

		// Insert and retrieve two AccountTypes and two AccountStatus'
		int id = accountStatusDAO.insertAccountStatus("Open");
		AccountStatus openStatus = accountStatusDAO.getAccountStatusById(id);

		id = accountStatusDAO.insertAccountStatus("Closed");
		AccountStatus closedStatus = accountStatusDAO.getAccountStatusById(id);

		id = accountTypeDAO.insertAccountType("Checking");
		AccountType checkingType = accountTypeDAO.getAccountTypeById(id);

		id = accountTypeDAO.insertAccountType("Savings");
		AccountType savingsType = accountTypeDAO.getAccountTypeById(id);

		// The data for 3 accounts
		double amounts[] = { 1.1, 2.2, 3.3 };
		AccountStatus statuses[] = { openStatus, closedStatus, openStatus };
		AccountType types[] = { checkingType, savingsType, checkingType };

		// Insert the accounts
		for (int i = 0; i < 3; i++) {
			accountDAO.insertAccount(amounts[i], types[i], statuses[i]);
		}

		// Get UserAccount dao
		UserAccountDAO userAccountDAO = DAOUtilities.getUserAccountDAO();

		// get list of Users and list of Accounts
		ArrayList<User> users = userDAO.getAllUsers();
		ArrayList<Account> accounts = accountDAO.getAllAccounts();

		// Add some user account relationships
		userAccountDAO.insertUserAccountRelationship(users.get(0), accounts.get(0));
		userAccountDAO.insertUserAccountRelationship(users.get(0), accounts.get(1));

		// Get a set of the accounts associated with User Jack
		Set<Account> jackAccounts = new HashSet<Account>();
		jackAccounts.add(accounts.get(0));
		jackAccounts.add(accounts.get(1));

		// Get all the accounts associated with Jack via getAccountsByUser
		ArrayList<Account> jackAccountsNew = userAccountDAO.getAccountsByUser(users.get(0));

		// Convert to set
		Set<Account> jackAccountsSet = new HashSet<Account>();
		jackAccountsSet.addAll(jackAccountsNew);

		// compare
		assertTrue(jackAccounts.equals(jackAccountsSet));
	}

	@Test
	public void testGetUserByAccount() throws Exception {
		// The User data
		String[] username = { "Jack", "Jake", "Jerry" };
		String[] password = { "pass1", "badpass", "passw$$" };
		String[] firstName = { "John", "Jacob", "Jerry" };
		String[] lastName = { "Dawkins", "McCregor", "Sander" };
		String[] email = { "jackdawkins1298@outlook.com", "jacob@gmail.com", "jj12p@outlook.com" };
		String[] roleStr = { "Admin", "Employee", "Standard" };

		// Get DAOs
		RoleDAO roleDAO = DAOUtilities.getRoleDAO();
		UserDAO userDAO = DAOUtilities.getUserDAO();

		// Insert the above users
		for (int i = 0; i < 3; i++) {
			int roleId = roleDAO.insertRole(roleStr[i]);

			Role role = roleDAO.getRoleById(roleId);

			userDAO.insertUser(username[i], password[i], firstName[i], lastName[i], email[i], role);

		}

		// Get the DAOs
		AccountStatusDAO accountStatusDAO = DAOUtilities.getAccountStatusDAO();
		AccountTypeDAO accountTypeDAO = DAOUtilities.getAccountTypeDAO();
		AccountDAO accountDAO = DAOUtilities.getAccountDAO();

		// Insert and retrieve two AccountTypes and two AccountStatus'
		int id = accountStatusDAO.insertAccountStatus("Open");
		AccountStatus openStatus = accountStatusDAO.getAccountStatusById(id);

		id = accountStatusDAO.insertAccountStatus("Closed");
		AccountStatus closedStatus = accountStatusDAO.getAccountStatusById(id);

		id = accountTypeDAO.insertAccountType("Checking");
		AccountType checkingType = accountTypeDAO.getAccountTypeById(id);

		id = accountTypeDAO.insertAccountType("Savings");
		AccountType savingsType = accountTypeDAO.getAccountTypeById(id);

		// The data for 3 accounts
		double amounts[] = { 1.1, 2.2, 3.3 };
		AccountStatus statuses[] = { openStatus, closedStatus, openStatus };
		AccountType types[] = { checkingType, savingsType, checkingType };

		// Insert the accounts
		for (int i = 0; i < 3; i++) {
			accountDAO.insertAccount(amounts[i], types[i], statuses[i]);
		}

		// Get UserAccount dao
		UserAccountDAO userAccountDAO = DAOUtilities.getUserAccountDAO();

		// get list of Users and list of Accounts
		ArrayList<User> users = userDAO.getAllUsers();
		ArrayList<Account> accounts = accountDAO.getAllAccounts();

		// Add some user account relationships
		userAccountDAO.insertUserAccountRelationship(users.get(0), accounts.get(0));
		userAccountDAO.insertUserAccountRelationship(users.get(2), accounts.get(1));

		// Get the user associated with accounts.get(0)
		User account0Owner = userAccountDAO.getAccountOwner(accounts.get(0));

		// Make sure it is correct
		assertTrue(account0Owner.equals(users.get(0)));

		// Get the user associated with accounts.get(0)
		User account1Owner = userAccountDAO.getAccountOwner(accounts.get(1));

		// Make sure it is correct
		assertTrue(account1Owner.equals(users.get(2)));

	}
}
