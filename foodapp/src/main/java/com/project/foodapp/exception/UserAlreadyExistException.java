package com.project.foodapp.exception;

public class UserAlreadyExistException extends Exception {

  public UserAlreadyExistException(String message) {
    super(message);
  }
}
