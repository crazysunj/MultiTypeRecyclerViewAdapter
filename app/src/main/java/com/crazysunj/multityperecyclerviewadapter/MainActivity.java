package com.crazysunj.multityperecyclerviewadapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void click1(View view) {
        startActivity(new Intent(this, LinearActivity.class));
    }

    public void click2(View view) {
        startActivity(new Intent(this, CommonLinearActivity.class));
    }


    public void click3(View view) {
        startActivity(new Intent(this, GridActivity.class));
    }

    public void click4(View view) {
        startActivity(new Intent(this, CommonGridActivity.class));
    }
}
