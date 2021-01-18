package com.revature.controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.revature.exceptions.NoSuchUserException;
import com.revature.model.User;
import com.revature.services.UserService;

public class CheckSessionServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException,
            ServletException {
        //Get the existing session if it exists
        HttpSession session = request.getSession(false);

        //If there is no existing session, return 400 status code
        if( session == null ){
            response.setStatus(400);
            return;
        }

        //Get the User representation of the client
        //This should always exist when there is a session
        //so return 500 on failure
        User client = null;
        try{
            client = UserService.getUserById( (int) session.getAttribute("user_id") );
        }catch(SQLException | NoSuchUserException e){
            response.setStatus(500);
            return;
        }

        //Write 200 status code and client in body using Gson
        Gson gson = new Gson();
        response.setStatus(200);
        response.getWriter().write(gson.toJson(client));
    }
}
