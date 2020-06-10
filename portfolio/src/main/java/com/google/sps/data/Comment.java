package com.google.sps.data;
import java.util.Date;

public final class Comment {

  private long id;
  private String nickname;
  private String comment;
  private Date dateTime;
  private String email;

  public Comment(long id, String nickname, String comment, Date dateTime) {
    this.id = id;
    this.nickname = nickname;
    this.comment = comment;
    this.dateTime = dateTime;
    this.email = email;
  }
}
