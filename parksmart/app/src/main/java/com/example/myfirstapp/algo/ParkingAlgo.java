package com.example.myfirstapp.algo;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;


import com.example.myfirstapp.sensors.RPISensorAdaptor;
import com.example.myfirstapp.sensors.SensorCoordinate;

import java.util.ArrayList;

public class ParkingAlgo extends AsyncTask<Void, String, Void>{
    public static final int ALGO_SLEEP_TIME = 500;

    public enum ParkingState{
        IDLE, SEARCH, FULL_RIGHT, FULL_LEFT;
    }

    //requires synchronization in async task
    public ParkingState current_state;
    private final ArrayList<SensorCoordinate> left_sensors, front_sensors, right_sensors, back_sensors;
    private final ArrayList<SensorCoordinate> parking_sensors;
    private SensorCoordinate gyro;
    private final OverlayConsole debugConsole;

    public ParkingAlgo(OverlayConsole debugConsole){
        current_state = ParkingState.IDLE;
        front_sensors = new ArrayList<SensorCoordinate>();
        left_sensors = new ArrayList<SensorCoordinate>();

        right_sensors = new ArrayList<SensorCoordinate>();
        back_sensors = new ArrayList<SensorCoordinate>();
        parking_sensors = new ArrayList<>();
        this.debugConsole = debugConsole;

    }

    /*
    Helper methods that define the constant ranges
     */
    private boolean isTooClose(float distance){
        return distance <= 35;
    }

    private boolean isInRange(float distance){
        return distance > 35 && distance <= 44;
    }

    private boolean isTooFar(float distance){
        return distance > 44 && distance <= 85;
    }

    private boolean isOutOfSight(float distance){
        return distance > 85;
    }



    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        for (String s: values) {
            debugConsole.log(s);
            Log.d("ALGO", s);
        }
    }

    @Override
    protected Void doInBackground(Void... voids) {
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
        while(!isCancelled()){
            try {
                main_iteration();

                Thread.sleep(ALGO_SLEEP_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    public void startAlgo(){
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

    public void endAlgo(){
            current_state = ParkingState.IDLE;

    }

    /**
     * sample usage:
     * while(true) main_iteration();
     */
    public void main_iteration(){
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

    /**
     * The checker used when in idle state
     * Will give warnings when sensor readings are too close to the vehicle
     */
    private void idle(){
        publishProgress("IDLE state");
    }
    private enum ParallelStates{
        RESET, OUT_OF_SIGHT, MID_BACK_OUT, BACK_END_OUT, END_OUT,
        PARALLEL, FRONT_OUT, FRONT_MID_OUT, FRONT_MID_BACK_OUT
    }
    private enum DistanceStates{
        RESET, TOO_FAR, IN_RANGE, TOO_CLOSE
    }
    private ParallelStates parallel_status_flag = ParallelStates.RESET;
    private DistanceStates distance_status_flag = DistanceStates.RESET;

    /**
     * State will change if the back parking sensor is within 18~22 cm to the vehicle next to us
     */
    private void search_parallel(){
        publishProgress("I'm in search state");
        float end = parking_sensors.get(0).getRaw();
        float front = right_sensors.get(0).getRaw();
        float mid = right_sensors.get(1).getRaw();
        float back = right_sensors.get(2).getRaw();

        withinSightCheck(front, mid, back, end);
        distanceAngleCheck(front, mid, back, end);

        if (parallel_status_flag == ParallelStates.PARALLEL
                && distance_status_flag == DistanceStates.IN_RANGE){
            current_state = ParkingState.FULL_RIGHT;
            publishProgress("That's a good parallel spot! Entering reverse state");
            /**
             * Insert instruction for reverse right here!!
             */
        }


    }

    private void distanceAngleCheck(float front, float mid, float back, float end){
        if (parallel_status_flag == ParallelStates.RESET ||
                parallel_status_flag == ParallelStates.OUT_OF_SIGHT){
            return;
        }
        if (isTooClose(front) || isTooClose(mid) || isTooClose(back) || isTooClose(end)){
            if (distance_status_flag != DistanceStates.TOO_CLOSE){
                publishProgress("Some part of the vehicle is too close to the reference car");
                distance_status_flag = DistanceStates.TOO_CLOSE;
            }
            return;
        }
        if (isTooFar(front) || isTooFar(mid) || isTooFar(back) || isTooFar(end)){
            if (distance_status_flag != DistanceStates.TOO_FAR){
                publishProgress("Some part of the vehicle is too far to the reference car");
                distance_status_flag = DistanceStates.TOO_FAR;
            }
            return;
        }
        if (isInRange(end)){
            if (distance_status_flag != DistanceStates.IN_RANGE){
                publishProgress("the distance on the right is good");
                distance_status_flag = DistanceStates.IN_RANGE;
            }
            return;
        }
    }

    private void withinSightCheck(float front, float mid, float back, float end){
        //first whether vehicle is in sight
        if (isOutOfSight(front) && isOutOfSight(mid)
                && isOutOfSight(back) && isOutOfSight(end)){
            if (parallel_status_flag != ParallelStates.OUT_OF_SIGHT) {
                publishProgress("The reference vehicle is completely out of sight. Pull forward or go back");
                parallel_status_flag = ParallelStates.OUT_OF_SIGHT;
            }
            return;
        }
        //partially in sight
        if (!isOutOfSight(front) && isOutOfSight(mid)
                && isOutOfSight(back) && isOutOfSight(end)) {
            if (parallel_status_flag != ParallelStates.MID_BACK_OUT) {
                publishProgress("Front wheel sees reference vehicle, pull forward slowly");
                parallel_status_flag = ParallelStates.MID_BACK_OUT;
            }
            return;
        }
        if (!isOutOfSight(front) && !isOutOfSight(mid)
                && isOutOfSight(back) && isOutOfSight(end)) {
            if (parallel_status_flag != ParallelStates.BACK_END_OUT) {
                publishProgress("Front & mid wheel sees reference vehicle, pull forward slowly");
                parallel_status_flag = ParallelStates.BACK_END_OUT;
            }
            return;
        }
        if (!isOutOfSight(front) && !isOutOfSight(mid)
                && !isOutOfSight(back) && isOutOfSight(end)) {
            if (parallel_status_flag != ParallelStates.END_OUT) {
                publishProgress("We see the reference vehicle, pull forward very slowly");
                parallel_status_flag = ParallelStates.END_OUT;
            }
            return;
        }
        //ideal position
        if (!isOutOfSight(front) && !isOutOfSight(mid)
                && !isOutOfSight(back) && !isOutOfSight(end)) {
            if (parallel_status_flag != ParallelStates.PARALLEL) {
                publishProgress("We have reached the ideal y position.Please stop!");
                parallel_status_flag = ParallelStates.PARALLEL;
            }
            return;
        }
        //partially in sight in the other direction
        if (isOutOfSight(front) && !isOutOfSight(mid)
                && !isOutOfSight(back) && !isOutOfSight(end)) {
            if (parallel_status_flag != ParallelStates.FRONT_OUT) {
                publishProgress("Front wheel passed reference vehicle, go backwards slowly");
                parallel_status_flag = ParallelStates.FRONT_OUT;
            }
            return;
        }
        if (isOutOfSight(front) && isOutOfSight(mid)
                && !isOutOfSight(back) && !isOutOfSight(end)) {
            if (parallel_status_flag != ParallelStates.FRONT_MID_OUT) {
                publishProgress("Front & mid wheel passed reference vehicle, go backwards slowly");
                parallel_status_flag = ParallelStates.FRONT_MID_OUT;
            }
            return;
        }
        if (isOutOfSight(front) && isOutOfSight(mid)
                && isOutOfSight(back) && !isOutOfSight(end)) {
            if (parallel_status_flag != ParallelStates.FRONT_MID_BACK_OUT) {
                publishProgress("Most of car passed the reference vehicle, go backwards slowly");
                parallel_status_flag = ParallelStates.FRONT_MID_BACK_OUT;
            }
            return;
        }
    }

    /**
     * Full right & reverse until right-front sensor reports large distance
     */
    private void reverse_right(){
        float front = right_sensors.get(0).getRaw();
        if (front >= 200){
            current_state = ParkingState.FULL_LEFT;
            publishProgress("Entering reverse left");
        }


    }

    /**
     * Reverse left until within 20 of the back obstacle
     */
    private void reverse_left(){
        float rear = back_sensors.get(0).getRaw();
        if (rear <= 20){
            current_state = ParkingState.IDLE;
            publishProgress("Done! Stop!");
        }

    }



}
