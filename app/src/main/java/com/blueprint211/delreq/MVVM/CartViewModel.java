package com.blueprint211.delreq.MVVM;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.blueprint211.delreq.Model.Cart;

import java.util.List;

public class CartViewModel extends ViewModel implements CartRepo.CartINTERFACEShit {


    MutableLiveData<List<Cart>> mutableLiveData = new MutableLiveData<>();

    CartRepo rep = new CartRepo(this);

    public CartViewModel() {
        rep.getCartShit();
    }

    public LiveData<List<Cart>> cartLiveDataShit() {
        return mutableLiveData;
    }





    @Override
    public void CartlIST(List<Cart> carts) {
        mutableLiveData.setValue(carts);
    }


}
