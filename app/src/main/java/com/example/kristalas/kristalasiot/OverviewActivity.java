package com.example.kristalas.kristalasiot;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OverviewActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);


        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    updateUI(ds);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    // Log.d("TAG", ds.getKey() + " " + ds.getValue());
                    updateUI(ds);
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void updateUI(DataSnapshot ds) {
        switch (ds.getKey()) {
            case "delay":
                textView = findViewById(R.id.textView3);
                textView.setText(ds.getValue().toString());
             //   Log.d("TAG", ds.getKey() + " " + ds.getValue());
            case "BCM3":
                textView = findViewById(R.id.textView6);
             /*   if (ds.getValue() == "0") {
                    value = getString(R.string.off);
                } else if (ds.getValue() == "1") {
                    value = getString(R.string.on);
                } else {
                    value = "ERR";
                }*/
                textView.setText(ds.getValue().toString());
            //    Log.d("TAG", ds.getKey() + " " + ds.getValue());
            case "BCM6":
                textView = findViewById(R.id.textView7);
               /* if (ds.getValue() == "0") {
                    value = getString(R.string.off);
                } else if (ds.getValue() == "1") {
                    value = getString(R.string.on);
                } else {
                    value = "ERR";
                }*/
                textView.setText(ds.getValue().toString());
              //  Log.d("TAG", ds.getKey() + " " + ds.getValue());
        }
    }
}
