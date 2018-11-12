package com.example.myfirstapp.sensors;

import android.util.Log;

import com.example.myfirstapp.plot.CarConstants;

import java.util.ArrayList;
import java.util.Arrays;

public class SensorCoordinate {
    public float x_coord;
    public float y_coord;
    public final SensorType sensorType;
    private final Object cur_val_lock;
    private volatile float cur_val;
    private volatile float incr_val;
    private volatile float prev_val;
    private volatile int num_incr;
    private long prev_time;
    private long cur_time;

    public SensorCoordinate(float x, float y, SensorType sensorType){
        x_coord = x;
        y_coord = y;
        this.sensorType = sensorType;
        cur_val_lock = new Integer(1);
        cur_val = 100;
        prev_val = 100;
        incr_val = 0;
    }


    public synchronized float getVal(){
        float ret_val;
        if (num_incr > 0) {
            ret_val = prev_val + incr_val;
            num_incr --;
        }
        else
            ret_val = prev_val;
        prev_val = ret_val;
        return ret_val* CarConstants.RATIO;
    }

    public synchronized void setVal(float new_val){
        Log.d("setval", String.valueOf(new_val));
        prev_val = cur_val;
        synchronized (cur_val_lock) {
            cur_val = new_val;
        }
        prev_time = cur_time;
        cur_time = System.currentTimeMillis();

        incr_val = (cur_val - prev_val)/((cur_time - prev_time)/CarConstants.REPLOT_SLEEP_TIME);
        num_incr = (int) ((cur_time - prev_time)/CarConstants.REPLOT_SLEEP_TIME);
    }

    public float getRaw(){
        float ret_val;
        synchronized(cur_val_lock) {
            ret_val= cur_val;
        }
        return ret_val;
    }
}