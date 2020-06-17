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
    //list of all applicable meeting times
    List<TimeRange> meetingTimes = new ArrayList<>();

    /*
       PRECONDITIONS:
         meeting cannot be longer than the whole day 
         if there are no events in the day, then the meeting could happen at any time
    */
    long duration = request.getDuration();
    if(duration > TimeRange.WHOLE_DAY.duration()) {
      return meetingTimes;
    }
    if (events.isEmpty()) {
      meetingTimes.add(TimeRange.WHOLE_DAY);
      return meetingTimes;
    }

    Collection<String> attendees = request.getAttendees();
    Collection<String> optionalAttendees = request.getOptionalAttendees();

    List<TimeRange> timeRanges = new ArrayList<>();
    List<TimeRange> optionalTimeRanges = new ArrayList<>();
    //check if any requested meeting attendees are attending any events
    //including optional attendees 
    for (Event e : events) {
      List<String> relevantAttendees = new ArrayList<String>(attendees);
      List<String> relevantOptionalAttendees = new ArrayList<String>(optionalAttendees);
      relevantAttendees.retainAll(e.getAttendees());
      relevantOptionalAttendees.retainAll(e.getAttendees());
 
      //if any are, add the time range of the event to the timeRanges list
      if (!relevantAttendees.isEmpty()) {
        timeRanges.add(e.getWhen());
      }
      
      if (!relevantOptionalAttendees.isEmpty()){
        optionalTimeRanges.add(e.getWhen());
      }
    }

    /*
      If timeRanges and optionalTimeRanges are empty, that means neither
      attendees or optional attendees have any other events scheduled for the day,
      meaning they are free all day for the meeting
    */
    if (timeRanges.isEmpty() && optionalTimeRanges.isEmpty()) {
      meetingTimes.add(TimeRange.WHOLE_DAY);
      return meetingTimes;
    }

    /*
      if timeranges is empty but optionalTimeRanges isnt, this means that 
      the only restrictions to time will come from optional attendees, which
      means they are the only ones that need to be considered when building
      an array of available times
    */
    if (timeRanges.isEmpty() && !optionalTimeRanges.isEmpty()) {
      Collections.sort(optionalTimeRanges, TimeRange.ORDER_BY_START);
      optionalTimeRanges = findOverlap(optionalTimeRanges);
      optionalTimeRanges = findMeetingTime(optionalTimeRanges, duration);
      return optionalTimeRanges;
    }

    /*
      if timeRanges isn't empty but optionalTimeRanges is, that means that 
      the only restrictions to time will come from mandatory attendees, which
      means they are the only ones that ened to be considered when building
      an array of available times
    */
    if (!timeRanges.isEmpty() && optionalTimeRanges.isEmpty()) {
      Collections.sort(timeRanges, TimeRange.ORDER_BY_START);
      timeRanges = findOverlap(timeRanges);
      timeRanges = findMeetingTime(timeRanges, duration);
      return timeRanges;
    }

    //sort the time ranges by start time in ascending order 
    Collections.sort(timeRanges, TimeRange.ORDER_BY_START);
    Collections.sort(optionalTimeRanges, TimeRange.ORDER_BY_START);

    //check if time range containes the time range after it, if so
    //combine them, if not continue on
    timeRanges = findOverlap(timeRanges);
    optionalTimeRanges = findOverlap(optionalTimeRanges);

    /*
      create array of time ranges that are available by filling in the
      gaps in timeRanges (which holds all the times a meeting can't be).
      Checks to make sure each time range fits the specified duration
    */
    timeRanges = findMeetingTime(timeRanges, duration);
    optionalTimeRanges = findMeetingTime(optionalTimeRanges, duration);

    return filterTimeRanges(timeRanges, optionalTimeRanges, duration);
  }

  //check if time range containes the time range after it, if so
  //combine them, if not continue on
  private List<TimeRange> findOverlap(List<TimeRange> timeRanges) {
    for (int i = 0; i < timeRanges.size() - 1;) {
      if(timeRanges.get(i).end() == timeRanges.get(i+1).start()) {
        boolean isEndOfDay = timeRanges.get(i+1).end() == TimeRange.END_OF_DAY ?
            true : false;
        timeRanges.add(TimeRange.fromStartEnd(timeRanges.get(i).start(), 
            timeRanges.get(i+1).end(), isEndOfDay));
      }
      if (timeRanges.get(i).overlaps(timeRanges.get(i+1))) {
        int end1 = timeRanges.get(i).end();
        int end2 = timeRanges.get(i+1).end();
        int laterEnd;
        laterEnd = end1 >= end2 ? end1 : end2;
        timeRanges.add(TimeRange.fromStartEnd(timeRanges.get(i).start(), laterEnd, false));
        timeRanges.remove(i);
        timeRanges.remove(i);
      }
      else {
        ++i;
      }
    }
    Collections.sort(timeRanges, TimeRange.ORDER_BY_START);
    return timeRanges;
  }

  private boolean checkDuration(TimeRange tr, long duration) {
    return tr.duration() >= duration;
  }

   /*
    create array of time ranges that are available by filling in the
    gaps in timeRanges (which holds all the times a meeting can't be).
    Checks to make sure each time range fits the specified duration
   */

  private List<TimeRange> findMeetingTime(List<TimeRange> timeRanges, long duration) {
    TimeRange tr;
    boolean trBool;
    List<TimeRange> meetingTimes = new ArrayList<>();
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

  private List<TimeRange> filterTimeRanges (List<TimeRange> timeRanges, List<TimeRange> optionalTimeRanges, 
          long duration) {
    List<TimeRange> meetingTimes = new ArrayList<>();
    for (int i = 0; i < optionalTimeRanges.size(); ++i) {
      for (int j = 0; j < timeRanges.size(); ++j) {
        if (timeRanges.get(j).overlaps(optionalTimeRanges.get(i))) {
          boolean optionalStartTimeFits = 
              timeRanges.get(j).start() + duration <= optionalTimeRanges.get(i).end();
          if (optionalStartTimeFits) {
            int newStart = timeRanges.get(j).start();
            int newEnd = timeRanges.get(j).end() <= optionalTimeRanges.get(i).end() ? 
                timeRanges.get(j).end() : optionalTimeRanges.get(i).end();
            boolean isEndOfDay = newEnd == TimeRange.END_OF_DAY ? true : false;
            TimeRange newTR = TimeRange.fromStartEnd(newStart, newEnd, isEndOfDay);
            if (checkDuration(newTR,  duration)) {
              meetingTimes.add(newTR);
            }
          }
        }
      }
    }
    return meetingTimes.isEmpty() ? timeRanges : meetingTimes;
  }
}
