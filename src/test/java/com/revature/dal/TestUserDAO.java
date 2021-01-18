/**
 * 
 */
package com.revature.dal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.revature.exceptions.InvalidCredentialsException;
import com.revature.exceptions.NoSuchUserException;
import com.revature.model.Role;
import com.revature.model.User;
import com.revature.utilities.DatabaseClearer;

/**
 * @author John Dawkins jackdawkins1298@outlook.com
 *
 */
public class TestUserDAO {

	@BeforeAll
	static void setTesting() {
		ConnectionHandler.testing = true;
	}
	
	@BeforeEach
	public void setUp() {
		assertTrue(DatabaseClearer.clearTables());
	}

	@Test
	public void testInsert() throws Exception {
		// The Users data
		String username = "jack";
		String password = "pass";
		String firstName = "john";
		String lastName = "dawkins";
		String email = "jackdawkins1298@outlook.com";
		String roleStr = "Admin";

		// Get DAOs
		RoleDAO roleDAO = DAOUtilities.getRoleDAO();
		UserDAO userDAO = DAOUtilities.getUserDAO();

		// Do insert & do getById for role
		int roleId = roleDAO.insertRole(roleStr);
		Role role = roleDAO.getRoleById(roleId);

		// Do insert and go getById for user
		int userId = userDAO.insertUser(username, password, firstName, lastName, email, role);
		User user = userDAO.getUserById(userId);

		// verify the created User
		assertEquals(user.getUsername(), username);
		assertEquals(user.getPassword(), password);
		assertEquals(user.getFirstName(), firstName);
		assertEquals(user.getLastName(), lastName);
		assertEquals(user.getEmail(), email);
		assertTrue(user.getRole().equals(role));
	}

	@Test
	public void testUpdate() throws Exception {
		// The original data
		String username = "jack";
		String password = "pass";
		String firstName = "john";
		String lastName = "dawkins";
		String email = "jackdawkins1298@outlook.com";
		String roleStr = "Admin";

		// Get DAOs
		RoleDAO roleDAO = DAOUtilities.getRoleDAO();
		UserDAO userDAO = DAOUtilities.getUserDAO();

		// Do insert & do getById for role
		int roleId = roleDAO.insertRole(roleStr);
		Role role = roleDAO.getRoleById(roleId);

		// Do insert and doGetById for user
		int userId = userDAO.insertUser(username, password, firstName, lastName, email, role);
		User user = userDAO.getUserById(userId);

		// The new data
		String username2 = "Jake";
		String password2 = "newpass";
		String firstName2 = "Jacob";
		String lastName2 = "McCregor";
		String email2 = "jacob1298@outlook.com";

		userDAO.updateUser(user, username2, password2, firstName2, lastName2, email2, role);

		user = userDAO.getUserById(user.getId());

		// make sure it is new data
		assertEquals(user.getUsername(), username2);
		assertEquals(user.getPassword(), password2);
		assertEquals(user.getFirstName(), firstName2);
		assertEquals(user.getLastName(), lastName2);
		assertEquals(user.getEmail(), email2);
		assertTrue(user.getRole().equals(role));

	}

	@Test
	public void test3() throws Exception {
		// The User data
		String[] username = { "Jack", "Jake", "Jerry" };
		String[] password = { "pass1", "badpass", "passw$$" };
		String[] firstName = { "John", "Jacob", "Jerry" };
		String[] lastName = { "Dawkins", "McCregor", "Sander" };
		String[] email = { "jackdawkins1298@outlook.com", "jacob@gmail.com", "jj12p@outlook.com" };
		String[] roleStr = { "Admin", "Employee", "Standard" };

		// Get DAOs
		RoleDAO roleDAO = DAOUtilities.getRoleDAO();
		UserDAO userDAO = DAOUtilities.getUserDAO();

		// Insert the data above. Add each created User and Role to the below arrays
		ArrayList<User> users = new ArrayList<User>();
		ArrayList<Role> roles = new ArrayList<Role>();

		for (int i = 0; i < email.length; i++) {
			int roleId = roleDAO.insertRole(roleStr[i]);

			roles.add(roleDAO.getRoleById(roleId));

			int userId = userDAO.insertUser(username[i], password[i], firstName[i], lastName[i], email[i],
					roles.get(i));

			users.add(userDAO.getUserById(userId));
		}

		// Check usernameExists and emailExists
		for (int i = 0; i < email.length; i++) {
			assertTrue(userDAO.emailExists(email[i]));
			assertFalse(userDAO.emailExists(email[i] + "K"));

			assertTrue(userDAO.usernameExists(username[i]));
			assertFalse(userDAO.usernameExists(username[i] + "KH"));
		}
	}

	@Test
	public void test4() throws Exception {
		// The User data
		String[] username = { "Jack", "Jake", "Jerry" };
		String[] password = { "pass1", "badpass", "passw$$" };
		String[] firstName = { "John", "Jacob", "Jerry" };
		String[] lastName = { "Dawkins", "McCregor", "Sander" };
		String[] email = { "jackdawkins1298@outlook.com", "jacob@gmail.com", "jj12p@outlook.com" };
		String[] roleStr = { "Admin", "Employee", "Standard" };

		// Get DAOs
		RoleDAO roleDAO = DAOUtilities.getRoleDAO();
		UserDAO userDAO = DAOUtilities.getUserDAO();

		// Insert the data above. Add each created User and Role to the below arrays
		ArrayList<User> users = new ArrayList<User>();
		ArrayList<Role> roles = new ArrayList<Role>();

		for (int i = 0; i < email.length; i++) {
			int roleId = roleDAO.insertRole(roleStr[i]);

			roles.add(roleDAO.getRoleById(roleId));

			int userId = userDAO.insertUser(username[i], password[i], firstName[i], lastName[i], email[i],
					roles.get(i));

			users.add(userDAO.getUserById(userId));
		}

		// Check getUserByUsernameAndPassword
		for (int i = 0; i < email.length; i++) {
			User user = userDAO.getUserByUsernameAndPassword(username[i], password[i]);
			assertTrue(user.equals(users.get(i)));
		}
	}

	public void testInvalidCredentialsExceptionAux() throws Exception {
		// The User data
		String[] username = { "Jack", "Jake", "Jerry" };
		String[] password = { "pass1", "badpass", "passw$$" };
		String[] firstName = { "John", "Jacob", "Jerry" };
		String[] lastName = { "Dawkins", "McCregor", "Sander" };
		String[] email = { "jackdawkins1298@outlook.com", "jacob@gmail.com", "jj12p@outlook.com" };
		String[] roleStr = { "Admin", "Employee", "Standard" };

		// Get DAOs
		RoleDAO roleDAO = DAOUtilities.getRoleDAO();
		UserDAO userDAO = DAOUtilities.getUserDAO();

		// Insert the data above. Add each created User and Role to the below arrays
		ArrayList<User> users = new ArrayList<User>();
		ArrayList<Role> roles = new ArrayList<Role>();

		for (int i = 0; i < email.length; i++) {
			int roleId = roleDAO.insertRole(roleStr[i]);

			roles.add(roleDAO.getRoleById(roleId));

			int userId = userDAO.insertUser(username[i], password[i], firstName[i], lastName[i], email[i],
					roles.get(i));

			users.add(userDAO.getUserById(userId));
		}

		// Check getUserByUsernameAndPassword throws InvalidCredentialsException
		User user = userDAO.getUserByUsernameAndPassword(username[0], password[1]);
	}
	
	@Test
	public void testInvalidCredentialsException() {
		assertThrows(InvalidCredentialsException.class, this::testInvalidCredentialsExceptionAux);
	}

	@Test
	public void test6() throws Exception {
		// The User data
		String[] username = { "Jack", "Jake", "Jerry" };
		String[] password = { "pass1", "badpass", "passw$$" };
		String[] firstName = { "John", "Jacob", "Jerry" };
		String[] lastName = { "Dawkins", "McCregor", "Sander" };
		String[] email = { "jackdawkins1298@outlook.com", "jacob@gmail.com", "jj12p@outlook.com" };
		String[] roleStr = { "Admin", "Employee", "Standard" };

		// Get DAOs
		RoleDAO roleDAO = DAOUtilities.getRoleDAO();
		UserDAO userDAO = DAOUtilities.getUserDAO();

		// Insert the data above. Add each created User and Role to the below arrays
		ArrayList<User> users = new ArrayList<User>();
		ArrayList<Role> roles = new ArrayList<Role>();

		for (int i = 0; i < email.length; i++) {
			int roleId = roleDAO.insertRole(roleStr[i]);

			roles.add(roleDAO.getRoleById(roleId));

			int userId = userDAO.insertUser(username[i], password[i], firstName[i], lastName[i], email[i],
					roles.get(i));

			users.add(userDAO.getUserById(userId));
		}

		// Get another array of Users with getAllRoles()
		ArrayList<User> users2 = userDAO.getAllUsers();

		// Convert them to sets for comparison purposes
		Set<User> userSet1 = new HashSet<User>();
		userSet1.addAll(users);

		Set<User> userSet2 = new HashSet<User>();
		userSet2.addAll(users2);

		// Compare
		assertTrue(userSet1.equals(userSet2));

	}

	public void testGetByIdExceptionAux() throws Exception {
		UserDAO dao = DAOUtilities.getUserDAO();

		dao.getUserById(1);
	}
	
	@Test
	public void testGetByIdException() {
		assertThrows(NoSuchUserException.class, this::testGetByIdExceptionAux);
	}

}
