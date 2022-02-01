package com.cassidyhale.goalmark.Model;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.cassidyhale.goalmark.Activities.ActivitySubGoalAdd;
import com.cassidyhale.goalmark.Activities.__GLOBAL_Const;
import com.cassidyhale.goalmark.Adapter.AdapterSubGoals;
import com.cassidyhale.goalmark.R;

public class ActivitySubGoals_Backup extends AppCompatActivity {

    private FloatingActionButton floatingActionButtonBackSubGoals;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private int positionGoal;

//    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_subgoal);

        if ( getIntent() != null) {
            positionGoal =  getIntent().getIntExtra("positionGoal", 0);
            Toast.makeText( this, "Position " + positionGoal, Toast.LENGTH_LONG).show();
        }
        this.initialize_views();
        this.setupAdapterSubGoals();
        ((Goal) ((DataList) getApplicationContext()).getListOngoing().get(positionGoal)).displaySubgoalsToString();
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

    private void setupAdapterSubGoals ()   {
        if (((DataList) getApplicationContext()) != null) {
            Goal modifiableGoal = (Goal) ((DataList) getApplicationContext()).getListOngoing().get(positionGoal);
            mAdapter = new AdapterSubGoals(this, ((DataList) getApplicationContext()), positionGoal);
            mRecyclerView.setAdapter(mAdapter);
        }
        else    {
            Log.d(__GLOBAL_Const.TAG_handleUserfb, "SUB-GOAL_screen -- Data List is null");
        }
    }





    // ------------------------ FRANGMENT PAGE VIEWER ADAPTER --------------------------------------






    // ------------------------ PAGE VIEWER ADAPTER ------------------------------------------------

//    public static class PlaceholderFragment extends Fragment {
//        /**
//         * The fragment argument representing the section number for this
//         * fragment.
//         */
//        private static final String ARG_SECTION_NUMBER = "section_number";
//
//        public PlaceholderFragment() {
//        }
//
//        /**
//         * Returns a new instance of this fragment for the given section
//         * number.
//         */
//        public static PlaceholderFragment newInstance(int sectionNumber) {
//            PlaceholderFragment fragment = new PlaceholderFragment();
//            Bundle args = new Bundle();
//            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
//            fragment.setArguments(args);
//            return fragment;
//        }
//
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                                 Bundle savedInstanceState) {
//            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
//            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
//            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
//            return rootView;
//        }
//    }
//
//    /**
//     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
//     * one of the sections/tabs/pages.
//     */
//    public class SectionsPagerAdapter extends FragmentPagerAdapter {
//
//        public SectionsPagerAdapter(FragmentManager fm) {
//            super(fm);
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//            // getItem is called to instantiate the fragment for the given page.
//            // Return a PlaceholderFragment (defined as a static inner class below).
//            return PlaceholderFragment.newInstance(position + 1);
//        }
//
//        @Override
//        public int getCount() {
//            // Show 3 total pages.
//            return 3;
//        }
//    }


}
