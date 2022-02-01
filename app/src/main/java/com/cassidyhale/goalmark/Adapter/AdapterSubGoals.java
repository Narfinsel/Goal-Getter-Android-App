package com.cassidyhale.goalmark.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.cassidyhale.goalmark.Activities.ActivityTasks;
import com.cassidyhale.goalmark.Model.DataList;
import com.cassidyhale.goalmark.Model.Goal;
import com.cassidyhale.goalmark.Model.SubGoal;
import com.cassidyhale.goalmark.Model.Task;
import com.cassidyhale.goalmark.R;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.util.ArrayList;


public class AdapterSubGoals extends RecyclerView.Adapter <AdapterSubGoals.ViewHolder> {

    private Context mContext;
    private DataList mDataList;
    private Goal mGoal;
    private int positionGoal;

    public AdapterSubGoals (Context context, DataList dataList, int position)    {
        this.mContext = context;
        this.mDataList = dataList;
        this.positionGoal = position;
        this.mGoal = this.mDataList.getListOngoing().get(positionGoal);
    }

    @NonNull
    @Override
    public AdapterSubGoals.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from( viewGroup.getContext() )
                .inflate( R.layout.template_layout_list_row_subgoals, viewGroup, false );
        return new ViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterSubGoals.ViewHolder viewHolder, int i) {
        SubGoal subGoal = (SubGoal) mGoal.getgListSubGoals().get(i);
        viewHolder.position = i;
        viewHolder.title.setText( subGoal.getsLabel() );
        //viewHolder.linearLayoutListTaskUnderSg.addView();
        //populateLayout( viewHolder.linearLayoutListTaskUnderSg , i);
    }

    @Override
    public int getItemCount() {
        //Log.d(__GLOBAL_Const.TAG_handleUserfb, "ADAPTER-SUB-GOAL_screen:: count:: " + mGoal.getgListSubGoals().size() );
        return mGoal.getgListSubGoals().size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener {

        public TextView title;
        public LinearLayout linearLayoutListTaskUnderSg;
        public int position;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener( this );
            this.title                          = itemView.findViewById(R.id.textViewTasksUnderSubgoal);
            this.linearLayoutListTaskUnderSg    = itemView.findViewById(R.id.linearLayoutUnderSubGoal);

              //  populateLayout(positiond);
        }

//        private void populateLayout (int position) {
//
//            int numTask = ((Goal) ((DataList) mContext.getApplicationContext()).getListOngoing().get(positionGoal)).getgListSubGoals().get(position).getsListTasks().size();
//            SubGoal sg = ((Goal) ((DataList) mContext.getApplicationContext()).getListOngoing().get(positionGoal)).getgListSubGoals().get(position);
//            //ViewGroup viewForLinear = new View(mContext );
//            View viewToAdd;
//
//            Log.d("USERx", "SUBGOAL sg-position: " + position + " - Num of Tasks: "+ numTask );
//            Log.d("USERx", "TASKS: " );
//            StringBuilder bldSTR =  new StringBuilder();
//            for (int j=0; j < numTask; j++)  {
//
//                LayoutInflater inflater = LayoutInflater.from(mContext);
//                viewToAdd = inflater.inflate(R.layout.template_layout_exp_list_subgoals_children_tasks, null);
//
//                TextView titleTask                      = viewToAdd.findViewById(R.id.textViewChildTasksUnderSubgoal);
//                CircularProgressBar circularProgressBar = viewToAdd.findViewById(R.id.circularProgressBar);
//                if (titleTask == null && circularProgressBar == null)
//                    Log.d("USERx", "Title Task is null." );
//                viewToAdd.setLayoutParams(new LinearLayout.LayoutParams(
//                        LinearLayout.LayoutParams.WRAP_CONTENT,
//                        LinearLayout.LayoutParams.WRAP_CONTENT));
//
//
//                circularProgressBar.setColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
//                circularProgressBar.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
////                circularProgressBar.setProgressBarWidth(getResources().getDimension(R.dimen.progressBarWidth));
////            circularProgressBar.setBackgroundProgressBarWidth(getResources().getDimension(R.dimen.backgroundProgressBarWidth));
////                int animationDuration = 2500; // 2500ms = 2,5s
////                circularProgressBar.setProgressWithAnimation(65, animationDuration); // Default duration = 1500ms
//                circularProgressBar.setProgress(31);
//
//                bldSTR.append(sg.getsListTasks().get(j).getTaskLabel() );
//                bldSTR.append("  ");
//                titleTask.setText( sg.getsListTasks().get(j).getTaskLabel() );
//
//                linearLayoutListTaskUnderSg.addView(viewToAdd);
//            }
//            Log.d("USERx", bldSTR.toString() );
//
//        }

        @Override
        public void onClick(View v) {
            int positionSubGoal = getAdapterPosition();
            // go to tasks list
            // send goal and subgoal positions, to know which tasks should getAplicationContext() address
            Intent intent = new Intent( mContext, ActivityTasks.class );
            intent.putExtra("positionGoal", positionGoal);
            intent.putExtra("positionSubGoal", positionSubGoal);
            mContext.startActivity( intent );
        }
    }
}
