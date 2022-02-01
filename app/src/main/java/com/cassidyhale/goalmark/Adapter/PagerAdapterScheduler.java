package com.cassidyhale.goalmark.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ListView;

import com.cassidyhale.goalmark.Activities.ActivityScheduler;
import com.cassidyhale.goalmark.Activities.FragmentDay;
import com.cassidyhale.goalmark.Activities.__GLOBAL_Const;
import com.cassidyhale.goalmark.Model.DataList;
import com.cassidyhale.goalmark.Model.DataListNoApp;
import com.cassidyhale.goalmark.Model.ItemScheduled;
import com.cassidyhale.goalmark.Model.Task;
import com.cassidyhale.goalmark.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;

public class PagerAdapterScheduler extends FragmentStatePagerAdapter {         // implements FragmentSchedulerImportTask.NoticeDialogListener

    private String arrayDayInWeek[];

    private int layout;
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private FragmentManager fragmentManager;
    private ArrayList<Fragment> listFragments;
    private ListView listViewDayScheduler;
    private ListAdapterHourly listAdapterHourly;
    int staringHour = 6;
    int numHours = 16;
    int incrementHours = 2;

    private int selected_Day_Of_Week;
    private int selected_List_Hour_Position;
    private int currentWeek;

    public PagerAdapterScheduler (Context context, FragmentManager fm, String[] arrayDayInWeek) {
        super(fm);
        this.mContext = context;
        this.fragmentManager = fm;
        this.arrayDayInWeek = arrayDayInWeek;
        if( areWeInANewWeek() == true)
            refresh_schedule();
        this.establish_week_of_year();
        //this.setCurrentWeek( Calendar.getInstance().get(Calendar.WEEK_OF_YEAR));
        //this.save_Today_Week_to_SharedPrefs( this.currentWeek);
        //Log.d(__GLOBAL_Const.TAG_handleUserfb, "PAGER_ADAPTER_SCHEDULER   :   current week      = " + this.currentWeek);
        //Log.d(__GLOBAL_Const.TAG_handleUserfb, "PAGER_ADAPTER_SCHEDULER   :   read_week_prefs   = " + this.read_Today_Week_from_SharedPrefs());
    }

    @Override
    public Fragment getItem(int i) {
        return FragmentDay.newInstance(i, arrayDayInWeek[i]);
    }

    @Override
    public int getCount() {
        return this.arrayDayInWeek.length;
    }

    private void refresh_schedule ()    {
        ((DataList) mContext.getApplicationContext()).getSchedule().reset_scheduler_for_new_week();
    }

    private boolean areWeInANewWeek ()   {
        boolean isThisNewWeek;
        int weekCurrent = this.read_Today_Week_from_SharedPrefs();
        int weekNumberToday = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
        if ( weekNumberToday > weekCurrent )
            // STORED week is older then TODAY's week
            isThisNewWeek = true;
        else
            isThisNewWeek = false;
        return isThisNewWeek;
    }

    private int lastWeekNumber ()   {
        int weekNumberToday = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
        int weekSharedPrefs = this.read_Today_Week_from_SharedPrefs();
        //Log.d(__GLOBAL_Const.TAG_handleUserfb, "PAGER_ADAPTER_SCHEDULER   :   week today = " + weekNumberToday);

        if ( weekNumberToday > weekSharedPrefs ) {
            // TODAY week is newer week than the SHARED PREFS week
            return weekNumberToday;
        }
        else
            // TODAY week == or < SHARED PREFS week
            return weekSharedPrefs;
    }

    private void establish_week_of_year () {
        int weekNumberToday = lastWeekNumber();
        int weekSharedPrefs = this.read_Today_Week_from_SharedPrefs();

        if ( weekNumberToday > weekSharedPrefs ) {
            this.setCurrentWeek( weekNumberToday );
            this.save_Today_Week_to_SharedPrefs( weekNumberToday );
        }
        else    {
            this.setCurrentWeek( weekSharedPrefs );
            this.save_Today_Week_to_SharedPrefs( weekSharedPrefs );
        }
    }

    private void save_Today_Week_to_SharedPrefs (int week) {
        SharedPreferences sharedPreferences;
        SharedPreferences.Editor editor;
        sharedPreferences = mContext.getSharedPreferences( __GLOBAL_Const.PREFS_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        editor.putInt( __GLOBAL_Const.PREFS_KEY_CURRENT_WEEK, week);
        editor.apply();     // editor.commit();
    }

    private int read_Today_Week_from_SharedPrefs() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences( __GLOBAL_Const.PREFS_NAME, Context.MODE_PRIVATE);
        int myIntValue = -1;
        if ( sharedPreferences.contains( __GLOBAL_Const.PREFS_KEY_CURRENT_WEEK) ) {
            myIntValue = sharedPreferences.getInt( __GLOBAL_Const.PREFS_KEY_CURRENT_WEEK, -1);
        }
        return myIntValue;
    }

    // ------------------------ PUT AN ITEM IN SCHEDULE  --------------------------------------
    public void test_print_scheduler () {
        int length = ((ActivityScheduler) mContext).getSchedule().getArrayHourSize();
        String format = "%-20s %20s %20s %20s %20s %20s %20s %n";
        StringBuilder stringBuilder = new StringBuilder();

        //((ActivityScheduler) mContext).getSchedule().getArray_1_Mon()

        String string;
        for (int i=0; i< length; i++ ) {
            string = String.format(format,
                    this.test_print_if_tasks_exists(   ((ActivityScheduler) mContext).getSchedule().getArray_1_Mon(), i),
                    this.test_print_if_tasks_exists(   ((ActivityScheduler) mContext).getSchedule().getArray_2_Tue(), i),
                    this.test_print_if_tasks_exists(   ((ActivityScheduler) mContext).getSchedule().getArray_3_Wed(), i),
                    this.test_print_if_tasks_exists(   ((ActivityScheduler) mContext).getSchedule().getArray_4_Thur(), i),
                    this.test_print_if_tasks_exists(   ((ActivityScheduler) mContext).getSchedule().getArray_5_Fri(), i),
                    this.test_print_if_tasks_exists(   ((ActivityScheduler) mContext).getSchedule().getArray_6_Sat(), i),
                    this.test_print_if_tasks_exists(   ((ActivityScheduler) mContext).getSchedule().getArray_7_Sun(), i));
            stringBuilder.append(string);
        }
        Log.d("Userx", stringBuilder.toString());
    }

    private String test_print_if_tasks_exists (ArrayList<ItemScheduled> arrayList, int position) {
        String print;
        ItemScheduled itemScheduled = arrayList.get(position);
        if (itemScheduled.getLocusTask() < 9000) {
            Task task = this.taskFromItem(itemScheduled);
            if (task == null)
                print = " ------- ";
            else
                print = task.getTaskLabel().trim();
        }
        else
            print = " ------- ";
        return print;
    }

    private Task taskFromItem (ItemScheduled itemScheduled) {
        if (itemScheduled != null) {
            int g = itemScheduled.getLocusGoal();
            int s = itemScheduled.getLocusSubGoal();
            int t = itemScheduled.getLocusTask();
            Task task = ((DataList) mContext.getApplicationContext()).getListOngoing().get(g)
                    .getgListSubGoals().get(s)
                    .getsListTasks().get(t);

            return task;
        }
        else
            return null;
    }

    public FragmentManager getFragmentManager() { return fragmentManager;   }

    public int getCurrentWeek() { return currentWeek; }

    public void setCurrentWeek(int currentWeek) {   this.currentWeek = currentWeek; }

    public int getSelectedListPosition() { return selected_List_Hour_Position; }

    public void setSelectedListPosition(int selected_List_Position) { this.selected_List_Hour_Position = selected_List_Position; }

    public int getSelected_Day_Of_Week() {  return selected_Day_Of_Week;    }

    public void setSelected_Day_Of_Week(int selected_Day_Of_Week) { this.selected_Day_Of_Week = selected_Day_Of_Week; }


}
