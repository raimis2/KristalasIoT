package com.example.kristalas.kristalasiot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.ToggleButton;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManagerService;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handleUI();

    }

    public void setDelay(View view) {
        EditText editText = findViewById(R.id.editText);
        int finalValue = Integer.parseInt(editText.getText().toString());
        mDatabase.child("Config").child("delay").setValue(finalValue);
    }



    public void showOverview(View view) {
        Intent intent = new Intent(this, OverviewActivity.class);
        startActivity(intent);
    }

    private void updateUI(DataSnapshot ds) {
        boolean state;
        final EditText ed;// = findViewById(R.id.editText);
        Switch sw; // sw = findViewById(R.id.switch1);

        switch (ds.getKey()) {
            case "delay":
                ed = findViewById(R.id.editText);
                ed.setHint(getString(R.string.delay_label).toString() + ds.getValue().toString());
                break;
            case "BCM3":
                sw = findViewById(R.id.switch1);
                if (Integer.parseInt(ds.getValue().toString()) == 0) {
                    sw.setText(getString(R.string.bcm3_off).toString());
                    state = false;
                } else if (Integer.parseInt(ds.getValue().toString()) == 1) {
                    sw.setText(getString(R.string.bcm3_on).toString());
                    state = true;
                } else {
                    state = false;
                }
                sw.setChecked(state);
                break;
            case "BCM6":
                sw = findViewById(R.id.switch2);
                if (Integer.parseInt(ds.getValue().toString()) == 0) {
                    sw.setText(getString(R.string.bcm6_off).toString());
                    state = false;
                } else if (Integer.parseInt(ds.getValue().toString()) == 1) {
                    sw.setText(getString(R.string.bcm6_on).toString());
                    state = true;
                } else {
                    state = false;
                }
                sw.setChecked(state);
                break;
        }
    }


    private void handleUI() {
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
                    case R.id.switch1:
                        if (isChecked) {
                            mDatabase.child("GPIO").child("BCM3").setValue(1);
                            //swtich.setText(getString(R.string.bcm3_on).toString());
                        } else {
                            mDatabase.child("GPIO").child("BCM3").setValue(0);
                            // swtich.setText(getString(R.string.bcm3_off).toString());
                        }
                        break;
                    case R.id.switch2:
                        if (isChecked) {
                            mDatabase.child("GPIO").child("BCM6").setValue(1);
                            //swtich.setText(getString(R.string.bcm3_on).toString());
                        } else {
                            mDatabase.child("GPIO").child("BCM6").setValue(0);
                            // swtich.setText(getString(R.string.bcm3_off).toString());
                        }
                        break;
                }
            }
        };
        ((Switch) findViewById(R.id.switch1)).setOnCheckedChangeListener(multiListener);
        ((Switch) findViewById(R.id.switch2)).setOnCheckedChangeListener(multiListener);
    }


}
