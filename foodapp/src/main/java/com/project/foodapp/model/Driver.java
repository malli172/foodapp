package com.project.foodapp.model;


import jakarta.persistence.*;

@Entity
@Table(name = "drivers")
public class Driver extends User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;



  public Driver() {
    this.setType("driver");
  }

  public Driver(String userName, String password, String phoneNumber, String address,
      String city, String state, String zip) {
    super(userName, password, phoneNumber, address, city, state, zip);
    this.setType("driver");
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
}

