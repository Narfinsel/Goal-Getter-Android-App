package com.cassidyhale.goalmark.Activities;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.cassidyhale.goalmark.R;

public class ActivityWalkthrough extends AppCompatActivity {

    private FloatingActionButton mFloatingActionButtonBackWalk1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_walkthrough);

        this.initializeViews();
    }

    private void initializeViews () {
        mFloatingActionButtonBackWalk1 = (FloatingActionButton) findViewById(R.id.floatingActButtBackWalk1);
        mFloatingActionButtonBackWalk1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
