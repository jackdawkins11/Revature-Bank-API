package com.revature.controller;

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
import com.revature.exceptions.InvalidUserParamsException;
import com.revature.exceptions.NoSuchRoleException;
import com.revature.exceptions.NoSuchUserException;
import com.revature.exceptions.NotAuthorizedException;
import com.revature.model.User;
import com.revature.services.RoleService;
import com.revature.services.UserService;

/**
 * Servlet implementation class UserServlet
 * 
 * Handles requests to '/users/*'
 */
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UserServlet() {
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

		// We handle requests to both /users/:id and /users
		final String URI = request.getRequestURI().substring("/BankAPI/users".length());

		if (URI.length() == 0) {
			// We hit /users

			// What is returned
			ArrayList<User> users = null;
			try {
				users = UserService.getAllUsers(client);
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
			response.getWriter().write(gson.toJson(users));
		} else if (URI.matches("^/\\d+$")) {
			// We hit /users/:id
			int id = Integer.parseInt(URI.substring(1));

			// The user to return
			User user = null;
			try {
				user = UserService.authorizeAndGetUserById(client, id);
			} catch (SQLException e) {
				// Internal server error
				throw new ServletException(e);
			} catch (NotAuthorizedException | NoSuchUserException e) {
				// Client error: invalid id or not authorized
				response.setStatus(400);
				return;
			}

			// Write status code and body using gson
			Gson gson = new Gson();
			response.setStatus(200);
			response.getWriter().write(gson.toJson(user));
		} else {
			// Client error: invalid URI
			response.setStatus(400);
			return;
		}
	}

	/**
	 * 
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

		// Call the updateUser method
		// Note how gson automatically turns numbers into the Double class
		// So we have to use the intValue() method to cast to int
		User updatedUser = null;
		try {
			updatedUser = UserService.update(client, UserService.getUserById(((Double) map.getOrDefault("userId", 0)).intValue()),
					(String) map.getOrDefault("username", ""),
					(String) map.getOrDefault("password", ""),
					(String) map.getOrDefault("firstName", ""),
					(String) map.getOrDefault("lastName", ""),
					(String) map.getOrDefault("email", ""),
					RoleService.getRoleById(((Double) map.getOrDefault("roleId", 0)).intValue()));
		} catch (SQLException e) {
			// Internal server error
			throw new ServletException(e);
		} catch (NotAuthorizedException | InvalidUserParamsException | NoSuchUserException
				| NoSuchRoleException e) {
			e.printStackTrace();
			// Client error
			response.setStatus(400);
			return;
		}

		// Write the status code and body using gson
		response.setStatus(200);
		response.getWriter().write(gson.toJson(updatedUser));
	}

}
