package com.google.sps.servlets;

import java.io.IOException;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static constants.DataStoreHelper.COMMENT_ENTITY_KEY;

@WebServlet("/likes")
public class LikesServlet extends HttpServlet {
  
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {  
    long id = Long.parseLong(request.getParameter("id"));
    long likes = Long.parseLong(request.getParameter("likes"));
    
    Query query = new Query(COMMENT_ENTITY_KEY);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);

    for (Entity entity : results.asIterable()) {
      if (entity.getKey().getId() == id) {
        entity.setProperty("likes", likes);
        datastore.put(entity);
      }
    }
  }
}
