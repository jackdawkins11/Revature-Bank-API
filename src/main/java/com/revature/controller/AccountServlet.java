package com.revature.controller;

import static java.util.Map.entry;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.revature.exceptions.InvalidAccountParamsException;
import com.revature.exceptions.NoSuchAccountException;
import com.revature.exceptions.NoSuchAccountStatusException;
import com.revature.exceptions.NoSuchAccountTypeException;
import com.revature.exceptions.NoSuchUserException;
import com.revature.exceptions.NotAuthorizedException;
import com.revature.model.Account;
import com.revature.model.AccountStatus;
import com.revature.model.User;
import com.revature.services.AccountService;
import com.revature.services.AccountStatusService;
import com.revature.services.AccountTypeService;
import com.revature.services.UserService;

/**
 * Servlet implementation class AccountServlet
 * 
 * Handles requests to '/accounts/*'
 */
public class AccountServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AccountServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// This endpoint requires a session
		HttpSession session = request.getSession(false);

		// Send a 400 if there is no session
		if (session == null) {
			response.setStatus(400);
			return;
		}

		// Get the client, represented using the User class
		User client = null;
		try {
			client = UserService.getUserById((int) session.getAttribute("user_id"));
		} catch (SQLException | NoSuchUserException e) {
			// Internal server error
			throw new ServletException(e);
		}

		// We handle requests to /accounts, /accounts/:id, /accounts/status/:id,
		// /accounts/owner/:id
		final String URI = request.getRequestURI().substring("/BankAPI/accounts".length());

		if (URI.length() == 0) {
			// We hit /accounts

			// What is returned
			ArrayList<Account> accounts = null;
			try {
				accounts = AccountService.getAllAccounts(client);
			} catch (SQLException e) {
				// Internal server error
				throw new ServletException(e);
			} catch (NotAuthorizedException e) {
				// Client error: not authorized
				response.setStatus(400);
				return;
			}

			// Write status code and body using gson
			Gson gson = new Gson();
			response.setStatus(200);
			response.getWriter().write(gson.toJson(accounts));
		} else if (URI.matches("^/\\d+$")) {
			// We hit /accounts/:id

			// Parse the Id in the URI
			int id = Integer.parseInt(URI.substring(1));

			// What is returned
			Account account = null;
			try {
				account = AccountService.getAccountById(id);
			} catch (SQLException e) {
				// Internal server error
				throw new ServletException(e);
			} catch (NoSuchAccountException e) {
				// Client error -- not authorized
				response.setStatus(400);
				return;
			}

			// Return 200 level status code
			Gson gson = new Gson();
			response.setStatus(200);
			response.getWriter().write(gson.toJson(account));
		} else if (URI.matches("^/status/\\d+$")) {
			// We hit /accounts/status/:id

			// Parse the Id in the URI
			int id = Integer.parseInt(URI.substring("/status/".length()));

			// Get the AccountStatus with this id
			AccountStatus status = null;
			try {
				status = AccountStatusService.getAccountStatusById(id);
			} catch (SQLException e) {
				// Let tomcat send response with 500 status code and html containing the
				// exception
				throw new ServletException(e);
			} catch (NoSuchAccountStatusException e) {
				// Client error: invalid id
				response.setStatus(400);
				return;
			}

			// We are returning an ArrayList of accounts
			ArrayList<Account> accounts = null;
			try {
				accounts = AccountService.getAccountsByStatus(client, status);
			} catch (SQLException e) {
				// Throw ServletException so tomcat sends a response with a 500 status code
				throw new ServletException(e);
			} catch (NotAuthorizedException e) {
				// Client error: not authorized
				response.setStatus(400);
				return;
			}

			// Response with status code=200 and body containing accounts
			Gson gson = new Gson();
			response.setStatus(200);
			response.getWriter().write(gson.toJson(accounts));
		} else if (URI.matches("^/owner/\\d+$")) {
			// We hit /owner/:id

			// Parse the id
			int id = Integer.parseInt(URI.substring("/owner/".length()));

			// Get the User with the given id
			User user = null;
			try {
				user = UserService.getUserById(id);
			} catch (SQLException e) {
				// Internal server error
				throw new ServletException(e);
			} catch (NoSuchUserException e) {
				// Client error: invalid id
				response.setStatus(400);
				return;
			}

			ArrayList<Account> accounts = null;
			try {
				accounts = AccountService.getAccountsByUser(client, user);
			} catch (SQLException e) {
				// Internal server error
				throw new ServletException(e);
			} catch (NotAuthorizedException e) {
				// Client error: not authorized
				response.setStatus(400);
				return;
			}

			// Write status code and body
			Gson gson = new Gson();
			response.setStatus(200);
			response.getWriter().write(gson.toJson(accounts));
		} else {
			// URI didn't match anything expected, send back 400 level status code
			response.setStatus(400);
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Check if URI matches /accounts
		// This endpoint requires a session
		HttpSession session = request.getSession(false);

		// Send a 400 if there is no session
		if (session == null) {
			response.setStatus(400);
			return;
		}

		// Get the client, represented using the User class
		User client = null;
		try {
			client = UserService.getUserById((int) session.getAttribute("user_id"));
		} catch (SQLException | NoSuchUserException e) {
			// Internal server error
			throw new ServletException(e);
		}

		// Get the json in the request body as a map using gson
		Gson gson = new Gson();
		Map<String, Object> map = gson.fromJson(request.getReader(), new TypeToken<Map<String, Object>>() {
		}.getType());

		// We handle requests to /accounts, /accounts/transfer, /accounts/deposit,
		// /accounts/withdraw
		final String URI = request.getRequestURI().substring("/BankAPI/accounts".length());

		if (URI.matches("/?") ){
			//We hit /accounts. This endpoint creates a new Account
			Account newAccount = null;
			try {
				newAccount = AccountService.insertAccount(client, (Double) map.getOrDefault("amount", -1),
						UserService.getUserById(((Double) map.getOrDefault("userId", 0)).intValue()),
						AccountTypeService.getAccountTypeById(((Double) map.getOrDefault("typeId", 0)).intValue()),
						AccountStatusService
								.getAccountStatusById(((Double) map.getOrDefault("statusId", 0)).intValue()));
			} catch (SQLException e) {
				// Internal server error
				throw new ServletException(e);
			} catch (NotAuthorizedException | InvalidAccountParamsException | NoSuchUserException
					| NoSuchAccountTypeException | NoSuchAccountStatusException e) {
				// Client error
				response.setStatus(400);
				return;
			}

			// Write a 201 status code and the new account to the body
			response.setStatus(201);
			response.getWriter().write(gson.toJson(newAccount));
		}else if( URI.matches("^/deposit$") ){
			//We hit /deposit. This endpoint deposits to the given account

			//Get the account with the id specified in the request
			Account account = null;
			try {
				account = AccountService.getAccountById(((Double) map.getOrDefault("accountId", 0)).intValue());
			} catch (SQLException e1) {
				// Server error: respond with status code: 500
				throw new ServletException(e1);
			} catch (NoSuchAccountException e1) {
				// Client error: respond with status code: 400
				response.setStatus(400);
				return;
			}

			//Get the amount specified in the request
			Double amount = (Double) map.get("amount");
			//Check if it was supplied
			if( amount == null ){
				//amount not given in request json: respond with 400.
				response.setStatus(400);
				return;
			}

			//Call the service layer to make the deposit
			try {
				AccountService.deposit(client, account, amount);
			} catch (SQLException e) {
				// Response with status code: 500
				throw new ServletException(e);
			}catch (NotAuthorizedException e) {
				// Respond with status code: 400
				response.setStatus(400);
				return;
			}

			//Respond with 200 status code and message
			response.setStatus(200);
			response.getWriter().write(gson.toJson(Map
					.ofEntries(entry("message", 
						String.format("%f has been deposited to account %d", amount, account.getId())))));
		
		}else if( URI.matches("^/withdraw$") ){
			//We hit /withdraw. This endpoint withdraws from the given account

			//Get the account with the id specified in the request
			Account account = null;
			try {
				account = AccountService.getAccountById(((Double) map.getOrDefault("accountId", 0)).intValue());
			} catch (SQLException e1) {
				// Server error: respond with status code: 500
				throw new ServletException(e1);
			} catch (NoSuchAccountException e1) {
				// Client error: respond with status code: 400
				response.setStatus(400);
				return;
			}

			//Get the amount specified in the request
			Double amount = (Double) map.get("amount");
			//Check if it was supplied
			if( amount == null ){
				//amount not given in request json: respond with 400.
				response.setStatus(400);
				return;
			}

			//Call the service layer to make the deposit
			try {
				AccountService.withdraw(client, account, amount);
			} catch (SQLException e) {
				// Response with status code: 500
				throw new ServletException(e);
			}catch (NotAuthorizedException e) {
				// Respond with status code: 400
				response.setStatus(400);
				return;
			}

			//Respond with 200 status code and message
			response.setStatus(200);
			response.getWriter().write(gson.toJson(Map
					.ofEntries(entry("message", 
						String.format("%f has been withdrawn from account %d", amount, account.getId())))));
		}else if( URI.matches("^/transfer$") ){
			//We hit /transfer. This endpoint calls the transfer service

			//Get the accounts with the ids specified in the request
			Account account1 = null;
			Account account2 = null;
			try {
				account1 = AccountService.getAccountById(((Double) map.getOrDefault("sourceAccountId", 0)).intValue());
				account2 = AccountService.getAccountById(((Double) map.getOrDefault("targetAccountId", 0)).intValue());
			} catch (SQLException e1) {
				// Server error: respond with status code: 500
				throw new ServletException(e1);
			} catch (NoSuchAccountException e1) {
				// Client error: respond with status code: 400
				response.setStatus(400);
				return;
			}

			//Get the amount specified in the request
			Double amount = (Double) map.get("amount");
			//Check if it was supplied
			if( amount == null ){
				//amount not given in request json: respond with 400.
				response.setStatus(400);
				return;
			}

			//Call the service layer to make the deposit
			try {
				AccountService.transfer(client, account1, account2, amount);
			} catch (SQLException e) {
				// Response with status code: 500
				throw new ServletException(e);
			}catch (NotAuthorizedException e) {
				// Respond with status code: 400
				response.setStatus(400);
				return;
			}

			//Respond with 200 status code and message
			response.setStatus(200);
			response.getWriter().write(gson.toJson(Map
					.ofEntries(entry("message", 
						String.format("%f has been withdrawn from account %d and deposited to account %d", amount, account1.getId(), account2.getId())))));
		}

	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// This endpoint requires a session
		HttpSession session = request.getSession(false);

		// Send a 400 if there is no session
		if (session == null) {
			response.setStatus(400);
			return;
		}

		// Get the client, represented using the User class
		User client = null;
		try {
			client = UserService.getUserById((int) session.getAttribute("user_id"));
		} catch (SQLException | NoSuchUserException e) {
			// Internal server error
			throw new ServletException(e);
		}

		// Get the json in the request body as a map using gson
		Gson gson = new Gson();
		Map<String, Object> map = gson.fromJson(request.getReader(), new TypeToken<Map<String, Object>>() {
		}.getType());

		Account updatedAccount = null;
		try {
			updatedAccount = AccountService.updateAccount(client,
					AccountService.getAccountById(((Double) map.getOrDefault("id", 0)).intValue()),
					(Double) map.getOrDefault("amount", 0),
					AccountTypeService.getAccountTypeById(((Double) map.getOrDefault("typeId", 0)).intValue()),
					AccountStatusService.getAccountStatusById(((Double) map.getOrDefault("statusId", 0)).intValue()));
		} catch (SQLException e) {
			// Internal server error
			throw new ServletException(e);
		} catch (NotAuthorizedException | InvalidAccountParamsException | NoSuchAccountException
				| NoSuchAccountTypeException | NoSuchAccountStatusException e) {
			// Client error
			response.setStatus(400);
			return;
		}

		// Write a 201 status code and the new account to the body
		response.setStatus(200);
		response.getWriter().write(gson.toJson(updatedAccount));
	}

}
