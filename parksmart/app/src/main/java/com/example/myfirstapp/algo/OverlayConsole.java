package com.example.myfirstapp.algo;

import android.widget.EditText;
import android.widget.TextView;

import com.example.myfirstapp.R;

public class OverlayConsole {
    EditText tv;
    public OverlayConsole(EditText tview){
        tv = tview;
        tview.setFocusable(false);
    }
    void log(String s){
        tv.append(s);
        tv.append("\n");
    }
    void clear_history(){
        tv.setText("");
    }
}
