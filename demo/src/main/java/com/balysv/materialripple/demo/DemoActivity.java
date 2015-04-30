package com.balysv.materialripple.demo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;


public class DemoActivity extends ActionBarActivity implements View.OnClickListener, View.OnLongClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo);

        // xml initialization
        findViewById(R.id.ripple_layout_1).setOnClickListener(this);
        findViewById(R.id.ripple_layout_1).setOnLongClickListener(this);

        // static initialization
        View view = findViewById(R.id.ripple_layout_2);
        MaterialRippleLayout.on(view)
            .rippleColor(Color.parseColor("#FF0000"))
            .rippleAlpha(0.2f)
            .rippleHover(true)
            .create();

        view.setOnLongClickListener(this);
        view.setOnClickListener(this);
    }

    @Override public void onClick(View v) {
        Toast.makeText(this, "Short click", Toast.LENGTH_SHORT).show();
    }

    @Override public boolean onLongClick(View v) {
        if (v.getId() == R.id.ripple_layout_1) {
            Toast.makeText(this, "Long click not consumed", Toast.LENGTH_SHORT).show();
            return false;
        }
        Toast.makeText(this, "Long click and consumed", Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_button, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.switch_list) {
            startActivity(new Intent(this, DemoListActivity.class));
            finish();
            return true;
        } else if (id == R.id.switch_recycler) {
            startActivity(new Intent(this, DemoRecyclerActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
