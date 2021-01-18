/**
 * 
 */
package com.revature.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.revature.dal.ConnectionHandler;
import com.revature.model.AccountStatus;
import com.revature.utilities.DatabaseClearer;

/**
 * @author John Dawkins jackdawkins1298@outlook.com
 *
 */
public class TestAccountStatusService {

	@BeforeAll
	static void setTesting() {
		ConnectionHandler.testing = true;
	}
	
	@BeforeEach
	void setUp() throws Exception {
		// Clear database
		assertTrue(DatabaseClearer.clearTables());
	}

	@Test
	void testInsertAccountStatus() throws Exception {
		// The status to insert
		String statusStr = "Open";

		// Insert using status service
		AccountStatus status = AccountStatusService.insertAccountStatus(statusStr);

		// Verify
		assertEquals(status.getStatus(), statusStr);
	}

	@Test
	void testGetAccountStatusById() throws Exception {
		// The status to insert
		String statusStr = "Open";

		// Insert using status service
		AccountStatus status = AccountStatusService.insertAccountStatus(statusStr);
		
		//Get status via getById
		AccountStatus status2 = AccountStatusService.getAccountStatusById(status.getId());
		
		//Verify
		assertTrue(status.equals(status2));
	}
	
	@Test
	void testGetAllAccountStatuss() throws Exception {
		//The status' to insert
		String[] statuss = {"Open", "Closed" };
		
		Set<AccountStatus> statusSet1 = new HashSet<AccountStatus>();
		
		//Insert all the status', adding to the set
		for(String status : statuss) {
			statusSet1.add(AccountStatusService.insertAccountStatus(status));
		}
		
		//Get another set of status' via getAllAccountStatuss
		Set<AccountStatus> statusSet2 = new HashSet<AccountStatus>();
		statusSet2.addAll(AccountStatusService.getAllAccountStatuss());
		
		//Compare
		assertTrue(statusSet2.equals(statusSet1));
	}
}
