package com.cassidyhale.goalmark.Adapter;

import android.content.Context;
import android.os.Build;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cassidyhale.goalmark.Activities.ActivityScheduler;
import com.cassidyhale.goalmark.Activities.__GLOBAL_Const;
import com.cassidyhale.goalmark.Model.DataList;
import com.cassidyhale.goalmark.Model.ItemScheduled;
import com.cassidyhale.goalmark.Model.Schedule;
import com.cassidyhale.goalmark.Model.Task;
import com.cassidyhale.goalmark.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.cassidyhale.goalmark.Activities.__GLOBAL_Const.child__Datalist__;
import static com.cassidyhale.goalmark.Activities.__GLOBAL_Const.defSubgoalLocus;


public class ListAdapterHourly extends BaseAdapter {
    private Context mContext;
    private Date dateScheduleDay;
    private int startingHour;
    private int totalHours;
    private int incrementHours;
    private int pageNumber;
    private Schedule mSchedule;

    public ListAdapterHourly(Context context, Date dateScheduledDay, int startingHour, int totalHours, int incrementHours, int pageNumber) {
        this.mContext = context;
        this.dateScheduleDay = dateScheduledDay;
        this.startingHour = startingHour;
        this.totalHours = totalHours;
        this.incrementHours = incrementHours;
        this.pageNumber = pageNumber;
        this.mSchedule = ((DataList)  mContext.getApplicationContext()).getSchedule();
        //((DataList)  mContext.getApplicationContext()).displayScheduleForDay(pageNumber);
    }

    @Override
    public int getCount() {
        int count = this.mSchedule.getArrayHourSize();
        return count;
    }

    @Override
    public Object getItem(int position) {
        ItemScheduled itemScheduled = this.mSchedule.getScheduledItemForDayAndHour( this.pageNumber, position );
        return itemScheduled;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = View.inflate(mContext, R.layout.scheduler_layout_for_one_hour, null);
        TextView textViewForHour = v.findViewById(R.id.textViewScheduledHour);
        TextView textViewTaskScheduled = v.findViewById(R.id.textViewScheduledTask);

        // CALCULATE HOUR
        int hour = this.startingHour + position * this.incrementHours;


        // SET HOUR LABEL
        ItemScheduled itemScheduled = ((ActivityScheduler) this.mContext).getSchedule().getScheduledItemForDayAndHour( pageNumber, position);
        String str;
        if ( itemScheduled.getDateScheduled() != null)
            str = new SimpleDateFormat("HH:mm").format( itemScheduled.getDateScheduled() );
        else {
            str = hour + ":00";
            if (hour < 10)
                str = "0" + str;
        }
        textViewForHour.setText( str );


        // TASK from TASK-LIST or SUB-goal-LIST
        Task task;
        if ( itemScheduled.getLocusSubGoal() == defSubgoalLocus )
            task = ((DataList) mContext.getApplicationContext())
                    .getListOngoing().get( itemScheduled.getLocusGoal() )
                    .getgListTasks().get( itemScheduled.getLocusTask() );
        else
            task  = this.taskFromItem( itemScheduled );


        // SET A DATE FOR current row
        Calendar calendar_row = Calendar.getInstance();
        calendar_row.setTime( this.dateScheduleDay );
        calendar_row.set( Calendar.HOUR_OF_DAY, hour);


        // SET TASK TITLE LABEL
        if ( task == null && calendar_row.getTime().before( Calendar.getInstance().getTime()) )    {
            textViewTaskScheduled.setText("SKIPPED");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                textViewTaskScheduled.setTextColor  ( mContext.getResources().getColor( R.color.colorLightGray, mContext.getTheme()) );
            } else {
                textViewTaskScheduled.setTextColor  ( mContext.getResources().getColor( R.color.colorLightGray )  );
            }
        }
        else if (task == null) {
            textViewTaskScheduled.setText("Free Time");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                textViewTaskScheduled.setTextColor  ( mContext.getResources().getColor( R.color.colorGrayVeryLight, mContext.getTheme()) );
            } else {
                textViewTaskScheduled.setTextColor  ( mContext.getResources().getColor( R.color.colorGrayVeryLight )  );
            }
        }
        else if ( task != null) {
            int goalPosition = itemScheduled.getLocusGoal();
            int color = ((DataList) mContext.getApplicationContext()).getListOngoing().get( goalPosition ).getGoalColor();
            textViewTaskScheduled.setTextColor( mContext.getResources().getColorStateList( color) );
            textViewTaskScheduled.setText(task.getTaskLabel());
            //Log.d (__GLOBAL_Const.TAG_handleUserfb, "       LIST-ADAPTER-HOURLY   -     getView( [" + this.pageNumber + "][" + position + "] )   :      " + this.taskFromItem( itemScheduled ).getTaskLabel());
        }

        // RETURN the VIEW
        return v;
    }

    private Task taskFromItem (ItemScheduled itemScheduled) {
        Task task = null;
        if (itemScheduled != null) {
            int g = itemScheduled.getLocusGoal();
            int s = itemScheduled.getLocusSubGoal();
            int t = itemScheduled.getLocusTask();
            //Log.d (__GLOBAL_Const.TAG_handleUserfb, "       LIST-ADAPTER-HOURLY   -     TaskFromItem( G =[" + g + "]    S =[" + s + "]      T =[" + t + "]");
            if (g >= 0 && s >= 0 && t >= 0) {
                task = ((DataList) mContext.getApplicationContext())
                        .getListOngoing().get(g)
                        .getgListSubGoals().get(s)
                        .getsListTasks().get(t);
            }
        }
        return task;
    }

    public int getPageNumber() { return this.pageNumber; }

    public void setPageNumber(int pageNumber) { this.pageNumber = pageNumber; }
}
