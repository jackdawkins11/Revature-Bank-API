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
import com.revature.exceptions.NoSuchUserException;
import com.revature.model.User;
import com.revature.services.UserService;

/**
 * Servlet implementation class LogoutServlet
 */
public class LogoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Gson will be used
		Gson gson = new Gson();

		// Get the session
		HttpSession session = request.getSession(false);

		if (session != null) {
			// Get the client
			User client = null;
			try {
				client = UserService.getUserById((int) session.getAttribute("user_id"));
			} catch (SQLException | NoSuchUserException e) {
				// This is a 500 level error -- let web container handle it
				throw new ServletException(e);
			}
			//Delete the session
			session.invalidate();
			
			// Write the response
			response.getWriter().write(gson.toJson(Map
					.ofEntries(entry("message", String.format("You have been logged out %s", client.getFirstName())))));

		}else {
			//Write the 400 status and response indicating there was no session
			response.setStatus(400);
			response.getWriter().write(gson.toJson(Map
					.ofEntries(entry("message", "There was no user logged into the session"))));
		}
	}

}
