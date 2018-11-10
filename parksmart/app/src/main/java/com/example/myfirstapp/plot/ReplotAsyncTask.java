package com.example.myfirstapp.plot;

import android.os.AsyncTask;
import android.view.View;

public class ReplotAsyncTask extends AsyncTask<View, Void, Void> {
    @Override
    protected Void doInBackground(View... views) {
        while (!isCancelled()) {
            try {
                Thread.sleep(CarConstants.REPLOT_SLEEP_TIME);
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
