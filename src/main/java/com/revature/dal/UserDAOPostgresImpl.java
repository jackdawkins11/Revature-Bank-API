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

import com.revature.exceptions.CouldNotPopulateException;
import com.revature.exceptions.InvalidCredentialsException;
import com.revature.exceptions.NoSuchRoleException;
import com.revature.exceptions.NoSuchUserException;
import com.revature.model.Role;
import com.revature.model.User;

/**
 * @author John Dawkins jackdawkins1298@outlook.com
 *
 */
public class UserDAOPostgresImpl implements UserDAO {

	public int insertUser(String username, String password, String firstName, String lastName, String email, Role role)
			throws SQLException {
		// the sql we execute
		String sql = "insert into Users values(default, ?, ?, ?, ?, ?, ?)";

		// get the database connection
		Connection conn = ConnectionHandler.getPostgresConnection();

		// create a statement
		PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

		// set the variables in the statement
		stmt.setString(1, username);
		stmt.setString(2, password);
		stmt.setString(3, firstName);
		stmt.setString(4, lastName);
		stmt.setString(5, email);
		stmt.setInt(6, role.getId());

		// execute it
		stmt.executeUpdate();

		// Get the id of the new user
		ResultSet rs = stmt.getGeneratedKeys();
		rs.next();
		int id = rs.getInt(1);

		// close resources
		ConnectionHandler.closeResources(conn, stmt, rs);

		return id;
	}

	public void updateUser(User user, String username, String password, String firstName, String lastName, String email,
			Role role) throws SQLException {
		// the sql we execute
		String sql = "update Users set username = ?, password = ?, first_name = ?, "
				+ "last_name = ?, email = ?, role_id = ? where id = ?";

		// get the database connection
		Connection conn = ConnectionHandler.getPostgresConnection();

		// create a statement
		PreparedStatement stmt = conn.prepareStatement(sql);

		// set the variables in the statement
		stmt.setString(1, username);
		stmt.setString(2, password);
		stmt.setString(3, firstName);
		stmt.setString(4, lastName);
		stmt.setString(5, email);
		stmt.setInt(6, role.getId());
		stmt.setInt(7, user.getId());

		// execute the statement
		stmt.executeUpdate();

		// close resources
		ConnectionHandler.closeResources(conn, stmt, null);
	}

	public boolean usernameExists(String username) throws SQLException {
		// the SQL we execute
		String sql = "select count(id) from Users where username = ?";

		// get the database connection
		Connection conn = ConnectionHandler.getPostgresConnection();

		// create a statement
		PreparedStatement stmt = conn.prepareStatement(sql);

		// Set the variables in the statement
		stmt.setString(1, username);

		// Get the result set
		ResultSet rs = stmt.executeQuery();

		// Return whether the result of the query is greater than 0
		rs.next();
		boolean exists = rs.getInt(1) > 0;

		// close resources
		ConnectionHandler.closeResources(conn, stmt, rs);

		return exists;
	}

	public boolean emailExists(String email) throws SQLException {
		// the SQL we execute
		String sql = "select count(id) from Users where email = ?";

		// get the database connection
		Connection conn = ConnectionHandler.getPostgresConnection();

		// create a statement
		PreparedStatement stmt = conn.prepareStatement(sql);

		// Set the variables in the statement
		stmt.setString(1, email);

		// Get the result set
		ResultSet rs = stmt.executeQuery();

		// Return whether the result of the query is greater than 0
		rs.next();
		boolean exists = rs.getInt(1) > 0;

		// close resources
		ConnectionHandler.closeResources(conn, stmt, rs);

		return exists;
	}

	public User getUserByUsernameAndPassword(String username, String password)
			throws InvalidCredentialsException, SQLException {
		// the sql to be executed
		String sql = "select Users.id, username, password, first_name, last_name, email, Roles.id, Roles.role from Users inner join Roles on Users.role_id = Roles.id where username = ? and password = ?";

		// get the database connection
		Connection conn = ConnectionHandler.getPostgresConnection();

		// create a statement
		PreparedStatement stmt = conn.prepareStatement(sql);

		// set the variables in the statement
		stmt.setString(1, username);
		stmt.setString(2, password);

		// Get result set
		ResultSet rs = stmt.executeQuery();

		// Make sure there is one row in result set
		if (!rs.next()) {
			throw new InvalidCredentialsException("No user with the given username and password.");
		}

		// User to return
		User user = new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
				rs.getString(6), new Role(rs.getInt(7), rs.getString(8)));

		// close resources
		ConnectionHandler.closeResources(conn, stmt, rs);

		return user;
	}

	public User getUserById(int id) throws SQLException, NoSuchUserException {
		// the sql to be executed
		String sql = "select Users.id, username, password, first_name, last_name, email, Roles.id, Roles.role from Users inner join Roles on Users.role_id = Roles.id where Users.id = ?";

		// get the database connection
		Connection conn = ConnectionHandler.getPostgresConnection();

		// create a statement
		PreparedStatement stmt = conn.prepareStatement(sql);

		// set the variables in the statement
		stmt.setInt(1, id);

		// Get result set
		ResultSet rs = stmt.executeQuery();

		// Make sure there is one row in result set
		if (!rs.next()) {
			throw new NoSuchUserException("No user with the given id.");
		}

		// The user to return
		// User to return
		User user = new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
				rs.getString(6), new Role(rs.getInt(7), rs.getString(8)));

		// close resources
		ConnectionHandler.closeResources(conn, stmt, rs);

		return user;
	}

	public ArrayList<User> getAllUsers() throws SQLException {
		// the sql to be executed
		String sql = "select Users.id, username, password, first_name, last_name, email, Roles.id, Roles.role from Users inner join Roles on Users.role_id = Roles.id";

		// get the database connection
		Connection conn = ConnectionHandler.getPostgresConnection();

		// create a statement
		PreparedStatement stmt = conn.prepareStatement(sql);

		// Get result set
		ResultSet rs = stmt.executeQuery();

		// What is returned
		ArrayList<User> users = new ArrayList<User>();

		// Add new User for each element in result set
		while (rs.next()) {
			users.add( new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
					rs.getString(6), new Role(rs.getInt(7), rs.getString(8))) );
		}

		// close resources
		ConnectionHandler.closeResources(conn, stmt, rs);

		return users;
	}

}
