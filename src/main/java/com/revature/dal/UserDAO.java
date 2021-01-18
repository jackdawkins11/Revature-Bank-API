/**
 * 
 */
package com.revature.dal;

import java.sql.SQLException;
import java.util.ArrayList;

import com.revature.exceptions.CouldNotPopulateException;
import com.revature.exceptions.InvalidCredentialsException;
import com.revature.exceptions.NoSuchUserException;
import com.revature.model.Role;
import com.revature.model.User;

/**
 * @author John Dawkins jackdawkins1298@outlook.com
 *
 */
public interface UserDAO {

	/**
	 * 
	 * @param username of the user
	 * @param password of the user
	 * @param firstName of the user
	 * @param lastName of the user
	 * @param email of the user
	 * @param role of the user
	 * @return id of the new User
	 * @throws SQLException error accessing the database
	 */
	int insertUser(String username,
			String password,
			String firstName,
			String lastName,
			String email,
			Role role ) throws SQLException;
	/**
	 * 
	 * @param user
	 * @param username
	 * @param password
	 * @param firstName
	 * @param lastName
	 * @param email
	 * @param role
	 * @throws SQLException
	 */
	void updateUser( User user,
			String username,
			String password,
			String firstName,
			String lastName,
			String email,
			Role role ) throws SQLException;
	
	/**
	 * 
	 * @param username
	 * @return
	 * @throws SQLException
	 */
	boolean usernameExists(String username) throws SQLException;
	
	/**
	 * 
	 * @param email
	 * @return
	 * @throws SQLException
	 */
	boolean emailExists(String email) throws SQLException;

	/**
	 * 
	 * @param username
	 * @param password
	 * @return
	 * @throws InvalidCredentialsException
	 * @throws SQLException
	 */
	User getUserByUsernameAndPassword(String username,
			String password )
			throws InvalidCredentialsException, SQLException;
	
	/**
	 * 
	 * @param id
	 * @return
	 * @throws SQLException
	 * @throws NoSuchUserException
	 */
	User getUserById(int id) throws SQLException, NoSuchUserException;
	
	/**
	 * 
	 * @return
	 * @throws SQLException
	 */
	ArrayList< User > getAllUsers() throws SQLException;
}
