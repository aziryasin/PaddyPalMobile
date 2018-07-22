package com.paddypal.azir.paddypal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
final static int TIMEOUT=1500;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ImageView splash=findViewById(R.id.splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                splash.setImageResource(R.drawable.paddy_text);
            }
        },TIMEOUT);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
                finish();
            }
        },TIMEOUT);
    }
}
