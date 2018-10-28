package com.example.myfirstapp.sensors;

import com.example.myfirstapp.plot.CarConstants;

public class SensorCoordinate {
    public final int x_coord;
    public final int y_coord;
    public final SensorType sensorType;
    private float val;

    public SensorCoordinate(int x, int y, SensorType sensorType){
        x_coord = x;
        y_coord = y;
        this.sensorType = sensorType;
        val = 100;
    }

    public synchronized float getVal(){
        return val* CarConstants.RATIO;
    }

    public synchronized void setVal(float new_val){
        val = new_val;
    }

}