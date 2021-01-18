/**
 * 
 */
package com.revature.dal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.revature.exceptions.NoSuchRoleException;
import com.revature.model.Role;
import com.revature.utilities.DatabaseClearer;

/**
 * @author John Dawkins jackdawkins1298@outlook.com
 *
 */
public class TestRoleDAO {

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
		// The role to insert
		String roleStr = "Employee";

		// Get DAO
		RoleDAO dao = DAOUtilities.getRoleDAO();

		// Insert the role string
		int id = dao.insertRole(roleStr);

		// Get the Role object
		Role role = dao.getRoleById(id);

		// Verify data in role
		assertEquals(role.getId(), id);
		assertEquals(role.getRole(), roleStr);
	}
	
  
  	public void testUniqueAux() throws Exception {
  		// Roles to be inserted. Note that Employee appears twice. This should throw an
  		// exception
  		String[] roleStrs = { "Employee", "Admin", "Employee", "Premium" };
  
  		// Get DAO
  		RoleDAO dao = DAOUtilities.getRoleDAO();
  
  		// Insert each of the above
  		for (String roleStr : roleStrs) {
  			dao.insertRole(roleStr);
  		}
  		
  	}
  	
  	@Test
  	public void testUnique() {
  		assertThrows(SQLException.class, this::testUniqueAux);
  	}
  	
  	public void testGetByIdExceptionAux() throws Exception {
  		//Get a RoleDAO
  		RoleDAO dao = DAOUtilities.getRoleDAO();
  		
  		//This should throw a NoSuchRoleException
  		dao.getRoleById(1);
  	}
  	
  	@Test
  	public void testGetByIdException() {
  		assertThrows(NoSuchRoleException.class, this::testGetByIdExceptionAux);
  	}
  	
  	@Test
  	public void testGetAll() throws Exception {
  		// Roles to be inserted.
  		String[] roleStrs = { "Employee", "Admin", "Standard", "Premium" };
  
  		// Get DAO
  		RoleDAO dao = DAOUtilities.getRoleDAO();
  
  		//We fill this set of roles as we insert
  		Set<Role> roles = new HashSet<Role>();
  		
  		// Insert each of the above
  		for (String roleStr : roleStrs) {
  			int id = dao.insertRole(roleStr);
  			roles.add(dao.getRoleById(id));
  		}
  		
  		//Get the roles again via dao.getAllRoles
  		ArrayList<Role> roles2 = dao.getAllRoles();
  		
  		//Add all the roles to another set for comparison purposes
  		Set<Role> roles3 = new HashSet<Role>();
  		roles3.addAll(roles2);
  		
  		//compare the two sets of roles
  		assertTrue(roles3.equals(roles));
  		  		
  	}

  	@Test
	void testRoleExists() throws Exception {
		// The role to insert
		String role = "Open";

		// Get DAO
		RoleDAO dao = DAOUtilities.getRoleDAO();

		//Insert the role
		int id = dao.insertRole(role);
		
		//Test accountTypeExists
		assertTrue(dao.roleExists(role));
		assertFalse(dao.roleExists(role + "1"));
	}
  	
}
