/**
 * 
 */
package com.revature.dal;

import java.sql.SQLException;
import java.util.ArrayList;

import com.revature.exceptions.NoSuchAccountTypeException;
import com.revature.model.AccountType;

/**
 * @author John Dawkins jackdawkins1298@outlook.com
 *
 */
public interface AccountTypeDAO {

	/**
	 * 
	 * @param type
	 * @return
	 * @throws SQLException
	 */
	int insertAccountType(String type) throws SQLException;
	
	/**
	 * 
	 * @param id
	 * @return
	 * @throws SQLException
	 * @throws NoSuchAccountTypeException
	 */
	AccountType getAccountTypeById(int id) throws SQLException, NoSuchAccountTypeException;
	
	/**
	 * 
	 * @return list of all AccountType
	 * @throws SQLException
	 */
	ArrayList< AccountType > getAllAccountTypes() throws SQLException;
	
	/**
	 * 
	 * @param type
	 * @return
	 * @throws SQLException
	 */
	boolean accountTypeExists(String type) throws SQLException;
}
