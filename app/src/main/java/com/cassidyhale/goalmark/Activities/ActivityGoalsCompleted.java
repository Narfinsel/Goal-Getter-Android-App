package com.cassidyhale.goalmark.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.cassidyhale.goalmark.Adapter.AdapterGoals;
import com.cassidyhale.goalmark.Model.DataList;
import com.cassidyhale.goalmark.R;

public class ActivityGoalsCompleted extends AppCompatActivity {

    private RecyclerView mRecyclerViewGoalsCompleted;
    private RecyclerView.Adapter mAdapterRecGoals;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_goals_completed);

        this.initializeViews();
        this.setupAdapterDataList();
    }

    private void initializeViews () {

        mRecyclerViewGoalsCompleted = (RecyclerView) findViewById(R.id.recyclerViewGoals);
        mRecyclerViewGoalsCompleted.setHasFixedSize(true);
        mRecyclerViewGoalsCompleted.setLayoutManager(new LinearLayoutManager(this));

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbarGoals);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle (R.string.string_Toolbar_Title_Completed);
    }

    private void setupAdapterDataList ()   {
        if (( getApplicationContext()) != null) {
            mAdapterRecGoals = new AdapterGoals(this, ((DataList) getApplicationContext()).getListCompleted());
            mRecyclerViewGoalsCompleted.setAdapter(mAdapterRecGoals);
        }
    }
}
