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
import com.revature.exceptions.InvalidRoleParamsException;
import com.revature.model.Role;
import com.revature.services.RoleService;

/**
 * Servlet implementation class RoleServlet
 */
public class RoleServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RoleServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// The roles to return
		ArrayList<Role> roles = null;
		try {
			roles = RoleService.getAllRoles();
		} catch (SQLException e) {
			// Internal server error
			throw new ServletException(e);
		}

		// Write a 200 status code and the roles in the body
		Gson gson = new Gson();
		response.setStatus(200);
		response.getWriter().write(gson.toJson(roles));
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

		// The role that is created
		Role role = null;
		try {
			role = RoleService.insertRole((String) map.getOrDefault("role", ""));
		} catch (SQLException e) {
			// Internal server error
			throw new ServletException(e);
		} catch (InvalidRoleParamsException e) {
			// Client error
			response.setStatus(400);
			return;
		}

		// Write a 200 status code and the role in the body
		response.setStatus(200);
		response.getWriter().write(gson.toJson(role));
	}

}
