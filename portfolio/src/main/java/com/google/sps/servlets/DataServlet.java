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

import java.io.IOException;
import java.util.Arrays;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.FetchOptions;
import java.util.ArrayList; 
import java.util.List;
import com.google.gson.Gson;
import com.google.sps.data.Task;



/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {

  private int quantity = 5; 

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    // Create a new query
    Query query = new Query("Task");    

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);

    // Get query string
    //int limit = Integer.parseInt(request.getQueryString()); 

    // Set query limit to control maximum number of comments
    List<Entity> sentence = results.asList(FetchOptions.Builder.withLimit(quantity));
    List<Task> tasks = new ArrayList<>();
    for (Entity entity : sentence) {
        long id = entity.getKey().getId();
        String animal = (String) entity.getProperty("animal");
        String verb = (String) entity.getProperty("verb");
        String adj = (String) entity.getProperty("adj");

        Task task = new Task(id, animal, verb, adj);
        tasks.add(task);

    }

    response.setContentType("application/json;");
    String json = new Gson().toJson(tasks);
    response.getWriter().println(json);
  }

   @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    
    quantity = Integer.parseInt(request.getParameter("quantity"));
    System.out.println(quantity);


    // Get the input from the form.
    String animal = getParameter(request, "animal", "");
    String verb = getParameter(request, "verb", "");
    String adj = getParameter(request, "adj", "");

    // Convert all text to lower case.
    animal = animal.toLowerCase(); 
    verb = verb.toLowerCase();
    adj = adj.toLowerCase(); 

    // Respond with the result.
    response.setContentType("text/html;");
    response.getWriter().println("Last Sunday, I was " + verb+ " and I saw this " + adj + " " + animal +", who was also " + verb +".") ;
    response.getWriter().println("<p><a href=\"/\">Back</a></p>");

    // Create an entity and set its properties 
    Entity taskEntity = new Entity("Task");
    taskEntity.setProperty("animal", animal);
    taskEntity.setProperty("verb", verb);
    taskEntity.setProperty("adj", adj);

    // Create an instance of DatastoreService class
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(taskEntity);

    response.sendRedirect("/index.html");

  }

  /**
   * @return the request parameter, or the default value if the parameter
   *         was not specified by the client
   */
  private String getParameter(HttpServletRequest request, String name, String defaultValue) {
    String value = request.getParameter(name);
    if (value == null) {
      return defaultValue;
    }
    return value;
  }

}

