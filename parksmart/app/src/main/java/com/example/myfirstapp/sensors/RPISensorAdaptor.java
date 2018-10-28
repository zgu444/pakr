package com.example.myfirstapp.sensors;

import android.hardware.Sensor;

import com.example.myfirstapp.comm.SocketClient;
import com.example.myfirstapp.plot.CarConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RPISensorAdaptor implements SensorAdaptor{
    public static final int PORT_NUMBER = 18500;
    public static RPISensorAdaptor get_rpiadaptor(int center_x, int center_y){
        RPISensorAdaptor my_rpi = new RPISensorAdaptor(10);
        // 0
        my_rpi.addSensorCoordinate(new SensorCoordinate(
                center_x + CarConstants.FRONT_WHEEL_LEFT_X,
                center_y + CarConstants.FRONT_WHEEL_LEFT_Y,
                SensorType.LEFT), 0);
        // 1
        my_rpi.addSensorCoordinate(new SensorCoordinate(
                center_x + CarConstants.FRONT_WHEEL_RIGHT_X,
                center_y + CarConstants.FRONT_WHEEL_RIGHT_Y,
                SensorType.RIGHT), 1);
        // 2
        my_rpi.addSensorCoordinate(new SensorCoordinate(
                center_x + CarConstants.FRONT_LEFT_X,
                center_y + CarConstants.FRONT_LEFT_Y,
                SensorType.LEFT_FRONT), 2);
        // 3
        my_rpi.addSensorCoordinate(new SensorCoordinate(
                center_x + CarConstants.FRONT_RIGHT_X,
                center_y + CarConstants.FRONT_RIGHT_Y,
                SensorType.RIGHT_FRONT), 3);
        // 4
        my_rpi.addSensorCoordinate(new SensorCoordinate(
                center_x,
                center_y,
                SensorType.GYRO), 4);
        // 5
        my_rpi.addSensorCoordinate(new SensorCoordinate(
                center_x + CarConstants.MID_LEFT_X,
                center_y + CarConstants.MID_LEFT_Y,
                SensorType.LEFT), 5);
        // 6
        my_rpi.addSensorCoordinate(new SensorCoordinate(
                center_x + CarConstants.MID_RIGHT_X,
                center_y + CarConstants.MID_RIGHT_Y,
                SensorType.RIGHT), 6);
        // 7
        my_rpi.addSensorCoordinate(new SensorCoordinate(
                center_x + CarConstants.BACK_WHEEL_LEFT_X,
                center_y + CarConstants.BACK_WHEEL_LEFT_Y,
                SensorType.LEFT), 7);
        // 8
        my_rpi.addSensorCoordinate(new SensorCoordinate(
                center_x + CarConstants.BACK_WHEEL_RIGHT_X,
                center_y + CarConstants.BACK_WHEEL_RIGHT_Y,
                SensorType.RIGHT), 8);
        // 9
        my_rpi.addSensorCoordinate(new SensorCoordinate(
                center_x + CarConstants.BACK_X,
                center_y + CarConstants.BACK_Y,
                SensorType.BACK), 9);

        return my_rpi;
    }

    private final SensorCoordinate[] sensors;
    private final SocketClient socket_client;
    private int size;

    private RPISensorAdaptor(int arr_size){
        sensors = new SensorCoordinate[arr_size];
        socket_client = new SocketClient(PORT_NUMBER);
        size = arr_size;
    }

    public SensorCoordinate[] getSensors(){
        return sensors;
    }

    private void addSensorCoordinate(SensorCoordinate new_coord, int index){
        sensors[index] = new_coord;
    }

    public int getSize(){
        return size;
    }


    public void refreshDistance(){
        String reader = socket_client.writeToAndReadFromSocket("0");
        parseReadings(reader);
    }

    private void parseReadings(String reader){
        for (int i = 0; i < size; i++) {

        }
    }
}