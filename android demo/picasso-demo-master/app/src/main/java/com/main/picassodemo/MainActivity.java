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
        resizeDemo();
        rotateDemo();
    }

    private void loadPicassoDemo() {
        ImageView im = (ImageView) findViewById(R.id.test_image1);
        Picasso.with(this)
                .load(IMG_URL)
                .into(im);
    }

    private void resizeDemo() {
        ImageView im = (ImageView) findViewById(R.id.test_image2);
        Picasso.with(this)
                .load(IMG_URL)
                .resize(70, 70)
                .into(im);
    }

    private void rotateDemo() {
        ImageView im = (ImageView) findViewById(R.id.test_image3);
        Picasso.with(this)
                .load(IMG_URL)
                .rotate(180)
                .into(im);
    }

}
