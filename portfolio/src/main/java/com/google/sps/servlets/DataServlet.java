// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.google.sps.data.Comment;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {

  private static final String COMMENT_ENTITY_KEY = "Comment";
  private static final String COMMENT_PROPERTY_NAME = "userComment";
  private static final String TIME_PROPERTY_NAME = "dateTimeCreated";
  private static final String COMMENT_PARAM_NAME = "comment";
  

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Query query = new Query(COMMENT_ENTITY_KEY)
                  .addSort(TIME_PROPERTY_NAME, SortDirection.DESCENDING);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);

    List<Comment> comments = new ArrayList<>();
    for (Entity entity : results.asIterable()) {
      String text = (String) entity.getProperty(COMMENT_PROPERTY_NAME);
      Date dateTimeCreated = (Date) entity.getProperty(TIME_PROPERTY_NAME);

      Comment userComment = new Comment(text, dateTimeCreated);
      comments.add(userComment);
    }

    Gson gson = new Gson();
    response.setContentType("application/json;");
    response.getWriter().println(gson.toJson(comments));
  }


  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String userComment = request.getParameter(COMMENT_PARAM_NAME);
    SimpleDateFormat format= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
    Date dateTimeCreated = new Date(System.currentTimeMillis());

    Entity commentEntity = new Entity(COMMENT_ENTITY_KEY);
    commentEntity.setProperty(COMMENT_PROPERTY_NAME, userComment);
    commentEntity.setProperty(TIME_PROPERTY_NAME, dateTimeCreated);
    
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(commentEntity);

    response.sendRedirect("/index.html"); 
  }

}
