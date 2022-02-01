package com.cassidyhale.goalmark.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.cassidyhale.goalmark.R;

public class ActivityAddToSchedule extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scheduler_activity_layout_import_task);

        this.initializeViews();
    }

    private void initializeViews() {
    }
}
