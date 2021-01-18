/**
 * 
 */
package com.revature.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import com.revature.dal.ConnectionHandler;
import com.revature.dal.DAOUtilities;
import com.revature.dal.RoleDAO;
import com.revature.dal.UserDAO;
import com.revature.exceptions.InvalidCredentialsException;
import com.revature.exceptions.NotAuthorizedException;
import com.revature.model.Role;
import com.revature.model.User;
import com.revature.utilities.DatabaseClearer;

/**
 * @author John Dawkins jackdawkins1298@outlook.com
 *
 */
@TestInstance(Lifecycle.PER_CLASS)
class TestUserService {

	User adminUser = null;
	User employeeUser = null;
	User standardUser = null;

	Role adminRole = null;
	Role employeeRole = null;
	Role standardRole = null;

	Role getRoleInListWithGivenRole(ArrayList<Role> roles, String roleStr) {
		for (Role role : roles) {
			if (role.getRole().equals(roleStr))
				return role;
		}
		return null;
	}

	@BeforeAll
	static void setTesting() {
		ConnectionHandler.testing = true;
	}
	
	@BeforeEach
	void setUp() throws Exception {
		// Clear database
		DatabaseClearer.clearTables();

		// Get DAOS
		UserDAO userDAO = DAOUtilities.getUserDAO();
		RoleDAO roleDAO = DAOUtilities.getRoleDAO();

		// Insert three roles
		roleDAO.insertRole("Admin");
		roleDAO.insertRole("Employee");
		roleDAO.insertRole("Standard");

		// Get all roles
		ArrayList<Role> roles = roleDAO.getAllRoles();

		// Set the Role variables
		adminRole = getRoleInListWithGivenRole(roles, "Admin");
		employeeRole = getRoleInListWithGivenRole(roles, "Employee");
		standardRole = getRoleInListWithGivenRole(roles, "Standard");

		// Insert and get the admin user
		int id = userDAO.insertUser("user1", "pass", "Fake", "Name", "fake@email.com", adminRole);
		adminUser = userDAO.getUserById(id);

		// Insert and get the employee user
		id = userDAO.insertUser("user2", "pass", "Fake", "Name", "fake2@email.com", employeeRole);
		employeeUser = userDAO.getUserById(id);

		// Insert and get the standard user
		id = userDAO.insertUser("user3", "pass", "Fake", "Name", "fake3@email.com", standardRole);
		standardUser = userDAO.getUserById(id);
	}

	void testRegister1Aux(User user) throws Exception {
		UserService.register(user, "username4", "passwordA1!", "Fake", "Name", "fake4@email.com", employeeRole);
	}

	@Test
	void testRegister1() {
		assertThrows(NotAuthorizedException.class, () -> testRegister1Aux(employeeUser));
		assertThrows(NotAuthorizedException.class, () -> testRegister1Aux(standardUser));
		assertDoesNotThrow(() -> testRegister1Aux(adminUser));
	}

	@Test
	void testUpdate() throws Exception {
		// Get a user for use later in this method
		User userToUpdate = UserService.register(adminUser, "username5", "passwordA1!", "Fake", "Name",
				"fake5@email.com", standardRole);

		// Make sure non-admin users can't elevate their privileges
		assertThrows(NotAuthorizedException.class, () -> {
			UserService.update(standardUser, employeeUser, "", "", "", "", "", employeeRole);
		});
		assertThrows(NotAuthorizedException.class, () -> {
			UserService.update(standardUser, employeeUser, "", "", "", "", "", adminRole);
		});
		assertThrows(NotAuthorizedException.class, () -> {
			UserService.update(employeeUser, employeeUser, "", "", "", "", "", adminRole);
		});
		assertThrows(NotAuthorizedException.class, () -> {
			UserService.update(standardUser, employeeUser, "", "", "", "", "", standardRole);
		});

		// userToUpdate can update themself
		userToUpdate = UserService.update(userToUpdate, userToUpdate, "username6", "passwordA1@", "Fake", "Name",
				"fake5@email.com", standardRole);

		// Make sure user has been updated
		assertEquals(userToUpdate.getUsername(), "username6");
		assertEquals(userToUpdate.getPassword(), "passwordA1@");
		assertEquals(userToUpdate.getFirstName(), "Fake");
		assertEquals(userToUpdate.getLastName(), "Name");
		assertEquals(userToUpdate.getEmail(), "fake5@email.com");

		// admin can update userToUpdate
		userToUpdate = UserService.update(adminUser, userToUpdate, "username6", "passwordA1@", "Faker", "Namer",
				"fake5@email.com", employeeRole);

		// Make sure user has been updated
		assertEquals(userToUpdate.getUsername(), "username6");
		assertEquals(userToUpdate.getPassword(), "passwordA1@");
		assertEquals(userToUpdate.getFirstName(), "Faker");
		assertEquals(userToUpdate.getLastName(), "Namer");
		assertEquals(userToUpdate.getEmail(), "fake5@email.com");
		assertTrue(userToUpdate.getRole().equals(employeeRole));
	}

	@Test
	void testGetUserById() throws Exception {
		// Get a user
		User user = UserService.register(adminUser, "username7", "passwordA1!", "Fake", "Name", "fake7@email.com",
				standardRole);

		// Get user via getUserById
		User userCopy = UserService.getUserById(user.getId());

		// Make sure they are the same
		assertTrue(userCopy.equals(user));

		// Get user again using authorizeAndGetUserById
		userCopy = UserService.authorizeAndGetUserById(user, user.getId());

		// Make sure they are the same
		assertTrue(userCopy.equals(user));

		// Get user again using authorizeAndGetUserById, this time with a different
		// authorized user
		userCopy = UserService.authorizeAndGetUserById(adminUser, user.getId());

		// Make sure they are the same
		assertTrue(userCopy.equals(user));

		// Get user again using authorizeAndGetUserById, this time with a different
		// authorized user
		userCopy = UserService.authorizeAndGetUserById(employeeUser, user.getId());

		// Make sure they are the same
		assertTrue(userCopy.equals(user));

		// Finally make sure a different standard user is not authorized
		assertThrows(NotAuthorizedException.class, () -> {
			UserService.authorizeAndGetUserById(standardUser, user.getId());
		});

	}

	@Test
	public void testAuthorize() throws Exception {
		// Get a user
		User user = UserService.register(adminUser, "username8", "passwordA1!", "Fake", "Name", "fake8@email.com",
				standardRole);

		// Make sure we can get this user via authorization
		User userCopy = UserService.authorize("username8", "passwordA1!");

		// Make sure they are the same
		assertTrue(user.equals(userCopy));

		// Make sure an exception is thrown when invalid credentials are supplied
		assertThrows(InvalidCredentialsException.class, () -> {
			UserService.authorize("username8", "passwordA1!!");
		});
		assertThrows(InvalidCredentialsException.class, () -> {
			UserService.authorize("username8 ", "passwordA1!");
		});
	}

	@Test
	void testGetAllUsers() throws Exception {
		// Create a Set containing the Users
		HashSet<User> users = new HashSet<User>();
		users.add(adminUser);
		users.add(employeeUser);
		users.add(standardUser);

		// Get the list of Users from UserService.getAllUsers()
		ArrayList<User> users2 = UserService.getAllUsers(adminUser);

		// Convert to HashSet and compare with users
		HashSet<User> users3 = new HashSet<User>();
		users3.addAll(users2);
		assertTrue(users3.equals(users));

		// Make sure no exception is thrown when an employee calls get all users
		UserService.getAllUsers(employeeUser);

		// Make sure an exception is thrown when a standard user calls get all users
		assertThrows(NotAuthorizedException.class, () -> {
			UserService.getAllUsers(standardUser);
		});

	}

}
