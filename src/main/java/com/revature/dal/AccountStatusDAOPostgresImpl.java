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

import com.revature.exceptions.NoSuchAccountStatusException;
import com.revature.model.AccountStatus;

/**
 * @author John Dawkins jackdawkins1298@outlook.com
 *
 */
public class AccountStatusDAOPostgresImpl implements AccountStatusDAO {

	public int insertAccountStatus(String status) throws SQLException {
		// the sql we execute
		String sql = "insert into AccountStatus values(default, ?)";

		// get the database connection
		Connection conn = ConnectionHandler.getPostgresConnection();

		// create a statement
		PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

		// set the variables in the statement
		stmt.setString(1, status);

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

	public AccountStatus getAccountStatusById(int id) throws SQLException, NoSuchAccountStatusException {
		// the sql to be executed
		String sql = "select id, status from AccountStatus where id = ?";

		// get the database connection
		Connection conn = ConnectionHandler.getPostgresConnection();

		// create a statement
		PreparedStatement stmt = conn.prepareStatement(sql);

		// set the variables in the statement
		stmt.setInt(1, id);

		// Get result set
		ResultSet rs = stmt.executeQuery();

		if (!rs.next()) {
			throw new NoSuchAccountStatusException("No account status with the given id.");
		}

		// the AccountStatus object to return
		AccountStatus accountStatus = new AccountStatus(id, rs.getString(2));

		// close resources
		ConnectionHandler.closeResources(conn, stmt, rs);

		return accountStatus;
	}

	public ArrayList<AccountStatus> getAllAccountStatus() throws SQLException {
		// the sql to be executed
		String sql = "select id, status from AccountStatus";

		// get the database connection
		Connection conn = ConnectionHandler.getPostgresConnection();

		// create a statement
		PreparedStatement stmt = conn.prepareStatement(sql);

		// Get result set
		ResultSet rs = stmt.executeQuery();

		// What is returned
		ArrayList<AccountStatus> accountStatuss = new ArrayList<AccountStatus>();

		// Iterate over each record in result
		while (rs.next()) {
			// Add AccountType
			accountStatuss.add(new AccountStatus(rs.getInt(1), rs.getString(2)));
		}

		// close resources
		ConnectionHandler.closeResources(conn, stmt, rs);

		return accountStatuss;
	}
	
	public boolean accountStatusExists(String status) throws SQLException {
		// the SQL we execute
		String sql = "select count(id) from AccountStatus where status = ?";

		// get the database connection
		Connection conn = ConnectionHandler.getPostgresConnection();

		// create a statement
		PreparedStatement stmt = conn.prepareStatement(sql);

		// Set the variables in the statement
		stmt.setString(1, status);

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
