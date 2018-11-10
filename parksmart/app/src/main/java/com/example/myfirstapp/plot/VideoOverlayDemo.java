package com.example.myfirstapp.plot;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
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


public class VideoOverlayDemo extends View {
    private static final int ANGLE_OFFSET = 30;
    private static final int MAX_ANGLE = 20;
    private static final float PLOT_RATIO = (float) (2.0/3.0);
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

    private void curveTo(Path pth, float a, float b, float c, float d, float e, float f){
        pth.cubicTo(a,b,c,d,e,f);
    }

    private SensorCoordinate gyro;

    @Override protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (gyro == null){
            SensorAdaptor my_rpi = RPISensorAdaptor.get_adaptor_no_create();
            if (my_rpi == null)
                return;
            SensorCoordinate[] sensors = my_rpi.getSensors();
            for (SensorCoordinate sensor: sensors){
                if (sensor.sensorType == SensorType.GYRO)
                    gyro = sensor;
            }
            if (gyro == null)
                return;
        }
        assert(gyro != null);

        Paint paint = new Paint();
        int width = getWidth();
        int height = getHeight();
        int edge_offset = 30;

        float angle_ratio = ((float)-2.0)/((float)3.0);
        float wheelAngle = gyro.getVal()*angle_ratio;


        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(20);
        paint.setColor(Color.RED);

        // Wheel turned to the right
        if (wheelAngle > 0) {
            if (wheelAngle > MAX_ANGLE)
            {
                wheelAngle = 20;
            }

            float x_offset = height*(float) Math.tan(Math.toRadians(ANGLE_OFFSET+wheelAngle));

            // Draw left curve
            Path pleft = new Path();
            pleft.moveTo(0+edge_offset, height);
            curveTo(pleft,0+edge_offset, height,
                    x_offset/8+edge_offset, height - height/4, x_offset*3/5+edge_offset, height*PLOT_RATIO);
            canvas.drawPath(pleft, paint);

            // Draw right curve
            Path pright = new Path();
            pright.moveTo(width-edge_offset, height);
            curveTo(pright,width-edge_offset, height,
                    width - x_offset/8 - edge_offset, height - height/4, width-edge_offset, height*PLOT_RATIO);
            canvas.drawPath(pright, paint);

            canvas.drawLine(x_offset*3/5+edge_offset, height*PLOT_RATIO,width-edge_offset, height*PLOT_RATIO, paint);
        }

        // Wheel turned to the left
        else if (wheelAngle < 0) {
            if (wheelAngle < -MAX_ANGLE) {
                wheelAngle = -20;
            }

            float x_offset = height*(float) Math.tan(Math.toRadians(ANGLE_OFFSET-wheelAngle));

            // Draw left curve
            Path pleft = new Path();
            pleft.moveTo(0+edge_offset, height);
            curveTo(pleft,0+edge_offset, height,
                    x_offset/8 + edge_offset, height - height/4, edge_offset, height*PLOT_RATIO);
            canvas.drawPath(pleft, paint);

            // Draw right curve
            Path pright = new Path();
            pright.moveTo(width-edge_offset, height);
            curveTo(pright,width-edge_offset, height,
                    width - x_offset/8-edge_offset, height - height/4, width - (x_offset*3/5)-edge_offset, height*PLOT_RATIO);
            canvas.drawPath(pright, paint);

            canvas.drawLine(edge_offset, height*PLOT_RATIO, width - (x_offset*3/5)-edge_offset, height*PLOT_RATIO, paint);

        }

        // If wheels didn't turn, draw straight lines
        else {
            float x_offset = height*(float) Math.tan(Math.toRadians(ANGLE_OFFSET));
            canvas.drawLine(x_offset/2+edge_offset, height*PLOT_RATIO, 0+edge_offset, height, paint);
            canvas.drawLine(width-x_offset/2-edge_offset, height*PLOT_RATIO, width-edge_offset, height, paint);
            canvas.drawLine(x_offset/2+edge_offset, height*PLOT_RATIO,width-x_offset/2-edge_offset, height*PLOT_RATIO, paint);
        }


    }

}
