package com.cassidyhale.goalmark.Activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.cassidyhale.goalmark.Model.DataList;
import com.cassidyhale.goalmark.Model.Goal;
import com.cassidyhale.goalmark.Model.SubGoal;
import com.cassidyhale.goalmark.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class ActivitySubGoalsEdit extends AppCompatActivity {

    private TextView mTextViewEditGoalLabel;
    private Button mButtonSaveChangesSubGoal;
    private ProgressDialog mProgressDialog;
    private TextView mTextViewEditStartDate;
    private TextView mTextViewEditEndDate;
    private Button mButtonEditStartDate;
    private Button mButtonEditEndDate;

    private Calendar mCalendar;
    private DatePickerDialog mDatePickerDialogStart;
    private DatePickerDialog mDatePickerDialogEnd;

    private int goalPosition;
    private int suboalPosition;
    private SubGoal subGoalEdited;

    private Date subGoalStartDate;
    private Date subGoalEndDate;
    private Date subgoalOldStartDate;
    private Date subgoalOldEndDate;
    private Date dateGoalStart;
    private Date dateGoalEnd;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_subgoal);

        this.goalPosition = getIntent().getIntExtra("positionGoal", __GLOBAL_Const.default_edit_goal_position);
        this.suboalPosition = getIntent().getIntExtra("positionSubGoal", __GLOBAL_Const.default_edit_subgoal_position);
        this.subGoalEdited = ((DataList) getApplicationContext()).getListOngoing().get( this.goalPosition ).getgListSubGoals().get( this.suboalPosition );
        this.subGoalStartDate = this.subGoalEdited.getsDateStart();
        this.subGoalEndDate = this.subGoalEdited.getsDateEnd();
        this.dateGoalStart = ((DataList) (getApplicationContext())).listOngoing.get( this.goalPosition ).getgDateStart();
        this.dateGoalEnd = ((DataList) (getApplicationContext())).listOngoing.get( this.goalPosition ).getgDateEnd();

        this.subgoalOldStartDate = this.subGoalEdited.getsDateStart();
        this.subgoalOldEndDate = this.subGoalEdited.getsDateEnd();

        this.initialize_views();
        this.initialize_value_fields_for_goal();
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.test_have_subgoal_date_updated_properly();
    }

    private void initialize_views ()    {
        // -----------------------------------------------------------------------------------------
        // ----------- TOOLBAR setup ---------------------------------------------------------------
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbarEditSubGoal);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle (R.string.string_Toolbar_Title_SubGoal_Edit);

        // -----------------------------------------------------------------------------------------
        // ----------- SET start / end DATES for NEW GOAL ------------------------------------------
        mCalendar = Calendar.getInstance();
        mTextViewEditStartDate  = (TextView) findViewById(R.id.textViewEditSubGStartDatePicked);
        mTextViewEditEndDate    = (TextView) findViewById(R.id.textViewEditSubGEndDatePicked);

        mButtonEditStartDate    = (Button)  findViewById(R.id.buttonEdtSubGoalStartDate);
        mButtonEditEndDate      = (Button)  findViewById(R.id.buttonEditSubGoalEndDate);

        this.initialize_date_pickers();

        // -----------------------------------------------------------------------------------------
        // ----------- EDIT the SUB-GOAL -----------------------------------------------------------
        mProgressDialog = new ProgressDialog(this);
        mTextViewEditGoalLabel = (TextView) findViewById(R.id.textViewEditSubGoalTitle);
        mButtonSaveChangesSubGoal = (Button) findViewById(R.id.buttonSaveChangesSubGoal);
        mButtonSaveChangesSubGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTheSubGoal();
            }
        });
    }

    private void initialize_value_fields_for_goal ()    {
        this.mTextViewEditGoalLabel.setText( this.subGoalEdited.getsLabel() );
        this.mTextViewEditStartDate.setText( dateToString( this.subGoalEdited.getsDateStart() ));
        this.mTextViewEditEndDate.setText( dateToString( this.subGoalEdited.getsDateEnd() ));
    }

    private void initialize_date_pickers () {
        int thisDay = mCalendar.get(Calendar.DAY_OF_MONTH);
        int thisMonth = mCalendar.get(Calendar.MONTH);
        int thisYear = mCalendar.get(Calendar.YEAR);

        //subGoalStartDate = new GregorianCalendar(thisYear, thisMonth, thisDay).getTime();
        //subGoalStartDate.setTime( dateGoalStart.getTime() );
        mButtonEditStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendarStartShow = Calendar.getInstance();

                if (subGoalStartDate != null )
                    calendarStartShow.setTime( subGoalStartDate );

                mDatePickerDialogStart = new DatePickerDialog(ActivitySubGoalsEdit.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        process_Date_Start(year, month, dayOfMonth);
                    }
                },  calendarStartShow.get (Calendar.YEAR),
                    calendarStartShow.get (Calendar.MONTH),
                    calendarStartShow.get (Calendar.DAY_OF_MONTH));
                // ---------------------------

                //mDatePickerDialogStart.getDatePicker().setMinDate( Calendar.getInstance().getTime().getTime());
                mDatePickerDialogStart.getDatePicker().setMinDate( dateGoalStart.getTime());
                mDatePickerDialogStart.getDatePicker().setMaxDate( dateGoalEnd.getTime() - 86_400_000 );
                if ( subGoalStartDate != null )
                    mDatePickerDialogStart.updateDate(  calendarStartShow.get(Calendar.YEAR),
                                                        calendarStartShow.get(Calendar.MONTH),
                                                        calendarStartShow.get(Calendar.DAY_OF_MONTH) );
                mDatePickerDialogStart.show();
            }
        });


        mButtonEditEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // show dialog picker and chose end date
                // At least one day (86400000 ms) should be between start date and end date

                Calendar calendarEndShow = Calendar.getInstance();
                Calendar calendarStartingDisable = Calendar.getInstance();

                if ( subGoalStartDate != null)
                    calendarStartingDisable.setTime ( subGoalStartDate );

                if ( subGoalStartDate != null && subGoalEndDate != null)
                    calendarEndShow.setTime ( subGoalEndDate );
                else if ( subGoalStartDate != null && subGoalEndDate == null)
                    calendarEndShow.setTime ( subGoalStartDate );

                mDatePickerDialogEnd = new DatePickerDialog(ActivitySubGoalsEdit.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet (DatePicker view, int year, int month, int dayOfMonth) {
                        process_Date_End( year, month, dayOfMonth);
                    }
                }, calendarEndShow.get (Calendar.YEAR), calendarEndShow.get (Calendar.MONTH), calendarEndShow.get (Calendar.DAY_OF_MONTH));

                mDatePickerDialogEnd.getDatePicker().setMinDate( calendarStartingDisable.getTime().getTime() + 86_400_000);
                mDatePickerDialogEnd.getDatePicker().setMaxDate( dateGoalEnd.getTime() );
                if ( subGoalEndDate != null )
                    mDatePickerDialogEnd.updateDate( calendarEndShow.get(Calendar.YEAR),
                            calendarEndShow.get(Calendar.MONTH),
                            calendarEndShow.get(Calendar.DAY_OF_MONTH) );
                mDatePickerDialogEnd.show();
            }
        });
    }

    private void process_Date_Start (int year, int month, int dayOfMonth)   {
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.set (Calendar.YEAR, year);
        calendarStart.set (Calendar.MONTH, month);
        calendarStart.set (Calendar.DAY_OF_MONTH, dayOfMonth);

        subGoalStartDate = calendarStart.getTime();
        mTextViewEditStartDate.setText( dateToString( subGoalStartDate ) );

        if ( subGoalEndDate != null )  {
            if ( ! subGoalStartDate.before(subGoalEndDate) ) {
                subGoalEndDate = null;
                mTextViewEditEndDate.setText(null);
            }
        }
    }

    private void process_Date_End (int year, int month, int dayOfMonth)   {
        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.set (Calendar.YEAR, year);
        calendarEnd.set (Calendar.MONTH, month);
        calendarEnd.set (Calendar.DAY_OF_MONTH, dayOfMonth);

        if ( subGoalStartDate != null )  {
            if ( subGoalStartDate.before( calendarEnd.getTime() ) ) {
                subGoalEndDate = calendarEnd.getTime();
                mTextViewEditEndDate.setText( dateToString( subGoalEndDate ) );
            }
        }
    }

    private void editTheSubGoal()   {
        mProgressDialog.setMessage("Updating...");
        mProgressDialog.show();

        String titleVal = mTextViewEditGoalLabel.getText().toString().trim();

        if ( getApplicationContext() != null)  {
            if (!TextUtils.isEmpty( titleVal ) &&
                    subGoalStartDate != null &&
                    subGoalEndDate != null ) {

                if (this.areDatesValid()) {
                    if (!this.isSubGoalTitleDuplicate(titleVal)) {
                        // start uploading ... new goal to server database
//                        Log.d(__GLOBAL_Const.TAG_handleUserfb, "DATE start  :   " + dateToString( goalStartDate ));
//                        Log.d(__GLOBAL_Const.TAG_handleUserfb, "DATE end    :   " + dateToString( goalEndDate ));
                        this.test_have_subgoal_date_updated_properly();
                        ((DataList) getApplicationContext()).getListOngoing().get( this.goalPosition ).getgListSubGoals().get( this.suboalPosition ).setsLabel( titleVal );
                        ((DataList) getApplicationContext()).getListOngoing().get( this.goalPosition ).getgListSubGoals().get( this.suboalPosition ).setsDateStart( this.subGoalStartDate );
                        ((DataList) getApplicationContext()).getListOngoing().get( this.goalPosition ).getgListSubGoals().get( this.suboalPosition ).setsDateEnd( this.subGoalEndDate );
                        ((DataList) getApplicationContext()).setNeedsUpdateToFire( true );
                        ((DataList) getApplicationContext()).setDateLastUpdate( Calendar.getInstance().getTime().getTime() );
                        this.update_subgoals_dates();
                        this.test_have_subgoal_date_updated_properly();

                        // Go back to previous screen "GOAL" and send the new goal there, to be added to the Data List object
                        //Intent intentBackTo_GoalsScr = new Intent(ActivityGoalAdd.this, ActivityGoals.class);
                        mProgressDialog.dismiss();
                        //startActivity (intentBackTo_GoalsScr);
                        finish();
                    } else {
                        this.showToast("This Sub-Goal Exists Already !");
                    }
                }
            }
            else
                this.showToast("Complete All Fields !");
        }
    }

    private void showToast (String toastMessage) {
        mProgressDialog.dismiss();
        Toast.makeText(ActivitySubGoalsEdit.this, toastMessage, Toast.LENGTH_LONG).show();
    }

    private boolean isSubGoalTitleDuplicate (String title)    {

        boolean foundSomething = false;
        for ( int i=0; i < ((DataList) getApplicationContext()).getListOngoing().get(this.goalPosition).getgListSubGoals().size(); i++ )   {
            if ( i != this.suboalPosition &&
                 title.equalsIgnoreCase( ((DataList) getApplicationContext()).getListOngoing().get(this.goalPosition).getgListSubGoals().get(i).getsLabel() ))    {
                foundSomething = true;
                break;
            }
        }
        return foundSomething;
    }

    private boolean areDatesValid ()    {
        if ( subGoalStartDate.before (subGoalEndDate) )
            return true;
        else
            return false;
    }

    private String dateToString (Date date)  {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String strDate = "XXXXXXXXXX";
        if (date != null)
            strDate = dateFormat.format(date);
        return strDate;
    }

    private void update_subgoals_dates () {
        int num_subgoals = ((DataList) getApplicationContext()).getListOngoing().get( this.goalPosition ).getgListSubGoals().size();
        SubGoal subGoal_current;
        Goal goal_current = ((DataList) getApplicationContext()).getListOngoing().get( this.goalPosition );

        for (int i=0; i < num_subgoals; i++)    {
            subGoal_current = ((DataList) getApplicationContext()).getListOngoing().get( this.goalPosition ).getgListSubGoals().get( i );

            if( (subGoal_current.getsDateStart() == null) || goal_current.getgDateStart().after( subGoal_current.getsDateStart() )      ) {
                Log.d( __GLOBAL_Const.TAG_handleUserfb, "UPDATE:    "+ dateToString( goal_current.getgDateStart() ));
                ((DataList) getApplicationContext()).getListOngoing().get(this.goalPosition).getgListSubGoals().get(i).setsDateStart(goal_current.getgDateStart());

                if(     ((DataList) getApplicationContext()).getListOngoing().get(this.goalPosition).getgListSubGoals().get(i).getsDateStart().after(
                        ((DataList) getApplicationContext()).getListOngoing().get(this.goalPosition).getgListSubGoals().get(i).getsDateEnd() ) )    {

                    ((DataList) getApplicationContext()).getListOngoing().get(this.goalPosition).getgListSubGoals().get(i).setsDateEnd( null );
                }
            }
        }
    }

    private void test_have_subgoal_date_updated_properly()  {
        int num_subgoals = ((DataList) getApplicationContext()).getListOngoing().get( this.goalPosition ).getgListSubGoals().size();
        StringBuilder str = new StringBuilder();
        str.append("\n\n");
        str.append("GOAL <"+this.goalPosition+">    "+ ((DataList) getApplicationContext()).getListOngoing().get( this.goalPosition ).getgLabel() +" has these subgoals: \n");


        str.append(  "\t\t\t\t\t\t\t\tSTART: " + dateToString(    ((DataList) getApplicationContext()).getListOngoing().get( this.goalPosition ).getgDateStart()    ) +
                                "\t     END: " + dateToString(    ((DataList) getApplicationContext()).getListOngoing().get( this.goalPosition ).getgDateEnd()    ) +  "\n");


        for (int i=0; i < num_subgoals; i++)    {
            str.append("\t\tSUB-goal <"+ i +">  "+ ((DataList) getApplicationContext()).getListOngoing().get( this.goalPosition ).getgListSubGoals().get(i).getsLabel() +
                    "\t\t START: "+dateToString(    ((DataList) getApplicationContext()).getListOngoing().get( this.goalPosition ).getgListSubGoals().get(i).getsDateStart()    ) +
                    "\t     END: "+dateToString(    ((DataList) getApplicationContext()).getListOngoing().get( this.goalPosition ).getgListSubGoals().get(i).getsDateEnd()    ) +
                    "\n");
        }
        Log.d(__GLOBAL_Const.TAG_handleUserfb, str.toString());
    }


}
