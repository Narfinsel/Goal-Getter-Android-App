package com.cassidyhale.goalmark.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cassidyhale.goalmark.Activities.ActivitySubGoals;
import com.cassidyhale.goalmark.Model.DataList;
import com.cassidyhale.goalmark.Model.Goal;
import com.cassidyhale.goalmark.R;


public class AdapterGoals_Backup extends RecyclerView.Adapter <AdapterGoals_Backup.ViewHolder> {

    private Context mContext;
    private DataList mDataList;

    public AdapterGoals_Backup(Context context, DataList dataList)    {
        this.mContext = context;
        this.mDataList = dataList;
    }

    @NonNull
    @Override
    public AdapterGoals_Backup.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from( viewGroup.getContext() )
                .inflate( R.layout.template_layout_list_row_goals, viewGroup, false );

        return new ViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterGoals_Backup.ViewHolder viewHolder, int i) {
        Goal goal = (Goal) mDataList.getListOngoing().get(i);
        viewHolder.title.setText( goal.getgLabel() );
        viewHolder.image.setImageResource( goal.getResourceBackroundId() );
    }

    @Override
    public int getItemCount() {
        return mDataList.getListOngoing().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener {

        public TextView title;
        public ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener( this );
            this.title = (TextView) itemView.findViewById(R.id.textViewGoalTitle);
            this.image = (ImageView) itemView.findViewById(R.id.imageViewGoalImage);
        }
        @Override
        public void onClick(View v) {
            int positionGoal = getAdapterPosition();
            Goal goalClicked = (Goal) mDataList.getListOngoing().get( positionGoal);

            Intent intent = new Intent( mContext, ActivitySubGoals.class );
            intent.putExtra("positionGoal", positionGoal);
            mContext.startActivity( intent );

            //Toast.makeText( mContext,goalClicked.getgLabel(), Toast.LENGTH_LONG).show();
        }
    }
}
