package com.example.kristalas.kristalasiot;

/**
 * Created by Kristalas on 2017.11.13.
 */
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Config {

    private int delay;

    public Config() {

    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }
}
