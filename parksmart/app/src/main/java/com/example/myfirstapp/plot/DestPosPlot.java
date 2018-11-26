package com.example.myfirstapp.plot;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import com.example.myfirstapp.algo.ParkingAlgo;
import com.example.myfirstapp.sensors.SensorAdaptor;

public class DestPosPlot {
    private final float car_width = CarConstants.CAR_WIDTH;
    private final float car_height = CarConstants.CAR_LENGTH;
    class START_POS {
        public static final float DIST_RIGHT = 40;
        public static final float DIST_FRONT = 110;
    }
    public void plot_car_dest(ParkingAlgo.ParkingState state, SensorAdaptor s){
        ;
    }
    private void draw_car(float left, float top, Canvas canvas){
        Paint paint = new Paint();
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.parseColor("#993300"));

        // draw the car
        Path car = new Path();
        car.moveTo(top, left);
        car.lineTo(top - car_height, left);
        car.lineTo(top - car_height, left+car_width);
        car.lineTo(top, left+car_width);
        car.lineTo(top, left);
        canvas.drawPath(car, paint);
    }
}
