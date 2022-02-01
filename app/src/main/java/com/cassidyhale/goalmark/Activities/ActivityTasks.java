package com.cassidyhale.goalmark.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.cassidyhale.goalmark.Adapter.AdapterTasks;
import com.cassidyhale.goalmark.Model.DataList;
import com.cassidyhale.goalmark.R;


public class ActivityTasks extends AppCompatActivity implements FragmentTaskAdd.NoticeDialogListener {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private int positionGoal;
    private int positionSubGoal;
    String activTaskLabel;
    int activTaskNumReps;

    private boolean isNewTaskAdded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_task);

        if ( getIntent() != null) {
            positionGoal    =  getIntent().getIntExtra("positionGoal", 0);
            positionSubGoal =  getIntent().getIntExtra("positionSubGoal", 0);
            //Toast.makeText( this, "Goal: " + positionGoal + "   Sub-Goal: " + positionSubGoal, Toast.LENGTH_LONG).show();
        }
        this.initialize_views();
        this.setupAdapterTasks();
    }

    @Override
    public void onBackPressed() {
        if (  this.isNewTaskAdded  ) {
            Intent intent = new Intent();
            setResult( __GLOBAL_Const.RESULT_IS_NEW_TASK_ADEDD_IN_SG, intent);
            Log.d( __GLOBAL_Const.TAG_handleUserfb, "            ACT-Task    :   onBackPressed = "+ __GLOBAL_Const.RESULT_IS_NEW_TASK_ADEDD_IN_SG );
            super.onBackPressed();
         }
         else
            super.onBackPressed();
    }                                           //  WORKS !!!

    private void initialize_views ()    {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewTasks);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbarTasks);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle (R.string.string_Toolbar_Title_Tasks);
    }

    private void setupAdapterTasks()   {
        if (((DataList) getApplicationContext()) != null) {
            mAdapter = new AdapterTasks(this, ((DataList) getApplicationContext()), positionGoal, positionSubGoal);
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu)  {
        getMenuInflater().inflate(R.menu.menu_edit_goal_add_task, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item)    {

        switch ( item.getItemId() ){
            case R.id.buttonToolbarAddTask:
                // Add New Task to selected sub-goal of the selected goal
                // Send the position of the current Goal and Subgoal - to the DialogFragment, where the task is created and added.
                DialogFragment newFragment = new FragmentTaskAdd();
                Bundle bundle = new Bundle();
                bundle.putInt("positionGoal", positionGoal);
                bundle.putInt("positionSubGoal", positionSubGoal);
                newFragment.setArguments(bundle);
                newFragment.show(getSupportFragmentManager(), "new_task");
                break;
            case R.id.buttonToolbarEditSubgoal:
                // Edit the Sub-Goal
                Intent intent = new Intent( this, ActivitySubGoalsEdit.class );
                intent.putExtra("positionGoal", positionGoal);
                intent.putExtra("positionSubGoal", positionSubGoal);
                startActivity( intent );
                break;
        }
        return super.onOptionsItemSelected(item);
    }



    // ------------------------ FRANGMENT DIALOG --------------------------------------
    @Override
    public void onClickAddNewTask (DialogFragment dialog) {
        this.setupAdapterTasks();
        this.isNewTaskAdded = true;
        ((DialogFragment) getSupportFragmentManager().findFragmentByTag("new_task")).dismiss();
    }

    @Override
    public void onClickCancelAddNewTask (DialogFragment dialog) {
        this.isNewTaskAdded = false;
        ((DialogFragment) getSupportFragmentManager().findFragmentByTag("new_task")).dismiss();
    }
}
