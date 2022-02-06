package com.blueprint211.delreq;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;


public class logoutFragment extends Fragment {
        NavController navController;
        TextView cancel, logout;




    public  logoutFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_logout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        cancel=view.findViewById(R.id.cancel);
        logout=view.findViewById(R.id.logout);



        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.isClickable()) {

                    FirebaseAuth.getInstance().signOut();

                    navController.navigate(R.id.action_logoutFragment_to_loginFragment);
                }

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.isClickable()) {

                    navController.navigate(R.id.action_logoutFragment_to_productsFragment);
                }

            }
        });

    }
}