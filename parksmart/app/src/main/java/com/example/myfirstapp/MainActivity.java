package com.example.myfirstapp;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.RectF;
import android.net.Uri;
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
import com.example.myfirstapp.sensors.RPISensorAdaptor;

public class MainActivity extends AppCompatActivity {
    public static final String RTSP_ADDR = "rtsp://PARKRPI.WV.CC.CMU.EDU:8554/test";
    public static final String KEY = "79393674363536526D3430416E5A316270474A6970655A76636D63755A57467A65575268636E64706269356C59584E356347786865575679567778576F502B6C3430566863336C4559584A33615735555A57467453584E55614756435A584E30514449774D54686C59584E35";
    private Boolean playing = false;
    private Boolean URIinit = false;
    private RPISensorAdaptor my_rpi;
    @SuppressLint("NewApi")
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

//        final VideoView videoView = findViewById(R.id.videoViewMain);
//        videoView.setVideoPath(RTSP_ADDR);

//        RequestQueue q = Volley.newRequestQueue(this);
//        StringRequest sr = new StringRequest(Request.Method.GET, "http://www.google.com",
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String r) {
//                        Log.d("rtsp string", r);
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.d("rtsp string", error.toString());
//                    }
//                });
//        q.add(sr);
//        Log.d("rtsp string", "added");

        final TextureView textureView = findViewById(R.id.textureVideoMain);
        textureView.setOpaque(false);
//        textureView.setBackgroundColor(Color.BLACK);
        final EasyPlayerClient client = new EasyPlayerClient(this, KEY, textureView, null, null);


        muteBv.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (playing){
                    client.stop();
                    playing = false;
                    textV.setText("Not Playing");
                }
                else{
                    client.play(RTSP_ADDR);
                    playing = true;
                    textV.setText("Playing");
                }
            }
        });

        View carPlot= findViewById(R.id.carPlot);

//        carPlot.setBackgroundDrawable();
        my_rpi = RPISensorAdaptor.get_rpiadaptor();
        my_rpi.execute(carPlot);

    }

}