/**
 * 
 */
package com.revature.dal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.revature.exceptions.NoSuchAccountStatusException;
import com.revature.model.AccountStatus;
import com.revature.utilities.DatabaseClearer;

/**
 * @author John Dawkins jackdawkins1298@outlook.com
 *
 */
public class TestAccountStatusDAO {

	@BeforeAll
	static void setTesting() {
		ConnectionHandler.testing = true;
	}
	
	@BeforeEach
	public void setUp() {
		assertTrue(DatabaseClearer.clearTables());
	}

	@Test
	public void testInsert() throws Exception {
		// The status to insert
		String status = "Open";

		// Get DAO
		AccountStatusDAO dao = DAOUtilities.getAccountStatusDAO();

		// Insert the status string
		int id = dao.insertAccountStatus(status);

		// Get the AccountStatus object
		AccountStatus accountStatus = dao.getAccountStatusById(id);

		// Verify data in AccountStatus
		assertEquals(accountStatus.getId(), id);
		assertEquals(accountStatus.getStatus(), status);

	}

	public void testUniqueAux() throws Exception {
		// Status' to be inserted. Note that Open appears twice. This should throw an
		// exception
		String[] statuss = { "Open", "Closed", "Open", "Frozen" };

		// Get DAO
		AccountStatusDAO dao = DAOUtilities.getAccountStatusDAO();

		// Insert each of the above
		for (String status : statuss) {
			dao.insertAccountStatus(status);
		}

	}

	@Test
	public void testUnique() {
		assertThrows(SQLException.class, this::testUniqueAux);
	}

	public void testGetByIdExceptionAux() throws Exception {
		// Get a AccountStatusDAO
		AccountStatusDAO dao = DAOUtilities.getAccountStatusDAO();

		// This should throw a NoSuchAccountStatusException
		dao.getAccountStatusById(1);
	}

	@Test
	public void testGetByIdException() {
		assertThrows(NoSuchAccountStatusException.class, this::testGetByIdExceptionAux);
	}

	@Test
	public void testGetAll() throws Exception {
		// AccountStatuss to be inserted.
		String[] statuss = { "Open", "Closed", "Frozen" };

		// Get DAO
		AccountStatusDAO dao = DAOUtilities.getAccountStatusDAO();

		// We fill this set of AccountStatuss as we insert
		Set<AccountStatus> accountStatuss = new HashSet<AccountStatus>();

		// Insert each of the above
		for (String status : statuss) {
			int id = dao.insertAccountStatus(status);
			accountStatuss.add(dao.getAccountStatusById(id));
		}

		// Get the AccountStatuss again via dao.getAllAccountStatuss
		ArrayList<AccountStatus> accountStatuss2 = dao.getAllAccountStatus();

		// Add all the AccountStatuss to another set for comparison purposes
		Set<AccountStatus> accountStatuss3 = new HashSet<AccountStatus>();
		accountStatuss3.addAll(accountStatuss2);

		// compare the two sets of AccountStatuss
		assertTrue(accountStatuss3.equals(accountStatuss));

	}

	@Test
	void testAccountStatusExists() throws Exception {
		// The status to insert
		String status = "Open";

		// Get DAO
		AccountStatusDAO dao = DAOUtilities.getAccountStatusDAO();

		//Insert the status
		int id = dao.insertAccountStatus(status);
		
		//Test accountStatusExists
		assertTrue(dao.accountStatusExists(status));
		assertFalse(dao.accountStatusExists(status + "1"));
	}
}
