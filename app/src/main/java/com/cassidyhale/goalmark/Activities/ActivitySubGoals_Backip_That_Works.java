package com.cassidyhale.goalmark.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cassidyhale.goalmark.Adapter.AdapterSubGoals;
import com.cassidyhale.goalmark.Adapter.PagerAdapterScheduler;
import com.cassidyhale.goalmark.Model.DataList;
import com.cassidyhale.goalmark.R;

public class ActivitySubGoals_Backip_That_Works extends AppCompatActivity {

    private FloatingActionButton floatingActionButtonBackSubGoals;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private int positionGoal;

    private PagerAdapterScheduler mPagerAdapterSubgoals;
    private ViewPager mViewPager;
    //private int[] layout = {R.layout.activity_list_subgoal, R.layout.layout_page_subgoal_timeline};
    private LinearLayout mDotsLayout;
    private ImageView[] mDotsArrayImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.z_activity_subgoal);
        setContentView(R.layout.activity_list_subgoal);

        if ( getIntent() != null) {
            positionGoal =  getIntent().getIntExtra("positionGoal", 0);
            Toast.makeText( this, "Position " + positionGoal, Toast.LENGTH_LONG).show();
        }
        initialize_views();
        setupAdapterSubGoals();
        //this.setupSectionsPageAdapter();
    }

    private void initialize_views ()    {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewSubGoalsID);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbarSubGoals);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle (R.string.string_Toolbar_Title_SubGoal);

        floatingActionButtonBackSubGoals = (FloatingActionButton) findViewById(R.id.floatingActButtBackMySubGoals);
        floatingActionButtonBackSubGoals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

//    private void createDots (int current_position)  {
//        if (mDotsLayout != null)   {
//            mDotsLayout.removeAllViews();
//        }
//        mDotsArrayImageView = new ImageView[layout.length];
//        for (int i = 0; i< mDotsArrayImageView.length; i++){
//            mDotsArrayImageView[i] = new ImageView(this);
//            if (i == current_position )
//                mDotsArrayImageView[i].setImageResource (R.drawable.dots_active);
//            else
//                mDotsArrayImageView[i].setImageResource (R.drawable.dots_inactive);
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams (
//                                                ViewGroup.LayoutParams.WRAP_CONTENT,
//                                                ViewGroup.LayoutParams.WRAP_CONTENT);
//            params.setMargins(4,0,4,0);
//            mDotsLayout.addView(mDotsArrayImageView[i], params);
//        }
//    }

    private void setupAdapterSubGoals ()   {
        if (((DataList) getApplicationContext()) != null) {
            //Goal modifiableGoal = (Goal) ((DataList) getApplicationContext()).getListOngoing().get(positionGoal);
            mAdapter = new AdapterSubGoals(this, ((DataList) getApplicationContext()), positionGoal);
            mRecyclerView.setAdapter(mAdapter);
        }
    }

//    private void setupSectionsPageAdapter () {
//        mPagerAdapterSubgoals = new PagerAdapterScheduler (layout, this);
////        mViewPager = (ViewPager) findViewById(R.id.viewPagerSubgoals);
//        mViewPager.setAdapter(mPagerAdapterSubgoals);
//
////        mDotsLayout = (LinearLayout) findViewById(R.id.linearLayoutDotsSubGoals);
//        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//
//            @Override
//            public void onPageScrolled(int i, float v, int i1) {            }
//
//            @Override
//            public void onPageSelected(int i) {
//                createDots(i);
//                if (i == 0) {
//                    initialize_views();
//                    setupAdapterSubGoals();
//                }
//                if (i == 1) {
//                    initialize_views();
//                    setupAdapterSubGoals();
//                }
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int i) {            }
//        });
//    }




    // ------------------------ FRANGMENT PAGE VIEWER ADAPTER --------------------------------------






}
