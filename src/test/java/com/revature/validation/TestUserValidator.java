/**
 * 
 */
package com.revature.validation;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.revature.dal.ConnectionHandler;
import com.revature.dal.DAOUtilities;
import com.revature.dal.UserDAO;
import com.revature.exceptions.InvalidUserParamsException;
import com.revature.exceptions.InvalidUserParamsReason;
import com.revature.utilities.DatabaseClearer;

/**
 * @author John Dawkins jackdawkins1298@outlook.com
 *
 */
public class TestUserValidator {

	@BeforeAll
	static void setTesting() {
		ConnectionHandler.testing = true;
	}
	
	@BeforeEach
	void setUp() throws Exception {
		// Clear database
		assertTrue(DatabaseClearer.clearTables());
	}

	@Test
	public void test1() {
		assertDoesNotThrow(() -> {
			UserValidator.validateNewUserParams("username", "passwordA1!", "Jack", "D", "jack@jack.com");
		});
	}

	@Test
	public void test2() {
		// Short username
		InvalidUserParamsException ex = assertThrows(InvalidUserParamsException.class, () -> {
			UserValidator.validateNewUserParams("user", "passwordA1!", "Jack", "D", "jack@jack.com");
		});

		// Check the exception
		assertEquals(ex.getReason(), InvalidUserParamsReason.USERNAMESTRING);

		// Weak password
		ex = assertThrows(InvalidUserParamsException.class, () -> {
			UserValidator.validateNewUserParams("username", "passwordA1", "Jack", "D", "jack@jack.com");
		});

		// Check the exception
		assertEquals(ex.getReason(), InvalidUserParamsReason.PASSWORDSTRING);

		// Weak password
		ex = assertThrows(InvalidUserParamsException.class, () -> {
			UserValidator.validateNewUserParams("username", "passwordA1!", "Jack1", "D", "jack@jack.com");
		});

		// Check the exception
		assertEquals(ex.getReason(), InvalidUserParamsReason.NAMESTRING);
	}


}
