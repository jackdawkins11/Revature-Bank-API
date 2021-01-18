/**
 * 
 */
package com.revature.validation;

import java.sql.SQLException;

import com.revature.dal.DAOUtilities;
import com.revature.dal.RoleDAO;
import com.revature.exceptions.InvalidRoleParamsException;

/**
 * @author John Dawkins jackdawkins1298@outlook.com
 *
 */
public class RoleValidator {

	public static void validateRoleParams(String role) throws SQLException, InvalidRoleParamsException {
		//Get DAO
		RoleDAO dao = DAOUtilities.getRoleDAO();
		
		//Make sure the role is unique
		if( dao.roleExists(role) ) {
			throw new InvalidRoleParamsException();
		}
	}
}
