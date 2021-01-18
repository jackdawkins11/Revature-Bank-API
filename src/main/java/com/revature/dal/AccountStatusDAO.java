/**
 * 
 */
package com.revature.dal;

import java.sql.SQLException;
import java.util.ArrayList;

import com.revature.exceptions.NoSuchAccountStatusException;
import com.revature.model.AccountStatus;

/**
 * @author John Dawkins jackdawkins1298@outlook.com
 *
 */
public interface AccountStatusDAO {

	/**
	 * 
	 * @param status
	 * @return the id of the new AccountStatus
	 * @throws SQLException
	 */
	int insertAccountStatus(String status) throws SQLException;
	
	/**
	 * 
	 * @param id
	 * @return the AccountStatus with the given id
	 * @throws SQLException
	 * @throws NoSuchAccountStatusException
	 */
	AccountStatus getAccountStatusById(int id) throws SQLException, NoSuchAccountStatusException;
	
	/**
	 * 
	 * @return list of all AccountStatus
	 * @throws SQLException
	 */
	ArrayList<AccountStatus> getAllAccountStatus() throws SQLException;
	
	/**
	 * 
	 * @param status
	 * @return
	 * @throws SQLException
	 */
	boolean accountStatusExists(String status) throws SQLException;
}
