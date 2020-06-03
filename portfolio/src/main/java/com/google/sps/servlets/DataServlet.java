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
import java.util.ArrayList; 
import com.google.gson.Gson;


/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {

/**
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    
    ArrayList<String> messages = new ArrayList<String>(); 
    messages.add("hello");
    messages.add("bonjour");
    messages.add("hola"); 

    // Converts Arraylist into JSON string using Gson
    String json = new Gson().toJson(messages);

    // Send the JSON as the response
    response.setContentType("application/json;");
    response.getWriter().println(json);


    response.setContentType("text/html;");
    response.getWriter().println("<h1>Hello Emily!</h1>");

  }
**/ 

   @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
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
    response.getWriter().println("Last Sunday, I was " + verb+ " and I saw this " + adj + " " + animal +", who was also " + verb +".");

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
