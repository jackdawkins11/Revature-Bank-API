/**
 * 
 */
package com.revature.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.revature.model.Account;
import com.revature.model.AccountStatus;
import com.revature.model.AccountType;
import com.revature.model.Role;
import com.revature.model.User;

/**
 * @author John Dawkins jackdawkins1298@outlook.com
 *
 */
public class UserAccountDAOPostgresImpl implements UserAccountDAO {

	public void insertUserAccountRelationship(User user, Account account) throws SQLException {
		// The sql to be executed
		String sql = "insert into UserAccount values (?, ?)";

		// get the database connection
		Connection conn = ConnectionHandler.getPostgresConnection();

		// create a statement
		PreparedStatement stmt = conn.prepareStatement(sql);

		// set the variables in the statement
		stmt.setInt(1, user.getId());
		stmt.setInt(2, account.getId());

		// execute it
		int rc = stmt.executeUpdate();

		if (rc == 0) {
			// TODO throw custom exception
		}

		// close resources
		ConnectionHandler.closeResources(conn, stmt, null);
	}

	public ArrayList<Account> getAccountsByUser(User user) throws SQLException {
		// The sql to get the accounts
		String sql = "select Account.id, Account.amount, AccountType.id, AccountType.type, AccountStatus.id, AccountStatus.status from UserAccount inner join Account on account_id = Account.id inner join AccountType on Account.type_id = AccountType.id inner join AccountStatus on Account.status_id = AccountStatus.id where user_id = ?";

		// get the database connection
		Connection conn = ConnectionHandler.getPostgresConnection();

		// create a statement
		PreparedStatement stmt = conn.prepareStatement(sql);

		// set the variables in the statement
		stmt.setInt(1, user.getId());

		// Get the result set containing the accounts
		ResultSet rs = stmt.executeQuery();

		// What is returned
		ArrayList<Account> accounts = new ArrayList<Account>();

		// Add an account for each id
		while (rs.next()) {
			Account account = new Account(rs.getInt(1), rs.getDouble(2), new AccountType(rs.getInt(3), rs.getString(4)),
					new AccountStatus(rs.getInt(5), rs.getString(6)));
			accounts.add(account);
		}

		// close resources
		ConnectionHandler.closeResources(conn, stmt, rs);

		return accounts;

	}

	public User getAccountOwner(Account account) throws SQLException {
		// The sql to execute
		String sql = "select Users.id, Users.username, Users.password, Users.first_name, Users.last_name, Users.email, Roles.id, Roles.role from UserAccount inner join Users on UserAccount.user_id = Users.id inner join Roles on Users.role_id = Roles.id where account_id = ?";

		// get the database connection
		Connection conn = ConnectionHandler.getPostgresConnection();

		// create a statement
		PreparedStatement stmt = conn.prepareStatement(sql);

		// set the variables in the statement
		stmt.setInt(1, account.getId());

		// Get the result set containing the users
		ResultSet rs = stmt.executeQuery();

		// Go to first result
		//Note that we will get an SQLException when using the rs.get methods if
		//there are no rows in the result set
		rs.next();

		// Get the user from the result set
		User user = new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
				rs.getString(6), new Role(rs.getInt(7), rs.getString(8)));

		// close resources
		ConnectionHandler.closeResources(conn, stmt, rs);

		return user;
	}

}
