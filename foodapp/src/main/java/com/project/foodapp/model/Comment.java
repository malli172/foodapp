package com.project.foodapp.model;

import jakarta.persistence.*;

@Entity
@Table(name = "comments")
public class Comment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private int rating;

  private String content;

  public Comment() {
  }

  public Comment(int rating, String content) {
    this.rating = rating;
    this.content = content;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public int getRating() {
    return rating;
  }

  public void setRating(int rating) {
    this.rating = rating;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  @Override
  public String toString() {
    return "Comment{" +
        "id=" + id +
        ", rating=" + rating +
        ", content='" + content + '\'' +
        '}';
  }
}
