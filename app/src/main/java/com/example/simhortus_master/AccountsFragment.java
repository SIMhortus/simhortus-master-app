package com.example.simhortus_master;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class AccountsFragment extends Fragment implements View.OnClickListener{

    ImageButton imageButton;
    public static TextView textView, passID;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth mAuth;
    FirebaseListAdapter adapter;


    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){

        final View rootView = inflater.inflate(R.layout.activity_connected_accounts, container, false);

        imageButton = rootView.findViewById(R.id.btnBack);
        textView = rootView.findViewById(R.id.toolbarText);

        textView.setText("Shared garden request");
        imageButton.setOnClickListener(this);

        passID = rootView.findViewById(R.id.passID);

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser firebaseUser = mAuth.getCurrentUser();

        firebaseDatabase = FirebaseDatabase.getInstance();
        final ListView listView = rootView.findViewById(R.id.listView);

        final DatabaseReference reference = firebaseDatabase.getReference("Users");
        DatabaseReference databaseReference =  reference.child(firebaseUser.getUid()).child("garden_id");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final String id = dataSnapshot.getValue().toString();

                passID.setText(id);


                final Query query = firebaseDatabase.getReference("Users")
                        .orderByChild("userType_deviceID_pending").equalTo("user_"+id+"_true");

                FirebaseListOptions<UserGardenInfo> options = new FirebaseListOptions.Builder<UserGardenInfo>()
                        .setLayout(R.layout.listview_row)
                        .setQuery(query, UserGardenInfo.class)
                        .build();

                adapter = new FirebaseListAdapter<UserGardenInfo>(options) {
                    @Override
                    protected void populateView(View v, UserGardenInfo model, int position){


                        TextView name = v.findViewById(R.id.displayName);
                        final TextView con = v.findViewById(R.id.displayCon);
                        Button btnAccept = v.findViewById(R.id.btn_approve);
                        Button btnRemove = v.findViewById(R.id.btn_remove);

                        final UserGardenInfo userGardenInfo = (UserGardenInfo) model;
                        name.setText(userGardenInfo.getFirst_name()+ " "+ userGardenInfo.getLast_name());
                        con.setText(userGardenInfo.getPhone_number());

                        if (con.getText().toString().isEmpty()) {
                            con.setText("No contact number");
                        }

                        btnAccept.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                reference.orderByChild("phone_number")
                                        .equalTo(userGardenInfo.getPhone_number())
                                        .addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for (DataSnapshot child: dataSnapshot.getChildren()) {

                                                    Log.e("Value", child.getKey());
                                                    con.setText("Accepted");
                                                    reference.child(child.getKey()).child("pending").setValue(null);
                                                    reference.child(child.getKey()).child("userType_deviceID_pending").setValue("user_"+id+"_false");
                                                    adapter.startListening();
                                                }
                                            }
                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                            }
                        });

                        btnRemove.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                reference.orderByChild("phone_number")
                                        .equalTo(userGardenInfo.getPhone_number())
                                        .addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                for (DataSnapshot child: dataSnapshot.getChildren()) {

                                                    Global.showToast("Under construction", getContext());
//                                                    if (child.getValue() == ""){
//                                                        Log.i("MainActivity", child.getKey());
//                                                    }
//                                                    con.setText("removed");
//                                                    reference.child(child.getKey()).child("is_approved").setValue(false);
//                                                    reference.child(child.getKey()).child("user_type").setValue("user_"+id+"_removed");
//                                                    adapter.startListening();

                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                            }
                        });

                    }

                };

                listView.setAdapter(adapter);
                adapter.startListening();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return rootView;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnResetPass:


                break;
            case R.id.btnBack:
                MenuFragment menuFragment = new MenuFragment();

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, menuFragment);
                transaction.commit();
                getActivity().overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);

                break;

        }
    }
}
