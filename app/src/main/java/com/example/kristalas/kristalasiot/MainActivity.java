package com.example.kristalas.kristalasiot;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private DatabaseReference mDatabase;
    private DatabaseReference refDelay;
    ChildEventListener childEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


    @Override
    protected void onStart() {
        super.onStart();

        //   Log.i(TAG, "On Start .....");
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


    public void setDelay(View view) {
        EditText editText = findViewById(R.id.editText);
        int delayValue = Integer.parseInt(editText.getText().toString());
        refDelay.setValue(delayValue);
    }


    public void showOverview(View view) {
        Intent intent = new Intent(this, OverviewActivity.class);
        startActivity(intent);
    }

    public void showGardenSystem(View view) {
        Intent intent = new Intent(this, GardenActivity.class);
        startActivity(intent);
    }

    private void updateUI(DataSnapshot ds) {
        final EditText ed;
        TextView tv;
        Switch sw;

        switch (ds.getKey()) {
            case "online":
                tv = findViewById(R.id.textView);
                if (getState(ds.getValue())) {
                    tv.setText(getString(R.string.rpi_online).toString());
                    tv.setTextColor(Color.GREEN);
                } else {
                    tv.setText(getString(R.string.rpi_offline).toString());
                    tv.setTextColor(Color.RED);
                }
                break;
            case "delay":
                ed = findViewById(R.id.editText);
                ed.setHint(getString(R.string.delay_label).toString() + ds.getValue().toString());
                break;
            case "BCM4":
                sw = findViewById(R.id.switch1);
                if (getState(ds.getValue())) {
                    sw.setText(getString(R.string.bcm4_on).toString());
                } else {
                    sw.setText(getString(R.string.bcm4_off).toString());
                }
                sw.setChecked(getState(ds.getValue()));
                break;
            case "BCM6":
                sw = findViewById(R.id.switch2);
                if (getState(ds.getValue())) {
                    sw.setText(getString(R.string.bcm6_on).toString());
                } else {
                    sw.setText(getString(R.string.bcm6_off).toString());
                }
                sw.setChecked(getState(ds.getValue()));
                break;
        }

    }


    private void handleUI() {

        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance().getReference();
        }
        if (refDelay == null) {
            refDelay = mDatabase.child("Config").child("delay");
        }

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


        CompoundButton.OnCheckedChangeListener multiListener = new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton v, boolean isChecked) {
                switch (v.getId()) {
                    case R.id.switch1:
                        if (isChecked) {
                            mDatabase.child("GPIO").child("BCM4").setValue(1);

                        } else {
                            mDatabase.child("GPIO").child("BCM4").setValue(0);
                        }
                        break;
                    case R.id.switch2:
                        if (isChecked) {
                            mDatabase.child("GPIO").child("BCM6").setValue(1);

                        } else {
                            mDatabase.child("GPIO").child("BCM6").setValue(0);
                        }
                        break;
                }
            }
        };
        ((Switch) findViewById(R.id.switch1)).setOnCheckedChangeListener(multiListener);
        ((Switch) findViewById(R.id.switch2)).setOnCheckedChangeListener(multiListener);
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
