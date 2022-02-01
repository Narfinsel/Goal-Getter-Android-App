package com.cassidyhale.goalmark.Activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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


public class ActivitySubGoalAdd extends AppCompatActivity {

    private TextView mTextView_SubGoal_Label;
    private TextView mTextView_SubGoal_SelStartDate;
    private TextView mTextView_SubGoal_SelEndDate;

    private ProgressDialog mProgressDialog;
    private DatePickerDialog mDatePickerDialog;
    private DatePickerDialog mDatePickerDialogStart;
    private DatePickerDialog mDatePickerDialogEnd;
    private Button mButtonSetStartData;
    private Button mButtonSetEndDate;
    private Button mButtonAddNewSubGoal;

    private Calendar mCalendar;
    private Date subGoalStartDate;
    private Date subGoalEndDate;
    private Date dateGoalStart;
    private Date dateGoalEnd;

    private int positionGoal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subgoal);

        if (getIntent() != null)
            positionGoal =  getIntent().getIntExtra("positionGoal", 0);

        this.initialize_views();
    }                                   // WORKS !!!

    private void initialize_views ()    {

        mCalendar = Calendar.getInstance();
        dateGoalStart = ((DataList) (getApplicationContext())).listOngoing.get(positionGoal).getgDateStart();
        dateGoalEnd = ((DataList) (getApplicationContext())).listOngoing.get(positionGoal).getgDateEnd();

        // -----------------------------------------------------------------------------------------
        // ----------- TOOLBAR setup ---------------------------------------------------------------
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbarAddSubGoal);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle (R.string.string_Toolbar_Title_SubGoal_Add);

        // -----------------------------------------------------------------------------------------
        // ----------- SET start / end DATES for NEW GOAL ------------------------------------------
        mTextView_SubGoal_SelStartDate  =   findViewById(R.id.textViewAddSubgoalStartDate);
        mTextView_SubGoal_SelStartDate.setText( this.dateToString( dateGoalStart ));
        mTextView_SubGoal_SelEndDate    =   findViewById(R.id.textViewAddSubgoalEndDate);
        mButtonSetStartData    =    findViewById(R.id.buttonSetSubGoalStartDate);
        mButtonSetEndDate      =    findViewById(R.id.buttonSetSubGoalEnDate);

        this.initialize_date_pickers();

        // -----------------------------------------------------------------------------------------
        // ----------- ADD the NEW SUB-GOAL --------------------------------------------------------
        mProgressDialog         = new ProgressDialog(this);
        mTextView_SubGoal_Label = (TextView) findViewById(R.id.textViewAddSubgoalTitle);
        mButtonAddNewSubGoal    = (Button) findViewById(R.id.buttonAddNewSubGoal);
        mButtonAddNewSubGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTheSubGoal();
            }
        });
    }                                                   // WORKS !!!

    private void initialize_date_pickers () {
        int thisDay = mCalendar.get(Calendar.DAY_OF_MONTH);
        int thisMonth = mCalendar.get(Calendar.MONTH);
        int thisYear = mCalendar.get(Calendar.YEAR);

        subGoalStartDate = new GregorianCalendar(thisYear, thisMonth, thisDay).getTime();
        subGoalStartDate.setTime( dateGoalStart.getTime() );

        mButtonSetStartData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendarStartShow = Calendar.getInstance();

                if (subGoalStartDate != null )
                    calendarStartShow.setTime(subGoalStartDate);

                mDatePickerDialogStart = new DatePickerDialog(ActivitySubGoalAdd.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        process_Date_Start(year, month, dayOfMonth);
                    }
                }, calendarStartShow.get(Calendar.YEAR), calendarStartShow.get(Calendar.MONTH), calendarStartShow.get(Calendar.DAY_OF_MONTH));
                // ---------------------------

                mDatePickerDialogStart.getDatePicker().setMinDate( dateGoalStart.getTime());
                mDatePickerDialogStart.getDatePicker().setMaxDate( dateGoalEnd.getTime() - 86_400_000 );
                if ( subGoalStartDate != null )
                    mDatePickerDialogStart.updateDate(
                            calendarStartShow.get(Calendar.YEAR),
                            calendarStartShow.get(Calendar.MONTH),
                            calendarStartShow.get(Calendar.DAY_OF_MONTH) );
                mDatePickerDialogStart.show();
            }
        });


        mButtonSetEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // show dialog picker and chose end date
                // At least one day (86400000 ms) should be between start date and end date

                Calendar calendarEndShow = Calendar.getInstance();
                Calendar calendarDisabledDatesRange = Calendar.getInstance();

                if ( subGoalStartDate != null)
                    calendarDisabledDatesRange.setTime (subGoalStartDate);

                if ( subGoalStartDate != null && subGoalEndDate != null)
                    calendarEndShow.setTime (subGoalEndDate);
                else if ( subGoalStartDate != null && subGoalEndDate == null)
                    calendarEndShow.setTime (subGoalStartDate);

                mDatePickerDialogEnd = new DatePickerDialog(ActivitySubGoalAdd.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet (DatePicker view, int year, int month, int dayOfMonth) {
                        process_Date_End( year, month, dayOfMonth);
                    }
                }, calendarEndShow.get (Calendar.YEAR), calendarEndShow.get (Calendar.MONTH), calendarEndShow.get (Calendar.DAY_OF_MONTH));

                mDatePickerDialogEnd.getDatePicker().setMinDate( calendarDisabledDatesRange.getTime().getTime() + 86_400_000);
                mDatePickerDialogEnd.getDatePicker().setMaxDate( dateGoalEnd.getTime() );
                if ( subGoalEndDate != null )
                    mDatePickerDialogEnd.updateDate( calendarEndShow.get(Calendar.YEAR),
                            calendarEndShow.get(Calendar.MONTH),
                            calendarEndShow.get(Calendar.DAY_OF_MONTH) );
                mDatePickerDialogEnd.show();
            }
        });
    }                                               // WORKS !!!

    private void process_Date_Start (int year, int month, int dayOfMonth)   {
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.set (Calendar.YEAR, year);
        calendarStart.set (Calendar.MONTH, month);
        calendarStart.set (Calendar.DAY_OF_MONTH, dayOfMonth);

        subGoalStartDate = calendarStart.getTime();
        mTextView_SubGoal_SelStartDate.setText( dateToString(subGoalStartDate) );

        if ( subGoalEndDate != null )  {
            if ( ! subGoalStartDate.before(subGoalEndDate) ) {
                subGoalEndDate = null;
                mTextView_SubGoal_SelEndDate.setText(null);
            }
        }
    }               // WORKS !!!

    private void process_Date_End (int year, int month, int dayOfMonth)   {
        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.set (Calendar.YEAR, year);
        calendarEnd.set (Calendar.MONTH, month);
        calendarEnd.set (Calendar.DAY_OF_MONTH, dayOfMonth);

        if ( subGoalStartDate != null )  {
            if ( subGoalStartDate.before( calendarEnd.getTime() ) ) {
                subGoalEndDate = calendarEnd.getTime();
                mTextView_SubGoal_SelEndDate.setText( dateToString(subGoalEndDate) );
            }
        }
    }                 // WORKS !!!

    private void addTheSubGoal()   {
        mProgressDialog.setMessage("Adding...");
        mProgressDialog.show();

        String titleVal = mTextView_SubGoal_Label.getText().toString().trim();

        if ((DataList) getApplicationContext() != null)  {
            if (!TextUtils.isEmpty( titleVal ) &&
                    subGoalStartDate != null &&
                    subGoalEndDate != null ) {

                if (this.areDatesValid()) {
                    if (!this.isSubGoalTitleDuplicate( titleVal )) {
                        // start uploading ... new goal to server database
                        SubGoal newSubGoal = new SubGoal(titleVal, subGoalStartDate, subGoalEndDate);

                        ((DataList) getApplicationContext()).getListOngoing().get(positionGoal).getgListSubGoals().add( newSubGoal );
                        ((DataList) getApplicationContext()).setNeedsUpdateToFire(true);
                        ((DataList) getApplicationContext()).setDateLastUpdate( Calendar.getInstance().getTime().getTime() );

                        // Go back to previous screen "GOAL" and send the new goal there, to be added to the Data List object
                        //Intent intentBackTo_GoalsScr = new Intent(ActivitySubGoalAdd.this, ActivitySubGoals.class);
                        Intent intentBackTo_GoalsScr = new Intent(ActivitySubGoalAdd.this, ActivitySubGoals.class);
                        mProgressDialog.dismiss();
                        //startActivity (intentBackTo_GoalsScr);
                        finish();
                    } else {
                        this.showToast("Subgoal already Exists !");
                    }
                }
            }
            else
                this.showToast("Fill all Fields !");
        }
    }                                                        // WORKS !!!

    private boolean isSubGoalTitleDuplicate (String title)    {

        boolean foundSomething = false;
        Goal goal =  ((DataList) getApplicationContext()).getListOngoing().get(positionGoal);
        for (SubGoal subGoal : goal.getgListSubGoals() )   {
            if ( title.equalsIgnoreCase( subGoal.getsLabel() )) {
                foundSomething = true;
                break;
            }
        }
        return foundSomething;
    }                               // WORKS !!!

    private void showToast (String toastMessage) {
        mProgressDialog.dismiss();
        Toast.makeText(ActivitySubGoalAdd.this, toastMessage, Toast.LENGTH_LONG).show();
    }                                           // WORKS !!!

    private String dateToString (Date date)  {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String strDate = dateFormat.format(date);
        return strDate;
    }                                               // WORKS !!!

    private boolean areDatesValid ()    {
        if ( subGoalStartDate.before (subGoalEndDate) )
            return true;
        else
            return false;
    }                                                   // WORKS !!!

}
