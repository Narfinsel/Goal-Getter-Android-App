package com.cassidyhale.goalmark.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cassidyhale.goalmark.Model.DataList;
import com.cassidyhale.goalmark.Model.Goal;
import com.cassidyhale.goalmark.Model.SubGoal;
import com.cassidyhale.goalmark.Model.Task;
import com.cassidyhale.goalmark.R;

public class AdapterTasks extends RecyclerView.Adapter <AdapterTasks.ViewHolder> {

    private Context mContext;
    private DataList mDataList;
    private Goal mGoal;
    private SubGoal mSubGoal;
    private int positionGoal;
    private int positionSubGoal;

    public AdapterTasks(Context context, DataList dataList, int positionGoal, int positionSubGoal)    {
        this.mContext = context;
        this.mDataList = dataList;
        this.positionGoal = positionGoal;
        this.positionSubGoal = positionSubGoal;
        this.mGoal = (Goal) (this.mDataList.getListOngoing().get( this.positionGoal ));
        this.mSubGoal= mGoal.getgListSubGoals().get( this.positionSubGoal );
    }
    public AdapterTasks(Context context, Goal goal)    {
        this.mContext = context;
        this.mGoal = goal;
    }


    @NonNull
    @Override
    public AdapterTasks.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from( viewGroup.getContext() )
                .inflate( R.layout.template_layout_list_row_tasks, viewGroup, false );
        return new ViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterTasks.ViewHolder viewHolder, int i) {
        Task task = mSubGoal.getsListTasks().get(i);
        viewHolder.taskTitle.setText( task.getTaskLabel() );
        viewHolder.taskImage.setBackgroundColor(423445);
    }

    @Override
    public int getItemCount() {
        return mSubGoal.getsListTasks().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener {

        public TextView taskTitle;
        public ImageView taskImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener( this );
            this.taskTitle = (TextView) itemView.findViewById(R.id.textViewTasksTitle);
            this.taskImage = (ImageView) itemView.findViewById(R.id.imageViewTaskImage);
        }

        @Override
        public void onClick(View v) {
            int positionGoal = getAdapterPosition();

//            SubGoal subGoal = mGoal.getgListSubGoals().get(position);
//            Intent intent = new Intent( mContext, ActivitySubGoalsTriplicate.class );
//            intent.putExtra("goalTitle", goalClicked.getgLabel());
//            mContext.startActivity( intent );
//            Toast.makeText( mContext, task.getsLabel(), Toast.LENGTH_LONG).show();
        }
    }
}
