package com.example.magazinonline.Classes;

public class User {
    private String id;
    private String Name;
    private String email;
    private String prenume;
    private String nrtel;
    private String image;

    public User() {

    }

    public User(String Name, String email, String prenume, String nrtel) {
        this.id = null;
        this.Name = Name;
        this.email = email;
        this.prenume = prenume;
        this.nrtel = nrtel;
        this.image = null;
    }

    public User(String name, String email, String prenume, String nrtel, String image) {
        this.id = null;
        this.Name = name;
        this.email = email;
        this.prenume = prenume;
        this.nrtel = nrtel;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
