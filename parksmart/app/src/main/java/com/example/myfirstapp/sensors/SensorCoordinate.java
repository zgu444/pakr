package com.example.myfirstapp.sensors;

import java.util.concurrent.atomic.AtomicInteger;

public class SensorCoordinate {
    public float x_coord;
    public float y_coord;
    public final SensorType sensorType;
    private float val;

    public SensorCoordinate(float x, float y, SensorType sensorType){
        x_coord = x;
        y_coord = y;
        this.sensorType = sensorType;
        val = 30;
    }

    public synchronized float getVal(){
        return val;
    }

    public synchronized void setVal(float new_val){
        val = new_val;
    }

}