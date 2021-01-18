/**
 * 
 */
package com.revature.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.revature.exceptions.NoSuchRoleException;
import com.revature.model.Role;

/**
 * @author John Dawkins jackdawkins1298@outlook.com
 *
 */
public class RoleDAOPostgresImpl implements RoleDAO {

	public int insertRole(String role) throws SQLException {
		// the sql we execute
		String sql = "insert into Roles values(default, ?)";

		// get the database connection
		Connection conn = ConnectionHandler.getPostgresConnection();

		// create a statement
		PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

		// set the variables in the statement
		stmt.setString(1, role);

		// execute it
		stmt.executeUpdate();

		// get the id of the new role
		ResultSet rs = stmt.getGeneratedKeys();
		rs.next();
		int id = rs.getInt(1);

		// close resources
		ConnectionHandler.closeResources(conn, stmt, rs);

		return id;
	}

	public Role getRoleById(int id) throws SQLException, NoSuchRoleException {
		// the sql to be executed
		String sql = "select id, role from Roles where id = ?";

		// get the database connection
		Connection conn = ConnectionHandler.getPostgresConnection();

		// create a statement
		PreparedStatement stmt = conn.prepareStatement(sql);

		// set the variables in the statement
		stmt.setInt(1, id);

		// Get result set
		ResultSet rs = stmt.executeQuery();

		// Make sure there is one row in result
		if (!rs.next()) {
			throw new NoSuchRoleException("No Role with the given id.");
		}

		// The role to return
		Role role = new Role(id, rs.getString(2));

		// close resources
		ConnectionHandler.closeResources(conn, stmt, rs);

		return role;
	}

	public ArrayList<Role> getAllRoles() throws SQLException {
		// the sql to be executed
		String sql = "select id, role from Roles";

		// get the database connection
		Connection conn = ConnectionHandler.getPostgresConnection();

		// create a statement
		PreparedStatement stmt = conn.prepareStatement(sql);

		// Get result set
		ResultSet rs = stmt.executeQuery();

		// What is returned
		ArrayList<Role> roles = new ArrayList<Role>();

		// Iterate over each record in result
		while (rs.next()) {
			// Add AccountType
			roles.add(new Role(rs.getInt(1), rs.getString(2)));
		}

		// close resources
		ConnectionHandler.closeResources(conn, stmt, rs);

		return roles;
	}
	
	public boolean roleExists(String role) throws SQLException {
		// the SQL we execute
		String sql = "select count(id) from Roles where role = ?";

		// get the database connection
		Connection conn = ConnectionHandler.getPostgresConnection();

		// create a statement
		PreparedStatement stmt = conn.prepareStatement(sql);

		// Set the variables in the statement
		stmt.setString(1, role);

		// Get the result set
		ResultSet rs = stmt.executeQuery();

		// Return whether the result of the query is greater than 0
		rs.next();
		boolean exists = rs.getInt(1) > 0;

		// close resources
		ConnectionHandler.closeResources(conn, stmt, rs);

		return exists;
	}

}
