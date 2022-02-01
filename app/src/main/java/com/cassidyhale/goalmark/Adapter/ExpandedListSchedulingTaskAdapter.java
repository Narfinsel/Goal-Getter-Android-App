package com.cassidyhale.goalmark.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.cassidyhale.goalmark.Model.DataList;
import com.cassidyhale.goalmark.Model.Goal;
import com.cassidyhale.goalmark.Model.ItemScheduled;
import com.cassidyhale.goalmark.Model.Task;
import com.cassidyhale.goalmark.R;

import static com.cassidyhale.goalmark.Activities.__GLOBAL_Const.defSubgoalLocus;

public class ExpandedListSchedulingTaskAdapter extends BaseExpandableListAdapter {

    private Context context;
    private DataList dataList;
    private LayoutInflater layoutInflater;

    private int[][] arraySizesGS;
    private int[] arrayCountTaskINSubgoalsForGoal;
    private int numberGroups;
    private int numberChildren;


    public ExpandedListSchedulingTaskAdapter(Context context)  {
        this.context = context;
        this.dataList = (DataList) context.getApplicationContext();
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.arraySizesGS = this.setArraySizeGS();
        this.arrayCountTaskINSubgoalsForGoal = this.countTaskForSubgoalsInGoal();
        this.numberGroups = this.dataList.listOngoing.size();
    }                                   // ---- WORKS

    @Override
    public int getGroupCount() {
        return this.dataList.listOngoing.size();
    }                                                                   // ---- WORKS

    @Override
    public int getChildrenCount(int groupPosition) {
        int num_subgoals = this.dataList.getListOngoing().get(groupPosition).getgListSubGoals().size();
        int counting_tasks = 0;
        for (int i= 0; i< num_subgoals; i++)   {
            counting_tasks += this.arraySizesGS[groupPosition][i];
        }
        /////////////////////
        if ( this.dataList.getListOngoing().get(groupPosition).getgListTasks().size() != 0 )
            counting_tasks += this.dataList.getListOngoing().get(groupPosition).getgListTasks().size() ;
        ////////////////////
        return counting_tasks;
    }                                               // ---- WORKS

    @Override
    public Object getGroup(int groupPosition) {
        Goal goal = this.dataList.listOngoing.get(groupPosition);
        return goal;
    }                                                   // ---- WORKS

    @Override
    public Object getChild(int groupPosition, int childPosition) {

        ItemScheduled itemScheduled = null;

        if ( childPosition < this.arrayCountTaskINSubgoalsForGoal[groupPosition] ) {
            int subgoalPositionInGoal = 0;
            int taskPositionInSubgoal = childPosition;
            for (int i = 0; i < this.arraySizesGS[groupPosition].length; i++) {
                int num_sub_goals_for_group_position = this.arraySizesGS[groupPosition][i];
                subgoalPositionInGoal = i;
                if (taskPositionInSubgoal - num_sub_goals_for_group_position >= 0) {
                    taskPositionInSubgoal -= num_sub_goals_for_group_position;
                } else {
                    itemScheduled = new ItemScheduled(groupPosition, subgoalPositionInGoal, taskPositionInSubgoal);
                    break;
                }
            }
        }
        else if ( childPosition >= this.arrayCountTaskINSubgoalsForGoal[groupPosition]) {
            if (this.dataList.getListOngoing().get(groupPosition).getgListTasks().size() != 0) {
                childPosition = childPosition - this.arrayCountTaskINSubgoalsForGoal[groupPosition];
                itemScheduled = new ItemScheduled(groupPosition, defSubgoalLocus, childPosition);
            }
        }
        return itemScheduled;         // return task;
    }                               // ---- WORKS

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = layoutInflater.inflate(R.layout.expandable_list_group_scheduler_layout, null);
        }
        TextView tvLabel        = (TextView) convertView.findViewById(R.id.textViewExpandListGroupGoalLabel);
        TextView tvRepetitions  = (TextView) convertView.findViewById(R.id.textViewExpandListGroupGoalProgress);

        Goal goal = (Goal) this.getGroup(groupPosition);
        String str = "50%";

        tvLabel.setText( goal.getgLabel() );
        tvRepetitions.setText( str );
        isExpanded = true;

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = layoutInflater.inflate(R.layout.expandable_list_child_scheduler_layout, null);
        }
        TextView tvChildNumber  = (TextView) convertView.findViewById(R.id.textViewExpandListChildTaskChildNumber);
        TextView tvLabel        = (TextView) convertView.findViewById(R.id.textViewExpandListChildTaskLabel);
        TextView tvRepetitions  = (TextView) convertView.findViewById(R.id.textViewExpandListChildTaskRepetitions);

        ItemScheduled itemScheduled = (ItemScheduled) this.getChild(groupPosition, childPosition);
        Task task;

        if ( itemScheduled.getLocusSubGoal() == defSubgoalLocus)
            task = this.dataList.getListOngoing().get(itemScheduled.getLocusGoal())
                                .getgListTasks().get(itemScheduled.getLocusTask());
        else
            task = this.dataList.getListOngoing().get(     itemScheduled.getLocusGoal())
                                .getgListSubGoals().get(    itemScheduled.getLocusSubGoal())
                                .getsListTasks().get(   itemScheduled.getLocusTask());

        String str0 = Integer.toString( childPosition );
        String str1 = task.getTaskNumOfTimesDone() +" / "+ task.getTaskNumRepetitions();
        String str2 = task.getTaskLabel();

        tvChildNumber.setText( str0 );
        tvRepetitions.setText( str1 );
        tvLabel.setText( str2 );
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    // ----------------- EVENT methods ---------------------------
    // ----------------- HELPER methods ---------------------------

    private int[][] setArraySizeGS()   {

        int countOfGoals = this.dataList.listOngoing.size();
        int[] arrayOfGoalsTsizes[] = new int[countOfGoals][];

        for (int i=0; i< countOfGoals; i++)    {
            arrayOfGoalsTsizes[i] = this.array_Sizes_Tasks_For_Subgoal_For_Goal(i);
        }
        return arrayOfGoalsTsizes;
    }                                                        // ---- WORKS

    private int[] array_Sizes_Tasks_For_Subgoal_For_Goal (int positionGoal)  {

        int countOfSubgoals = this.dataList.listOngoing.get(positionGoal).getgListSubGoals().size();
        int[] arrayOfTaskCounts = new int[countOfSubgoals];

        for (int i=0; i< countOfSubgoals; i++)  {
            arrayOfTaskCounts[i] = this.dataList.listOngoing.get(positionGoal).getgListSubGoals().get(i).getsListTasks().size();
        }
        return arrayOfTaskCounts;
    }                  // ---- WORKS

    private int[] countTaskForSubgoalsInGoal()   {                                                   // ---- WORKS
        int countOfGoals = this.dataList.listOngoing.size();
        int array[] = new int[countOfGoals];

        int countTaskOnlyInSubGoals = 0;
        for (int i=0; i < countOfGoals; i++)    {
            array[i] = 0;
            for (int j=0; j < this.arraySizesGS[i].length; j++)    {
                array[i] += this.arraySizesGS[i][j];
            }
        }
        return array;
    }                                              // ---- WORKS

    public void test_Display_ArrayGS() {
        StringBuilder string = new StringBuilder();
        string.append("\n");
        string.append(" ----------------------------------------------- \n");
        string.append(" ---------- EXPANDBALE LIST VIEW TASK ---------- \n");
        int i;
        for (i = 0; i < arraySizesGS.length; i++)  {
            string.append("Array G[" + i + "]: \n");
            for (int j = 0; j < arraySizesGS[i].length; j++) {
                string.append("     Array S[" + j + "] = "+ arraySizesGS[i][j] +"\n");
            }
        }
        string.append(" ----------------------------------------------- \n");
        string.append(" ----------------------------------------------- \n");
        Log.d("USERx", string.toString() );
    }                                                        // ---- WORKS

    public void test_Print_ArrayGS()    {
        StringBuilder string = new StringBuilder();
        string.append("\n");
        string.append(" ----------------------------------------------- \n");
        string.append(" ----------- PRINT ARRAY of SubGoals ----------- \n");
        int i;
        string.append("{");
        for (i = 0; i < arraySizesGS.length; i++)  {
            if (i!=0)
                string.append(" ");
            string.append("  g["+ i +"] = {");
            for (int j = 0; j < arraySizesGS[i].length; j++) {
                string.append(" "+ arraySizesGS[i][j] + ",");
            }
            string.deleteCharAt(string.length()-1);
            string.append("} \n");
        }
        string.append("}");
        string.append(" ----------------------------------------------- \n");
        string.append(" ----------------------------------------------- \n");
        Log.d("USERx", string.toString() );
    }                                                       // ---- WORKS

}

//    @Override
//    public Object getChild(int groupPosition, int childPosition) {
//
//        int subgoalPositionInGoal = 0;
//        int taskPositionInSubgoal = childPosition;
//        Task task = null;
//        //Log.d ("Userx", "  this.arraySizesGS[groupPosition].length = " + this.arraySizesGS[groupPosition].length);
//        for (int i=0; i< this.arraySizesGS[groupPosition].length; i++){
//            int num_sub_goals_for_group_position = this.arraySizesGS[groupPosition][i];
//            subgoalPositionInGoal = i;
//            if ( taskPositionInSubgoal - num_sub_goals_for_group_position >= 0) {
//                taskPositionInSubgoal -= num_sub_goals_for_group_position;
//            }
//            else {
//
//                task = this.dataList.getListOngoing().get(groupPosition)
//                        .getgListSubGoals().get(subgoalPositionInGoal)
//                        .getsListTasks().get(taskPositionInSubgoal);
////                String str = "      G: "+ groupPosition + "    S: "+ subgoalPositionInGoal +"    T: "+ taskPositionInSubgoal +
////                        "            groupPos = "+ groupPosition + "    childPos = "+ childPosition;
//                String str = "            groupPos = "+ groupPosition + "    childPos = "+ childPosition +
//                        "      ( G="+ groupPosition + "  S="+ subgoalPositionInGoal +"  T="+ taskPositionInSubgoal +" )";
//                Log.d ("Userx", str);
//                break;
//            }
//        }
//        return task;
//    }
