package com.example.simhortus_master;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

public class MenuFragment extends Fragment {


    Button btnLogout, btnPass, btnLinked;
    FirebaseAuth mAuth;


    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_menu, container, false);

        btnLogout = rootView.findViewById(R.id.btnMenuLogOut);
        btnPass = rootView.findViewById(R.id.btnPass);
        btnLinked = rootView.findViewById(R.id.btnLinkedDevice);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //repeated
                mAuth.getInstance().signOut();
                startActivity(new Intent(getContext(), Login.class));

            }
        });

        btnPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePass();
            }
        });

        btnLinked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToScanActivity();
            }
        });



        return rootView;
    }

    public void updatePass() {
        UpdatePassword updatePassword = new UpdatePassword();
        updatePassword.show(getFragmentManager(), "Change password");
    }

    public void goToScanActivity(){
        startActivity(new Intent(getContext(), ScanGarden.class));
    }

}
