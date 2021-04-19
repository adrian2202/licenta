package com.example.magazinonline;

public class User {

    public String Name, email,prenume ,nrtel;

    public User()
    {

    }
    public User(String Name, String email, String prenume, String nrtel)
    {
        this.Name=Name;
        this.email=email;
        this.prenume=prenume;
        this.nrtel=nrtel;

    }

    public String getEmail() {
        return email;
    }
}
