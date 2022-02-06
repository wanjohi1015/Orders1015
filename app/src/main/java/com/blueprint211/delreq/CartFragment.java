package com.blueprint211.delreq;

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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blueprint211.delreq.Adapter.CartAdapter;
import com.blueprint211.delreq.MVVM.CartViewModel;
import com.blueprint211.delreq.Model.Cart;
import com.blueprint211.delreq.Model.Orders;
import com.blueprint211.delreq.ProductsDetailFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class CartFragment extends Fragment {


    RecyclerView recyclerView;
    CartViewModel viewModel;
    CartAdapter mAdapter;
    ImageView back;

    TextView displaytotalprice;
    int totalprice = 0,tprice,quantity;
    NavController navController;
    FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth;
    FirebaseAuth auth;
    String title,  email, userid,productid;



    Button checkOut;
    List<Integer> savetotalprice = new ArrayList<>();


    public CartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();

        firestore = FirebaseFirestore.getInstance();
        back = view.findViewById(R.id.back);

        displaytotalprice = view.findViewById(R.id.totalPriceordercart);
        checkOut = view.findViewById(R.id.buttoncheckout);
        navController = Navigation.findNavController(view);

        userid = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        auth = FirebaseAuth.getInstance();
        recyclerView = view.findViewById(R.id.recyclerviewcart);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new CartAdapter();


        viewModel = new ViewModelProvider(requireActivity()).get(CartViewModel.class);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_cartFragment_to_productsFragment);
            }
        });

        viewModel.cartLiveDataShit().observe(getViewLifecycleOwner(), new Observer<List<Cart>>() {
            @Override
            public void onChanged(List<Cart> carts) {


                mAdapter.setCartList(carts);
                recyclerView.setAdapter(mAdapter);
            }
        });







        firestore.collection("Cart" + userid).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {


                assert value != null;
                for (DocumentSnapshot dsn : value.getDocuments()) {

                    Cart cart = dsn.toObject(Cart.class);

                    assert cart != null;

                    int total = cart.getPrice();

                    savetotalprice.add(total);




                }

                for (int i = 0; i < savetotalprice.size(); i++) {
                    totalprice += Integer.parseInt(String.valueOf(savetotalprice.get(i)));

                    displaytotalprice.setText(String.valueOf(totalprice));


                }
                savetotalprice.clear();

                totalprice = 0;

            }
        });







        checkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                firestore.collection("Products").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {


                        if (task.isSuccessful()) {
                            QuerySnapshot shit = task.getResult();


                            assert shit != null;
                            for (DocumentSnapshot shs : shit.getDocuments()) {


                                shs.getReference().update("quantity", 0);

                            }


                        }


                    }
                });
                firestore.collection("Cart"+ userid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {


                        if (task.isSuccessful()) {
                            QuerySnapshot shit = task.getResult();



                            assert shit != null;
                            for (DocumentSnapshot shs : shit.getDocuments()) {


                                Cart cart = shs.toObject(Cart.class);
                                title = cart.getTitle();

                                tprice = cart.getPrice();
                                quantity = cart.getQuantity();

                                email = cart.getUserEmail();




                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("timestamp", FieldValue.serverTimestamp());
                        hashMap.put("title", title);
                        hashMap.put("quantity", quantity);
                        hashMap.put("price", tprice);

                        hashMap.put("userEmail", email);


                        firestore.collection("Orders" ).document().set(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        });

                            }
                    }
                    }


                });







                firestore.collection("Cart" + userid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {


                        if (task.isSuccessful()) {

                            QuerySnapshot tasks = task.getResult();

                            assert tasks != null;
                            for (DocumentSnapshot ds : tasks.getDocuments()) {


                                ds.getReference().delete();
                            }


                        }
                    }
                });


                navController.navigate(R.id.action_cartFragment_to_productsFragment);
                Toast.makeText(getContext(), "Order Placed", Toast.LENGTH_SHORT).show();


            }


        });
    }


}

