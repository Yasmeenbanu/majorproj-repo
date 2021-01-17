package com.pmap.voter.models;

public class UserLoginInput {
    public String user_name;
    public String user_password;

    public UserLoginInput(String user_name, String user_password) {
        this.user_name = user_name;
        this.user_password = user_password;
    }
}
