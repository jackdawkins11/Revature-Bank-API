/**
 * 
 */
package com.revature.validation;

import java.sql.SQLException;

import com.revature.dal.DAOUtilities;
import com.revature.dal.UserDAO;
import com.revature.exceptions.InvalidUserParamsException;
import com.revature.exceptions.InvalidUserParamsReason;
import com.revature.model.User;

/**
 * @author John Dawkins jackdawkins1298@outlook.com
 *
 */
public class UserValidator {

	private static void regexValidate(String username, String password, String firstName, String lastName, String email)
			throws InvalidUserParamsException {
		// Validate the strings using regex'
		if (username.length() < 8 || username.length() > 100) {
			throw new InvalidUserParamsException(InvalidUserParamsReason.USERNAMESTRING);
		}
		if (!password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=]).{8,100}$")) {
			throw new InvalidUserParamsException(InvalidUserParamsReason.PASSWORDSTRING);
		}
		if (!firstName.matches("^[A-Za-z-]*")) {
			throw new InvalidUserParamsException(InvalidUserParamsReason.NAMESTRING);
		}
		if (!lastName.matches("^[A-Za-z-]*")) {
			throw new InvalidUserParamsException(InvalidUserParamsReason.NAMESTRING);
		}
		// TODO test for valid email
	}

	public static void validateNewUserParams(String username, String password, String firstName, String lastName,
			String email) throws InvalidUserParamsException, SQLException {
		// Check if the parameters pass the regex tests
		regexValidate(username, password, firstName, lastName, email);

		// Get a dao
		UserDAO dao = DAOUtilities.getUserDAO();

		// Check if the username is taken
		if (dao.usernameExists(username)) {
			throw new InvalidUserParamsException(InvalidUserParamsReason.USERNAMETAKEN);
		}

		// Check if the email is taken
		if (dao.emailExists(email)) {
			throw new InvalidUserParamsException(InvalidUserParamsReason.EMAILTAKEN);
		}
	}

	public static void validateUpdateUserParams(User user, String username, String password, String firstName,
			String lastName, String email) throws InvalidUserParamsException, SQLException {
		// Check if the parameters pass the regex tests
		regexValidate(username, password, firstName, lastName, email);

		// Get a dao
		UserDAO dao = DAOUtilities.getUserDAO();

		// If the username is being updated, check if it is taken
		if (!username.equals(user.getUsername())) {
			if (dao.usernameExists(username)) {
				throw new InvalidUserParamsException(InvalidUserParamsReason.USERNAMETAKEN);
			}
		}
		
		// If the email is new, check if it is taken
		if (!email.equals(user.getEmail())) {
			if (dao.emailExists(email)) {
				throw new InvalidUserParamsException(InvalidUserParamsReason.EMAILTAKEN);
			}
		}
	}
}
