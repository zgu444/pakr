package com.example.myfirstapp;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {
    public static final String RTSP_ADDR = "rtsp://mpv.cdn3.bigCDN.com:554/bigCDN/definst/mp4:bigbuckbunnyiphone_400.mp4";// "rtsp://PARKRPI.WV.CC.CMU.EDU:8554/test";
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
                        videoView.setVideoURI(Uri.parse(RTSP_ADDR));
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