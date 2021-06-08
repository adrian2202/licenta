package com.example.magazinonline.Classes;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Product implements Serializable {
    private String idProdus;
    private String idProducator;
    private String NumeProdus;
    private String descriereProdus;
    private String PretProdus;
    private ProducerAddress AdresaProducator;
    private double LatitudineProducator;
    private double LongitudineProducator;
    private MyTime dataAdaugareProdus;
    private String Categorie;
    private String image;

    public Product() {

    }

    public Product(String idProdus,
                   String idProducator,
                   String numeProdus,
                   String descriereProdus,
                   String pretProdus,
                   ProducerAddress adresaProducator,
                   double latitudineProducator,
                   double longitudineProducator,
                   MyTime dataAdaugareProdus,
                   String categorie) {
        this.idProdus = idProdus;
        this.idProducator = idProducator;
        this.NumeProdus = numeProdus;
        this.descriereProdus = descriereProdus;
        this.PretProdus = pretProdus;
        this.AdresaProducator = adresaProducator;
        this.LatitudineProducator = latitudineProducator;
        this.LongitudineProducator = longitudineProducator;
        this.dataAdaugareProdus = dataAdaugareProdus;
        this.Categorie = categorie;
    }

    public Product(String idProdus,
                   String idProducator,
                   String numeProdus,
                   String descriereProdus,
                   String pretProdus,
                   MyTime dataAdaugareProdus,
                   String categorie) {
        this.idProdus = idProdus;
        this.idProducator = idProducator;
        this.NumeProdus = numeProdus;
        this.descriereProdus = descriereProdus;
        this.PretProdus = pretProdus;
        this.dataAdaugareProdus = dataAdaugareProdus;
        this.Categorie = categorie;
    }

    public Product(String idProdus,
                   String idProducator,
                   String numeProdus,
                   String descriereProdus,
                   String pretProdus,
                   ProducerAddress adresaProducator,
                   double latitudineProducator,
                   double longitudineProducator,
                   MyTime dataAdaugareProdus,
                   String categorie,
                   String image) {
        this.idProdus = idProdus;
        this.idProducator = idProducator;
        NumeProdus = numeProdus;
        this.descriereProdus = descriereProdus;
        PretProdus = pretProdus;
        AdresaProducator = adresaProducator;
        LatitudineProducator = latitudineProducator;
        LongitudineProducator = longitudineProducator;
        this.dataAdaugareProdus = dataAdaugareProdus;
        Categorie = categorie;
        this.image = image;
    }

    public String getIdProducator() {
        return idProducator;
    }

    public void setIdProducator(String idProducator) {
        this.idProducator = idProducator;
    }

    public String getIdProdus() {
        return idProdus;
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

    public ProducerAddress getAdresaProducator() {
        return AdresaProducator;
    }

    public void setAdresaProducator(ProducerAddress adresaProducator) {
        AdresaProducator = adresaProducator;
    }

    public double getLatitudineProducator() {
        return LatitudineProducator;
    }

    public void setLatitudineProducator(double latitudineProducator) {
        LatitudineProducator = latitudineProducator;
    }

    public double getLongitudineProducator() {
        return LongitudineProducator;
    }

    public void setLongitudineProducator(double longitudineProducator) {
        LongitudineProducator = longitudineProducator;
    }

    public MyTime getDataAdaugareProdus() {
        return dataAdaugareProdus;
    }

    public void setDataAdaugareProdus(MyTime dataAdaugareProdus) {
        this.dataAdaugareProdus = dataAdaugareProdus;
    }

    public String getCategorie() {
        return Categorie;
    }

    public void setCategorie(String categorie) {
        Categorie = categorie;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @NonNull
    @Override
    public String toString() {
        return "Product{" +
                "idProdus='" + idProdus + '\'' +
                ", idProducator='" + idProducator + '\'' +
                ", NumeProdus='" + NumeProdus + '\'' +
                ", descriereProdus='" + descriereProdus + '\'' +
                ", PretProdus='" + PretProdus + '\'' +
                ", AdresaProducator=" + AdresaProducator +
                ", LatitudineProducator=" + LatitudineProducator +
                ", LongitudineProducator=" + LongitudineProducator +
                ", dataAdaugareProdus=" + dataAdaugareProdus +
                ", Categorie='" + Categorie + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}