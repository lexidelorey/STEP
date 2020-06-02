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
import com.google.gson.Gson;
import java.io.IOException;
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
  private static final String COMMENT_PROPERTY_NAME = "comment";
  private static final String TIME_PROPERTY_NAME = "dateTime";
  
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String text = request.getParameter(COMMENT_PROPERTY_NAME);
    long time = System.currentTimeMillis();
    SimpleDateFormat format= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
    Date dateTime = new Date(System.currentTimeMillis());

    Entity commentEntity = new Entity(COMMENT_ENTITY_KEY);
    commentEntity.setProperty(COMMENT_PROPERTY_NAME, text);
    commentEntity.setProperty(TIME_PROPERTY_NAME, dateTime);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(commentEntity);

    response.setContentType("text/html;");
    response.getWriter()
            .println("Thank you for your comment! Redirecting you back to portfolio...");
    response.sendRedirect("/index.html"); 

  }

}
