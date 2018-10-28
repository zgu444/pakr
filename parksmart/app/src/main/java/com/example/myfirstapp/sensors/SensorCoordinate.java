package com.example.myfirstapp.sensors;

import android.util.Log;

import com.example.myfirstapp.plot.CarConstants;

public class SensorCoordinate {
    public float x_coord;
    public float y_coord;
    public final SensorType sensorType;
    private volatile float val;

    public SensorCoordinate(float x, float y, SensorType sensorType){
        x_coord = x;
        y_coord = y;
        this.sensorType = sensorType;
        val = 100;
    }

    public synchronized float getVal(){
        Log.d("getval", String.valueOf(val* CarConstants.RATIO));
        return val* CarConstants.RATIO;
    }

    public synchronized void setVal(float new_val){
        Log.d("setval", String.valueOf(new_val));
        val = new_val;
    }

}