package com.blueprint211.delreq.MVVM;

import androidx.annotation.ArrayRes;
import androidx.annotation.Nullable;

import com.blueprint211.delreq.Model.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.ToIntFunction;

public class ProductRepo {


    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    List<Product> productList = new ArrayList<>();

    OnProductInter interfaceprodcuts;


    public ProductRepo(OnProductInter interfaceprodcuts) {
        this.interfaceprodcuts = interfaceprodcuts;
    }

    public void GetAllPRO() {





        firestore.collection("Products").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {


                productList.clear();
                assert value != null;
                for (DocumentSnapshot ds: value.getDocuments()) {

                    Product product = ds.toObject(Product.class);




                    productList.add(product);
                    Collections.sort(productList, new Comparator<Product>() {
                        @Override
                        public int compare(Product lhs, Product rhs) {
                            return lhs.getTitle().compareTo(rhs.getTitle());
                        }
                    });




                    interfaceprodcuts.Products(productList);



                }
            }
        });






    }


    public interface OnProductInter{
        void Products(List<Product> products);
    }
}
