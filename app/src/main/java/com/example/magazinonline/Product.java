package com.example.magazinonline;

import android.widget.Spinner;

public class Product {

    public String NumeProdus,descriereProdus,PretProdus,AdresaProducator, data,Categorie;
    public Product(){

    }


    public Product(String NumeProdus, String descriereProdus, String PretProdus, String AdresaProducator, String data, String Categorie) {

        this.NumeProdus=NumeProdus;
        this.descriereProdus=descriereProdus;
        this.PretProdus=PretProdus;
        this.AdresaProducator=AdresaProducator;
        this.data=data;
        this.Categorie=Categorie;


    }
}
