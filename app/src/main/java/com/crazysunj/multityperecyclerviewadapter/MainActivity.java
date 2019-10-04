package com.crazysunj.multityperecyclerviewadapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.crazysunj.multityperecyclerviewadapter.switchtype.SwitchTypeActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void click1(View view) {
        startActivity(new Intent(this, LinearActivity.class));
    }

    public void click3(View view) {
        startActivity(new Intent(this, GridActivity.class));
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

    public void click14(View view) {
        startActivity(new Intent(this, OpenCloseActivity.class));
    }

    public void click16(View view) {
        startActivity(new Intent(this, SwitchTypeActivity.class));
    }

    public void click17(View view) {
        startActivity(new Intent(this, TestLevelActivity.class));
    }

    public void click18(View view) {
        startActivity(new Intent(this, Refresh240Activity.class));
    }
}
