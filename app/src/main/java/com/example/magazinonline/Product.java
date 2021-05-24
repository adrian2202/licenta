package com.example.magazinonline;

public class Product {
    private String idProdus;
    private String idProducator;
    private String NumeProdus;
    private String descriereProdus;
    private String PretProdus;
    private String AdresaProducator;
    private String data;
    private String Categorie;

    public Product() {

    }

    public Product(String idProdus, String idProducator, String NumeProdus, String descriereProdus, String PretProdus, String AdresaProducator, String data, String Categorie) {
        this.idProdus = idProdus;
        this.idProducator = idProducator;
        this.NumeProdus = NumeProdus;
        this.descriereProdus = descriereProdus;
        this.PretProdus = PretProdus;
        this.AdresaProducator = AdresaProducator;
        this.data = data;
        this.Categorie = Categorie;
    }

    public String getIdProducator() {
        return idProducator;
    }

    public void setIdProducator(String idProducator) {
        this.idProducator = idProducator;
    }

    public void setIdProdus(String idProdus) {
        this.idProdus = idProdus;
    }

    public String getNumeProdus() {
        return NumeProdus;
    }

    public void setNumeProdus(String numeProdus) {
        NumeProdus = numeProdus;
    }

    public String getDescriereProdus() {
        return descriereProdus;
    }

    public void setDescriereProdus(String descriereProdus) {
        this.descriereProdus = descriereProdus;
    }

    public String getPretProdus() {
        return PretProdus;
    }

    public void setPretProdus(String pretProdus) {
        PretProdus = pretProdus;
    }

    public String getAdresaProducator() {
        return AdresaProducator;
    }

    public void setAdresaProducator(String adresaProducator) {
        AdresaProducator = adresaProducator;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getCategorie() {
        return Categorie;
    }

    public void setCategorie(String categorie) {
        Categorie = categorie;
    }

    public String getIdProdus() {
        return idProdus;
    }

    @Override
    public String toString() {
        return "Product{" +
                "idProdus='" + idProdus + '\'' +
                ", NumeProdus='" + NumeProdus + '\'' +
                ", descriereProdus='" + descriereProdus + '\'' +
                ", PretProdus='" + PretProdus + '\'' +
                ", AdresaProducator='" + AdresaProducator + '\'' +
                ", data='" + data + '\'' +
                ", Categorie='" + Categorie + '\'' +
                '}';
    }
}