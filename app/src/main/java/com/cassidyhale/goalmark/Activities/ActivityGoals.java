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

public class ActivityGoals extends AppCompatActivity {

    private FloatingActionButton floatingActionCompletedGoals;
    private RecyclerView mRecyclerViewGoals;
    private RecyclerView.Adapter mAdapterRecGoals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_goals);

        this.initializeViews();
        this.setupAdapterDataList();

    }

    @Override
    protected void onResume() {
        super.onResume();
        this.mRecyclerViewGoals.getAdapter().notifyDataSetChanged();
    }

    private void initializeViews () {

        mRecyclerViewGoals = (RecyclerView) findViewById(R.id.recyclerViewGoals);
        mRecyclerViewGoals.setHasFixedSize(true);
        mRecyclerViewGoals.setLayoutManager(new LinearLayoutManager(this));

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbarGoals);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle (R.string.string_Toolbar_Title_Goals);

        floatingActionCompletedGoals = (FloatingActionButton) findViewById(R.id.floatingActButtBackMyGoals1);
        floatingActionCompletedGoals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentGoToCompleted = new Intent (ActivityGoals.this, ActivityGoalsCompleted.class);
                startActivity( intentGoToCompleted );
            }
        });
    }

    private void setupAdapterDataList ()   {
        if (( getApplicationContext()) != null) {
            mAdapterRecGoals = new AdapterGoals(this, ((DataList) getApplicationContext()).getListOngoing());
            mRecyclerViewGoals.setAdapter(mAdapterRecGoals);
        }
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu)  {
        getMenuInflater().inflate(R.menu.menu_add_goal, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item)    {
        switch ( item.getItemId() ){
            case R.id.buttonAddGoal:
                    Intent intentToAddGoalScreen = new Intent (ActivityGoals.this, ActivityGoalAdd.class);
                    startActivity( intentToAddGoalScreen );
                    break;
        }
        return super.onOptionsItemSelected(item);
    }


}
