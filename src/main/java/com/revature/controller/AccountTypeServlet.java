package com.revature.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.revature.exceptions.InvalidAccountTypeParamsException;
import com.revature.model.AccountType;
import com.revature.services.AccountTypeService;

/**
 * Servlet implementation class AccountTypeServlet
 */
public class AccountTypeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AccountTypeServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// The types to return
		ArrayList<AccountType> accountTypes = null;
		try {
			accountTypes = AccountTypeService.getAllAccountTypes();
		} catch (SQLException e) {
			// Internal server error
			throw new ServletException(e);
		}

		// Write a 200 status code and the accountTypes in the body
		Gson gson = new Gson();
		response.setStatus(200);
		response.getWriter().write(gson.toJson(accountTypes));
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Get the json in the request body as a map using gson
		Gson gson = new Gson();
		Map<String, Object> map = gson.fromJson(request.getReader(), new TypeToken<Map<String, Object>>() {
		}.getType());

		// The AccountType that is created
		AccountType accountType = null;
		try {
			accountType = AccountTypeService.insertAccountType((String) map.getOrDefault("type", ""));
		} catch (SQLException e) {
			// Internal server error
			throw new ServletException(e);
		} catch (InvalidAccountTypeParamsException e) {
			// Client error
			response.setStatus(400);
			return;
		}

		// Write a 200 status code and the accountType in the body
		response.setStatus(200);
		response.getWriter().write(gson.toJson(accountType));
	}

}
