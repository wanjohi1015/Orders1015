package com.blueprint211.delreq.MVVM;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.blueprint211.delreq.Model.Product;

import java.util.List;

public class ProductViewModel extends ViewModel implements ProductRepo.OnProductInter {

    MutableLiveData<List<Product>> mutableLiveData = new MutableLiveData<>();
    ProductRepo productRepo = new ProductRepo(this);


    public ProductViewModel() {
        productRepo.GetAllPRO();
    }

    public LiveData<List<Product>> getAllProducts() {
        return mutableLiveData;





    }

    @Override
    public void Products(List<Product> products) {
        mutableLiveData.setValue(products);
    }
}
