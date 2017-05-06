package com.crazysunj.multityperecyclerviewadapter;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.crazysunj.multitypeadapter.helper.RecyclerViewAdapterHelper;

public class HighLightActivity extends AppCompatActivity {

    private EditText et_hl;
    private EditText et_hl_kw;
    private TextView tv_hl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_light);
        setTitle("高亮");
        et_hl = (EditText) findViewById(R.id.et_hl);
        et_hl_kw = (EditText) findViewById(R.id.et_hl_kw);
        tv_hl = (TextView) findViewById(R.id.tv_hl);
    }

    public void handle(View view) {
        String hl = et_hl.getText().toString().trim();
        String hl_kw = et_hl_kw.getText().toString().trim();
        tv_hl.setText(RecyclerViewAdapterHelper.handleKeyWordHighLight(hl, hl_kw, Color.RED));
    }
}
