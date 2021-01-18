/**
 * 
 */
package com.revature.services;

import java.sql.SQLException;
import java.util.ArrayList;

import com.revature.dal.DAOUtilities;
import com.revature.dal.UserDAO;
import com.revature.exceptions.InvalidCredentialsException;
import com.revature.exceptions.InvalidUserParamsException;
import com.revature.exceptions.NoSuchUserException;
import com.revature.exceptions.NotAuthorizedException;
import com.revature.model.Role;
import com.revature.model.User;
import com.revature.validation.UserValidator;

/**
 * @author John Dawkins jackdawkins1298@outlook.com
 *
 */
public class UserService {

	public static User register(User user, String username, String password, String firstName, String lastName,
			String email, Role role) throws SQLException, NotAuthorizedException, InvalidUserParamsException {
		// Check if user is authorized to create a new user
		if (!user.getRole().getRole().equals("Admin")) {
			throw new NotAuthorizedException();
		}

		// Check if the parameters are valid
		UserValidator.validateNewUserParams(username, password, firstName, lastName, email);

		// Get a DAO
		UserDAO dao = DAOUtilities.getUserDAO();

		// Insert the new user
		int id = dao.insertUser(username, password, firstName, lastName, email, role);

		try {
			return dao.getUserById(id);
		} catch (NoSuchUserException e) {
			// We know the ID is valid, so if we got a NoSuchUserException, the problem is
			// somewhere else,
			// most likely jdbc, so just throw an SQLException as it is a more accurate
			// description of the problem.
			// It doesn't make sense to throw a NoSuchUserException in this function.
			throw new SQLException(e);
		}
	}

	public static User update(User user, User userToUpdate, String username, String password, String firstName,
			String lastName, String email, Role role)
			throws SQLException, NotAuthorizedException, InvalidUserParamsException {
		// Check if the user is authorized to update userToUpdate
		// Admins are always allowed
		if (!user.getRole().getRole().equals("Admin")) {
			// Non-admins can only update their own account
			if (!user.equals(userToUpdate)) {
				throw new NotAuthorizedException();
			}
			// Non-admins can not escalate privileges
			if (!user.getRole().equals(role)) {
				throw new NotAuthorizedException();
			}
		}

		// Check if the parameters are valid
		UserValidator.validateUpdateUserParams(userToUpdate, username, password, firstName, lastName, email);

		// Get a DAO
		UserDAO dao = DAOUtilities.getUserDAO();

		// Update the user
		dao.updateUser(userToUpdate, username, password, firstName, lastName, email, role);

		try {
			return dao.getUserById(userToUpdate.getId());
		} catch (NoSuchUserException e) {
			// We know the ID is valid, so if we got a NoSuchUserException, the problem is
			// somewhere else,
			// most likely jdbc, so just throw an SQLException as it is a more accurate
			// description of the problem.
			// It doesn't make sense to throw a NoSuchUserException in this function.
			throw new SQLException(e);
		}
	}

	public static User authorize(String username, String password) throws SQLException, InvalidCredentialsException {
		// Get a dao
		UserDAO dao = DAOUtilities.getUserDAO();

		// Return the user with the given username and password or throw an exception
		return dao.getUserByUsernameAndPassword(username, password);
	}

	public static User getUserById(int id) throws SQLException, NoSuchUserException {
		// Get a dao
		UserDAO dao = DAOUtilities.getUserDAO();

		// Return the user with the given id or throw an exception
		return dao.getUserById(id);
	}

	public static User authorizeAndGetUserById(User user, int id)
			throws NotAuthorizedException, SQLException, NoSuchUserException {
		// Check if the user is authorized
		// Admins and employees are authorized
		if (!(user.getRole().getRole().equals("Admin") || user.getRole().getRole().equals("Employee"))) {
			// Standard users can get their own account
			if (user.getId() != id) {
				throw new NotAuthorizedException();
			}
		}

		// Get a dao
		UserDAO dao = DAOUtilities.getUserDAO();

		// Return the user with the given id or throw an exception
		return dao.getUserById(id);
	}

	public static ArrayList<User> getAllUsers(User user) throws NotAuthorizedException, SQLException {
		// Check if user is authorized
		if (!(user.getRole().getRole().equals("Admin") || user.getRole().getRole().equals("Employee"))) {
			throw new NotAuthorizedException();
		}
		// Get a dao
		UserDAO dao = DAOUtilities.getUserDAO();

		// Return the user with the given id or throw an exception
		return dao.getAllUsers();
	}
}
