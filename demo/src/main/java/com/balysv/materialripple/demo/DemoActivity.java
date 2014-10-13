package com.balysv.materialripple.demo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.balysv.materialripple.MaterialRippleLayout;


public class DemoActivity extends ActionBarActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo);
        findViewById(R.id.ripple_layout_1).setOnClickListener(this);
        findViewById(R.id.ripple_layout_3).setOnClickListener(this);

        // static initialization
        View view = findViewById(R.id.ripple_layout_2);
        MaterialRippleLayout.on(view)
            .rippleColor(0xAA0044)
            .rippleAlpha(0.9f)
            .rippleHover(false)
            .create();

        MaterialRippleLayout.on(view)
                .rippleColor(Color.BLUE)
                .rippleAlpha(0.7f)
                .rippleHover(true)
                .create();

        view.setOnClickListener(this);
    }

    @Override public void onClick(View v) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.switch_list) {
            startActivity(new Intent(this, DemoListActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
