package com.project.foodapp.model;


import jakarta.persistence.*;

@Entity
@Table(name = "restaurant_info")
public class RestaurantInfo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private boolean open;
  private String restaurantName;
  private String description;
  
  @Column(name = "image_url", length = 10000)
  private String imageUrl;
  private String tag1;
  private String tag2;
  private String tag3;

  public RestaurantInfo() {
  }

  public RestaurantInfo(boolean open, String restaurantName, String description,
                        String imageUrl, String tag1, String tag2, String tag3) {
    this.open = open;
    this.restaurantName = restaurantName;
    this.description = description;
    this.imageUrl = imageUrl;
    this.tag1 = tag1;
    this.tag2 = tag2;
    this.tag3 = tag3;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public boolean isOpen() {
    return open;
  }

  public void setOpen(boolean open) {
    this.open = open;
  }

  public String getRestaurantName() {
    return restaurantName;
  }

  public void setRestaurantName(String restaurantName) {
    this.restaurantName = restaurantName;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public String getTag1() {
    return tag1;
  }

  public void setTag1(String tag1) {
    this.tag1 = tag1;
  }

  public String getTag2() {
    return tag2;
  }

  public void setTag2(String tag2) {
    this.tag2 = tag2;
  }

  public String getTag3() {
    return tag3;
  }

  public void setTag3(String tag3) {
    this.tag3 = tag3;
  }

  @Override
  public String toString() {
    return "RestaurantInfo{" +
        "id=" + id +
        ", open=" + open +
        ", restaurantName='" + restaurantName + '\'' +
        ", description='" + description + '\'' +
        ", imageUrl='" + imageUrl + '\'' +
        ", tag1='" + tag1 + '\'' +
        ", tag2='" + tag2 + '\'' +
        ", tag3='" + tag3 + '\'' +
        '}';
  }
}
