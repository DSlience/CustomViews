package com.adealink.customviews.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.adealink.customviews.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goMagnifySeekBar(View view) {
        startActivity(new Intent(this, MagnifySeekBarActivity.class));
    }

    public void goViewFlipper(View view) {
        startActivity(new Intent(this, ViewFlipperActivity.class));
    }

}
