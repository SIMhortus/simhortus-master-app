package com.example.simhortus_master;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import static androidx.core.content.ContextCompat.getSystemService;

public class NotificationsFragment extends Fragment {
    Button button;
    FirebaseListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){

        final View rootView = inflater.inflate(R.layout.fragment_notifications, container, false);
       FirebaseUser firebaseUser = Global.getmAuth;
       final String uID = firebaseUser.getUid();

        final Query query =  Global.notifRef.child(uID);
        final ListView listView = rootView.findViewById(R.id.listView);

        Global.notifRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(uID)){
//                Global.showToast(uID, getActivity());
//                    FirebaseListOptions<NotificationInfo> options = new FirebaseListOptions.Builder<NotificationInfo>()
//                            .setLayout(R.layout.listview_row)
//                            .setQuery(query, NotificationInfo.class)
//                            .build();
//
//                    adapter = new FirebaseListAdapter<NotificationInfo>(options) {
//                        @Override
//                        protected void populateView(@NonNull View v, @NonNull NotificationInfo model, int position) {
//                            TextView name = v.findViewById(R.id.displayName);
//                            TextView desc = v.findViewById(R.id.desc);
//                            TextView date = v.findViewById(R.id.date);
//
//                            final NotificationInfo notificationInfo = (NotificationInfo) model;
//                            name.setText(notificationInfo.getFull_name());
//                            desc.setText(notificationInfo.getDescription());
//                            date.setText(notificationInfo.getDate());
//
//
//
//                        }
//                    };
//
//                    listView.setAdapter(adapter);
//                    adapter.startListening();

                } else {

                    Global.showToast("Nothing to show", getActivity());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        return rootView;

    }




}
