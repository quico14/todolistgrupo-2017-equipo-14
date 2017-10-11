package controllers;

import play.data.validation.Constraints;

public class Login {
  @Constraints.Required
  public String username;
  @Constraints.Required
  public String password;

  public String getUsername() {
     return username;
  }

  public void setUsername(String username) {
     this.username = username;
  }

  public String getPassword() {
     return password;
  }

  public void setPassword(String password) {
     this.password = password;
  }
}
