package com.example.magazinonline.Classes;

public class App {
    private int image;
    private String name;
//    int size;

    public App(int image, String name) {   //mai trebuie pus un int size
        this.image = image;
        this.name = name;
//        this.size=size;
    }

    public int getImage() {
        return image;
    }

    public String getName() {
        return name;
    }
//    public int getSize() {
//        return size;
//    }
}
