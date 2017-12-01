package com.example.kristalas.kristalasiot;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SettingsActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private static final String TAG = SettingsActivity.class.getSimpleName();
    private DatabaseReference refDelay;
    private DatabaseReference refConfig;
    private DatabaseReference refMalfOff;
    ChildEventListener childEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initFirebase();
        handleSwitches();
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
        if (refDelay != null) {
            refDelay = null;
        }
        if (refMalfOff != null) {
            refMalfOff = null;
        }
        if (refConfig != null) {
            refConfig = null;
        }

    }

    public void setDelay(View view) {
        EditText editText = findViewById(R.id.editText);
        int delayValue = Integer.parseInt(editText.getText().toString());
        editText.setText("");
        refDelay.setValue(delayValue);
    }

    public void setMalfOffset(View view) {
        EditText editText = findViewById(R.id.editText2);
        int delayValue = Integer.parseInt(editText.getText().toString());
        editText.setText("");
        refMalfOff.setValue(delayValue);
    }


    private void updateUI(DataSnapshot ds) {
        final EditText ed;
        //TextView tv;
        Switch sw;
        SeekBar sb;

        switch (ds.getKey()) {
            case "delay":
                ed = findViewById(R.id.editText);
                ed.setHint(getString(R.string.delay_label).toString() + ds.getValue().toString());
                break;
            case "malfunc_offset":
                ed = findViewById(R.id.editText2);
                ed.setHint(getString(R.string.malf_label).toString() + ds.getValue().toString());
                break;
            case "h1_desired":
                sb = findViewById(R.id.seekBar1);
                sb.setProgress(Integer.parseInt(ds.getValue().toString()));
                //  ed = findViewById(R.id.editText);
                // ed.setHint(getString(R.string.delay_label).toString() + ds.getValue().toString());
                break;
            case "h2_desired":
                sb = findViewById(R.id.seekBar2);
                sb.setProgress(Integer.parseInt(ds.getValue().toString()));
                // ed = findViewById(R.id.editText);
                //  ed.setHint(getString(R.string.delay_label).toString() + ds.getValue().toString());
                break;
            case "h3_desired":
                sb = findViewById(R.id.seekBar3);
                sb.setProgress(Integer.parseInt(ds.getValue().toString()));
                //   ed = findViewById(R.id.editText);
                //   ed.setHint(getString(R.string.delay_label).toString() + ds.getValue().toString());
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
        if (refDelay == null) {
            refDelay = mDatabase.child("Config").child("delay");
        }
        if (refMalfOff == null) {
            refMalfOff = mDatabase.child("Config").child("malfunc_offset");
        }
        if (refConfig == null) {
            refConfig = mDatabase.child("Config");
        }

    }

    private void handleSwitches() {
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


        SeekBar sb1 = (SeekBar) findViewById(R.id.seekBar1);
        SeekBar sb2 = (SeekBar) findViewById(R.id.seekBar2);
        SeekBar sb3 = (SeekBar) findViewById(R.id.seekBar3);


        //childEventListener = new ChildEventListener() {
        SeekBar.OnSeekBarChangeListener seekBarListener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
                switch (seekBar.getId()) {
                    case R.id.seekBar1:
                        // textHours.setText("" + progress + "Heure(s)");
                        refConfig.child("h1_desired").setValue(seekBar.getProgress());
                        break;

                    case R.id.seekBar2:
                        refConfig.child("h2_desired").setValue(seekBar.getProgress());
                        break;
                    case R.id.seekBar3:
                        refConfig.child("h3_desired").setValue(seekBar.getProgress());
                        break;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // TODO Auto-generated method stub
                TextView textView;
                switch (seekBar.getId()) {
                    case R.id.seekBar1:
                        textView = findViewById(R.id.textView28);
                        textView.setText(String.valueOf(progress));
                        textView.setTextColor(getTextColor(progress));
                        break;
                    case R.id.seekBar2:
                        textView = findViewById(R.id.textView29);
                        textView.setText(String.valueOf(progress));
                        textView.setTextColor(getTextColor(progress));
                        break;
                    case R.id.seekBar3:
                        textView = findViewById(R.id.textView30);
                        textView.setText(String.valueOf(progress));
                        textView.setTextColor(getTextColor(progress));
                        break;
                }

            }
        };
        sb1.setOnSeekBarChangeListener(seekBarListener);
        sb2.setOnSeekBarChangeListener(seekBarListener);
        sb3.setOnSeekBarChangeListener(seekBarListener);


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

}
