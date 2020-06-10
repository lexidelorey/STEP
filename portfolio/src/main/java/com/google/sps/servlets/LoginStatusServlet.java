package com.google.sps.servlets;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import java.io.IOException;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import org.json.JSONObject;

@WebServlet("/login-status")
public class LoginStatusServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    UserService userService = UserServiceFactory.getUserService();
    Boolean loggedIn = userService.isUserLoggedIn();
    JSONObject json = new JSONObject();

    //check login status and put appropriate JSON
    if (!loggedIn) {
      String urlToRedirectToAfterUserLogsIn = "/nickname";
      String loginUrl = userService.createLoginURL(urlToRedirectToAfterUserLogsIn);

      json.put("loggedIn", "false");
      json.put("loginUrl", loginUrl);
    }
    else if(loggedIn) {
      String urlToRedirectToAfterUserLogsOut = "/index.html";
      String logoutUrl = userService.createLogoutURL(urlToRedirectToAfterUserLogsOut);
 
      json.put("loggedIn", "true");
      json.put("logoutUrl", logoutUrl);

      //if no nickname is set, redirect them to nickname form
      String nickname = getUserNickname(userService.getCurrentUser().getUserId());
      json.put("nickname", nickname);

    }

    response.setContentType("application/json;");
    response.getWriter().println(json);
  }
  private String getUserNickname(String id) {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Query query =
        new Query("UserInfo")
            .setFilter(new Query.FilterPredicate("id", Query.FilterOperator.EQUAL, id));
    PreparedQuery results = datastore.prepare(query);
    Entity entity = results.asSingleEntity();
    if (entity == null) {
      return null;
    }
    String nickname = (String) entity.getProperty("nickname");
    return nickname;
  }
}
