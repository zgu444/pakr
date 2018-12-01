package com.example.myfirstapp.algo;

import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;



import com.example.myfirstapp.MainActivity;
import com.example.myfirstapp.R;
import com.example.myfirstapp.sensors.RPISensorAdaptor;
import com.example.myfirstapp.sensors.SensorCoordinate;

import java.util.ArrayList;

public class ParkingAlgo extends AsyncTask<Void, String, Void>{
    public static final int ALGO_SLEEP_TIME = 100;

    public enum ParkingState{
        IDLE, SEARCH, FULL_RIGHT, FULL_LEFT;
    }

    //requires synchronization in async task
    public ParkingState current_state;
    private final ArrayList<SensorCoordinate> left_sensors, front_sensors, right_sensors, back_sensors;
    private final ArrayList<SensorCoordinate> parking_sensors;
    private SensorCoordinate gyro;
    private final OverlayConsole debugConsole;


    // Test code for playing audio
    private SoundPool soundPool;
    private static final int MAX_STREAMS = 5;
    private static final int streamType = AudioManager.STREAM_MUSIC;
    private boolean loaded;
    private int all_left;
    private int all_right;
    private int pull_forward;
    private int to_reference;
    private int go_back;
    private int reverse;
    private int stop;
    private int to_left;
    private int to_right;
    private float volume;


    public ParkingAlgo(OverlayConsole debugConsole, MainActivity mainActivity, AudioManager audioManager){
        current_state = ParkingState.IDLE;
        front_sensors = new ArrayList<SensorCoordinate>();
        left_sensors = new ArrayList<SensorCoordinate>();

        right_sensors = new ArrayList<SensorCoordinate>();
        back_sensors = new ArrayList<SensorCoordinate>();
        parking_sensors = new ArrayList<>();
        this.debugConsole = debugConsole;


        float currentVolumeIndex = (float) audioManager.getStreamVolume(streamType);
        float maxVolumeIndex  = (float) audioManager.getStreamMaxVolume(streamType);
        this.volume =  currentVolumeIndex/maxVolumeIndex;
        mainActivity.setVolumeControlStream(streamType);

        AudioAttributes audioAttrib = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        SoundPool.Builder builder= new SoundPool.Builder();
        builder.setAudioAttributes(audioAttrib).setMaxStreams(MAX_STREAMS);

        this.soundPool = builder.build();
        this.soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                loaded = true;
            }
        });

        // Load sound files into SoundPool.
        this.all_left = this.soundPool.load(mainActivity, R.raw.all_left,1);
        this.all_right = this.soundPool.load(mainActivity, R.raw.all_right,1);
        this.pull_forward = this.soundPool.load(mainActivity, R.raw.pull_forward, 1);
        this.go_back = this.soundPool.load(mainActivity, R.raw.go_back, 1);
        this.reverse = this.soundPool.load(mainActivity, R.raw.reverse,1);
        this.stop = this.soundPool.load(mainActivity, R.raw.stop,1);
        this.to_reference = this.soundPool.load(mainActivity, R.raw.to_reference, 1);
        this.to_left = this.soundPool.load(mainActivity, R.raw.to_left, 1);
        this.to_right = this.soundPool.load(mainActivity, R.raw.to_right, 1);

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

            //debugConsole.log(s);
            Log.d("ALGO", s);

            if (s.equals(AlgoConstants.PULL_FORWARD) || s.equals(AlgoConstants.PULL_FORWARD_1)
                    || s.equals(AlgoConstants.PULL_FORWARD_2)){
                soundPool.play(pull_forward, volume, volume, 1, 0, 1f);
            }
            else if (s.equals(AlgoConstants.GO_BACK) || s.equals(AlgoConstants.GO_BACK_1)
                    ||s.equals(AlgoConstants.GO_BACK_2)){
                soundPool.play(go_back, volume, volume, 1, 0, 1f);
            }
            else if (s.equals(AlgoConstants.ENTER_REVERSE)) {
                soundPool.play(reverse, volume, volume, 1, 0, 1f);
            }
            else if (s.equals(AlgoConstants.REVERSE_LEFT)){
                soundPool.play(all_left, volume, volume, 1, 0, 1f);
            }
            else if (s.equals(AlgoConstants.TO_LEFT)){
                soundPool.play(to_left, volume, volume, 1, 0, 1f);
            }
            else if (s.equals(AlgoConstants.TO_RIGHT)){
                soundPool.play(to_right, volume, volume, 1, 0, 1f);
            }
            else if (s.equals(AlgoConstants.OUT_SIGHT)){
                soundPool.play(to_reference, volume, volume, 1, 0, 1f);
            }
            else if (s.equals(AlgoConstants.STOP)){
                soundPool.play(stop, volume, volume, 1, 0, 1f);
            }

        }
    }

    @Override
    protected Void doInBackground(Void... voids) {
        RPISensorAdaptor myAdaptor = null;
        int count = 0;
        while (myAdaptor == null && count < 50) {
            myAdaptor = RPISensorAdaptor.get_adaptor_no_create();
            count ++;
            if (myAdaptor == null){
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
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

            parallel_status_flag = ParallelStates.RESET;
            distance_status_flag = DistanceStates.RESET;
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
    }
    private enum ParallelStates{
        RESET, OUT_OF_SIGHT, HALF_BACK_OUT,
        PARALLEL, HALF_FRONT_OUT
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
        float end = parking_sensors.get(0).getRaw();
        float front = right_sensors.get(0).getRaw();
        float mid = right_sensors.get(1).getRaw();
        float back = right_sensors.get(2).getRaw();
        //publishProgress("end "+end+" front "+front+" mid "+mid+" back "+back);

        withinSightCheck(front, mid, back, end);
        distanceAngleCheck(front, mid, back, end);

        if (parallel_status_flag == ParallelStates.PARALLEL
                && distance_status_flag == DistanceStates.IN_RANGE){
            current_state = ParkingState.FULL_RIGHT;
            publishProgress(AlgoConstants.ENTER_REVERSE);
            /**
             * Insert instruction for reverse right here!!
             */
        }


    }

    private void distanceAngleCheck(float front, float mid, float back, float end){
        if (parallel_status_flag != ParallelStates.PARALLEL){
            return;
        }
        Log.d("algo","front: "+front+"mid: "+mid+"back: "+back+"end: "+end);
        if (isTooClose(end)){
            if (distance_status_flag != DistanceStates.TOO_CLOSE){
                publishProgress(AlgoConstants.TO_LEFT);
                distance_status_flag = DistanceStates.TOO_CLOSE;
            }
            return;
        }
        if (isTooFar(end)){
            if (distance_status_flag != DistanceStates.TOO_FAR){
                publishProgress(AlgoConstants.TO_RIGHT);
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
                if (parallel_status_flag != ParallelStates.HALF_BACK_OUT)
                    publishProgress(AlgoConstants.OUT_SIGHT);
                parallel_status_flag = ParallelStates.OUT_OF_SIGHT;
            }
            return;
        }
        //partially in sight
        if (!isOutOfSight(front) && isOutOfSight(mid)
                && isOutOfSight(back) && isOutOfSight(end)) {
            if (parallel_status_flag != ParallelStates.HALF_BACK_OUT) {
                publishProgress(AlgoConstants.PULL_FORWARD);
                parallel_status_flag = ParallelStates.HALF_BACK_OUT;
            }
            return;
        }
        if (!isOutOfSight(front) && !isOutOfSight(mid)
                && isOutOfSight(back) && isOutOfSight(end)) {
            if (parallel_status_flag != ParallelStates.HALF_BACK_OUT) {
                publishProgress(AlgoConstants.PULL_FORWARD_1);
                parallel_status_flag = ParallelStates.HALF_BACK_OUT;
            }
            return;
        }
        if (!isOutOfSight(front) && !isOutOfSight(mid)
                && !isOutOfSight(back) && isOutOfSight(end)) {
            if (parallel_status_flag != ParallelStates.HALF_BACK_OUT) {
                publishProgress(AlgoConstants.PULL_FORWARD_2);
                parallel_status_flag = ParallelStates.HALF_BACK_OUT;
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
            if (parallel_status_flag != ParallelStates.HALF_FRONT_OUT) {
                publishProgress(AlgoConstants.GO_BACK);
                parallel_status_flag = ParallelStates.HALF_FRONT_OUT;
            }
            return;
        }
        if (isOutOfSight(front) && isOutOfSight(mid)
                && !isOutOfSight(back) && !isOutOfSight(end)) {
            if (parallel_status_flag != ParallelStates.HALF_FRONT_OUT) {
                publishProgress(AlgoConstants.GO_BACK_1);
                parallel_status_flag = ParallelStates.HALF_FRONT_OUT;
            }
            return;
        }
        if (isOutOfSight(front) && isOutOfSight(mid)
                && isOutOfSight(back) && !isOutOfSight(end)) {
            if (parallel_status_flag != ParallelStates.HALF_FRONT_OUT) {
                publishProgress(AlgoConstants.GO_BACK_2);
                parallel_status_flag = ParallelStates.HALF_FRONT_OUT;
            }
            return;
        }
    }

    /**
     * Full right & reverse until right-front sensor reports large distance
     */
    private void reverse_right(){
        float front = right_sensors.get(0).getRaw();
        float mid = right_sensors.get(1).getRaw();
        if (front >= 200 && mid >= 130){
            current_state = ParkingState.FULL_LEFT;
            publishProgress(AlgoConstants.REVERSE_LEFT);
        }


    }

    /**
     * Reverse left until within 20 of the back obstacle
     */
    private void reverse_left(){
        float rear = back_sensors.get(0).getRaw();
        if (rear <= 40){
            current_state = ParkingState.IDLE;
            publishProgress(AlgoConstants.STOP);
        }

    }



}
