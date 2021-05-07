package com.example.magazinonline;

public class Product {

    public String NumeProdus,descriereProdus,PretProdus,AdresaProducator, data;
    public Product(){

    }


    public Product(String NumeProdus, String descriereProdus, String PretProdus, String AdresaProducator, String data) {

        this.NumeProdus=NumeProdus;
        this.descriereProdus=descriereProdus;
        this.PretProdus=PretProdus;
        this.AdresaProducator=AdresaProducator;
        this.data=data;
    }
}
