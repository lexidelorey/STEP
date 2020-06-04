package com.google.sps.data;
import java.util.Date;

public final class Comment {

  private String comment;
  private Date dateTime;

  public Comment(String comment, Date dateTime) {
    this.comment = comment;
    this.dateTime = dateTime;
  }
}
