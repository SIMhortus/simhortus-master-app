package com.example.simhortus_master;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

public class MenuFragment extends Fragment {


    Button btnLogout, btnEmail, btnPass, btnLinked;
    FirebaseAuth mAuth;


    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_menu, container, false);

        btnLogout = rootView.findViewById(R.id.btnMenuLogOut);
        btnEmail = rootView.findViewById(R.id.btnEmail);
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

        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateEmail();
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

    public void updateEmail() {
        UpdateEmail updateEmail = new UpdateEmail();
        updateEmail.show(getFragmentManager(), "Update email");
    }

    public void updatePass() {
        UpdatePassword updatePassword = new UpdatePassword();
        updatePassword.show(getFragmentManager(), "Update password");
    }

    public void goToScanActivity(){
        startActivity(new Intent(getContext(), ScanGarden.class));
    }

}
