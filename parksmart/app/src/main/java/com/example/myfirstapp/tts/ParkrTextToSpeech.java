package com.example.myfirstapp.tts;

import android.content.Context;
import android.speech.tts.TextToSpeech;

public class ParkrTextToSpeech extends TextToSpeech {
    public ParkrTextToSpeech(Context context, OnInitListener listener) {
        super(context, listener);
    }
}
