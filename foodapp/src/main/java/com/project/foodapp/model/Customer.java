package com.project.foodapp.model;


import jakarta.persistence.*;


@Entity
@Table(name = "customer")
public class Customer extends User {

  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;


 public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }




  public Customer() {
    this.setType("customer");
  }

  public Customer(String userName, String password, String phoneNumber, String address,
      String city, String state, String zip) {
    super(userName, password, phoneNumber, address, city, state, zip);
    this.setType("customer");
  }
}
