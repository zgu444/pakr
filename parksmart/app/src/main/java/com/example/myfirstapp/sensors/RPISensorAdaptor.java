package com.example.myfirstapp.sensors;

import android.annotation.SuppressLint;
import android.hardware.Sensor;
import android.os.AsyncTask;

import com.example.myfirstapp.comm.SocketClient;
import com.example.myfirstapp.plot.CarConstants;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressLint("NewApi")
public class RPISensorAdaptor extends AsyncTask<Void, Void, Void> implements SensorAdaptor {
    public static final int PORT_NUMBER = 18500;
    private static RPISensorAdaptor my_adaptor;
    public static RPISensorAdaptor get_rpiadaptor(){
        if (my_adaptor == null){
            my_adaptor = new RPISensorAdaptor(10);
            my_adaptor.populate_rpiadaptor(0, 0);
        }
        return my_adaptor;
    }
    public static RPISensorAdaptor get_rpiadaptor(int center_x, int center_y){
        if (my_adaptor == null){
            my_adaptor = create_rpiadaptor(center_x, center_y);
        }else {
            for (SensorCoordinate sensor : my_adaptor.sensors) {
                sensor.x_coord = sensor.x_coord - my_adaptor.x_center + center_x;
                sensor.y_coord = sensor.y_coord - my_adaptor.y_center + center_y;
            }
        }
        return my_adaptor;
    }
    private void populate_rpiadaptor(int center_x, int center_y){
        this.x_center = center_x;
        this.y_center = center_y;
        // 0
        addSensorCoordinate(new SensorCoordinate(
                center_x + CarConstants.FRONT_WHEEL_LEFT_X,
                center_y + CarConstants.FRONT_WHEEL_LEFT_Y,
                SensorType.LEFT), 0);
        // 1
        addSensorCoordinate(new SensorCoordinate(
                center_x + CarConstants.FRONT_WHEEL_RIGHT_X,
                center_y + CarConstants.FRONT_WHEEL_RIGHT_Y,
                SensorType.RIGHT), 1);
        // 2
        addSensorCoordinate(new SensorCoordinate(
                center_x + CarConstants.FRONT_LEFT_X,
                center_y + CarConstants.FRONT_LEFT_Y,
                SensorType.LEFT_FRONT), 2);
        // 3
        addSensorCoordinate(new SensorCoordinate(
                center_x + CarConstants.FRONT_RIGHT_X,
                center_y + CarConstants.FRONT_RIGHT_Y,
                SensorType.RIGHT_FRONT), 3);
        // 4
        addSensorCoordinate(new SensorCoordinate(
                center_x,
                center_y,
                SensorType.GYRO), 4);
        // 5
        addSensorCoordinate(new SensorCoordinate(
                center_x + CarConstants.MID_LEFT_X,
                center_y + CarConstants.MID_LEFT_Y,
                SensorType.LEFT), 5);
        // 6
        addSensorCoordinate(new SensorCoordinate(
                center_x + CarConstants.MID_RIGHT_X,
                center_y + CarConstants.MID_RIGHT_Y,
                SensorType.RIGHT), 6);
        // 7
        addSensorCoordinate(new SensorCoordinate(
                center_x + CarConstants.BACK_WHEEL_LEFT_X,
                center_y + CarConstants.BACK_WHEEL_LEFT_Y,
                SensorType.LEFT), 7);
        // 8
        addSensorCoordinate(new SensorCoordinate(
                center_x + CarConstants.BACK_WHEEL_RIGHT_X,
                center_y + CarConstants.BACK_WHEEL_RIGHT_Y,
                SensorType.RIGHT), 8);
        // 9
        addSensorCoordinate(new SensorCoordinate(
                center_x + CarConstants.BACK_X,
                center_y + CarConstants.BACK_Y,
                SensorType.BACK), 9);
    }

    private static RPISensorAdaptor create_rpiadaptor(int center_x, int center_y){
        RPISensorAdaptor my_rpi = new RPISensorAdaptor(10);
        my_rpi.populate_rpiadaptor(center_x, center_y);
        return my_rpi;
    }

    private final SensorCoordinate[] sensors;
    private final SocketClient socket_client;
    private int size;
    private float x_center;
    private float y_center;

    private RPISensorAdaptor(int arr_size){
        sensors = new SensorCoordinate[arr_size];
        socket_client = new SocketClient(PORT_NUMBER);
        size = arr_size;
    }

    @Override
    public SensorCoordinate[] getSensors(){
        return sensors;
    }

    private void addSensorCoordinate(SensorCoordinate new_coord, int index){
        sensors[index] = new_coord;
    }

    @Override
    public int getSize(){
        return size;
    }


    public synchronized void refreshDistance(){
        String data = socket_client.writeToAndReadFromSocket("0");
        parseReadings(data);
    }

    private void parseReadings(String data){
        String[] datas = data.split(", ");
        if (datas.length != size)
            return;
        for (int i = 0; i < size; i++) {
            sensors[i].setVal(Float.valueOf(datas[i]));
        }
    }

    @Override
    protected Void doInBackground(Void... voids) {
        while(true){
            try {
                Thread.sleep(200);
                refreshDistance();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}