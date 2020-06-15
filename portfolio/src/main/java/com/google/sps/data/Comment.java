package com.google.sps.data;
import java.util.Date;

public final class Comment {

  private long id;
  private String name;
  private String comment;
  private Date dateTime;
  private String email;

  public Comment(long id, String name, String comment, Date dateTime, String email) {
    this.id = id;
    this.name = name;
    this.comment = comment;
    this.dateTime = dateTime;
    this.email = email;
  }
}
