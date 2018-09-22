package com.main.picassodemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private static final String IMG_URL = "http://i.imgur.com/HFEFssS.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadPicassoDemo();
    }

    private void loadPicassoDemo() {
        ImageView im = (ImageView) findViewById(R.id.test_image1);
        Picasso.with(this)
                .load(IMG_URL)
                .into(im);
    }


}
