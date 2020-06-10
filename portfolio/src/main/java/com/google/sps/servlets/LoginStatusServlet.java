package com.google.sps.servlets;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;

@WebServlet("/login-status")
public class LoginStatusServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    UserService userService = UserServiceFactory.getUserService();
    Boolean loggedIn = userService.isUserLoggedIn();

    String json = "{";

    if (loggedIn) {
      String userEmail = userService.getCurrentUser().getEmail();
      String urlToRedirectToAfterUserLogsOut = "/login-status";
      String logoutUrl = userService.createLogoutURL(urlToRedirectToAfterUserLogsOut);

      json += "\"loggedIn\": \"true\"";
      json += ", ";
      json += "\"logoutUrl\": " + "\"" + logoutUrl + "\"";
      json += "}"; 

      response.setContentType("application/json;");
      response.getWriter().println(json);
    } else {
      String urlToRedirectToAfterUserLogsIn = "/login-status";
      String loginUrl = userService.createLoginURL(urlToRedirectToAfterUserLogsIn);

      json += "\"loggedIn\": \"false\"";
      json += ", ";
      json += "\"loginUrl\": " + "\"" + loginUrl + "\"";
      json += "}"; 

      response.setContentType("application/json;");
      response.getWriter().println(json);
    }
  }
}