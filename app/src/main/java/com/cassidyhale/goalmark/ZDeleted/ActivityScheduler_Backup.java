package com.cassidyhale.goalmark.ZDeleted;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cassidyhale.goalmark.Adapter.PagerAdapterScheduler;
import com.cassidyhale.goalmark.Model.DataList;
import com.cassidyhale.goalmark.Model.Schedule;
import com.cassidyhale.goalmark.R;


public class ActivityScheduler_Backup extends AppCompatActivity  {

    private int[] layout = {R.layout.activity_list_subgoal, R.layout.layout_page_subgoal_timeline};
    private LinearLayout mDotsLayout;
    private ImageView[] mDotsArrayImageView ;

    private PagerAdapterScheduler mPagerAdapterScheduler;
    private ViewPager mViewPager;

    private Schedule schedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheduler);
        this.schedule = ((DataList) getApplicationContext()).getSchedule();

        this.initializeViews();
        createDots(0);
        this.setupSectionsPageAdapter();

    }

    private void initializeViews()  {
        mDotsLayout = (LinearLayout) findViewById(R.id.linearLayoutDotsScheduler);
    }

    private void setupSectionsPageAdapter () {
//        mPagerAdapterScheduler = new PagerAdapterScheduler(R.layout.scheduler_template_day_of_week, ActivityScheduler_Backup.this);
        mViewPager = findViewById(R.id.viewPagerScheduler);
        mViewPager.setAdapter(mPagerAdapterScheduler);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {            }

            @Override
            public void onPageSelected(int i) {
                createDots(i);
                if (i == 0) {

                }
                if (i == 1) {

                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {            }
        });
    }

    private void createDots (int current_position)  {
        if (mDotsLayout != null)   {
            mDotsLayout.removeAllViews();
        }
        mDotsArrayImageView = new ImageView[7];
        for (int i = 0; i<= 6; i++){
            mDotsArrayImageView[i] = new ImageView(this);
            if (i == current_position )
                mDotsArrayImageView[i].setImageResource (R.drawable.dots_active);
            else
                mDotsArrayImageView[i].setImageResource (R.drawable.dots_inactive);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams (
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(4,0,4,0);
            mDotsLayout.addView(mDotsArrayImageView[i], params);
        }
    }

    public ViewPager getViewPager() {
        return mViewPager;
    }

    // ------------------------ MANAGE the SCHEDULE  ---------------------------------
    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }
}

//public class ActivityScheduler extends AppCompatActivity implements FragmentSchedulerImportTask.NoticeDialogListener {
//    @Override
//    public void onClickImportToSchedule (DialogFragment dialog, ItemScheduled itemScheduledFromFragment) {
////        this.setupAdapterTasks();
//        String str =    itemScheduledFromFragment.getLocusGoal() + " " +
//                        itemScheduledFromFragment.getLocusSubGoal() + " " +
//                        itemScheduledFromFragment.getLocusTask() + " ";
//        Toast.makeText(ActivityScheduler.this, str, Toast.LENGTH_LONG).show();
//        ((DialogFragment) getSupportFragmentManager().findFragmentByTag("new_frag_schedule")).dismiss();
//    }
//
//    @Override
//    public void onClickCancelImportToSchedule (DialogFragment dialog) {
//        ((DialogFragment) getSupportFragmentManager().findFragmentByTag("new_frag_schedule")).dismiss();
//    }
