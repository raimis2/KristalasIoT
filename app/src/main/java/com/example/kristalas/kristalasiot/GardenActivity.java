package com.example.kristalas.kristalasiot;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GardenActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garden);

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

        CompoundButton.OnCheckedChangeListener multiListener = new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton v, boolean isChecked) {
                switch (v.getId()) {
                    case R.id.switch3:
                        if (isChecked) {
                            mDatabase.child("GPIO").child("BCM24").setValue(1);

                        } else {
                            mDatabase.child("GPIO").child("BCM24").setValue(0);
                        }
                        break;
                    case R.id.switch4:
                        if (isChecked) {
                            mDatabase.child("GPIO").child("BCM25").setValue(1);

                        } else {
                            mDatabase.child("GPIO").child("BCM25").setValue(0);
                        }
                        break;
                    case R.id.switch5:
                        if (isChecked) {
                            mDatabase.child("GPIO").child("BCM23").setValue(1);

                        } else {
                            mDatabase.child("GPIO").child("BCM23").setValue(0);
                        }
                        break;
                }
            }
        };
        ((Switch) findViewById(R.id.switch3)).setOnCheckedChangeListener(multiListener);
        ((Switch) findViewById(R.id.switch4)).setOnCheckedChangeListener(multiListener);
        ((Switch) findViewById(R.id.switch5)).setOnCheckedChangeListener(multiListener);
    }

    private void updateUI(DataSnapshot ds) {
        TextView tv;
        Switch sw;

        switch (ds.getKey()) {
            case "BCM24":
                sw = findViewById(R.id.switch3);
                sw.setChecked(getState(ds.getValue()));
                break;
            case "BCM25":
                sw = findViewById(R.id.switch4);
                sw.setChecked(getState(ds.getValue()));
                break;
            case "BCM23":
                sw = findViewById(R.id.switch5);
                sw.setChecked(getState(ds.getValue()));
                break;
            case "BCM17":
                tv = findViewById(R.id.textView10);
                if (getState(ds.getValue())) {
                    tv.setText(getString(R.string.wet).toString());
                } else {
                    tv.setText(getString(R.string.dry).toString());
                }
                break;
            case "BCM27":
                tv = findViewById(R.id.textView12);
                if (getState(ds.getValue())) {
                    tv.setText(getString(R.string.wet).toString());
                } else {
                    tv.setText(getString(R.string.dry).toString());
                }
                break;
            case "BCM22":
                tv = findViewById(R.id.textView14);
                if (getState(ds.getValue())) {
                    tv.setText(getString(R.string.wet).toString());
                } else {
                    tv.setText(getString(R.string.dry).toString());
                }
                break;
        }
    }

    private boolean getState(Object gpioState) {
        if (Integer.parseInt(gpioState.toString()) == 0) {
            return false;
        } else if (Integer.parseInt(gpioState.toString()) == 1) {
            return true;
        } else {
            return false;
        }
    }
}
