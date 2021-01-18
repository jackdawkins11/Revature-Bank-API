/**
 * 
 */
package com.revature.dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author John Dawkins jackdawkins1298@outlook.com
 *
 */
public class ConnectionHandler {
	
	public static boolean testing = false;

	public static Connection getPostgresConnection() throws SQLException {
		//The following is necessary to load the driver
		try {
			Class.forName("org.postgresql.Driver");
		}catch(ClassNotFoundException e) {
			throw new SQLException(e);
		}
		
		//Set the connection URL for either the production or testing database
		String url = null;
		if(testing) {
			url = System.getenv("postgres_URL_test");
		}else {
			url = System.getenv("postgres_URL");
		}
		
		//Return a Connection
		return DriverManager.getConnection(url,
				System.getenv("postgres_user"),
				System.getenv("postgres_pass"));
	}
	
	static void closeResources(Connection conn, Statement stmt, ResultSet rs ) throws SQLException {
		if( conn != null ) {
			conn.close();
		}
		if( stmt != null ) {
			stmt.close();
		}
		if( rs != null ) {
			rs.close();
		}
	}

}
