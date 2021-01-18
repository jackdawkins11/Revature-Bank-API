/**
 * 
 */
package com.revature.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.revature.dal.ConnectionHandler;
import com.revature.model.Role;
import com.revature.utilities.DatabaseClearer;

/**
 * @author John Dawkins jackdawkins1298@outlook.com
 *
 */
public class TestRoleService {

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
	void testInsertRole() throws Exception {
		// The role to insert
		String roleStr = "Admin";

		// Insert using role service
		Role role = RoleService.insertRole(roleStr);

		// Verify
		assertEquals(role.getRole(), roleStr);
	}

	@Test
	void testGetRoleById() throws Exception {
		// The role to insert
		String roleStr = "Admin";

		// Insert using role service
		Role role = RoleService.insertRole(roleStr);
		
		//Get role via getById
		Role role2 = RoleService.getRoleById(role.getId());
		
		//Verify
		assertTrue(role.equals(role2));
	}
	
	@Test
	void testGetAllRoles() throws Exception {
		//The roles to insert
		String[] roles = {"Admin", "Employee", "Standard" };
		
		Set<Role> roleSet1 = new HashSet<Role>();
		
		//Insert all the roles, adding to the set
		for(String role : roles) {
			roleSet1.add(RoleService.insertRole(role));
		}
		
		//Get another set of roles via getAllRoles
		Set<Role> roleSet2 = new HashSet<Role>();
		roleSet2.addAll(RoleService.getAllRoles());
		
		//Compare
		assertTrue(roleSet2.equals(roleSet1));
	}
}
