package com.example.myfirstapp.plot;

public class CarConstants {
    public static final int RATIO = 2;
    public static final int CAR_WIDTH = 74*RATIO;
    public static final int CAR_LENGTH = 125*RATIO;
    public static final int POINTY_LENGTH = 7*RATIO;
    public static final float POINTY_YX_RATIO = POINTY_LENGTH/CAR_WIDTH/2;
    public static final int SENSOR_FRONT_WHEEL = 24*RATIO;
    public static final int SENSOR_MID = 67*RATIO;
    public static final int SENSOR_BACK_WHEEL = 112*RATIO;

    /**
     * All coordinates are with respect to the center of the car
     */

    // Front left sensor
    public static final int FRONT_LEFT_X = - CAR_WIDTH/4;
    public static final int FRONT_LEFT_Y = - CAR_LENGTH/2 - POINTY_LENGTH
            + (int)(POINTY_YX_RATIO * CAR_WIDTH/4);

    // Front right sensor
    public static final int FRONT_RIGHT_X = -FRONT_LEFT_X;
    public static final int FRONT_RIGHT_Y = FRONT_LEFT_Y;

    // Front wheel sensors
    public static final int FRONT_WHEEL_LEFT_X = - CAR_WIDTH/2;
    public static final int FRONT_WHEEL_LEFT_Y = - CAR_LENGTH/2 + SENSOR_FRONT_WHEEL;

    public static final int FRONT_WHEEL_RIGHT_X = - FRONT_WHEEL_LEFT_X;
    public static final int FRONT_WHEEL_RIGHT_Y = FRONT_WHEEL_LEFT_Y;

    // Mid point sensors
    public static final int MID_LEFT_X = FRONT_WHEEL_LEFT_X;
    public static final int MID_LEFT_Y = - CAR_LENGTH/2 + SENSOR_MID;

    public static final int MID_RIGHT_X = -MID_LEFT_X;
    public static final int MID_RIGHT_Y = MID_LEFT_Y;

    // Back wheel sensors
    public static final int BACK_WHEEL_LEFT_X = FRONT_WHEEL_LEFT_X;
    public static final int BACK_WHEEL_LEFT_Y = - CAR_LENGTH/2 + SENSOR_BACK_WHEEL;;

    public static final int BACK_WHEEL_RIGHT_X = -BACK_WHEEL_LEFT_X;
    public static final int BACK_WHEEL_RIGHT_Y = BACK_WHEEL_LEFT_Y;

    public static final int BACK_X = 0;
    public static final int BACK_Y = CAR_LENGTH/2;
}
