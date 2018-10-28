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

import com.example.myfirstapp.sensors.SensorAdaptor;

import java.util.ArrayList;


public class VideoOverlayDemo extends View {
    private static final int ANGLE_OFFSET = 15;
    public VideoOverlayDemo(Context context){
        super(context);
    }
    public VideoOverlayDemo(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public VideoOverlayDemo(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    protected void onFinishInflate(){
        super.onFinishInflate();
    }

    private void curveTo(Path pth, int a, int b, int c, int d, int e, int f){
        pth.cubicTo((float)a,(float)b,(float)c,(float)d,(float)e,(float)f);
    }

    @Override protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        int width = getWidth();
        int height = getHeight();

        int x_center = width / 2;
        int y_center = height / 2;
        int wheelAngle = 0;

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(20);
        paint.setColor(Color.RED);


        // If wheel turns, draw arcs
        if (wheelAngle != 10) {

//            Path pback = new Path();
//
//            pback.moveTo(x_center-width/2, y_center+height/2);
//            curveTo(pback,x_center-width/2, y_center+height/2, x_center,
//                    y_center+height/2 + 20, x_center+width/2, y_center+height/2);
//            canvas.drawPath(pback, paint);

            double tan = Math.tan(Math.toRadians(wheelAngle));
            float turn_radius = Math.round(CarConstants.CAR_LENGTH/tan-width/2);
            float turn_center_x = x_center+turn_radius;
            float turn_center_y = y_center+CarConstants.CAR_LENGTH/2;

            float upper_left_x_right = turn_center_x - turn_radius + width/2;
            float upper_left_y_right = turn_center_y - turn_radius;
            float arcAngle_right = Math.round(2*CarConstants.CAR_LENGTH/(2*turn_radius*Math.PI)*360);

            float upper_left_x_left = turn_center_x - turn_radius -  width/2;
            float upper_left_y_left = turn_center_y - turn_radius - width;
            float arcAngle_left = Math.round(2*CarConstants.CAR_LENGTH/(2*(turn_radius+ width)*Math.PI)*360);

            // Draw right curve
            RectF rect_Right = new RectF(upper_left_x_right, upper_left_y_right, upper_left_x_right+2*turn_radius, upper_left_y_right+2*turn_radius);
            canvas.drawArc (rect_Right, 180, arcAngle_right, false, paint);

            // Draw left curve
            RectF rect_Left = new RectF(upper_left_x_left, upper_left_y_left, upper_left_x_left+2*(turn_radius+ width), upper_left_y_left+2*(turn_radius+ width));
            canvas.drawArc (rect_Left, 180, arcAngle_left, false, paint);
        }

        // If wheels didn't turn, draw straight lines
        else {
            float x_offset = height*(float) Math.tan(Math.toRadians(ANGLE_OFFSET));
            canvas.drawLine(x_offset, 0, 0, height, paint);
            canvas.drawLine(width-x_offset, 0, width, height, paint);
            canvas.drawLine(x_offset, 0,width-x_offset, 0, paint);
        }


    }

}
