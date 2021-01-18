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

import com.revature.exceptions.NoSuchAccountTypeException;
import com.revature.model.AccountType;
import com.revature.utilities.DatabaseClearer;

/**
 * @author John Dawkins jackdawkins1298@outlook.com
 *
 */
public class TestAccountTypeDAO {

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
		// The type to insert
		String type = "Savings";

		// Get DAO
		AccountTypeDAO dao = DAOUtilities.getAccountTypeDAO();

		// Insert the type string
		int id = dao.insertAccountType(type);

		// Get the AccountType object
		AccountType accountType = dao.getAccountTypeById(id);

		// Verify data in AccountType
		assertEquals(accountType.getId(), id);
		assertEquals(accountType.getType(), type);

	}

	public void testUniqueAux() throws Exception {
		// Types to be inserted. Note that Savings appears twice. This should throw an
		// exception
		String[] types = { "Savings", "Checking", "Savings", "Premium" };

		// Get DAO
		AccountTypeDAO dao = DAOUtilities.getAccountTypeDAO();

		// Insert each of the above
		for (String type : types) {
			dao.insertAccountType(type);
		}
		
	}
	
	@Test
	public void testUnique() {
		assertThrows(SQLException.class, this::testUniqueAux);
	}
	
	public void testGetByIdExceptionAux() throws Exception {
		//Get a AccountTypeDAO
		AccountTypeDAO dao = DAOUtilities.getAccountTypeDAO();
		
		//This should throw a NoSuchAccountTypeException
		dao.getAccountTypeById(1);
	}
	
	@Test
	public void testGetByIdException() {
		assertThrows(NoSuchAccountTypeException.class, this::testGetByIdExceptionAux);
	}
	
	@Test
	public void testGetAll() throws Exception {
		// AccountTypes to be inserted.
		String[] types = { "Savings", "Checking", "Premium" };

		// Get DAO
		AccountTypeDAO dao = DAOUtilities.getAccountTypeDAO();

		//We fill this set of AccountTypes as we insert
		Set<AccountType> accountTypes = new HashSet<AccountType>();
		
		// Insert each of the above
		for (String type : types ) {
			int id = dao.insertAccountType(type);
			accountTypes.add(dao.getAccountTypeById(id));
		}
		
		//Get the AccountTypes again via dao.getAllAccountTypes
		ArrayList<AccountType> accountTypes2 = dao.getAllAccountTypes();
		
		//Add all the AccountTypes to another set for comparison purposes
		Set<AccountType> accountTypes3 = new HashSet<AccountType>();
		accountTypes3.addAll(accountTypes2);
		
		//compare the two sets of AccountTypes
		assertTrue(accountTypes3.equals(accountTypes));
		
	}

	@Test
	void testAccountTypeExists() throws Exception {
		// The type to insert
		String type = "Open";

		// Get DAO
		AccountTypeDAO dao = DAOUtilities.getAccountTypeDAO();

		//Insert the type
		int id = dao.insertAccountType(type);
		
		//Test accountTypeExists
		assertTrue(dao.accountTypeExists(type));
		assertFalse(dao.accountTypeExists(type + "1"));
	}

}
