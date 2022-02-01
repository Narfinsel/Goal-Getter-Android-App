package com.cassidyhale.goalmark.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cassidyhale.goalmark.Activities.ActivitySubGoals;
import com.cassidyhale.goalmark.Model.DataList;
import com.cassidyhale.goalmark.Model.Goal;
import com.cassidyhale.goalmark.Model.SubGoal;
import com.cassidyhale.goalmark.Model.Task;
import com.cassidyhale.goalmark.R;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

public class AdapterGoalsUnderTasks extends RecyclerView.Adapter <AdapterGoalsUnderTasks.ViewHolder> {

    private Context mContext;
    private DataList mDataList;
    private int positionGoal;

    public AdapterGoalsUnderTasks(Context context, DataList dataList, int positionGoal)    {
        this.mContext = context;
        this.mDataList = dataList;
        this.positionGoal = positionGoal;
//        Log.d( "userx", "-------------------------------------------------------");
//        Log.d( "userx", "       AdapterGoalsUnderTasks       :       CONSTRUCTOR, itemCount =  "+ this.getItemCount());
//        for(int i=0; i<this.getItemCount(); i++)
//            Log.d( "userx", "           Item ["+ i +"] = "+ this.mDataList.getListOngoing().get( this.positionGoal ).getgListTasks().get(i).getTaskLabel() );
//        Log.d( "userx", "-------------------------------------------------------");
    }
    public AdapterGoalsUnderTasks(Context context, Goal goal)    {
        this.mContext = context;
    }


    @NonNull
    @Override
    public AdapterGoalsUnderTasks.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //Log.d( "userx", "       AdapterGoalsUnderTasks.ViewHolder       :       onCreateViewHolder");
        View view = LayoutInflater.from( viewGroup.getContext() )
                .inflate( R.layout.template_layout_exp_list_subgoals_children_tasks, viewGroup, false );
        //template_layout_list_row_tasks
        return new ViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterGoalsUnderTasks.ViewHolder viewHolder, int i) {
        Task task = this.mDataList.getListOngoing().get( this.positionGoal ).getgListTasks().get(i);
        viewHolder.taskTitle.setText( task.getTaskLabel() );
        //viewHolder.taskImage.setBackgroundColor(423445);
        Log.d( "userx", "       AdapterGoalsUnderTasks.ViewHolder       :       TASK    :   "+ task.getTaskLabel());
    }

    @Override
    public int getItemCount() {
        return  this.mDataList.getListOngoing().get( this.positionGoal ).getgListTasks().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener {

        public TextView taskTitle;
        //public ImageView taskImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener( this );
            this.taskTitle = (TextView) itemView.findViewById(R.id.textViewChildTasksUnderSubgoal); //textViewTasksTitle
            //this.taskImage = (ImageView) itemView.findViewById(R.id.imageViewGoalImage);

            CircularProgressBar circularProgressBar = itemView.findViewById(R.id.circularProgressBar);
            itemView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            circularProgressBar.setColor(ContextCompat.getColor( mContext, R.color.colorAccent));
            circularProgressBar.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
            circularProgressBar.setProgress(31);
        }

        @Override
        public void onClick(View v) { }
    }
}
