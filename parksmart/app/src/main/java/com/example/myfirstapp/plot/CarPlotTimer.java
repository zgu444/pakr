package com.example.myfirstapp.plot;

import android.os.AsyncTask;
import android.view.View;


public class CarPlotTimer extends AsyncTask<View, Void, Void> {

    @Override
    protected Void doInBackground(View... views) {
        while(!this.isCancelled()){
            try {
                Thread.sleep(20);

                for (View view : views) {
                    view.invalidate();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
