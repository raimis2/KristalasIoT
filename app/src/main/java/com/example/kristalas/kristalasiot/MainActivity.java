package com.example.kristalas.kristalasiot;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private DatabaseReference mDatabase;
    private DatabaseReference refGPIO;
    private DatabaseReference refConfig;
    ChildEventListener childEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFirebase();
        handleSwitches();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.overview:
                startActivity(new Intent(this, OverviewActivity.class));
                return true;
            case R.id.settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
        if (refGPIO != null) {
            refGPIO = null;
        }
        if (refConfig != null) {
            refConfig = null;
        }
    }

    private void updateUI(DataSnapshot ds) {
        TextView tv;
        Switch sw;
        ToggleButton tb;

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
            case "malfunction":
                tv = findViewById(R.id.textView21);
                if (getState(ds.getValue())) {
                    tv.setText(getString(R.string.malf_info).toString());
                } else {
                    tv.setText("");
                }
                break;
            case "auto":
                tb = findViewById(R.id.toggleButton);
                tb.setChecked(getState(ds.getValue()));
                if (Integer.parseInt(ds.getValue().toString()) == 1) {

                    refGPIO.child("BCM23").setValue(0);
                    refGPIO.child("BCM24").setValue(0);
                    refGPIO.child("BCM25").setValue(0);
                }
                sw = findViewById(R.id.switch3);
                sw.setClickable(!getState(ds.getValue()));
                sw = findViewById(R.id.switch4);
                sw.setClickable(!getState(ds.getValue()));
                sw = findViewById(R.id.switch5);
                sw.setClickable(!getState(ds.getValue()));
                //Log.d(TAG, "Error on PeripheralIO API" + " " + ds.getValue());
                break;
            case "H1":
                tv = findViewById(R.id.textView10);
                tv.setText(ds.getValue().toString());
                tv.setTextColor(getTextColor(Integer.parseInt(ds.getValue().toString())));
                break;
            case "H2":
                tv = findViewById(R.id.textView12);
                tv.setText(ds.getValue().toString());
                tv.setTextColor(getTextColor(Integer.parseInt(ds.getValue().toString())));
                break;
            case "H3":
                tv = findViewById(R.id.textView14);
                tv.setText(ds.getValue().toString());
                tv.setTextColor(getTextColor(Integer.parseInt(ds.getValue().toString())));
                ;
                break;
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
               /* tv = findViewById(R.id.textView10);
                if (getState(ds.getValue())) {
                    tv.setText(getString(R.string.wet).toString());
                } else {
                    tv.setText(getString(R.string.dry).toString());
                }*/
                break;
            case "BCM27":
                /*tv = findViewById(R.id.textView12);
                if (getState(ds.getValue())) {
                    tv.setText(getString(R.string.wet).toString());
                } else {
                    tv.setText(getString(R.string.dry).toString());
                }*/
                break;
            case "BCM22":
               /*tv = findViewById(R.id.textView14);
                if (getState(ds.getValue())) {
                    tv.setText(getString(R.string.wet).toString());
                } else {
                    tv.setText(getString(R.string.dry).toString());
                }*/
                break;
        }

    }

    public int getTextColor(int value) {
        if (Integer.parseInt(String.valueOf(value)) <= 33) {
            return getResources().getColor(R.color.colorDarkRed);
        } else if (Integer.parseInt(String.valueOf(value)) <= 66) {
            return getResources().getColor(R.color.colorOrange);
        } else {
            return getResources().getColor(R.color.colorDarkGreen);
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

    private boolean getState(Object gpioState) {
        if (Integer.parseInt(gpioState.toString()) == 0) {
            return false;
        } else if (Integer.parseInt(gpioState.toString()) == 1) {
            return true;
        } else {
            return false;
        }
    }

    private void initFirebase() {
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance().getReference();
        }
        if (refGPIO == null) {
            refGPIO = mDatabase.child("GPIO");
        }
        if (refConfig == null) {
            refConfig = mDatabase.child("Config");
        }
    }

    private void handleSwitches() {

        CompoundButton.OnCheckedChangeListener multiListener = new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton v, boolean isChecked) {
                switch (v.getId()) {
                    case R.id.switch3:
                        if (isChecked) {
                            refGPIO.child("BCM24").setValue(1);
                        } else {

                            refGPIO.child("BCM24").setValue(0);
                        }
                        break;
                    case R.id.switch4:
                        if (isChecked) {
                            refGPIO.child("BCM25").setValue(1);

                        } else {
                            refGPIO.child("BCM25").setValue(0);
                        }
                        break;
                    case R.id.switch5:
                        if (isChecked) {
                            refGPIO.child("BCM23").setValue(1);

                        } else {
                            refGPIO.child("BCM23").setValue(0);
                        }
                        break;
                }
            }
        };
        ((Switch) findViewById(R.id.switch3)).setOnCheckedChangeListener(multiListener);
        ((Switch) findViewById(R.id.switch4)).setOnCheckedChangeListener(multiListener);
        ((Switch) findViewById(R.id.switch5)).setOnCheckedChangeListener(multiListener);

        ToggleButton toggle = (ToggleButton) findViewById(R.id.toggleButton);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // AUTO mode is ON
                    refConfig.child("auto").setValue(1);

                } else {
                    // Manual mode is ON
                    refConfig.child("auto").setValue(0);
                }
            }
        });

    }
}
