/**
 * 
 */
package com.revature.services;

import java.sql.SQLException;
import java.util.ArrayList;

import com.revature.dal.DAOUtilities;
import com.revature.dal.RoleDAO;
import com.revature.exceptions.InvalidRoleParamsException;
import com.revature.exceptions.NoSuchRoleException;
import com.revature.model.Role;
import com.revature.validation.RoleValidator;

/**
 * @author John Dawkins jackdawkins1298@outlook.com
 *
 */
public class RoleService {

	public static Role insertRole(String role) throws SQLException, InvalidRoleParamsException {
		// Validate the role trying to be inserted
		RoleValidator.validateRoleParams(role);

		// Get DAO
		RoleDAO dao = DAOUtilities.getRoleDAO();

		// Insert the role and get the id
		int id = dao.insertRole(role);

		// Return the generated role
		try {
			return dao.getRoleById(id);
		} catch (NoSuchRoleException e) {
			// We know this id is valid, so just throw SQLException
			throw new SQLException(e);
		}
	}

	public static Role getRoleById(int id) throws SQLException, NoSuchRoleException {
		// Get dao
		RoleDAO dao = DAOUtilities.getRoleDAO();

		// Return the role with the id, propagating exceptions
		return dao.getRoleById(id);
	}

	public static ArrayList<Role> getAllRoles() throws SQLException {
		// Get dao
		RoleDAO dao = DAOUtilities.getRoleDAO();

		// Return all the roles
		return dao.getAllRoles();
	}

}
