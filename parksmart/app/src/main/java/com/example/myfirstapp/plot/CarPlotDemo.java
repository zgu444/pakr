package com.example.myfirstapp.plot;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.example.myfirstapp.sensors.SensorAdaptor;

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
        left_sensors = new ArrayList();
    }
    final static int maxCharHeight = 15;
    final static int minFontSize = 6;

    final static int bg = Color.WHITE;
    final static int fg = Color.BLACK;
    final static int red = Color.RED;
    final static int white = Color.WHITE;

    private ArrayList<SensorAdaptor> left_sensors;

    private void arcTo(Path pth, int a, int b, int c, int d, int e, int f){
        pth.arcTo((float)a,(float)b,(float)c,(float)d,(float)e,(float)f, false);
    }

    @Override protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // custom drawing code here
        Paint paint = new Paint();

//        Graphics2D g2 = (Graphics2D) g;
//        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int width = getWidth();
        int height = getHeight();

        int gridWidth = width / 6;
        int gridHeight = height / 2;

        int x_center = width/2;
        int y_center = height/2;

        paint.setStyle(Paint.Style.STROKE);

        // make the entire canvas white
//        paint.setColor(Color.LTGRAY);
//        RectF r1 = new RectF(0, 0 , width - 1, height -1);
//        canvas.drawRoundRect(r1, (float)0.0, (float)0.0, paint);
//
//        RectF r2 = new RectF(3, 3, width - 7, height - 7);
//        canvas.drawRoundRect(r2, (float)0.0, (float)0.0, paint);

        paint.setColor(fg);

        int rectWidth = 79;
        int rectHeight = 127;


        // draw the car

        Path pfront = new Path();
        pfront.moveTo(x_center-rectWidth/2, y_center-rectHeight/2);
        pfront.arcTo((float)x_center-rectWidth/2, (float)y_center-rectHeight/2,
                (float)x_center, (float)y_center-rectHeight/2 - 20,
                (float)x_center+rectWidth/2, (float)y_center-rectHeight/2,
                false);
        pfront.close();
        canvas.drawPath(pfront, paint);

        Path pback = new Path();
        pback.moveTo(x_center-rectWidth/2, y_center+rectHeight/2);
        arcTo(pback,x_center-rectWidth/2, y_center+rectHeight/2, x_center, y_center+rectHeight/2 + 20, x_center+rectWidth/2, y_center+rectHeight/2);
        pback.close();
        canvas.drawPath(pback, paint);

        canvas.drawLine(x_center-rectWidth/2, y_center-rectHeight/2, x_center-rectWidth/2, y_center+rectHeight/2, paint);
        canvas.drawLine(x_center+rectWidth/2, y_center-rectHeight/2, x_center+rectWidth/2, y_center+rectHeight/2, paint);

        // Draw outline on the left side of based on (x,y) of a sensor and its distance feedback

        canvas.drawArc(x_center-rectWidth/2-40, y_center-rectHeight/2-40+10, 40*2,
                40*2, 173, 15, false, paint);
        int x_arc = x_center-rectWidth/2;
        int y_arc1 = y_center-rectHeight/2+10;
        int y_arc2 = y_center-rectHeight/2+60;
        int y_arc3 = y_center-rectHeight/2+120;
        int dist1 = 40;
        int dist2 = 30;
        int dist3 = 50;
        int x_1 = (int)(x_arc- dist1*(Math.sin(8)));
        int y_1_low = (int)(y_arc1 - dist1*(Math.cos(8)));
        int y_1_high = (int)(y_arc1 + dist1*(Math.cos(8)));

        int x_2 = (int)(x_arc - dist2*(Math.sin(8)));
        int y_2_high = (int)(y_arc2 + dist2*(Math.cos(8)));
        int y_2_low = (int)(y_arc2 - dist2*(Math.cos(8)));


        int x_3 = (int)(x_arc - dist3*(Math.sin(8)));
        int y_3_high = (int)(y_arc3 + dist3*(Math.cos(8)));
        int y_3_low = (int)(y_arc3 - dist3*(Math.cos(8)));

        canvas.drawLine(x_1, y_1_low, x_2, y_2_high, paint);
        canvas.drawLine(x_2, y_2_low, x_3, y_3_high, paint);
        canvas.drawArc(x_center-rectWidth/2-30, y_center-rectHeight/2-30+60, 30*2,
                30*2, 173, 15, false, paint);
        canvas.drawArc(x_center-rectWidth/2-50, y_center-rectHeight/2-50+120, 50*2,
                50*2, 173, 15,false, paint);
        // for(int i = 0; i < left_sensors.size(); i++){
        //     g2.drawArc(left_sensors.get(0)+dist, left_sensors);
        // }

    }

}