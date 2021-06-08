package com.example.magazinonline.Classes;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Model implements Serializable {
    private String idProdus;
    private String NumeProdus;
    private String descriereProdus;
    private String PretProdus;
    private ProducerAddress AdresaProducator;
    private String image;
    private String Categorie;
    private String data;
    private int cantitate = 1;

    public Model() {

    }

    public Model(String idProdus, String numeProdus, String descriereProdus, String pretProdus,
                 ProducerAddress adresaProducator, String data, String image, String categorie) {
        this.idProdus = idProdus;
        NumeProdus = numeProdus;
        this.descriereProdus = descriereProdus;
        PretProdus = pretProdus;
        AdresaProducator = adresaProducator;
        this.data = data;
        this.image = image;
        Categorie = categorie;
    }

    public String getIdProdus() {
        return idProdus;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategorie() {
        return Categorie;
    }

    public void setCategorie(String categorie) {
        Categorie = categorie;
    }

    public String getData() {
        return data;
    }

    public int getCantitate() {
        return cantitate;
    }

    public void setCantitate(int cantitate) {
        this.cantitate = cantitate;
    }

    @Override
    @NonNull
    public String toString() {
        return "model{" +
                "idProdus='" + idProdus + '\'' +
                ", NumeProdus='" + NumeProdus + '\'' +
                ", descriereProdus='" + descriereProdus + '\'' +
                ", PretProdus='" + PretProdus + '\'' +
                ", AdresaProducator='" + AdresaProducator + '\'' +
                ", image='" + image + '\'' +
                ", Categorie='" + Categorie + '\'' +
                ", data='" + data + '\'' +
                ", cantitate=" + cantitate +
                '}';
    }
}