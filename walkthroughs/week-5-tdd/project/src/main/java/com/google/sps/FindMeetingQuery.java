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

package com.google.sps;

import java.util.Collection;
import java.util.HashSet;
import java.util.Arrays;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;



public final class FindMeetingQuery {

  // create a set of events to avoid duplicates
  private Collection<Event> events = new HashSet<>();

  // the meeting request
  private MeetingRequest request; 

  private static final Collection<Event> NO_EVENTS = Collections.emptySet();
  private static final Collection<String> NO_ATTENDEES = Collections.emptySet();

  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
 
    this.events = events;
    this.request = request; 

    // Check if duration is longer than a day
    if (request.getDuration() > TimeRange.WHOLE_DAY.duration()){
        return Arrays.asList(); 
    }
    // Check for no events or no attendees
    if (events == NO_EVENTS || request.getAttendees() == NO_ATTENDEES) {
        return Arrays.asList((TimeRange.WHOLE_DAY)); 
    }
    // Add event time ranges to array      
    List<TimeRange> times = new ArrayList();
    for (Event event: events) {
        times.add(event.getWhen());

        // Check if attendees from event are requested for meeting
        for (String attendee : event.getAttendees()) {
            if (!(request.getAttendees().contains(attendee))) {
                return Arrays.asList((TimeRange.WHOLE_DAY)); 
            }
        }
    }

    // Sort list of time ranges in ascending order
    Collections.sort(times, TimeRange.ORDER_BY_START);

    // Create empty Arraylist of options
    List<TimeRange> options = new ArrayList();

    int start = TimeRange.START_OF_DAY;
    int end = TimeRange.END_OF_DAY; 
    int lastEventEnd = 0;

    for (int i = 0; i <= (times.size() -1); i++)  {
        // Get event start and end time
        int eventStart = times.get(i).start();
        int eventEnd = times.get(i).end(); 
    
        // Check for overlap
        if (start < eventStart) {
            if (start != eventStart) {
                // Check if there is enough room for a meeting
                if ((eventStart - start) > request.getDuration() || (eventStart - start) == request.getDuration()) {
                    options.add(TimeRange.fromStartEnd(start,eventStart,false)); 
                }
                start = eventEnd; 
                lastEventEnd = eventEnd;
            }
            else {
                start = eventEnd;
                lastEventEnd = eventEnd; 
            }
        }
        else {
            if (start < eventEnd) {
                start = eventEnd;
                lastEventEnd = eventEnd;
            }
            else {
                lastEventEnd = start; 
            }
        }
    } 

    // Check if last event ends when day ends
    if (lastEventEnd != end+1) {
        options.add(TimeRange.fromStartEnd(lastEventEnd,end,true)); 
    }

    // Return timeRange options list
    return options;
  }
} 
