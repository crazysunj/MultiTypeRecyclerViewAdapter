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

    public void click5(View view) {
        startActivity(new Intent(this, HighLightActivity.class));
    }

    public void click6(View view) {
        startActivity(new Intent(this, RxLinearActivity.class));
    }

    public void click7(View view) {
        startActivity(new Intent(this, RxErrorAndEmptyLinearActivity.class));
    }

    public void click8(View view) {

        startActivity(new Intent(this, RxContinuityActivity.class));
    }

    public void click9(View view) {
        startActivity(new Intent(this, RxStandardLinearActivity.class));
    }

    public void click10(View view) {
        startActivity(new Intent(this, RxMixedLinearActivity.class));
    }

    public void click11(View view) {
        startActivity(new Intent(this, RxAptDefaultActivity.class));
    }

    public void click12(View view) {
        startActivity(new Intent(this, RxAptStandardLinearActivity.class));
    }

    public void click13(View view) {
        startActivity(new Intent(this, RxAptErrorAndEmptyLinearActivity.class));
    }
}
