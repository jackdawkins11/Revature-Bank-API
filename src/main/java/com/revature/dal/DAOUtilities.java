/**
 * 
 */
package com.revature.dal;

/**
 * @author John Dawkins jackdawkins1298@outlook.com
 *
 */
public class DAOUtilities {
	
	/**
	 * 
	 * @return AccountDAO used to access Accounts table
	 */
	public static AccountDAO getAccountDAO() {
		return new AccountDAOPostgresImpl();
	}
	
	/**
	 * 
	 * @return AccountStatusDAO used to access AccountStatus table
	 */
	public static AccountStatusDAO getAccountStatusDAO() {
		return new AccountStatusDAOPostgresImpl();
	}
	
	/**
	 * 
	 * @return 
	 */
	public static AccountTypeDAO getAccountTypeDAO() {
		return new AccountTypeDAOPostgresImpl();
	}
	
	/**
	 * 
	 * @return
	 */
	public static RoleDAO getRoleDAO() {
		return new RoleDAOPostgresImpl();
	}
	
	/**
	 * 
	 * @return
	 */
	public static UserAccountDAO getUserAccountDAO() {
		return new UserAccountDAOPostgresImpl();
	}
	
	/**
	 * 
	 * @return
	 */
	public static UserDAO getUserDAO() {
		return new UserDAOPostgresImpl();
	}
	
}
