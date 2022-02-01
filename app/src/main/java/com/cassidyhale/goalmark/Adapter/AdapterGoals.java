package com.cassidyhale.goalmark.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.cassidyhale.goalmark.Activities.ActivityGoalsEdit;
import com.cassidyhale.goalmark.Activities.ActivitySubGoals;
import com.cassidyhale.goalmark.Model.DataList;
import com.cassidyhale.goalmark.Model.Goal;
import com.cassidyhale.goalmark.R;

import java.util.List;


public class AdapterGoals extends RecyclerView.Adapter <AdapterGoals.ViewHolder> {

    private Context mContext;
    private List<Goal> mListGoals;

    public AdapterGoals (Context context, List<Goal> listGoals)    {
        this.mContext = context;
        this.mListGoals = listGoals;
    }

    @NonNull
    @Override
    public AdapterGoals.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from( viewGroup.getContext() )
                .inflate( R.layout.template_layout_list_row_goals, viewGroup, false );
        return new ViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterGoals.ViewHolder viewHolder, int i) {
        Goal goal = this.mListGoals.get(i);
        viewHolder.title.setText( goal.getgLabel() );
        viewHolder.image.setImageResource( goal.getResourceBackroundId() );
    }

    @Override
    public int getItemCount() {
        return this.mListGoals.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener {

        public TextView title;
        public ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener( this );
            this.title = (TextView) itemView.findViewById( R.id.textViewGoalTitle);
            this.image = (ImageView) itemView.findViewById( R.id.imageViewGoalImage);
        }
        @Override
        public void onClick(View v) {
            int positionGoal = getAdapterPosition();
            Goal goalClicked = mListGoals.get( positionGoal);

            Intent intent = new Intent( mContext, ActivitySubGoals.class );
            intent.putExtra("positionGoal", positionGoal);
            mContext.startActivity( intent );
        }
    }
}
