package com.example.myfirstapp.plot;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import com.example.myfirstapp.plot.CarConstants;
import com.example.myfirstapp.sensors.RPISensorAdaptor;
import com.example.myfirstapp.sensors.SensorAdaptor;
import com.example.myfirstapp.sensors.SensorCoordinate;
import com.example.myfirstapp.sensors.SensorType;

import java.util.ArrayList;

public class CarPlotDemo extends View {
    public CarPlotDemo(Context context){
        super(context);
        init();
    }
    public CarPlotDemo(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public CarPlotDemo(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        x_center = getWidth()/2;
        y_center = getHeight()/2;
        myAdaptor = RPISensorAdaptor.get_rpiadaptor(x_center, y_center);
        SensorCoordinate[] coordinates = myAdaptor.getSensors();
        left_sensors = new ArrayList<>();
        front_sensors = new ArrayList<>();
        right_sensors = new ArrayList<>();
        back_sensor = new ArrayList<>();
        for (SensorCoordinate coord : coordinates) {
            switch (coord.sensorType){
                case LEFT_FRONT:
                case RIGHT_FRONT:
                    front_sensors.add(coord);
                    break;
                case BACK:
                    back_sensor.add(coord);
                    break;
                case LEFT:
                    left_sensors.add(coord);
                    break;
                case RIGHT:
                    right_sensors.add(coord);
                    break;
                case GYRO:
                    break;
            }

        }
    }
    @Override
    protected void onFinishInflate(){
        super.onFinishInflate();
    }
    private SensorAdaptor myAdaptor;
    private ArrayList<SensorCoordinate> left_sensors, front_sensors, right_sensors, back_sensor;
    private int x_center, y_center;

    private void curveTo(Path pth, int a, int b, int c, int d, int e, int f){
        pth.cubicTo((float)a,(float)b,(float)c,(float)d,(float)e,(float)f);
    }

    @Override protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();

        int canvasWidth = getWidth();
        int canvasHeight = getHeight();

        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);

        int rectWidth = CarConstants.CAR_WIDTH*2;
        int rectHeight = CarConstants.CAR_LENGTH*2;
        int front = CarConstants.POINTY_LENGTH*2;

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

        // TODO: replace with values from SensorAdaptor once implemented
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

        // This for loop draws the outline of obstacles
        // based on the (x,y) values of sensors, placement of sensors (left, right, front, back)
        // and distance feedback

        for (int i=0; i<sensors.length; i++) {
            float sensor_x = sensors[i][0];
            float sensor_y = sensors[i][1];
            float sensor_distance = sensors[i][2];

            float rect_left = sensor_x-sensor_distance;
            float rect_top = sensor_y-sensor_distance;
            float rect_right = sensor_x+sensor_distance;
            float rect_bottom = sensor_y+sensor_distance;

            // TODO: different start_angle for left/right sensors

            // if sensor on the left (not including the FRONT LEFT sensor)
            float start_angle = (float) 172.5;
            // if sensor on the right (not including the FRONT RIGHT sensor)
            // float start_angle = (float) -8.5;
            // if back sensor
            // float start_angle = (float) 82.5;

            float sweep_angle = 15;

            RectF rectF = new RectF(rect_left, rect_top, rect_right, rect_bottom);
            canvas.drawArc (rectF, start_angle, sweep_angle, false, paint);

            float half_angle = sweep_angle/2;
            float triangle_top = sensor_distance*(float)Math.cos(Math.toRadians(half_angle));
            float triangle_bottom = triangle_top*(float)Math.cos(Math.toRadians(half_angle));
            float triangle_side = triangle_top*(float)Math.sin(Math.toRadians(half_angle));

            // TODO: diff calculation for start_x/end_x for left/right sensors

            // if left sensor
            // Find the coordinates of the start of the curve (top)
            float curve_start_x = sensor_x - triangle_bottom;
            float curve_start_y = sensor_y - triangle_side;

            // Find the coordinates of the end of the curve (bottom)
            float curve_end_x = sensor_x - triangle_bottom;
            float curve_end_y = sensor_y + triangle_side;


            // if right sensor
            // Find the coordinates of the start of the curve (top)
//            float curve_start_x = sensor_x + triangle_bottom;
//            float curve_start_y = sensor_y - triangle_side;

            // Find the coordinates of the end of the curve (bottom)
//            float curve_end_x = sensor_x + triangle_bottom;
//            float curve_end_y = sensor_y + triangle_side;


            // if back sensor
            // find the coordinates of start of the curve (left)
            // float curve_start_x = sensor_x - triangle_side;
            // float curve_start_y = sensor_y + triangle_bottom;

            // Find the coordinates of the end of the curve (right)
            // float curve_end_x = sensor_x + triangle_side;
            // float curve_end_y = sensor_y + triangle_bottom;


            // This part adds the (x,y) coordinates of the start and end point of each curve to an array
            // so that we can later connect the curves
            curve_points[i*2][0] = curve_start_x;
            curve_points[i*2][1] = curve_start_y;
            curve_points[i*2+1][0] = curve_end_x;
            curve_points[i*2+1][1] = curve_end_y;
        }


        // TODO: index step by 2; differentiate left and right
        // TODO: arrange values such that all left sensors are together,
        // TODO: all right sensors are together
        // TODO: figure out how to connect the front/back curves with the side curves

        // Connect the different curves
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
            // Find the coordinates of the start of the curve (top)
            float curve_start_x = sensor_x + triangle_bottom;
            float curve_start_y = sensor_y - triangle_side;

            // Find the coordinates of the end of the curve (bottom)
            float curve_end_x = sensor_x + triangle_bottom;
            float curve_end_y = sensor_y + triangle_side;

            curve_pointsr[i*2][0] = curve_start_x;
            curve_pointsr[i*2][1] = curve_start_y;
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

        // Draw outline on the BACK based on (x,y) of a sensor and its distance feedback
        paint.setColor(Color.BLUE);

        // TODO: combine this section with the left/right side drawing
        // Testing values
        // Assume sensorb is at the center of the back
        float sensorb_x = x_center;
        float sensorb_y = y_center+rectHeight/2;
        float sensorb_distance = 200;
        float[] sensorb = {sensorb_x, sensorb_y, sensorb_distance};

        float[][] sensorsb = {sensorb};

        for (int i=0; i<1; i++) {
            float sensor_x = sensorsb[i][0];
            float sensor_y = sensorsb[i][1];
            float sensor_distance = sensorsb[i][2];

            float rect_left = sensor_x-sensor_distance;
            float rect_top = sensor_y-sensor_distance;
            float rect_right = sensor_x+sensor_distance;
            float rect_bottom = sensor_y+sensor_distance;

            // different from left/right
            float start_angle = (float) 82.5;
            float sweep_angle = 15;

            RectF rectF = new RectF(rect_left, rect_top, rect_right, rect_bottom);
            canvas.drawArc (rectF, start_angle, sweep_angle, false, paint);

            float half_angle = sweep_angle/2;
            float triangle_top = sensor_distance*(float)Math.cos(Math.toRadians(half_angle));
            float triangle_bottom = triangle_top*(float)Math.cos(Math.toRadians(half_angle));
            float triangle_side = triangle_top*(float)Math.sin(Math.toRadians(half_angle));

            // different from left/right sensors
            // find the coordinates of start of the curve (left)
            float curve_start_x = sensor_x - triangle_side;
            float curve_start_y = sensor_y + triangle_bottom;

            // Find the coordinates of the end of the curve (right)
            float curve_end_x = sensor_x + triangle_side;
            float curve_end_y = sensor_y + triangle_bottom;
        }


        // TODO: combine this section with the left/right side drawing
        // Testing values
        // Assume sensor_f1 is at the center of the left front
        float angle_offset = (float) Math.toDegrees(Math.atan(37/7));

        float sensor_f1_x = x_center-rectWidth/4;
        float sensor_f1_y = y_center-rectHeight/2-7;
        float sensor_f1_distance = 200;

        float[] sensor_f1 = {sensor_f1_x, sensor_f1_y, sensor_f1_distance};

        // Assume sensor_f2 is at the center of the right front
        float sensor_f2_x = x_center+rectWidth/4;
        float sensor_f2_y = y_center-rectHeight/2-7;
        float sensor_f2_distance = 200;
        float[] sensor_f2 = {sensor_f2_x, sensor_f2_y, sensor_f2_distance};

        float[][] sensors_front = {sensor_f1, sensor_f2};

        for (int i=1; i<2; i++) {
            float sensor_x = sensors_front[i][0];
            float sensor_y = sensors_front[i][1];
            float sensor_distance = sensors_front[i][2];

            float rect_left = sensor_x-sensor_distance;
            float rect_top = sensor_y-sensor_distance;
            float rect_right = sensor_x+sensor_distance;
            float rect_bottom = sensor_y+sensor_distance;

            // left front sensor
//            float start_angle = (float) -((90-angle_offset)+82.5);
//            float sweep_angle = -15;

            // right front sensor
            float start_angle = (float) -(82.5-(90-angle_offset));
            float sweep_angle = -15;

            RectF rectF = new RectF(rect_left, rect_top, rect_right, rect_bottom);
            canvas.drawArc (rectF, start_angle, sweep_angle, false, paint);

            float half_angle = sweep_angle/2;

            // NOTE: for finding start and end point of curve

            // for LEFT front sensor
            float triangle_top = sensor_distance*(float)Math.cos(Math.toRadians(half_angle));
            
//            float straight_angle = (float) 82.5+(90-angle_offset)-90;
//            float opp = triangle_top*(float)Math.sin(Math.toRadians(straight_angle));
//            float neighbor = triangle_top*(float)Math.cos(Math.toRadians(straight_angle));
//
//            // Find the coordinates of the end of the curve (right)
//            float curve_end_x = sensor_x - opp;
//            float curve_end_y = sensor_y - neighbor;
//            canvas.drawLine(curve_end_x, curve_end_y, 100, 100, paint);
//
//            // find the coordinates of start of the curve (left)
//            float side_angle = (float) (180-15-(90-angle_offset)-82.5);
//            float left = triangle_top*(float) Math.sin(Math.toRadians((side_angle)));
//            float bottom = triangle_top*(float) Math.cos(Math.toRadians((side_angle)));
//
//            float curve_start_x = sensor_x-bottom;
//            float curve_start_y = sensor_y - left;
//            canvas.drawLine(curve_start_x, curve_start_y, 100, 100, paint);


            // for RIGHT front sensor
            float straight_angle = (float) 90-15+start_angle;
            float opp = triangle_top*(float)Math.sin(Math.toRadians(straight_angle));
            float neighbor = triangle_top*(float)Math.cos(Math.toRadians(straight_angle));

            // Find the coordinates of the end of the curve (right)
            float curve_end_x = sensor_x + opp;
            float curve_end_y = sensor_y - neighbor;
            canvas.drawLine(curve_end_x, curve_end_y, 100, 100, paint);

            // find the coordinates of start of the curve (left)
            float side_angle = -start_angle;
            float right = triangle_top*(float) Math.sin(Math.toRadians((side_angle)));
            float bottom = triangle_top*(float) Math.cos(Math.toRadians((side_angle)));

            float curve_start_x = sensor_x + bottom;
            float curve_start_y = sensor_y - right;
            canvas.drawLine(curve_start_x, curve_start_y, 100, 100, paint);




        }

    }

}
