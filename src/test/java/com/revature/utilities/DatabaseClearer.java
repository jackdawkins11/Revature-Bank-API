/**
 * 
 */
package com.revature.utilities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.revature.dal.ConnectionHandler;

/**
 * @author John Dawkins jackdawkins1298@outlook.com
 *
 */
public class DatabaseClearer {

	public static boolean clearTables() {

		// The sql to be executed
		String[] sql = { "delete from UserAccount", "delete from Users", "delete from Account", "delete from Roles", "delete from AccountStatus",
				"delete from AccountType"  };

		try {
			// Get a connection
			Connection conn = ConnectionHandler.getPostgresConnection();

			for (String cSql : sql) {
				// prepare a statement
				PreparedStatement stmt = conn.prepareStatement(cSql);

				stmt.executeUpdate();
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
}
