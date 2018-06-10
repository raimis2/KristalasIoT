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
    ChildEventListener childEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
        initFirebase();
    }

    @Override
    protected void onStart() {

        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (childEventListener != null) {
            mDatabase.removeEventListener(childEventListener);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        handleUI();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDatabase != null) {
            mDatabase = null;
        }
    }

    private void initFirebase() {
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance().getReference();
        }
    }

    private void handleUI() {
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    updateUI(ds);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
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
        };
        mDatabase.addChildEventListener(childEventListener);
    }

    private void updateUI(DataSnapshot ds) {
        switch (ds.getKey()) {
            case "delay":
                textView = findViewById(R.id.textView3);
                textView.setText(ds.getValue().toString());
                break;
            case "malfunc_offset":
                textView = findViewById(R.id.textView26);
                textView.setText(ds.getValue().toString());
                break;
            case "BCM4":
                textView = findViewById(R.id.textView6);
                textView.setText(ds.getValue().toString());
                break;
            case "BCM6":
                textView = findViewById(R.id.textView7);
                textView.setText(ds.getValue().toString());
                break;
            case "BCM24":
                textView = findViewById(R.id.textView15);
                textView.setText(ds.getValue().toString());
                break;
            case "BCM25":
                textView = findViewById(R.id.textView16);
                textView.setText(ds.getValue().toString());
                break;
            case "BCM23":
                textView = findViewById(R.id.textView17);
                textView.setText(ds.getValue().toString());
                break;
            case "BCM17":
                textView = findViewById(R.id.textView18);
                textView.setText(ds.getValue().toString());
                break;
            case "BCM27":
                textView = findViewById(R.id.textView19);
                textView.setText(ds.getValue().toString());
                break;
            case "BCM22":
                textView = findViewById(R.id.textView20);
                textView.setText(ds.getValue().toString());
                break;
        }
    }
}
