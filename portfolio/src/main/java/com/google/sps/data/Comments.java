package com.google.sps.data;
import java.util.Date;

public final class Comments {

  private long id;
  private String comment;
  private Date dateTime;

  public Comments(long id, String comment, Date dateTime) {
    this.id = id;
    this.comment = comment;
    this.dateTime = dateTime;
  }
}