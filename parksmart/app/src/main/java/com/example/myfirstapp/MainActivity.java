package com.example.myfirstapp;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.RectF;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;

import org.easydarwin.video.EasyPlayerClient;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myfirstapp.algo.ParkingAlgo;
import com.example.myfirstapp.plot.CarPlotDemo;
import com.example.myfirstapp.plot.ReplotAsyncTask;
import com.example.myfirstapp.sensors.RPISensorAdaptor;

public class MainActivity extends AppCompatActivity {
    public static final String RTSP_ADDR = "rtsp://PARKRPI.WV.CC.CMU.EDU:8554/test";
    public static final String KEY = "79393674363536526D3430416E5A316270474A6970655A76636D63755A57467A65575268636E64706269356C59584E356347786865575679567778576F502B6C3430566863336C4559584A33615735555A57467453584E55614756435A584E30514449774D54686C59584E35";
    private Boolean playing = false;
    private Boolean URIinit = false;
    private Boolean filled = false;
    private RPISensorAdaptor my_rpi;
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button playButton = findViewById(R.id.playButton);
        final Button fillButton = findViewById(R.id.fillButton);
        final TextView textV = findViewById(R.id.playStatus);
        final CarPlotDemo carPlot = findViewById(R.id.carPlot);
        final TextureView textureView = findViewById(R.id.textureVideoMain);
        textureView.setOpaque(false);
        final EasyPlayerClient client = new EasyPlayerClient(this, KEY, textureView, null, null);


        playButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (playing){
                    client.stop();
                    playing = false;
                    textV.setText("Not Playing");
                    playButton.setText("Play");
                }
                else{
                    client.play(RTSP_ADDR);
                    playing = true;
                    textV.setText("Playing");
                    playButton.setText("Pause");
                }
            }
        });

        fillButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (filled) {
                    filled = false;
                    fillButton.setText("Fill");
                    carPlot.filled = false;
                    carPlot.invalidate();
                }
                else{
                    carPlot.filled = true;
                    filled = true;
                    fillButton.setText("Contour");
                    carPlot.invalidate();

                }
            }
        });

        View carPath = findViewById(R.id.videoOverlay);
        ParkingAlgo myAlgo = new ParkingAlgo();

        my_rpi = RPISensorAdaptor.get_rpiadaptor();
        my_rpi.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        ReplotAsyncTask replotAsync = new ReplotAsyncTask();
        replotAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, carPlot, carPath);

        myAlgo.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


    }

}