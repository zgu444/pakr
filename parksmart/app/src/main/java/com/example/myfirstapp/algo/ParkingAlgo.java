package com.example.myfirstapp.algo;

import android.os.AsyncTask;
import android.util.Log;

import com.example.myfirstapp.sensors.SensorCoordinate;

import java.util.ArrayList;

public class ParkingAlgo extends AsyncTask<Void, Void, Void>{
    public enum ParkingState{
        IDLE, SEARCH, FULL_RIGHT, FULL_LEFT;
    }

    //requires synchronization in async task
    private ParkingState current_state;
    private final ArrayList<SensorCoordinate> left_sensors, front_sensors, right_sensors, back_sensors;
    private final ArrayList<SensorCoordinate> parking_sensors;

    public ParkingAlgo(){
        current_state = ParkingState.IDLE;
        front_sensors = new ArrayList<SensorCoordinate>();
        left_sensors = new ArrayList<SensorCoordinate>();

        right_sensors = new ArrayList<SensorCoordinate>();
        back_sensors = new ArrayList<SensorCoordinate>();
        parking_sensors = new ArrayList<>();
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
                Thread.sleep(100);
                main_iteration();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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
        ParkingState myState;
        synchronized (current_state){
            myState = current_state;
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
