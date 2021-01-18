/**
 * 
 */
package com.revature.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import com.revature.dal.AccountStatusDAO;
import com.revature.dal.AccountTypeDAO;
import com.revature.dal.ConnectionHandler;
import com.revature.dal.DAOUtilities;
import com.revature.dal.RoleDAO;
import com.revature.dal.UserAccountDAO;
import com.revature.dal.UserDAO;
import com.revature.exceptions.InvalidAccountParamsException;
import com.revature.exceptions.NotAuthorizedException;
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
@TestInstance(Lifecycle.PER_CLASS)
public class TestAccountService {

	User user = null;
	User employee = null;
	User admin = null;

	AccountType checking = null;
	AccountType savings = null;

	AccountStatus open = null;
	AccountStatus closed = null;

	@BeforeAll
	static void setTesting() {
		ConnectionHandler.testing = true;
	}
	
	@BeforeEach
	void setUp() throws Exception {
		// Clear database
		assertTrue(DatabaseClearer.clearTables());

		// Get needed DAOS
		RoleDAO roleDAO = DAOUtilities.getRoleDAO();
		UserDAO userDAO = DAOUtilities.getUserDAO();
		AccountTypeDAO accountTypeDAO = DAOUtilities.getAccountTypeDAO();
		AccountStatusDAO accountStatusDAO = DAOUtilities.getAccountStatusDAO();

		// Insert and get standard role
		int id = roleDAO.insertRole("Standard");
		Role standardRole = roleDAO.getRoleById(id);

		// Insert and get employee role
		id = roleDAO.insertRole("Employee");
		Role employeeRole = roleDAO.getRoleById(id);

		// Insert and get admin role
		id = roleDAO.insertRole("Admin");
		Role adminRole = roleDAO.getRoleById(id);

		// Insert and get user
		id = userDAO.insertUser("user1", "pass", "Fake", "Fake", "email1", standardRole);
		user = userDAO.getUserById(id);

		// Insert and get employee
		id = userDAO.insertUser("user2", "pass", "Fake", "Fake", "email2", employeeRole);
		employee = userDAO.getUserById(id);

		// Insert and get admin
		id = userDAO.insertUser("user3", "pass", "Fake", "Fake", "email3", adminRole);
		admin = userDAO.getUserById(id);

		// Insert and get checking
		id = accountTypeDAO.insertAccountType("Checking");
		checking = accountTypeDAO.getAccountTypeById(id);

		// Insert and get savings
		id = accountTypeDAO.insertAccountType("Savings");
		savings = accountTypeDAO.getAccountTypeById(id);

		// Insert and get open
		id = accountStatusDAO.insertAccountStatus("Open");
		open = accountStatusDAO.getAccountStatusById(id);

		// Insert and get closed
		id = accountStatusDAO.insertAccountStatus("Closed");
		closed = accountStatusDAO.getAccountStatusById(id);
	}

	@Test
	void testInsertAccount1() throws Exception {
		// The amount in the account
		double amount = 1000.1;

		// Call insertAccount, getting an Account
		Account account = AccountService.insertAccount(user, amount, user, checking, open);

		// Get UserAccountDAO
		UserAccountDAO userAccountDAO = DAOUtilities.getUserAccountDAO();

		// Get the account owner
		User accountOwner = userAccountDAO.getAccountOwner(account);

		// Verify data
		assertEquals(account.getAmount(), amount, 0.00001);
		assertTrue(account.getType().equals(checking));
		assertTrue(account.getStatus().equals(open));
		assertTrue(accountOwner.equals(user));
	}

	@Test
	void testInsertAccount2() throws Exception {

		// Regular users are not authorized to create an account for other users
		assertThrows(NotAuthorizedException.class, () -> {
			AccountService.insertAccount(user, 10.1, employee, checking, open);
		});

		// Employees are authorized to create an account for any user
		assertDoesNotThrow(() -> {
			AccountService.insertAccount(employee, 10.1, user, checking, open);
		});

		// Accounts can not have a negative amount
		assertThrows(InvalidAccountParamsException.class, () -> {
			AccountService.insertAccount(user, -0.1, user, checking, open);
		});
	}

	@Test
	void testUpdateAccount() throws Exception {
		// The amount in the fake account
		double amount = 100.1;

		// Insert and get new Account
		Account account = AccountService.insertAccount(admin, amount, employee, savings, closed);

		// The updated amount
		double newAmount = 1000.1;

		// Update account
		Account updatedAccount = AccountService.updateAccount(admin, account, newAmount, checking, open);

		// Verify
		assertEquals(account.getId(), updatedAccount.getId());
		assertEquals(updatedAccount.getAmount(), newAmount, 0.0001);
		assertTrue(updatedAccount.getStatus().equals(open));
		assertTrue(updatedAccount.getType().equals(checking));
	}

	@Test
	void testUpdateAccount2() throws Exception {
		// Insert an account
		Account account = AccountService.insertAccount(admin, 100.1, user, savings, closed);

		// Admin can call update
		AccountService.updateAccount(admin, account, 10.1, checking, closed);

		// Employee can't call update
		assertThrows(NotAuthorizedException.class, () -> {
			AccountService.updateAccount(employee, account, 10.1, checking, closed);
		});

		// User can't call update
		assertThrows(NotAuthorizedException.class, () -> {
			AccountService.updateAccount(user, account, 10.1, checking, closed);
		});
	}

	@Test
	void testGetAccountById() throws Exception {
		// The amount in the fake account
		double amount = 1000.1;

		// Insert and get new Account
		Account account = AccountService.insertAccount(admin, amount, user, savings, open);

		// Get a new copy via getById
		Account accountCopy = AccountService.getAccountById(account.getId());

		// Verify
		assertTrue(account.equals(accountCopy));
	}

	@Test
	void testAuthorizeAndGetAccountById() throws Exception {
		// The amount in the fake account
		double amount = 1000.1;

		// Insert and get new Account
		Account account = AccountService.insertAccount(admin, amount, admin, savings, open);

		// Get a new copy via authorizeAndGetById
		Account accountCopy = AccountService.authorizeAndGetAccountById(admin, account.getId());

		// Verify
		assertTrue(account.equals(accountCopy));
	}

	@Test
	void testAuthorizeAndGetAccountByIdAuthorization() throws Exception {
		// The amount in the fake account
		double amount = 1000.1;

		// Insert and get new Account
		Account account = AccountService.insertAccount(admin, amount, admin, savings, open);

		// Ensure regular user is not authorized
		assertThrows(NotAuthorizedException.class, () -> {
			AccountService.authorizeAndGetAccountById(user, account.getId());
		});

		// Ensure employee is authorized
		assertDoesNotThrow(() -> {
			AccountService.authorizeAndGetAccountById(employee, account.getId());
		});

		// Ensure admin is authorized
		assertDoesNotThrow(() -> {
			AccountService.authorizeAndGetAccountById(admin, account.getId());
		});
	}

	@Test
	void testGetAllAccounts() throws Exception {
		// Create some accounts
		Account account1 = AccountService.insertAccount(user, 100.1, user, checking, open);
		Account account2 = AccountService.insertAccount(employee, 100.1, user, savings, closed);
		Account account3 = AccountService.insertAccount(employee, 100.1, user, checking, open);

		// Add them to a HashSet
		HashSet<Account> accountSet1 = new HashSet<Account>();
		accountSet1.add(account1);
		accountSet1.add(account2);
		accountSet1.add(account3);

		// Get another HashSet via getAllAccounts()
		HashSet<Account> accountSet2 = new HashSet<Account>();
		accountSet2.addAll(AccountService.getAllAccounts(employee));

		// Compare
		assertTrue(accountSet2.equals(accountSet1));
	}

	@Test
	void testGetAllAccountsAuthorization() throws Exception {
		// employee can get all accounts
		AccountService.getAllAccounts(employee);

		// admin can get all accounts
		AccountService.getAllAccounts(admin);

		// Users can NOT get all accounts
		assertThrows(NotAuthorizedException.class, () -> {
			AccountService.getAllAccounts(user);
		});
	}

	@Test
	void testGetAccountsByStatus() throws Exception {
		// Create some accounts
		Account account1 = AccountService.insertAccount(user, 100.1, user, checking, open);
		Account account2 = AccountService.insertAccount(employee, 100.1, user, savings, closed);
		Account account3 = AccountService.insertAccount(employee, 100.1, user, checking, open);

		// Add accounts with status="Open" to a hashset
		HashSet<Account> openAccountsSet = new HashSet<Account>();
		openAccountsSet.add(account1);
		openAccountsSet.add(account3);

		// Get another HashSet of accounts with status="Open" via getAccountsByStatus
		HashSet<Account> openAccountsSet2 = new HashSet<Account>();
		openAccountsSet2.addAll(AccountService.getAccountsByStatus(admin, open));

		// Verify
		assertTrue(openAccountsSet.equals(openAccountsSet2));
	}

	@Test
	void testGetAccountsByStatusAuthorization() throws Exception {
		// Regular users can't call getAccountsByStatus
		assertThrows(NotAuthorizedException.class, () -> {
			AccountService.getAccountsByStatus(user, open);
		});

		// Employees can call get accounts by status
		AccountService.getAccountsByStatus(employee, open);

		// Admin can call get accounts by status
		AccountService.getAccountsByStatus(admin, open);
	}

	@Test
	void testGetAccountsByUser() throws Exception {
		// Create some accounts
		Account account1 = AccountService.insertAccount(user, 100.1, user, checking, open);
		Account account2 = AccountService.insertAccount(employee, 100.1, user, savings, closed);
		Account account3 = AccountService.insertAccount(employee, 100.1, employee, checking, open);

		// Add accounts of 'user' to a hashset
		HashSet<Account> openAccountsSet = new HashSet<Account>();
		openAccountsSet.add(account1);
		openAccountsSet.add(account2);

		// Get another HashSet of accounts of 'user' via getAccountsByUser
		HashSet<Account> openAccountsSet2 = new HashSet<Account>();
		openAccountsSet2.addAll(AccountService.getAccountsByUser(user, user));

		// Verify
		assertTrue(openAccountsSet.equals(openAccountsSet2));
	}

	@Test
	void testGetAccountsByUserAuthorization() throws Exception {
		// Regular users can't get the accounts of another user
		assertThrows(NotAuthorizedException.class, () -> {
			AccountService.getAccountsByUser(user, employee);
		});

		// Regular users can get their own accounts
		AccountService.getAccountsByUser(user, user);

		// Employees can always call getAccountsByUser
		AccountService.getAccountsByUser(employee, user);

		// Admin can always call getAccountsByUser
		AccountService.getAccountsByUser(admin, user);
	}

	@Test
	void testDeposit() throws Exception {
		// Amount in account and amount to deposit
		double amount = 100.1;
		double deposit = 1.2;

		// Add an account
		Account account1 = AccountService.insertAccount(user, amount, user, checking, open);

		// Deposit to it
		AccountService.deposit(user, account1, deposit);

		// Refresh the account
		account1 = AccountService.getAccountById(account1.getId());

		// Verify
		assertEquals(account1.getAmount(), amount + deposit, 0.00001);
	}
	
	@Test
	void testDepositAuthorization() throws Exception {
		// Amount in account and amount to deposit
		double amount = 100.1;
		double deposit = 1.2;

		// Add an account
		Account account1 = AccountService.insertAccount(user, amount, user, checking, open);

		// Regular user can deposit into their own account
		AccountService.deposit(user, account1, deposit);
		
		//Employees can't deposit into another users account
		assertThrows(NotAuthorizedException.class, () -> {
			AccountService.withdraw(employee, account1, deposit);
		});
		
		//Admins can always deposit
		AccountService.withdraw(admin, account1, deposit);
	}

	@Test
	void testWithdraw() throws Exception {
		// Amount in account and amount to withdraw
		double amount = 100.1;
		double withdraw = 1.2;

		// Add an account
		Account account1 = AccountService.insertAccount(user, amount, user, checking, open);

		// Withdraw from it
		AccountService.withdraw(user, account1, withdraw);

		// Refresh the account
		account1 = AccountService.getAccountById(account1.getId());

		// Verify
		assertEquals(account1.getAmount(), amount - withdraw, 0.00001);
	}

	@Test
	void testWithdrawAuthorization() throws Exception {
		// Amount in account and amount to withdraw
		double amount = 100.1;
		double withdraw = 1.2;

		// Add an account
		Account account1 = AccountService.insertAccount(user, amount, user, checking, open);

		// Regular user can withdraw from their own account
		AccountService.withdraw(user, account1, withdraw);
		
		//Employees can't withdraw from another users account
		assertThrows(NotAuthorizedException.class, () -> {
			AccountService.withdraw(employee, account1, amount);
		});
		
		//Admins can always withdraw
		AccountService.withdraw(admin, account1, withdraw);
	}

	@Test
	void testTransfer() throws Exception {
		// Amount in account and amount to transfer
		double amount = 100.1;
		double transfer = 1.2;

		// Add 2 accounts
		Account account1 = AccountService.insertAccount(user, amount, user, checking, open);
		Account account2 = AccountService.insertAccount(employee, amount, admin, checking, open);

		// Transfer from 1 to 2
		AccountService.transfer(user, account1, account2, transfer);

		// Refresh the accounts
		account1 = AccountService.getAccountById(account1.getId());
		account2 = AccountService.getAccountById(account2.getId());

		// Verify
		assertEquals(account1.getAmount(), amount - transfer, 0.00001);
		assertEquals(account2.getAmount(), amount + transfer, 0.00001);
	}

	@Test
	void testTransferAuthorization() throws Exception {
		// Amount in account and amount to transfer
		double amount = 100.1;
		double transfer = 1.2;

		// Add 3 accounts
		Account account1 = AccountService.insertAccount(user, amount, user, checking, open);
		Account account2 = AccountService.insertAccount(employee, amount, employee, checking, open);
		Account account3 = AccountService.insertAccount(admin, amount, admin, checking, open);

		// Standard users can transfer from their own account
		AccountService.transfer(user, account1, account2, transfer);

		// Standard user can't transfer from another users account
		assertThrows(NotAuthorizedException.class, () -> {
			AccountService.transfer(user, account2, account3, transfer);
		});

		// Employees can transfer from their own account
		AccountService.transfer(employee, account2, account3, transfer);

		// Employees can't transfer from another users account
		assertThrows(NotAuthorizedException.class, () -> {
			AccountService.transfer(employee, account3, account2, transfer);
		});

		// Admins can always transfer
		AccountService.transfer(admin, account2, account3, transfer);
	}

}
