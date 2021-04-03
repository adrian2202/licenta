package com.example.magazinonline;

public class User {

    public String fullName, email;

    public User()
    {

    }
    public User(String email,String fullName)
    {
        this.fullName=fullName;
        this.email=email;

    }

    public String getEmail() {
        return email;
    }
}
