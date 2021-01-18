/**
 * 
 */
package com.revature.dal;

import java.sql.SQLException;
import java.util.ArrayList;

import com.revature.exceptions.CouldNotPopulateException;
import com.revature.exceptions.NoSuchAccountException;
import com.revature.model.Account;
import com.revature.model.AccountStatus;
import com.revature.model.AccountType;

/**
 * @author John Dawkins jackdawkins1298@outlook.com
 *
 */
public interface AccountDAO {

	/**
	 * 
	 * @param amount
	 * @param type
	 * @param status
	 * @return
	 * @throws SQLException
	 */
	int insertAccount(double amount,
			AccountType type,
			AccountStatus status ) throws SQLException;
	
	/**
	 * 
	 * @param account
	 * @param amount
	 * @param type
	 * @param status
	 * @throws SQLException
	 */
	void updateAccount(Account account,
			double amount,
			AccountType type,
			AccountStatus status ) throws SQLException;
	
	/**
	 * 
	 * @param id
	 * @return
	 * @throws SQLException
	 * @throws NoSuchAccountException
	 */
	Account getAccountById(int id)
		throws SQLException, NoSuchAccountException;
	
	/**
	 * 
	 * @return
	 * @throws SQLException
	 */
	ArrayList<Account> getAllAccounts() throws SQLException;
	
	/**
	 * 
	 * @param status
	 * @return
	 * @throws SQLException
	 */
	ArrayList<Account> getAccountsByStatus(AccountStatus status) throws SQLException;
	
	/**
	 * 
	 * @param account
	 * @param amount
	 * @throws SQLException
	 */
	void addAmount(Account account, double amount) throws SQLException;
}
