package com.revature.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.revature.exceptions.InvalidUserParamsException;
import com.revature.exceptions.NoSuchRoleException;
import com.revature.exceptions.NoSuchUserException;
import com.revature.exceptions.NotAuthorizedException;
import com.revature.model.User;
import com.revature.services.RoleService;
import com.revature.services.UserService;

/**
 * Servlet implementation class RegisterServlet
 */
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// This endpoint requires a session
		HttpSession session = request.getSession(false);

		// Send a 400 if there is no session
		if (session == null) {
			response.setStatus(400);
			return;
		}

		try {
			// Get the client, represented using the User class
			int id = (int) session.getAttribute("user_id");
			User client = UserService.getUserById(id);

			// Get the json in the request body as a map using gson
			Gson gson = new Gson();
			Map<String, Object> map = gson.fromJson(request.getReader(), new TypeToken<Map<String, Object>>() {
			}.getType());

			// Call the register method
			// Note how gson automatically turns numbers into the Double class
			// So we have to use the intValue() method to cast to int
			User newUser = UserService.register(client, (String) map.getOrDefault("username", ""),
					(String) map.getOrDefault("password", ""), (String) map.getOrDefault("firstName", ""),
					(String) map.getOrDefault("lastName", ""), (String) map.getOrDefault("email", ""),
					RoleService.getRoleById(((Double) map.getOrDefault("roleId", 0)).intValue()));

			// Write the status code
			response.setStatus(201);

			// Write the response
			response.getWriter().write(gson.toJson(newUser));
		} catch (JsonSyntaxException | SQLException | NoSuchUserException | IOException e) {
			// TODO What should I do here?
			// I could
			// throw ServletException
			// write status code and body manually
			// forward to error servlet? How do I pass parameters while forwarding
			throw new ServletException(e);
		} catch (NotAuthorizedException | InvalidUserParamsException | NoSuchRoleException e) {
			// Client side error: send 400 status code
			response.setStatus(400);
		}

	}

}
