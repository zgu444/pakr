package com.example.myfirstapp.algo;

import android.os.AsyncTask;
import android.util.Log;

public class ParkingAlgo{
    public enum ParkingState{
        IDLE, SEARCH, FULL_RIGHT, FULL_LEFT;
    }
    private ParkingState current_state;

    public ParkingAlgo(){
        current_state = ParkingState.IDLE;
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
        synchronized (current_state){
            switch (current_state){
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
