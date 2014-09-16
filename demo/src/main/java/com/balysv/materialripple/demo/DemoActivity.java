package com.balysv.materialripple.demo;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;


public class DemoActivity extends ActionBarActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo);
        findViewById(R.id.ripple_layout_1).setOnClickListener(this);
        findViewById(R.id.ripple_layout_2).setOnClickListener(this);
        findViewById(R.id.ripple_layout_3).setOnClickListener(this);
    }

    @Override public void onClick(View v) {
    }
}
