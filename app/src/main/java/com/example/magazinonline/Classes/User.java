package com.example.magazinonline.Classes;

public class User {
    private String Name;
    private String email;
    private String prenume;
    private String nrtel;

    public User() {

    }

    public User(String Name, String email, String prenume, String nrtel) {
        this.Name = Name;
        this.email = email;
        this.prenume = prenume;
        this.nrtel = nrtel;

    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPrenume() {
        return prenume;
    }

    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }

    public String getNrtel() {
        return nrtel;
    }

    public void setNrtel(String nrtel) {
        this.nrtel = nrtel;
    }
}
