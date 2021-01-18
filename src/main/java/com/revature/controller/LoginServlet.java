package com.revature.controller;

import static java.util.Map.entry;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.revature.exceptions.InvalidCredentialsException;
import com.revature.model.User;
import com.revature.services.UserService;

/**
 * Servlet implementation class LoginServlet
 */
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Get the json in the request body as a map
		Gson gson = new Gson();
		Map<String, Object> map = gson.fromJson(request.getReader(), new TypeToken<Map<String, Object>>() {
		}.getType());

		// Call authorize, getting the authorized client or an exception
		User client;
		try {
			client = UserService.authorize((String) map.getOrDefault("username", ""),
					(String) map.getOrDefault("password", ""));
		} catch (SQLException e) {
			// This is an internal server error -- throw it
			throw new ServletException(e);
		} catch (InvalidCredentialsException e) {
			// This is a client error -- handle differently
			response.setStatus(400);
			response.getWriter().write(gson.toJson(Map.ofEntries(entry("message", "Invalid credentials"))));
			return;
		}
		
		//Start a session, storing the client's id
		HttpSession session = request.getSession();
		session.setAttribute("user_id", client.getId());

		// Write the status code and body
		response.setStatus(200);
		response.getWriter().write(gson.toJson(client));
	}

}
