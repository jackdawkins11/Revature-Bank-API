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

import com.revature.exceptions.NoSuchAccountTypeException;
import com.revature.model.AccountType;

/**
 * @author John Dawkins jackdawkins1298@outlook.com
 *
 */
public class AccountTypeDAOPostgresImpl implements AccountTypeDAO {

	public int insertAccountType(String type) throws SQLException {
		// the sql we execute
		String sql = "insert into AccountType values(default, ?)";

		// get the database connection
		Connection conn = ConnectionHandler.getPostgresConnection();

		// create a statement
		PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

		// set the variables in the statement
		stmt.setString(1, type);

		// execute it
		stmt.executeUpdate();

		// get the id of the new account
		ResultSet rs = stmt.getGeneratedKeys();
		rs.next();
		int id = rs.getInt(1);

		// close resources
		ConnectionHandler.closeResources(conn, stmt, rs);

		return id;
	}

	public AccountType getAccountTypeById(int id) throws SQLException, NoSuchAccountTypeException {
		// the sql to be executed
		String sql = "select id, type from AccountType where id = ?";

		// get the database connection
		Connection conn = ConnectionHandler.getPostgresConnection();

		// create a statement
		PreparedStatement stmt = conn.prepareStatement(sql);

		// set the variables in the statement
		stmt.setInt(1, id);

		// Get result set
		ResultSet rs = stmt.executeQuery();

		// Check for no rows in result set
		if (!rs.next()) {
			throw new NoSuchAccountTypeException("No account type with the given id.");
		}

		// the AccountType to return
		AccountType accountType = new AccountType(id, rs.getString(2));

		// close resources
		ConnectionHandler.closeResources(conn, stmt, rs);

		return accountType;
	}

	public ArrayList<AccountType> getAllAccountTypes() throws SQLException {
		// the sql to be executed
		String sql = "select id, type from AccountType";

		// get the database connection
		Connection conn = ConnectionHandler.getPostgresConnection();

		// create a statement
		PreparedStatement stmt = conn.prepareStatement(sql);

		// Get result set
		ResultSet rs = stmt.executeQuery();

		// What is returned
		ArrayList<AccountType> accountTypes = new ArrayList<AccountType>();

		// Iterate over each record in result
		while (rs.next()) {
			// Add AccountType
			accountTypes.add(new AccountType(rs.getInt(1), rs.getString(2)));
		}

		// close resources
		ConnectionHandler.closeResources(conn, stmt, rs);

		return accountTypes;
	}
	
	public boolean accountTypeExists(String type) throws SQLException {
		// the SQL we execute
		String sql = "select count(id) from AccountType where type = ?";

		// get the database connection
		Connection conn = ConnectionHandler.getPostgresConnection();

		// create a statement
		PreparedStatement stmt = conn.prepareStatement(sql);

		// Set the variables in the statement
		stmt.setString(1, type);

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
