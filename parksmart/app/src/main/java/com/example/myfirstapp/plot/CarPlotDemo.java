package com.example.myfirstapp.plot;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;


import java.util.ArrayList;

public class CarPlotDemo extends View {
    public CarPlotDemo(Context context){
        super(context);
    }
    public CarPlotDemo(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public CarPlotDemo(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    protected void onFinishInflate(){
        super.onFinishInflate();

    }

    final static int bg = Color.WHITE;
    final static int fg = Color.BLACK;
    final static int red = Color.RED;


    private void curveTo(Path pth, int a, int b, int c, int d, int e, int f){
        pth.cubicTo((float)a,(float)b,(float)c,(float)d,(float)e,(float)f);
    }

    @Override protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // custom drawing code here
        Paint paint = new Paint();

        int canvasWidth = getWidth();
        int canvasHeight = getHeight();
        int x_center = canvasWidth / 2;
        int y_center = canvasHeight / 2;

        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(fg);

        // 72, 125, and 7 are in cm
        // real measurements of the testing vehicle
        int rectWidth = 74*2;
        int rectHeight = 125*2;
        int front = 7*2;

        // draw the car
        // Draw the front and back curves
        canvas.drawLine(x_center-rectWidth/2, y_center-rectHeight/2,
                x_center, y_center-rectHeight/2-front, paint);
        canvas.drawLine(x_center+rectWidth/2, y_center-rectHeight/2,
                x_center, y_center-rectHeight/2-front, paint);

        Path pback = new Path();
        pback.moveTo(x_center-rectWidth/2, y_center+rectHeight/2);
        curveTo(pback,x_center-rectWidth/2, y_center+rectHeight/2, x_center,
                y_center+rectHeight/2 + 20, x_center+rectWidth/2, y_center+rectHeight/2);
        canvas.drawPath(pback, paint);

        // Draw 2 vertical lines
        canvas.drawLine(x_center-rectWidth/2, y_center-rectHeight/2,
                x_center-rectWidth/2, y_center+rectHeight/2, paint);
        canvas.drawLine(x_center+rectWidth/2, y_center-rectHeight/2,
                x_center+rectWidth/2, y_center+rectHeight/2, paint);

        // Draw outline on the left side of based on (x,y) of a sensor and its distance feedback
        paint.setColor(Color.BLUE);

        // Testing values
        // Assume sensor1 is at 1/4 of car length from the front on the left
        float sensor1_x = x_center-rectWidth/2;
        float sensor1_y = y_center-rectHeight/4;
        float sensor1_distance = 200;

        // Assume sensor2 is at 3/4 of car length from the front on the left
        float sensor2_x = x_center-rectWidth/2;
        float sensor2_y = y_center+rectHeight/4;
        float sensor2_distance = 150;

        float[] sensor1 = {sensor1_x, sensor1_y, sensor1_distance};
        float[] sensor2 = {sensor2_x, sensor2_y, sensor2_distance};

        float[][] sensors = {sensor1, sensor2};
        float[][] curve_points = new float[4][2];

        for (int i=0; i<2; i++) {
            float sensor_x = sensors[i][0];
            float sensor_y = sensors[i][1];
            float sensor_distance = sensors[i][2];

            float rect_left = sensor_x-sensor_distance;
            float rect_top = sensor_y-sensor_distance;
            float rect_right = sensor_x+sensor_distance;
            float rect_bottom = sensor_y+sensor_distance;

            // TODO: different start_angle for left/right sensors
            float start_angle = (float) 172.5;
            float sweep_angle = 15;

            RectF rectF = new RectF(rect_left, rect_top, rect_right, rect_bottom);
            canvas.drawArc (rectF, start_angle, sweep_angle, false, paint);

            float half_angle = sweep_angle/2;
            float triangle_top = sensor_distance*(float)Math.cos(Math.toRadians(half_angle));
            float triangle_bottom = triangle_top*(float)Math.cos(Math.toRadians(half_angle));
            float triangle_side = triangle_top*(float)Math.sin(Math.toRadians(half_angle));

            // TODO: diff calculation for start_x/end_x for left/right sensors
            float curve_start_x = sensor_x - triangle_bottom;
            float curve_start_y = sensor_y - triangle_side;

            // Find the coordinates of the start of the curve (top)
            curve_points[i*2][0] = curve_start_x;
            curve_points[i*2][1] = curve_start_y;

            // Find the coordinates of the end of the curve (bottom)
            float curve_end_x = sensor_x - triangle_bottom;
            float curve_end_y = sensor_y + triangle_side;

            curve_points[i*2+1][0] = curve_end_x;
            curve_points[i*2+1][1] = curve_end_y;

        }


        // TODO: index step by 2; differentiate left and right
        for (int i=1; i<2; i++) {
            float start_x = curve_points[i][0];
            float start_y = curve_points[i][1];
            float end_x = curve_points[i+1][0];
            float end_y = curve_points[i+1][1];

            canvas.drawLine(start_x, start_y, end_x, end_y, paint);
        }

        // TODO: remove whole section once integrated with sensor adaptor
        // Draw outline on the right side of based on (x,y) of a sensor and its distance feedback
        paint.setColor(Color.BLUE);

        // Testing values
        // Assume sensor1 is at 1/4 of car length from the front on the left
        float sensor1r_x = x_center+rectWidth/2;
        float sensor1r_y = y_center-rectHeight/4;
        float sensor1r_distance = 200;

        // Assume sensor2 is at 3/4 of car length from the front on the left
        float sensor2r_x = x_center+rectWidth/2;
        float sensor2r_y = y_center+rectHeight/4;
        float sensor2r_distance = 150;

        float[] sensor1r = {sensor1r_x, sensor1r_y, sensor1r_distance};
        float[] sensor2r = {sensor2r_x, sensor2r_y, sensor2r_distance};

        float[][] sensorsr = {sensor1r, sensor2r};
        float[][] curve_pointsr = new float[4][2];

        for (int i=0; i<2; i++) {
            float sensor_x = sensorsr[i][0];
            float sensor_y = sensorsr[i][1];
            float sensor_distance = sensorsr[i][2];

            float rect_left = sensor_x-sensor_distance;
            float rect_top = sensor_y-sensor_distance;
            float rect_right = sensor_x+sensor_distance;
            float rect_bottom = sensor_y+sensor_distance;

            // different from left
            float start_angle = (float) -8.5;
            float sweep_angle = 15;

            RectF rectF = new RectF(rect_left, rect_top, rect_right, rect_bottom);
            canvas.drawArc (rectF, start_angle, sweep_angle, false, paint);

            float half_angle = sweep_angle/2;
            float triangle_top = sensor_distance*(float)Math.cos(Math.toRadians(half_angle));
            float triangle_bottom = triangle_top*(float)Math.cos(Math.toRadians(half_angle));
            float triangle_side = triangle_top*(float)Math.sin(Math.toRadians(half_angle));

            // different from left sensors
            float curve_start_x = sensor_x + triangle_bottom;
            float curve_start_y = sensor_y - triangle_side;

            // Find the coordinates of the start of the curve (top)
            curve_pointsr[i*2][0] = curve_start_x;
            curve_pointsr[i*2][1] = curve_start_y;

            // Find the coordinates of the end of the curve (bottom)
            float curve_end_x = sensor_x + triangle_bottom;
            float curve_end_y = sensor_y + triangle_side;

            curve_pointsr[i*2+1][0] = curve_end_x;
            curve_pointsr[i*2+1][1] = curve_end_y;

        }

        for (int i=1; i<2; i++) {
            float start_x = curve_pointsr[i][0];
            float start_y = curve_pointsr[i][1];
            float end_x = curve_pointsr[i+1][0];
            float end_y = curve_pointsr[i+1][1];

            canvas.drawLine(start_x, start_y, end_x, end_y, paint);
        }

    }

}
