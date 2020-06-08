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

/** Adds a random fact about me to the page.*/
function addRandomFact() {
  const facts = ['I love tropical destinations and spending time by the ocean!', 'I have torn the ACLs in both my knees playing soccer (at separate times). I got surgery to repair both of them.', 'I am a huge cat person (but I love most pets too).', 'I am a huge foodie. My favourite foods are sushi, thai basil beef and tacos.',
  'I have a huge sweet tooth for desserts, especially any homemade baked good.', 'I\m a pretty decent mariokart player.', 'I love being creative. I like drawing, painting, and any form of art.'];

  // Pick a random fact.
  const fact = facts[Math.floor(Math.random() * facts.length)];

  // Add it to the page.
  const factContainer = document.getElementById('fact-container');
  factContainer.innerText = fact;
}

/** Fetches tasks from the server and adds them to the DOM. */
function loadTasks() {
  // get user selection for quantity
  getSelect(); 

  fetch('/data').then(response => response.json()).then((tasks) => {
    const taskListElement = document.getElementById('sentence-list');
    tasks.forEach((task) => {
      console.log(taskListElement.appendChild(createTaskElement(task)));
    })
  });
}

/** Creates an element that represents a task, including its delete button. */
function createTaskElement(task) {
  const taskElement = document.createElement('li');
  taskElement.className = 'task';

  const sentence = document.createElement('span');
  sentence.innerText = ("Last Sunday, I was " + task.verb + " and I saw this " 
  + task.adj + " " + task.animal + ", who was also " + task.verb +"."); 

  const deleteButtonElement = document.createElement('button');
  deleteButtonElement.innerText = 'Delete';
  deleteButtonElement.addEventListener('click', () => {
  deleteTask(task);

  // Remove the task from the DOM.
  taskElement.remove();
  });

  taskElement.appendChild(sentence);
  taskElement.appendChild(deleteButtonElement);
  return taskElement;
}

/** Tells the server to delete the task. */
function deleteTask(task) {
  const params = new URLSearchParams();
  params.append('id', task.id);
  fetch('/delete-data', {method: 'POST', body: params});
}

/** Gets user input from display quantity drop down menu */
function getSelect() {
   var quantity = document.getElementById("quantity"); 
}
