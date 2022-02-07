package com.blueprint211.delreq;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.blueprint211.delreq.Adapter.ProductsAdapter;
import com.blueprint211.delreq.MVVM.ProductViewModel;
import com.blueprint211.delreq.Model.Cart;
import com.blueprint211.delreq.Model.Product;
import com.example.flatdialoglibrary.dialog.FlatDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.example.flatdialoglibrary.dialog.FlatDialog;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProductsFragment extends Fragment implements ProductsAdapter.OnClickedProducct {


    RecyclerView recyclerView;
    ProductViewModel viewModel;
    ProductsAdapter mAdapter;
    FloatingActionButton fab;
    TextView quantityincart;
    ImageView logout;


    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    String userid;
    int sum = 0;


    NavController navController;
    List<Integer> savequantity = new ArrayList<>();
    List<Product> productList;


    public ProductsFragment () {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onViewCreated(view, savedInstanceState);


        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();


        userid = firebaseAuth.getCurrentUser().getUid();

        recyclerView = view.findViewById(R.id.recyclerviewproduct);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        mAdapter = new ProductsAdapter(this);
        fab = view.findViewById(R.id.fabMainProductPage);
        logout = view.findViewById(R.id.imageView2);
        navController = Navigation.findNavController(view);
        quantityincart = view.findViewById(R.id.cartquantity);





        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_productsFragment_to_cartFragment);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoginDialog();
            }
        });







        firestore.collection("Cart" + userid).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                assert value != null;
                for (DocumentSnapshot ds : value.getDocuments()) {

                    Cart cart = ds.toObject(Cart.class);


                    int quantitycounter = cart.getQuantity();

                    // add all integers into Integer arraylist
                    savequantity.add(quantitycounter);


                }


                for (int i = 0; i < savequantity.size(); i++) {


                    sum += Integer.parseInt(String.valueOf(savequantity.get(i)));

                }

                quantityincart.setText(String.valueOf(sum));

                sum = 0;
                savequantity.clear(); // unless we add something new to our list// previous records are cleared.


            }
        });



        viewModel = new ViewModelProvider(requireActivity()).get(ProductViewModel.class);
        viewModel.getAllProducts().observe(getViewLifecycleOwner(), new Observer<List<Product>>() {




            @Override
            public void onChanged(List<Product> products) {

                mAdapter.setProductList(products);
                recyclerView.setAdapter(mAdapter);
            }
        });




    }




    // for going to next fragment product detail.

    @Override
    public void OnProClicked(List<Product> productList, int position) {


        ProductsFragmentDirections.ActionProductsFragmentToProductsDetailFragment
                actions = ProductsFragmentDirections.actionProductsFragmentToProductsDetailFragment();

        actions.setTitle(productList.get(position).getTitle());
        actions.setDescription(productList.get(position).getDescription());
        actions.setProductid(productList.get(position).getProductid());
        actions.setPosition(position);
        actions.setImageUrl(productList.get(position).getImageUrl());
        actions.setPrice(productList.get(position).getPrice());

        navController.navigate((NavDirections) actions);


    }


    @Override
    public void onResume() {
        super.onResume();
        sum = 0;
    }
    private void showLoginDialog() {
        final FlatDialog flatDialog = new FlatDialog(requireContext());
        flatDialog.setTitle("   Logout?")
                .setFirstButtonText("ACCEPT")
                .setSecondButtonText("CANCEL")
                .withFirstButtonListner(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseAuth.getInstance().signOut();
                        navController.navigate(R.id.action_productsFragment_to_loginFragment);
                        flatDialog.dismiss();
                    }
                })
                .withSecondButtonListner(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        flatDialog.dismiss();
                    }
                })
                .show();
    }
}
    




