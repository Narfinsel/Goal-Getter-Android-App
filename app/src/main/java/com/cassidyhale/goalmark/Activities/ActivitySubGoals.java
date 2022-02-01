package com.cassidyhale.goalmark.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cassidyhale.goalmark.Model.DataList;
import com.cassidyhale.goalmark.Model.Goal;
import com.cassidyhale.goalmark.Model.SubGoal;
import com.cassidyhale.goalmark.Model.Task;
import com.cassidyhale.goalmark.R;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.util.ArrayList;

public class ActivitySubGoals extends AppCompatActivity implements FragmentTaskAddToGoal.NoticeDialogListener{

    private Toolbar mToolbar;
    private ImageView mImageViewGoalHeader;
    private LinearLayout mLinearLayoutSubgoals;
    private LinearLayout mLinearLayoutTasks;
    private LinearLayout mLinearLayoutInScroll;
    private ListView mListViewSubgoals;
    private ListView mListViewTasksInGoal;
    private ImageButton mImageButtonAddSubGoalToGoal;
    private ImageButton mImageButtonAddTaskToGoal;

    private CustomListAdapter mCustomListAdapter;

    private ArrayList<View> arrayViewSubgoals;
    private ArrayList<View> arrayViewTasks;
    private int positionGoal;
    private int positionSubGoalForClicks;
    private int colorForThisGoal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_subgoal);

        this.arrayViewSubgoals  = new ArrayList<>();
        this.arrayViewTasks     = new ArrayList<>();
        if ( getIntent() != null) {
            positionGoal =  getIntent().getIntExtra("positionGoal", 0);
            this.colorForThisGoal = ((DataList) getApplicationContext()).getListOngoing().get(positionGoal).getGoalColor();
            if ( this.colorForThisGoal == 0) {
                this.colorForThisGoal = R.color.color_goal_white;
                Log.d(__GLOBAL_Const.TAG_handleUserfb, "FUCK 1!");
            }
        }
        initialize_views();
    }                               //      WORKS !!!

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(__GLOBAL_Const.TAG_handleUserfb, "Activity_Sub_Goals  -   onRestart()");
        if ( ((DataList) getApplicationContext()).isNeedsUpdateToFire() ) {
            this.refresh_LinearLayout_Subgoal();
            this.mImageViewGoalHeader.setImageResource( ((DataList) getApplicationContext()).getListOngoing().get(positionGoal).getResourceBackroundId() );
        }
    }

    private void initialize_views ()    {
        // ------------ Toolbar
        mToolbar = findViewById(R.id.toolbarSubGoals);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle (R.string.string_Toolbar_Title_SubGoal);

        // ------------ ImageView
        mImageViewGoalHeader = findViewById(R.id.imageViewGoalHeader);
        mImageViewGoalHeader.setImageResource( ((DataList) getApplicationContext()).getListOngoing().get(positionGoal).getResourceBackroundId() );

        // ------------ ImageButton
        mImageButtonAddSubGoalToGoal = findViewById(R.id.imageButtonAddSubgoalToGoal);
        mImageButtonAddSubGoalToGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentTo_AddSubGoals_Screen = new Intent (ActivitySubGoals.this, ActivitySubGoalAdd.class);
                intentTo_AddSubGoals_Screen.putExtra("positionGoal", positionGoal);
                startActivityForResult( intentTo_AddSubGoals_Screen, __GLOBAL_Const.REQUEST_CODE_NEW_TASK );
            }
        });
        mImageButtonAddTaskToGoal = findViewById(R.id.imageButtonAddNewTaskToGoal);
        mImageButtonAddTaskToGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new FragmentTaskAddToGoal();
                Bundle bundle = new Bundle();
                bundle.putInt("positionGoal", positionGoal);
                newFragment.setArguments(bundle);
                newFragment.show(getSupportFragmentManager(), "new_task_to_goal");
            }
        });

        // ------------ Layout
        mLinearLayoutInScroll =findViewById(R.id.linearLayoutInSideScroll);
        mLinearLayoutSubgoals = findViewById(R.id.linearLayoutSubgoals);
        mLinearLayoutTasks = findViewById(R.id.linearLayoutTasks);

        // ------------ Populating Layouts
        this.add_Views_to_LinearLayout_Subgoals();
        this.add_Views_to_LinearLayout_Tasks();
    }                                               //      WORKS !!!

    private void add_Views_to_LinearLayout_Subgoals ()  {

        int numberOfSubgoalsInGoal = ((DataList) getApplicationContext()).getListOngoing().get(positionGoal).getgListSubGoals().size();
        View viewToAdd_Subgoal;

        for (int i=0 ; i< numberOfSubgoalsInGoal; i++)  {

            this.setPositionSubGoalForClicks(i);
            viewToAdd_Subgoal = getLayoutInflater().inflate(R.layout.template_layout_list_row_subgoals_listview, null);

            SubGoal subGoalCurrent = ((DataList) getApplicationContext()).getListOngoing().get(positionGoal).getgListSubGoals().get( i );
            TextView textView = (TextView) viewToAdd_Subgoal.findViewById(R.id.textViewListViewSubGoal);
            String textForSubgoalTV = "SG_"+ i +"  -  " +subGoalCurrent.getsLabel();
            textView.setText( textForSubgoalTV );
            LinearLayout linearInLinearTasks = viewToAdd_Subgoal.findViewById(R.id.linearLayoutListViewSubGoalTasts);
            viewToAdd_Subgoal.setLayoutParams( new LinearLayout.LayoutParams (LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            View viewToAdd_Task;
            int numberOfTasksPerSubgoal = ((DataList) getApplicationContext()).getListOngoing().get(positionGoal).getgListSubGoals().get( i ).getsListTasks().size();

            for (int j = 0; j < numberOfTasksPerSubgoal; j++) {

                LayoutInflater inflater = LayoutInflater.from( ActivitySubGoals.this );
                viewToAdd_Task = inflater.inflate(R.layout.template_layout_exp_list_subgoals_children_tasks, null);

                TextView taxtViewTitleTask              = viewToAdd_Task.findViewById(R.id.textViewChildTasksUnderSubgoal);
                CircularProgressBar circularProgressBar = viewToAdd_Task.findViewById(R.id.circularProgressBar);

                viewToAdd_Task.setLayoutParams( new LinearLayout.LayoutParams (LinearLayout.LayoutParams.WRAP_CONTENT,  LinearLayout.LayoutParams.WRAP_CONTENT));

                circularProgressBar.setColor(ContextCompat.getColor(ActivitySubGoals.this, this.colorForThisGoal));
                circularProgressBar.setBackgroundColor(ContextCompat.getColor(ActivitySubGoals.this, R.color.colorPrimary));
                circularProgressBar.setProgress(86);
                String textForTaskTV = "T_"+ j +"  -  "+ subGoalCurrent.getsListTasks().get(j).getTaskLabel();
                taxtViewTitleTask.setText( textForTaskTV );

                linearInLinearTasks.addView (viewToAdd_Task);
            }
            //viewToAdd_Subgoal.setClickable(false);
            //viewToAdd_Subgoal.setDuplicateParentStateEnabled(true);
            this.arrayViewSubgoals.add ( viewToAdd_Subgoal );
            mLinearLayoutSubgoals.addView ( viewToAdd_Subgoal );
        }
        //mLinearLayoutSubgoals.setClickable(false);
        this.attach_On_Click_Listeners_To_Subgoal_View();
    }                               //      WORKS !!!

    private void add_Views_to_LinearLayout_Tasks ()  {

        int numberOfTasksInGoal = ((DataList) getApplicationContext()).getListOngoing().get(positionGoal).getgListTasks().size();
        View viewToAdd_Task;

        for (int i=0 ; i< numberOfTasksInGoal; i++)  {

            LayoutInflater inflater = LayoutInflater.from( ActivitySubGoals.this );
            viewToAdd_Task = inflater.inflate(R.layout.template_layout_exp_list_subgoals_children_tasks, null);

            TextView taxtViewTitleTask              = viewToAdd_Task.findViewById(R.id.textViewChildTasksUnderSubgoal);
            CircularProgressBar circularProgressBar = viewToAdd_Task.findViewById(R.id.circularProgressBar);

            viewToAdd_Task.setLayoutParams( new LinearLayout.LayoutParams ( LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            circularProgressBar.setColor(ContextCompat.getColor(ActivitySubGoals.this, this.colorForThisGoal));
            circularProgressBar.setBackgroundColor(ContextCompat.getColor(ActivitySubGoals.this, R.color.colorPrimary));
            circularProgressBar.setProgress(86);
            String textForTaskTV = "T_"+ i +"  -  "+ ((DataList) getApplicationContext()).getListOngoing().get(positionGoal).getgListTasks().get(i).getTaskLabel();
            taxtViewTitleTask.setText( textForTaskTV );

            this.arrayViewTasks.add ( viewToAdd_Task );
            mLinearLayoutTasks.addView ( viewToAdd_Task );
        }

    }                                   //      WORKS !!!

    private void attach_On_Click_Listeners_To_Subgoal_View ()   {
        for (int i =0; i< this.arrayViewSubgoals.size(); i++)   {
            setPositionSubGoalForClicks(i);
            this.arrayViewSubgoals.get( i ).setOnClickListener( new View.OnClickListener() {

                public final int positionSubGoal = getPositionSubGoalForClicks();

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent( ActivitySubGoals.this, ActivityTasks.class );
                    intent.putExtra("positionGoal", positionGoal);
                    intent.putExtra("positionSubGoal", positionSubGoal);
                    startActivityForResult( intent, __GLOBAL_Const.REQUEST_IS_NEW_TASK_ADEDD_IN_SG );
                }
            });
        }
    }                       //      WORKS !!!

    private void refresh_LinearLayout_Subgoal ()    {
        this.arrayViewSubgoals.clear();
        this.mLinearLayoutSubgoals.removeAllViews();
        this.add_Views_to_LinearLayout_Subgoals();
    }                                   //      WORKS !!!

    private void refresh_LinearLayout_Task ()    {
        this.arrayViewTasks.clear();
        this.mLinearLayoutTasks.removeAllViews();
        this.add_Views_to_LinearLayout_Tasks();
    }                                       //      WORKS !!!

    @Override
    public boolean onCreateOptionsMenu (Menu menu)  {
        getMenuInflater().inflate(R.menu.menu_edit_goal, menu);
        return super.onCreateOptionsMenu(menu);
    }                                   //      WORKS !!!

    @Override
    public boolean onOptionsItemSelected (MenuItem item)    {

        switch ( item.getItemId() ){
            case R.id.buttonEditGoal:
                // Edit current Goal
                Intent intent = new Intent( this, ActivityGoalsEdit.class );
                intent.putExtra("positionGoal", positionGoal);
                startActivity( intent );
                break;
            /*
            case R.id.buttonAddSubGoal:
                // Add New SubGoal to Current Goal
                // Send the position of the current Goal in the (DataList)getAplicationContext() - to the next Add Sub-goal Activity
                // The next activity needs to know to which Goal in the getAplicationContext() (Datalist object) to add the subgoal
                Intent intentTo_AddSubGoals_Screen = new Intent (ActivitySubGoals.this, ActivitySubGoalAdd.class);
                intentTo_AddSubGoals_Screen.putExtra("positionGoal", positionGoal);
                startActivityForResult( intentTo_AddSubGoals_Screen, __GLOBAL_Const.REQUEST_CODE_NEW_TASK );
                break;
            case R.id.buttonAddTaskToGoal:
                // Add New Task to Current Goal
                // Send the position of the current Goal in the (DataList)getAplicationContext() - to the next Add Task
                // The next fragment needs to know to which Goal in the getAplicationContext() (Datalist object) to add the Task
                DialogFragment newFragment = new FragmentTaskAddToGoal();
                Bundle bundle = new Bundle();
                bundle.putInt("positionGoal", positionGoal);
                newFragment.setArguments(bundle);
                newFragment.show(getSupportFragmentManager(), "new_task_to_goal");
                break;  */
        }
        return super.onOptionsItemSelected(item);
    }                           //      WORKS !!!

    @Override
    public void onClickAddNewTask (DialogFragment dialog) {
        ((DialogFragment) getSupportFragmentManager().findFragmentByTag("new_task_to_goal")).dismiss();
        this.refresh_LinearLayout_Task();
    }                           //      WORKS !!!

    @Override
    public void onClickCancelAddNewTask (DialogFragment dialog) {
        ((DialogFragment) getSupportFragmentManager().findFragmentByTag("new_task_to_goal")).dismiss();
    }                       //      WORKS !!!

    public int getPositionSubGoalForClicks() {
        return positionSubGoalForClicks;
    }                                           //      WORKS !!!

    public void setPositionSubGoalForClicks(int positionSubGoalForClicks) {
        this.positionSubGoalForClicks = positionSubGoalForClicks;
    }               //      WORKS !!!

//  ------------------------------- Useless --------------------------------------
//    private int findColorInResources (int colorRes)    {
//        for (int i=0; i< __GLOBAL_Const.array_colors.length; i++)
//            if ( colorRes == __GLOBAL_Const.array_colors[i] ) {
//                Log.d(__GLOBAL_Const.TAG_handleUserfb, "FUCK 2 - "+ i +"!");
//                return __GLOBAL_Const.array_colors[i];
//            }
//        Log.d(__GLOBAL_Const.TAG_handleUserfb, "FUCK 2 - end!");
//        return __GLOBAL_Const.array_colors[0];
//    }

//  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == __GLOBAL_Const.REQUEST_CODE_NEW_TASK) {
//            if (resultCode == __GLOBAL_Const.RESULT_CODE_NEW_TASK) {
//                mCustomListAdapter.notifyDataSetChanged();
//            }
//        }
//        if (requestCode == __GLOBAL_Const.REQUEST_IS_NEW_TASK_ADEDD_IN_SG) {
//            Log.d(__GLOBAL_Const.TAG_handleUserfb, "            ACT-SubGoal    :   onActivityResult = " + __GLOBAL_Const.REQUEST_IS_NEW_TASK_ADEDD_IN_SG );
//            if (resultCode == __GLOBAL_Const.RESULT_IS_NEW_TASK_ADEDD_IN_SG) {
//                Log.d(__GLOBAL_Const.TAG_handleUserfb, "            ACT-SubGoal    :   onActivityResult = " + __GLOBAL_Const.REQUEST_IS_NEW_TASK_ADEDD_IN_SG + "    "
//                                                                                                                    + __GLOBAL_Const.RESULT_IS_NEW_TASK_ADEDD_IN_SG );
//                this.refresh_LinearLayout_Subgoal();
//            }
//        }
//    }       //      WORKS !!!
//





    class CustomListAdapter extends BaseAdapter {

        public CustomListAdapter() {
        }

        @Override
        public int getCount() {
            return ((Goal) ((DataList) getApplicationContext()).getListOngoing().get(positionGoal))
                    .getgListSubGoals().size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            int totalHeight = 0;

            SubGoal subGoalCurrent = ((Goal) ((DataList) getApplicationContext()).getListOngoing().get(positionGoal)).getgListSubGoals().get(position);

            convertView = getLayoutInflater().inflate(R.layout.template_layout_list_row_subgoals_listview, null);
            TextView textView = (TextView) convertView.findViewById(R.id.textViewListViewSubGoal);
                 textView.setText( subGoalCurrent.getsLabel() );
            LinearLayout linearLayout = (LinearLayout) convertView.findViewById(R.id.linearLayoutListViewSubGoalTasts);

            //totalHeight = listView.getPaddingTop() + listView.getPaddingBottom();

            View viewToAdd;
            int numberOfTasks = ((Goal) ((DataList) getApplicationContext()).getListOngoing().get(positionGoal)).getgListSubGoals().get(position).getsListTasks().size();
            int heigthAllTextViews = 0;
            for (int i=0; i < numberOfTasks; i++) {

                LayoutInflater inflater = LayoutInflater.from( ActivitySubGoals.this );
                viewToAdd = inflater.inflate(R.layout.template_layout_exp_list_subgoals_children_tasks, null);

                TextView taxtViewTitleTask                      = viewToAdd.findViewById(R.id.textViewChildTasksUnderSubgoal);
                CircularProgressBar circularProgressBar = viewToAdd.findViewById(R.id.circularProgressBar);
                if (taxtViewTitleTask == null && circularProgressBar == null)
                    Log.d("USERx", "Title Task is null." );
                viewToAdd.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));

                circularProgressBar.setColor(ContextCompat.getColor(ActivitySubGoals.this, R.color.colorAccent));
                circularProgressBar.setBackgroundColor(ContextCompat.getColor(ActivitySubGoals.this, R.color.colorPrimary));
                circularProgressBar.setProgress(31);

                taxtViewTitleTask.setText( subGoalCurrent.getsListTasks().get(i).getTaskLabel() );
                heigthAllTextViews += taxtViewTitleTask.getMeasuredHeight();
                linearLayout.addView(viewToAdd);
            }
            return convertView;
        }
    }

    private void old_list_method_does_not_work()    {
        // ------------ List View of Subgoals and their Tasks
        mCustomListAdapter = new CustomListAdapter();
        mListViewSubgoals = (ListView) findViewById(R.id.listViewSubgoals);
        mListViewSubgoals.setAdapter(mCustomListAdapter);
        mListViewSubgoals.setClickable(true);

        mListViewSubgoals.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Intent intent = new Intent( ActivitySubGoals.this, ActivityTasks.class );
                intent.putExtra("positionGoal", positionGoal);
                intent.putExtra("positionSubGoal", position);
                startActivity( intent );
            }
        });
        mListViewSubgoals.setScrollContainer(false);
        mListViewTasksInGoal = findViewById(R.id.listViewTasksForGoal);
        mListViewTasksInGoal.setAdapter( new ListAdapterTasksInGoal (this,
                R.layout.template_layout_exp_list_subgoals_children_tasks ,
                ((DataList) getApplicationContext()).getListOngoing().get(positionGoal).getgListTasks() ) );
    }

    public static class Utility {
        public static void setListViewHeightBasedOnChildren(ListView listView) {
            ListAdapter listAdapter = listView.getAdapter();
            if (listAdapter == null) {
                // pre-condition
                return;
            }

            int totalHeight = listView.getPaddingTop() + listView.getPaddingBottom();
            //Log.d ("Userx", "           LIST-UTILS  :   height [initial] =       " + totalHeight);

            for (int i = 0; i < listAdapter.getCount(); i++) {
                View listItem = listAdapter.getView(i, null, listView);
                if (listItem instanceof ViewGroup) {
                    listItem.setLayoutParams(new ViewGroup.LayoutParams (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                }

                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
                //Log.d ("Userx", "              LIST-UTILS  :   height["+ i +"] =       " + listItem.getMeasuredHeight());
            }

            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
            Log.d ("Userx", "           LIST-UTILS  :   params.heigth =       " + params.height );
            listView.setLayoutParams(params);
        }
    }

    public static class ListUtils {
        public static void setDynamicHeight(ListView mListView) {
            ListAdapter mListAdapter = mListView.getAdapter();
            if (mListAdapter == null) {
                // when adapter is null
                return;
            }
            int height = 0;
            int desiredWidth = MeasureSpec.makeMeasureSpec (mListView.getWidth(), MeasureSpec.UNSPECIFIED);
            for (int i = 0; i < mListAdapter.getCount(); i++) {
                View listItem = mListAdapter.getView(i, null, mListView);
                listItem.measure (desiredWidth, MeasureSpec.AT_MOST);
                height += listItem.getHeight();
                Log.d ("Userx", "           LIST-UTILS  :   height["+ i +"] =       " + height);
            }
            ViewGroup.LayoutParams params = mListView.getLayoutParams();
            params.height = height + (mListView.getDividerHeight() * (mListAdapter.getCount() - 1));
            Log.d ("Userx", "           LIST-UTILS  :   params.height =     "+ params.height);
            mListView.setLayoutParams(params);
            mListView.requestLayout();
        }
    }

    class ListAdapterTasksInGoal extends ArrayAdapter<Task> {

        private Context mContext;
        private int resourceOfListElement;

        public ListAdapterTasksInGoal(@NonNull Context context, int resource, ArrayList<Task> tasks) {
            super(context, resource, tasks);
            this.mContext = context;
            this.resourceOfListElement = resource;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            //return super.getView(position, convertView, parent);

            //convertView = getLayoutInflater().inflate( this.resourceOfListElement, parent, false);
            convertView = getLayoutInflater().inflate( this.resourceOfListElement, null);

//            LayoutInflater inflater = LayoutInflater.from( ActivitySubGoals.this );
//            convertView = inflater.inflate(R.layout.template_layout_exp_list_subgoals_children_tasks, null);

            // ------------ TASK LABEL ------------------
            TextView titleTask                      = convertView.findViewById(R.id.textViewChildTasksUnderSubgoal);
            String taskLabel = ((DataList) mContext.getApplicationContext()).getListOngoing().get(positionGoal).getgListTasks().get(position).getTaskLabel();
            titleTask.setText( taskLabel );

            // ------------ CIRCULAR BAR ------------------
            CircularProgressBar circularProgressBar = convertView.findViewById(R.id.circularProgressBar);
            if (titleTask == null && circularProgressBar == null)
                Log.d("USERx", "Title Task is null." );
            convertView.setLayoutParams(new LinearLayout.LayoutParams(  LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            circularProgressBar.setColor(ContextCompat.getColor(ActivitySubGoals.this, R.color.colorAccent));
            circularProgressBar.setBackgroundColor(ContextCompat.getColor(ActivitySubGoals.this, R.color.colorPrimary));
            circularProgressBar.setProgress(69);

            return convertView;
        }
    }



}
