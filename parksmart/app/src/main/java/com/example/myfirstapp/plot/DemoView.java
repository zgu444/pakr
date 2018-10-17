package com.example.myfirstapp.plot;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class DemoView extends View {
    public DemoView(Context context){
        super(context);
    }
    public DemoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public DemoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // custom drawing code here
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);

        // make the entire canvas white
        paint.setColor(Color.WHITE);
        canvas.drawPaint(paint);

        // draw blue circle with anti aliasing turned off
        paint.setAntiAlias(false);
        paint.setColor(Color.BLUE);
        canvas.drawCircle(20, 620, 15, paint);

        // draw green circle with anti aliasing turned on
        paint.setAntiAlias(true);
        paint.setColor(Color.GREEN);
        canvas.drawCircle(60, 320, 15, paint);

        // draw red rectangle with anti aliasing turned off
        paint.setAntiAlias(false);
        paint.setColor(Color.RED);
        canvas.drawRect(100, 105, 200, 930, paint);

//        // draw the rotated text
//        canvas.rotate(-45);
//
//        paint.setStyle(Paint.Style.FILL);
//        canvas.drawText("Graphics Rotation", 40, 180, paint);
//
//        //undo the rotate
//        canvas.restore();
    }
}
