package com.example.myfirstapp.algo;

import android.os.AsyncTask;
import android.util.Log;

import com.example.myfirstapp.sensors.RPISensorAdaptor;
import com.example.myfirstapp.sensors.SensorCoordinate;

import java.util.ArrayList;

public class ParkingAlgo extends AsyncTask<Void, Void, Void>{


    public static final int ALGO_SLEEP_TIME = 60;

    public enum ParkingState{
        IDLE, SEARCH, FULL_RIGHT, FULL_LEFT;
    }

    //requires synchronization in async task
    private ParkingState current_state;
    private final ArrayList<SensorCoordinate> left_sensors, front_sensors, right_sensors, back_sensors;
    private final ArrayList<SensorCoordinate> parking_sensors;
    private SensorCoordinate gyro;

    public ParkingAlgo(){
        current_state = ParkingState.IDLE;
        front_sensors = new ArrayList<SensorCoordinate>();
        left_sensors = new ArrayList<SensorCoordinate>();

        right_sensors = new ArrayList<SensorCoordinate>();
        back_sensors = new ArrayList<SensorCoordinate>();
        parking_sensors = new ArrayList<>();
        RPISensorAdaptor myAdaptor = null;
        while (myAdaptor == null)
            myAdaptor = RPISensorAdaptor.get_adaptor_no_create();
        SensorCoordinate[] coordinates = myAdaptor.getSensors();

        for (SensorCoordinate coord : coordinates) {
            switch (coord.sensorType) {
                case LEFT_FRONT:
                case RIGHT_FRONT:
                    front_sensors.add(coord);
                    break;
                case BACK:
                    back_sensors.add(coord);
                    break;
                case LEFT:
                    left_sensors.add(coord);
                    break;
                case RIGHT:
                    right_sensors.add(coord);
                    break;
                case GYRO:
                    gyro = coord;
                    break;
                case PARK:
                    parking_sensors.add(coord);
                    break;
            }
        }

    }

    /**
     * Override this method to perform a computation on a background thread. The
     * specified parameters are the parameters passed to {@link #execute}
     * by the caller of this task.
     * <p>
     * This method can call {@link #publishProgress} to publish updates
     * on the UI thread.
     *
     * @param voids The parameters of the task.
     * @return A result, defined by the subclass of this task.
     * @see #onPreExecute()
     * @see #onPostExecute
     * @see #publishProgress
     */
    @Override
    protected Void doInBackground(Void... voids) {
        while(!isCancelled()){
            try {
                Thread.sleep(ALGO_SLEEP_TIME);
                main_iteration();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    public void startAlgo(){
        synchronized (current_state) {
            switch (current_state) {
                case IDLE:
                    current_state = ParkingState.SEARCH;
                    break;
                case SEARCH:
                case FULL_LEFT:
                case FULL_RIGHT:
                    current_state = ParkingState.SEARCH;

            }
        }
    }

    public void endAlgo(){
        synchronized (current_state) {
            current_state = ParkingState.IDLE;
        }
    }

    /**
     * sample usage:
     * while(true) main_iteration();
     */
    public void main_iteration(){
        ParkingState myState = ParkingState.IDLE;
        synchronized (current_state){
            switch (current_state){
                case IDLE:
                    myState = ParkingState.IDLE;
                    break;
                case SEARCH:
                    myState = ParkingState.SEARCH;
                    break;
                case FULL_RIGHT:
                    myState = ParkingState.FULL_RIGHT;
                    break;
                case FULL_LEFT:
                    myState = ParkingState.FULL_LEFT;
                    break;
            }
        }
        switch (myState){
            case IDLE:
                idle();
                break;
            case SEARCH:
                search_parallel();
                break;
            case FULL_RIGHT:
                reverse_right();
                break;
            case FULL_LEFT:
                reverse_left();
                break;
        }
    }

    /**
     * The checker used when in idle state
     * Will give warnings when sensor readings are too close to the vehicle
     */
    private void idle(){
        Log.d("ALGO", "Algo is in " + current_state.name() + "state");
    }

    /**
     * State will change if the back parking sensor is within 18~22 cm to the vehicle next to us
     */
    private void search_parallel(){
        Log.d("Search State", "I'm in search state");
        float distanceFromRight = parking_sensors.get(0).getRaw();
        float front_wheel_distance = right_sensors.get(0).getRaw();
        if (front_wheel_distance <= 44 && front_wheel_distance >= 38
                && distanceFromRight <= 44 && distanceFromRight >= 38){
            Log.d("Search State", "correct position has been seen, switch to reverse_right state");
//            synchronized (current_state){
//                current_state = ParkingState.FULL_RIGHT;
//            }
        }else if (front_wheel_distance <= 80 && distanceFromRight >45 && distanceFromRight <= 80) {
            Log.d("Search State", "Too far from car");
        }else if (front_wheel_distance <= 80 && distanceFromRight < 38){
            Log.d("Search State", "Too close from car");
        }else {
            if (distanceFromRight > 80){
                if (front_wheel_distance > 80){
                    Log.d("Search State", "keep pulling forward");
                }else {
                    Log.d("Search State", "Front wheel sees car, slow down and keep pulling forward");
                }
            }
        }


    }

    /**
     * Full right & reverse until right-front sensor reports large distance
     */
    private void reverse_right(){

    }

    /**
     * Reverse left until within 20 of the back obstacle
     */
    private void reverse_left(){

    }


}
