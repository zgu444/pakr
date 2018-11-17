package com.example.myfirstapp.algo;

import android.widget.TextView;

import com.example.myfirstapp.R;

public class OverlayConsole {
    TextView tv;
    public OverlayConsole(TextView tview){
        tv = tview;
    }
    void log(String s){
        tv.append(s);
    }
    void clear_history(){
        tv.setText("");
    }
}
