/**
 * 
 */
package com.revature.dal;

import java.sql.SQLException;
import java.util.ArrayList;

import com.revature.exceptions.NoSuchRoleException;
import com.revature.model.Role;

/**
 * @author John Dawkins jackdawkins1298@outlook.com
 *
 */
public interface RoleDAO {

	/**
	 * @return id of the new role
	 * @throws SQLException error accessing database
	 * Creates a new entry in the Roles table.
	 */
	int insertRole(String role) throws SQLException;
	
	/**
	 * 
	 * @param id The id of the Role to get
	 * @return the Role with the given id
	 * @throws SQLException error accessing database
	 * @throws NoSuchRoleException no Role with the given id
	 */
	Role getRoleById(int id) throws SQLException, NoSuchRoleException;
	
	/**
	 * 
	 * @return list of all roles
	 * @throws SQLException
	 */
	ArrayList< Role > getAllRoles() throws SQLException;
	
	boolean roleExists(String role) throws SQLException;
}
