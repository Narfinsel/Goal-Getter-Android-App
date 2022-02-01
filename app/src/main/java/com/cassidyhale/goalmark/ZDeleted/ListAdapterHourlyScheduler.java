package com.cassidyhale.goalmark.ZDeleted;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cassidyhale.goalmark.Activities.ActivityScheduler;
import com.cassidyhale.goalmark.Model.DataList;
import com.cassidyhale.goalmark.Model.ItemScheduled;
import com.cassidyhale.goalmark.Model.Schedule;
import com.cassidyhale.goalmark.Model.Task;
import com.cassidyhale.goalmark.R;


public class ListAdapterHourlyScheduler extends BaseAdapter {
    private Context mContext;
    private int startingHour;
    private int totalHours;
    private int incrementHours;
    private int pageNumber;
    private Schedule mSchedule;

    public ListAdapterHourlyScheduler(Context context, int startingHour, int totalHours, int incrementHours, int pageNumber) {
        this.mContext = context;
        this.startingHour = startingHour;
        this.totalHours = totalHours;
        this.incrementHours = incrementHours;
        this.pageNumber = pageNumber;
        this.mSchedule = ((DataList) ((ActivityScheduler) mContext).getApplicationContext()).getSchedule();
    }

    @Override
    public int getCount() {
        int count = this.mSchedule.getArrayHourSize();
        return count;
    }

    @Override
    public Object getItem(int position) {
        //Log.d("Userx", "Page Number: " + this.pageNumber + "        Position: " + position);
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

        TextView textViewForHour = (TextView) v.findViewById(R.id.textViewScheduledHour);
        int hour = this.startingHour + position * this.incrementHours;
        textViewForHour.setText( hour + ":00" );

        TextView textViewTaskScheduled = (TextView) v.findViewById(R.id.textViewScheduledTask);
        //ItemScheduled item = ((ActivityScheduler) this.mContext).getSchedule().getScheduledItemForDayAndHour( pageNumber, position);
        ItemScheduled itemScheduled = this.mSchedule.getScheduledItemForDayAndHour( this.pageNumber, position);
        Task task  = this.taskFromItem( itemScheduled );
        if ( task != null)
            textViewTaskScheduled.setText( task.getTaskLabel() );
        else
            textViewTaskScheduled.setText( "    Free Time   " );

        return v;
    }

    public void updateViewListViewWithSelectedItemSchedule ()   {
        this.notifyDataSetChanged();
//        View v = getView(position, null, null);
//        TextView tvLabel = v.findViewById( R.id.textViewScheduledTask );
//        ItemScheduled item = ((ActivityScheduler) mContext).getSchedule().getScheduledItemForDayAndHour( this.pageNumber, position);
//
//        tvLabel.setText( this.taskFromItem( item ).getTaskLabel() );
    }

    public int getPageNumber() {
        return this.pageNumber;
    }

    private Task taskFromItem (ItemScheduled itemScheduled) {
        Task task = null;
        if (itemScheduled != null) {
            int g = itemScheduled.getLocusGoal();
            int s = itemScheduled.getLocusSubGoal();
            int t = itemScheduled.getLocusTask();

            if (g > 0 && s > 0 && t > 0) {
                task = ((DataList) mContext.getApplicationContext()).getListOngoing().get(g)
                        .getgListSubGoals().get(s)
                        .getsListTasks().get(t);
            }
        }
        return task;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }
}
