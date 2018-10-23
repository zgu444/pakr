package com.example.myfirstapp;

import android.graphics.RectF;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class MainActivity extends AppCompatActivity {
    public static final String RTSP_ADDR = "rtsp://PARKRPI.WV.CC.CMU.EDU:8554/test";
    private Boolean playing = false;
    private Boolean URIinit = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button muteButton = findViewById(R.id.mute);

        muteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Button muteBv = findViewById(R.id.mute);
        final TextView textV = findViewById(R.id.playStatus);
        final VideoView videoView = findViewById(R.id.videoViewMain);
//        videoView.setVideoPath(RTSP_ADDR);

        RequestQueue q = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.GET, "http://www.google.com",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String r) {
                        Log.d("rtsp string", r);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("rtsp string", error.toString());
                    }
                });
//        q.add(sr);
        Log.d("rtsp string", "added");

        muteBv.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (playing){
                    videoView.suspend();
                    playing = false;
                    textV.setText("Not Playing");
                }
                else{
                    if (true || !URIinit) {
                        videoView.setVideoPath((RTSP_ADDR));
//                        videoView.setVideoURI(Uri.parse(RTSP_ADDR));
                        videoView.requestFocus();
                        URIinit = true;
                    }
                    videoView.start();
                    playing = true;
                    textV.setText("Playing");
                }
            }
        });

//        View carPlot= findViewById(R.id.carPlot);

//        carPlot.setBackgroundDrawable();
    }

}