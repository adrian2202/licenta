package com.example.magazinonline.MainPart.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.AndroidViewModel;

import com.denzcoskun.imageslider.models.SlideModel;
import com.example.magazinonline.Classes.App;
import com.example.magazinonline.Classes.Product;
import com.example.magazinonline.Classes.User;
import com.example.magazinonline.MainPart.Fragments.HomeFragment;
import com.example.magazinonline.R;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends AndroidViewModel {
    private List<SlideModel> slideModels = new ArrayList<>();
    private List<App> categories = new ArrayList<>();
    private Fragment selectedFragment = new HomeFragment();
    private Product selectedProduct;
    private boolean productIsModified = false;
    private boolean editProductImageModified = false;
    private User selectedProducer = null;
    private Product selectedProducerProduct = null;

    public HomeViewModel(@NonNull Application application) {
        super(application);

        String[] categoryListString = application.getResources().getStringArray(R.array.names);

        slideModels.add(new SlideModel("https://revistaprogresiv.ro/sites/default/" +
                "files/article/images/gusturi_romanesti_4.jpg", "Imagine 1"));

        slideModels.add(new SlideModel("https://www.bioshopromania.com/" +
                "images/thumbnails/770/709/detailed/4/" +
                "cos_traditional_romanesc_mare_2_BioShopRomania.JPG?t=1602080293",
                "Imagine 2"));

        slideModels.add(new SlideModel("https://www.ziromania.ro/wp-content/uploads/" +
                "2018/12/b%C4%83uturi-tradi%C8%9Bionale-rom%C3%A2ne%C8%99ti" +
                "-vazute-la-crama-1777-3.jpg", "Imagine 3"));

        slideModels.add(new SlideModel("https://www.banateanul.ro/wp-content/" +
                "uploads/2019/07/bauturi-traditionale-romanesti.jpg", "Imagine 4"));

        slideModels.add(new SlideModel("https://medisf.traasgpu.com/ifis/" +
                "62277a094eff337f-1024x576.jpg", "Imagine 5"));

        categories.add(new App(R.drawable.mancaruri_traditionale, categoryListString[0]));
        categories.add(new App(R.drawable.produse_bio, categoryListString[1]));
        categories.add(new App(R.drawable.bauturi_specifice, categoryListString[2]));
        categories.add(new App(R.drawable.fructe_legume, categoryListString[3]));
    }

    public List<SlideModel> getSlideModels() {
        return slideModels;
    }

    public List<App> getCategories() {
        return categories;
    }

    public Fragment getSelectedFragment() {
        return selectedFragment;
    }

    public void setSelectedFragment(Fragment selectedFragment) {
        this.selectedFragment = selectedFragment;
    }

    public Product getSelectedProduct() {
        return selectedProduct;
    }

    public void setSelectedProduct(Product selectedProduct) {
        this.selectedProduct = selectedProduct;
    }

    public boolean isProductIsModified() {
        return productIsModified;
    }

    public void setProductIsModified(boolean productIsModified) {
        this.productIsModified = productIsModified;
    }

    public int getPLACE_PICKER_REQUEST() {
        return 1;
    }

    public int getPICK_IMAGE() {
        return 2;
    }

    public boolean isEditProductImageModified() {
        return editProductImageModified;
    }

    public void setEditProductImageModified(boolean editProductImageModified) {
        this.editProductImageModified = editProductImageModified;
    }

    public User getSelectedProducer() {
        return selectedProducer;
    }

    public void setSelectedProducer(User selectedProducer) {
        this.selectedProducer = selectedProducer;
    }

    public Product getSelectedProducerProduct() {
        return selectedProducerProduct;
    }

    public void setSelectedProducerProduct(Product selectedProducerProduct) {
        this.selectedProducerProduct = selectedProducerProduct;
    }
}
