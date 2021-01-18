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
import com.revature.exceptions.NoSuchAccountException;
import com.revature.exceptions.NoSuchAccountStatusException;
import com.revature.exceptions.NoSuchAccountTypeException;
import com.revature.model.Account;
import com.revature.model.AccountStatus;
import com.revature.model.AccountType;

/**
 * @author John Dawkins jackdawkins1298@outlook.com
 *
 */
public class AccountDAOPostgresImpl implements AccountDAO {

	public int insertAccount(double amount, AccountType type, AccountStatus status) throws SQLException {
		// the sql we execute
		String sql = "insert into Account values(default, ?, ?, ?)";

		// get the database connection
		Connection conn = ConnectionHandler.getPostgresConnection();

		// create a statement
		PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

		// set the variables in the statement
		stmt.setDouble(1, amount);
		stmt.setInt(2, type.getId());
		stmt.setInt(3, status.getId());

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

	public void updateAccount(Account account, double amount, AccountType type, AccountStatus status)
			throws SQLException {
		// the sql we execute
		String sql = "update Account set amount = ?, type_id = ?, status_id = ? where id = ?";

		// get the database connection
		Connection conn = ConnectionHandler.getPostgresConnection();

		// create a statement
		PreparedStatement stmt = conn.prepareStatement(sql);

		// set the variables in the statement
		stmt.setDouble(1, amount);
		stmt.setInt(2, type.getId());
		stmt.setInt(3, status.getId());
		stmt.setInt(4, account.getId());

		// execute the statement
		stmt.executeUpdate();

		// close resources
		ConnectionHandler.closeResources(conn, stmt, null);
	}

	public Account getAccountById(int id) throws SQLException, NoSuchAccountException {
		// the sql to be executed
		String sql = "select A.id, A.amount, AT.id, AT.type, ACTS.id, ACTS.status from Account A inner join AccountType AT on A.type_id = AT.id inner join AccountStatus ACTS on A.status_id = ACTS.id where A.id = ?";

		// get the database connection
		Connection conn = ConnectionHandler.getPostgresConnection();

		// create a statement
		PreparedStatement stmt = conn.prepareStatement(sql);

		// set the variables in the statement
		stmt.setInt(1, id);

		// Get result set
		ResultSet rs = stmt.executeQuery();

		// Make sure there is one result
		if (!rs.next()) {
			throw new NoSuchAccountException("No account with the given id.");
		}

		// Build the Account object
		Account account = new Account(rs.getInt(1), rs.getDouble(2), new AccountType(rs.getInt(3), rs.getString(4)),
				new AccountStatus(rs.getInt(5), rs.getString(6)));

		// close resources
		ConnectionHandler.closeResources(conn, stmt, rs);

		return account;
	}

	public ArrayList<Account> getAllAccounts() throws SQLException {
		// the sql to be executed
		String sql = "select A.id, A.amount, AT.id, AT.type, ACTS.id, ACTS.status from Account A inner join AccountType AT on A.type_id = AT.id inner join AccountStatus ACTS on A.status_id = ACTS.id";

		// get the database connection
		Connection conn = ConnectionHandler.getPostgresConnection();

		// create a statement
		PreparedStatement stmt = conn.prepareStatement(sql);

		// Get result set
		ResultSet rs = stmt.executeQuery();

		// What is returned
		ArrayList<Account> accounts = new ArrayList<Account>();

		// Iterate over result set
		while (rs.next()) {

			// Populate and add an account
			Account account = new Account(rs.getInt(1), rs.getDouble(2), new AccountType(rs.getInt(3), rs.getString(4)),
					new AccountStatus(rs.getInt(5), rs.getString(6)));
			accounts.add(account);
		}

		// close resources
		ConnectionHandler.closeResources(conn, stmt, rs);

		return accounts;
	}

	public ArrayList<Account> getAccountsByStatus(AccountStatus status) throws SQLException {
		// the sql to be executed
		String sql = "select A.id, A.amount, AT.id, AT.type, ACTS.id, ACTS.status from Account A inner join AccountType AT on A.type_id = AT.id inner join AccountStatus ACTS on A.status_id = ACTS.id where ACTS.id = ?";

		// get the database connection
		Connection conn = ConnectionHandler.getPostgresConnection();

		// create a statement
		PreparedStatement stmt = conn.prepareStatement(sql);

		// set the variables in the statement
		stmt.setInt(1, status.getId());

		// Get result set
		ResultSet rs = stmt.executeQuery();

		// What is returned
		ArrayList<Account> accounts = new ArrayList<Account>();

		// Iterate over result set
		while (rs.next()) {

			// Populate and add an account
			Account account = new Account(rs.getInt(1), rs.getDouble(2), new AccountType(rs.getInt(3), rs.getString(4)),
					new AccountStatus(rs.getInt(5), rs.getString(6)));
			accounts.add(account);
		}

		// close resources
		ConnectionHandler.closeResources(conn, stmt, rs);

		return accounts;
	}

	public void addAmount(Account account, double amount) throws SQLException {

		// The updated amount TODO check it is greater than 0
		double newAmount = account.getAmount() + amount;

		// The sql to be executed
		String sql = "update Account set amount = ? where id = ?";

		// get the database connection
		Connection conn = ConnectionHandler.getPostgresConnection();

		// create a statement
		PreparedStatement stmt = conn.prepareStatement(sql);

		// set the variables in the statement
		stmt.setDouble(1, newAmount);
		stmt.setInt(2, account.getId());

		// Execute
		int rc = stmt.executeUpdate();

		if (rc == 0) {
			// TODO throw a special exception
		}

		// close resources
		ConnectionHandler.closeResources(conn, stmt, null);

	}

}
