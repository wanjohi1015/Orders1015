package com.blueprint211.delreq;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.blueprint211.delreq.Model.Product;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ServerValue;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Objects;

public class ProductsDetailFragment extends Fragment {


    TextView quantitydisplay, titleview, descview, priceview;
    int quantity = 0;
    Button addquantity, subquantity, addtocart;
    FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth;
    ImageView imageView;
    String title, imageUrl, desc, productid, userid;
    int position = 0;
    int price = 0;
    int finalprice = 0;

    NavController navController;


    public ProductsDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product_detail, container, false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        userid = firebaseAuth.getCurrentUser().getUid();

        navController = Navigation.findNavController(view);

        // initializing the views
        quantitydisplay = view.findViewById(R.id.quantitycounterproductdetail);
        addquantity = view.findViewById(R.id.addquantity);
        subquantity = view.findViewById(R.id.subquantity);
        addtocart = view.findViewById(R.id.buttonaddtocart);
        titleview = view.findViewById(R.id.productdetailtitle);
        descview = view.findViewById(R.id.productdetaildetail);
        imageView = view.findViewById(R.id.productdetailimage);
        priceview = view.findViewById(R.id.totalPriceproductdetail);


        // receiving arguments // values
        imageUrl = ProductsDetailFragmentArgs.fromBundle(getArguments()).getImageUrl();
        title = ProductsDetailFragmentArgs.fromBundle(getArguments()).getTitle();
        desc = ProductsDetailFragmentArgs.fromBundle(getArguments()).getDescription();
        position = ProductsDetailFragmentArgs.fromBundle(getArguments()).getPosition();
        productid = ProductsDetailFragmentArgs.fromBundle(getArguments()).getProductid();
        price = ProductsDetailFragmentArgs.fromBundle(getArguments()).getPrice();

        titleview.setText(title);
        descview.setText(desc);
        Glide.with(requireActivity()).load(imageUrl).centerCrop().into(imageView);
        priceview.setText("Price for 1 item is " + String.valueOf(price));


        firestore.collection("Products").document(productid).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {


                assert value != null;
                Product product = value.toObject(Product.class);


                assert product != null;
                String latestquantity = String.valueOf(product.getQuantity());

                // display the quantity

                quantitydisplay.setText(latestquantity);

            }
        });


        addquantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                quantity = Integer.parseInt(quantitydisplay.getText().toString());

                quantity++;


                finalprice = quantity * price;
                priceview.setText("Total is " + String.valueOf(quantity) + " x " + String.valueOf(finalprice));

                firestore.collection("Products").document(productid).update("quantity", quantity).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });


            }
        });


        subquantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                quantity = Integer.parseInt(quantitydisplay.getText().toString());


                if (quantity <= 0) {

                    quantity = 0;
                    finalprice = 0;


                } else {

                    quantity--;
                    finalprice = quantity * price;
                    priceview.setText("Total is " + String.valueOf(quantity) + " x " + String.valueOf(finalprice));

                    firestore.collection("Products").document(productid).update("quantity", quantity).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });


                }


            }
        });


        addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (quantity == 0) {


                    navController.navigate(R.id.action_productsDetailFragment_to_productsFragment);
                    Toast.makeText(getContext(), "Nothing Added to Order List", Toast.LENGTH_SHORT).show();


                } else {


                    AddedInCart();
                    ProductsDetailFragmentDirections.ActionProductsDetailFragmentToProductsFragment actions = ProductsDetailFragmentDirections.actionProductsDetailFragmentToProductsFragment();

                    actions.setQuantity(quantity);
                    navController.navigate(actions);

                    Toast.makeText(getContext(), "Added to Order List", Toast.LENGTH_SHORT).show();


                }
            }
        });




    }

    private void AddedInCart() {

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        {
            assert firebaseUser != null;
            String userEmail = firebaseUser.getEmail();

            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("quantity", quantity);
            hashMap.put("price", finalprice);
            hashMap.put("title", title);
            hashMap.put("imageUrl", imageUrl);
            hashMap.put("productid", productid);
            hashMap.put("userEmail", userEmail);
            hashMap.put("timestamp", FieldValue.serverTimestamp());


            firestore.collection("Cart" + userid).document(title).set(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                }
            });

        }
    }












}
