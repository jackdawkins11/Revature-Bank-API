/**
 * 
 */
package com.revature.dal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.revature.model.Account;
import com.revature.model.AccountStatus;
import com.revature.model.AccountType;
import com.revature.utilities.DatabaseClearer;

/**
 * @author John Dawkins jackdawkins1298@outlook.com
 *
 */
public class TestAccountDAO {
	
	@BeforeAll
	static void setTesting() {
		ConnectionHandler.testing = true;
	}

	@BeforeEach
	public void setUp() {
		assertTrue(DatabaseClearer.clearTables());
	}

	@Test
	public void testInsert() throws Exception {
		// The data for our Account
		double amount = 12.3;
		String type = "Savings";
		String status = "Open";

		// Get the DAOs
		AccountStatusDAO accountStatusDAO = DAOUtilities.getAccountStatusDAO();
		AccountTypeDAO accountTypeDAO = DAOUtilities.getAccountTypeDAO();
		AccountDAO accountDAO = DAOUtilities.getAccountDAO();

		// Get an AccountStatus
		int statusId = accountStatusDAO.insertAccountStatus(status);
		AccountStatus accountStatus = accountStatusDAO.getAccountStatusById(statusId);

		// Get an AccountType
		int typeId = accountTypeDAO.insertAccountType(type);
		AccountType accountType = accountTypeDAO.getAccountTypeById(typeId);

		// Get an Account
		int accountId = accountDAO.insertAccount(amount, accountType, accountStatus);
		Account account = accountDAO.getAccountById(accountId);

		// verify data
		assertEquals(accountId, account.getId());
		assertEquals(amount, account.getAmount(), 0.0001);
		assertEquals(status, account.getStatus().getStatus());
		assertEquals(type, account.getType().getType());

	}

	@Test
	public void testUpdate() throws Exception {
		// The data for our Account
		double amount = 12.3;
		String type = "Savings";
		String status = "Open";

		// Get the DAOs
		AccountStatusDAO accountStatusDAO = DAOUtilities.getAccountStatusDAO();
		AccountTypeDAO accountTypeDAO = DAOUtilities.getAccountTypeDAO();
		AccountDAO accountDAO = DAOUtilities.getAccountDAO();

		// Get an AccountStatus
		int statusId = accountStatusDAO.insertAccountStatus(status);
		AccountStatus accountStatus = accountStatusDAO.getAccountStatusById(statusId);

		// Get an AccountType
		int typeId = accountTypeDAO.insertAccountType(type);
		AccountType accountType = accountTypeDAO.getAccountTypeById(typeId);

		// Get an Account
		int accountId = accountDAO.insertAccount(amount, accountType, accountStatus);
		Account account = accountDAO.getAccountById(accountId);

		// verify data
		assertEquals(accountId, account.getId());
		assertEquals(amount, account.getAmount(), 0.0001);
		assertEquals(status, account.getStatus().getStatus());
		assertEquals(type, account.getType().getType());

		// The updated data
		double newAmount = 32.3;
		String newType = "Checking";
		String newStatus = "Closed";

		// Get a new AccountStatus
		statusId = accountStatusDAO.insertAccountStatus(newStatus);
		AccountStatus newAccountStatus = accountStatusDAO.getAccountStatusById(statusId);

		// Get a new AccountType
		typeId = accountTypeDAO.insertAccountType(newType);
		AccountType newAccountType = accountTypeDAO.getAccountTypeById(typeId);

		// Update account
		accountDAO.updateAccount(account, newAmount, newAccountType, newAccountStatus);
		account = accountDAO.getAccountById(account.getId());

		// verify data
		assertEquals(newAmount, account.getAmount(), 0.0001);
		assertEquals(newStatus, account.getStatus().getStatus());
		assertEquals(newType, account.getType().getType());

	}

	@Test
	public void testGetAllAccounts() throws Exception {

		// Get the DAOs
		AccountStatusDAO accountStatusDAO = DAOUtilities.getAccountStatusDAO();
		AccountTypeDAO accountTypeDAO = DAOUtilities.getAccountTypeDAO();
		AccountDAO accountDAO = DAOUtilities.getAccountDAO();

		// Get an account status
		int id = accountStatusDAO.insertAccountStatus("Open");
		AccountStatus openStatus = accountStatusDAO.getAccountStatusById(id);

		// get an account status
		id = accountStatusDAO.insertAccountStatus("Closed");
		AccountStatus closedStatus = accountStatusDAO.getAccountStatusById(id);

		// get an account type
		id = accountTypeDAO.insertAccountType("Checking");
		AccountType checkingType = accountTypeDAO.getAccountTypeById(id);

		// get an account type
		id = accountTypeDAO.insertAccountType("Savings");
		AccountType savingsType = accountTypeDAO.getAccountTypeById(id);

		// The data for 3 accounts
		double amounts[] = { 1.1, 2.2, 3.3 };
		AccountStatus statuses[] = { openStatus, closedStatus, openStatus };
		AccountType types[] = { checkingType, savingsType, checkingType };

		// Fill this set of accounts
		HashSet<Account> accounts1 = new HashSet<Account>();

		for (int i = 0; i < 3; i++) {
			id = accountDAO.insertAccount(amounts[i], types[i], statuses[i]);
			accounts1.add(accountDAO.getAccountById(id));
		}

		// Retrieve the accounts again via getAllAccounts
		ArrayList<Account> accounts2 = accountDAO.getAllAccounts();

		// Convert accounts2 to set for comparison purposes
		HashSet<Account> accounts3 = new HashSet<Account>();
		accounts3.addAll(accounts2);

		// compare
		assertTrue(accounts1.equals(accounts3));
		assertEquals(accounts1.size(), 3);
	}

	@Test
	public void testGetAccountsByStatus() throws Exception {

		// Get the DAOs
		AccountStatusDAO accountStatusDAO = DAOUtilities.getAccountStatusDAO();
		AccountTypeDAO accountTypeDAO = DAOUtilities.getAccountTypeDAO();
		AccountDAO accountDAO = DAOUtilities.getAccountDAO();

		// Get an account status
		int id = accountStatusDAO.insertAccountStatus("Open");
		AccountStatus openStatus = accountStatusDAO.getAccountStatusById(id);

		// get an account status
		id = accountStatusDAO.insertAccountStatus("Closed");
		AccountStatus closedStatus = accountStatusDAO.getAccountStatusById(id);

		// get an account type
		id = accountTypeDAO.insertAccountType("Checking");
		AccountType checkingType = accountTypeDAO.getAccountTypeById(id);

		// get an account type
		id = accountTypeDAO.insertAccountType("Savings");
		AccountType savingsType = accountTypeDAO.getAccountTypeById(id);

		// The data for 3 accounts
		double amounts[] = { 1.1, 2.2, 3.3 };
		AccountStatus statuses[] = { openStatus, closedStatus, openStatus };
		AccountType types[] = { checkingType, savingsType, checkingType };

		// Fill this with accounts with status = "Open"
		HashSet<Account> accounts1 = new HashSet<Account>();

		for (int i = 0; i < 3; i++) {
			id = accountDAO.insertAccount(amounts[i], types[i], statuses[i]);
			if (statuses[i].getStatus().equals("Open"))
				accounts1.add(accountDAO.getAccountById(id));
		}

		// Retrieve the accounts with status Open
		ArrayList<Account> accounts2 = accountDAO.getAccountsByStatus(openStatus);

		// Convert accounts2 to set for comparison purposes
		HashSet<Account> accounts3 = new HashSet<Account>();
		accounts3.addAll(accounts2);

		// compare
		assertTrue(accounts1.equals(accounts3));
		assertEquals(accounts1.size(), 2);
	}

	@Test
	public void testAddAmount() throws Exception {
		// The data for our Account
		double amount = 12.3;
		String type = "Savings";
		String status = "Open";

		// Get the DAOs
		AccountStatusDAO accountStatusDAO = DAOUtilities.getAccountStatusDAO();
		AccountTypeDAO accountTypeDAO = DAOUtilities.getAccountTypeDAO();
		AccountDAO accountDAO = DAOUtilities.getAccountDAO();

		// Get an AccountStatus
		int statusId = accountStatusDAO.insertAccountStatus(status);
		AccountStatus accountStatus = accountStatusDAO.getAccountStatusById(statusId);

		// Get an AccountType
		int typeId = accountTypeDAO.insertAccountType(type);
		AccountType accountType = accountTypeDAO.getAccountTypeById(typeId);

		// Get an Account
		int accountId = accountDAO.insertAccount(amount, accountType, accountStatus);
		Account account = accountDAO.getAccountById(accountId);

		// verify data
		assertEquals(accountId, account.getId());
		assertEquals(amount, account.getAmount(), 0.0001);
		assertEquals(status, account.getStatus().getStatus());
		assertEquals(type, account.getType().getType());

		// The amount to add
		double deltaAmount = 32.3;
		
		// add amount
		accountDAO.addAmount(account, deltaAmount);
		account = accountDAO.getAccountById(account.getId());

		// verify data
		assertEquals(amount + deltaAmount, account.getAmount(), 0.0001);

	}

}
