// Copyright 2020 Google LLC
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

/** Servlet responsible for updating the maximum amount of comments displayed. */ 
@WebServlet("/update-data")
public class UpdateServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
   
    int quantity = Integer.parseInt(request.getParameter("quantity"));
    System.out.println(quantity);

    // Create a new query
    Query query = new Query("Task");    

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);

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
}