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
import com.revature.exceptions.InvalidAccountStatusParamsException;
import com.revature.model.AccountStatus;
import com.revature.services.AccountStatusService;

/**
 * Servlet implementation class AccountStatusServlet
 */
public class AccountStatusServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AccountStatusServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// The status' to return
		ArrayList<AccountStatus> accountStatuss = null;
		try {
			accountStatuss = AccountStatusService.getAllAccountStatuss();
		} catch (SQLException e) {
			// Internal server error
			throw new ServletException(e);
		}

		// Write a 200 status code and the accountStatuss in the body
		Gson gson = new Gson();
		response.setStatus(200);
		response.getWriter().write(gson.toJson(accountStatuss));
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

		// The AccountStatus that is created
		AccountStatus accountStatus = null;
		try {
			accountStatus = AccountStatusService.insertAccountStatus((String) map.getOrDefault("status", ""));
		} catch (SQLException e) {
			// Internal server error
			throw new ServletException(e);
		} catch (InvalidAccountStatusParamsException e) {
			// Client error
			response.setStatus(400);
			return;
		}

		// Write a 200 status code and the accountStatus in the body
		response.setStatus(200);
		response.getWriter().write(gson.toJson(accountStatus));
	}

}
