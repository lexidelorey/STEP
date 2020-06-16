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
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

public final class FindMeetingQuery {
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    //list of all possible meeting times
    List<TimeRange> meetingTimes = new ArrayList<>();

    //meeting cannot be longer than the whole day 
    long duration = request.getDuration();
    if(duration > TimeRange.WHOLE_DAY.duration()) {
      return meetingTimes;
    }

    //if there are no events in the day, then the meeting could happen at any time
    if (events.isEmpty()) {
      meetingTimes.add(TimeRange.WHOLE_DAY);
      return meetingTimes;
    }

    Collection<String> attendees = request.getAttendees();

    List<TimeRange> timeRanges = new ArrayList<>();
    //check if any requested meeting attendees are attending any events 
    for (Event e : events) {
      List<String> relevantAttendees = new ArrayList<String>(attendees);
      relevantAttendees.retainAll(e.getAttendees());
 
      //if any are, add the time range of the event to the timeRanges list
      if (!relevantAttendees.isEmpty()) {
        timeRanges.add(e.getWhen());
      }
    }

    /*
      If there are no times that the meeting cannot happen, available all day.
      this will only happen if the person invited to the meeting is not invited
      to the event(s).
    */
    if (timeRanges.isEmpty()) {
      meetingTimes.add(TimeRange.WHOLE_DAY);
      return meetingTimes;
    }

    //sort the time ranges by start time in ascending order 
    Collections.sort(timeRanges, TimeRange.ORDER_BY_START);

    //check if time range containes the time range after it, if so
    //combine them, if not continue on
    for (int i = 0; i < timeRanges.size() - 1; ++i) {
      if (timeRanges.get(i).overlaps(timeRanges.get(i+1))) {
        int end1 = timeRanges.get(i).end();
        int end2 = timeRanges.get(i+1).end();
        int laterEnd;
        laterEnd = end1 >= end2 ? end1 : end2;
        timeRanges.add(TimeRange.fromStartEnd(timeRanges.get(i).start(), laterEnd, false));
        timeRanges.remove(i);
        timeRanges.remove(i);
      }
    }

    /*
      create array of time ranges that are available by filling in the
      gaps in timeRanges (which holds all the times a meeting can't be).
      Checks to make sure each time range fits the specified duration
    */
    TimeRange tr;
    boolean trBool;
    for (int i = 0; i < timeRanges.size(); ++i) {
      //if there is only one time when the meeting can't be, split the time ranges around that    
      if (timeRanges.size() == 1) {
        TimeRange tr1 = TimeRange.fromStartEnd(TimeRange.START_OF_DAY, timeRanges.get(0).start(), false);
        TimeRange tr2 = TimeRange.fromStartEnd(timeRanges.get(0).end(), TimeRange.END_OF_DAY, true);
        boolean tr1Bool = checkDuration(tr1, duration);
        boolean tr2Bool = checkDuration(tr2, duration);
        if (tr1Bool) {
          meetingTimes.add(tr1);
        }
        if (tr2Bool) {
          meetingTimes.add(tr2);
        }
        break;
      }
      if (i == 0 && timeRanges.get(i).start() != TimeRange.START_OF_DAY) {
        tr = TimeRange.fromStartEnd(TimeRange.START_OF_DAY, timeRanges.get(i).start(), false);
        trBool = checkDuration(tr, duration);
        if (trBool) {
          meetingTimes.add(tr);
        }
      }
      if (i == timeRanges.size() - 1 && timeRanges.get(i).end() != TimeRange.END_OF_DAY) {
        tr = TimeRange.fromStartEnd(timeRanges.get(i).end(), TimeRange.END_OF_DAY, true);
        trBool = checkDuration(tr, duration);
        if (trBool) {
          meetingTimes.add(tr);
        }
        break;
      }
      tr = TimeRange.fromStartEnd(timeRanges.get(i).end(), timeRanges.get(i+1).start(), false);
      trBool = checkDuration(tr, duration);
      if (trBool) {
        meetingTimes.add(tr);
      }
    }

    return meetingTimes;
  }

  private boolean checkDuration(TimeRange tr, long duration) {
    return tr.duration() >= duration;
  }
}
