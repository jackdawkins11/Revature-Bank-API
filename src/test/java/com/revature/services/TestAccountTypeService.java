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
import com.revature.model.AccountType;
import com.revature.utilities.DatabaseClearer;

/**
 * @author John Dawkins jackdawkins1298@outlook.com
 *
 */
public class TestAccountTypeService {

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
	void testInsertAccountType() throws Exception {
		// The type to insert
		String typeStr = "Checking";

		// Insert using type service
		AccountType type = AccountTypeService.insertAccountType(typeStr);

		// Verify
		assertEquals(type.getType(), typeStr);
	}

	@Test
	void testGetAccountTypeById() throws Exception {
		// The type to insert
		String typeStr = "Checking";

		// Insert using type service
		AccountType type = AccountTypeService.insertAccountType(typeStr);
		
		//Get type via getById
		AccountType type2 = AccountTypeService.getAccountTypeById(type.getId());
		
		//Verify
		assertTrue(type.equals(type2));
	}
	
	@Test
	void testGetAllAccountTypes() throws Exception {
		//The types to insert
		String[] types = {"Checking", "Savings" };
		
		Set<AccountType> typeSet1 = new HashSet<AccountType>();
		
		//Insert all the types, adding to the set
		for(String type : types) {
			typeSet1.add(AccountTypeService.insertAccountType(type));
		}
		
		//Get another set of types via getAllAccountTypes
		Set<AccountType> typeSet2 = new HashSet<AccountType>();
		typeSet2.addAll(AccountTypeService.getAllAccountTypes());
		
		//Compare
		assertTrue(typeSet2.equals(typeSet1));
	}
}
