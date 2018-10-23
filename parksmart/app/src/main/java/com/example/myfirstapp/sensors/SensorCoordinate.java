package com.example.myfirstapp.sensors;

public class SensorCoordinate {
    public final int x_coord;
    public final int y_coord;
    public final SensorType sensorType;

    public Coordinate(int x, int y, SensorType sensorType){
        x_coord = x;
        y_coord = y;
        this.sensorType = sensorType;
    }

}