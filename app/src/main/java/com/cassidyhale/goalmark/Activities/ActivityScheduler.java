package com.cassidyhale.goalmark.Activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.cassidyhale.goalmark.Adapter.PagerAdapterScheduler;
import com.cassidyhale.goalmark.Model.DataList;
import com.cassidyhale.goalmark.Model.Schedule;
import com.cassidyhale.goalmark.R;

import java.util.Calendar;


public class ActivityScheduler extends AppCompatActivity {

    String arrayDayInWeek[] = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

    private PagerAdapterScheduler mPagerAdapterScheduler;
    private ViewPager mViewPager;
    private Schedule schedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheduler);
        this.schedule = ((DataList) getApplicationContext()).getSchedule();
        this.setupSectionsPageAdapter();
    }

    private void setupSectionsPageAdapter () {
        mPagerAdapterScheduler = new PagerAdapterScheduler(ActivityScheduler.this,
                                                            getSupportFragmentManager(),
                                                            arrayDayInWeek  );
        mViewPager = findViewById(R.id.viewPagerScheduler);
        mViewPager.setAdapter(mPagerAdapterScheduler);
        mViewPager.setCurrentItem( this.computeIntForDayOfWeek() );
    }

    private int computeIntForDayOfWeek ()   {
        int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        switch ( day ){
            case Calendar.MONDAY:       return 0;
            case Calendar.TUESDAY:      return 1;
            case Calendar.WEDNESDAY:    return 2;
            case Calendar.THURSDAY:     return 3;
            case Calendar.FRIDAY:       return 4;
            case Calendar.SATURDAY:     return 5;
            case Calendar.SUNDAY:       return 6;
            default:                    return 0;
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

