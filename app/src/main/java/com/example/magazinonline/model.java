package com.example.magazinonline;

public class model {
    String NumeProdus,descriereProdus,PretProdus,AdresaProducator, image,Categorie;
    model()
    {

    }

    public model(String numeProdus, String descriereProdus, String pretProdus, String adresaProducator, String image, String categorie) {
        NumeProdus = numeProdus;
        this.descriereProdus = descriereProdus;
        PretProdus = pretProdus;
        AdresaProducator = adresaProducator;
        this.image = image;
        Categorie = categorie;
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
}
