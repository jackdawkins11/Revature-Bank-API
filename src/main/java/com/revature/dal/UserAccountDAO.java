/**
 * 
 */
package com.revature.dal;

import java.sql.SQLException;
import java.util.ArrayList;

import com.revature.exceptions.CouldNotPopulateException;
import com.revature.exceptions.NoSuchAccountException;
import com.revature.model.Account;
import com.revature.model.User;

/**
 * @author John Dawkins jackdawkins1298@outlook.com
 *
 */
public interface UserAccountDAO {

	/**
	 * 
	 * @param user
	 * @param account
	 * @throws SQLException
	 */
	void insertUserAccountRelationship( User user,
			Account account ) throws SQLException;
	
	/**
	 * 
	 * @param user
	 * @return
	 * @throws SQLException
	 */
	ArrayList<Account> getAccountsByUser( User user ) throws SQLException;

	/**
	 * 
	 * @param account
	 * @return
	 * @throws SQLException
	 */
	User getAccountOwner(Account account) throws SQLException;
}
