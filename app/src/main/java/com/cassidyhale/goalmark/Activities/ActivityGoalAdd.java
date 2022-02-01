package com.cassidyhale.goalmark.Activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.cassidyhale.goalmark.Adapter.AdapterSpinnerCustom;
import com.cassidyhale.goalmark.Model.DataList;
import com.cassidyhale.goalmark.Model.Goal;
import com.cassidyhale.goalmark.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class ActivityGoalAdd extends AppCompatActivity implements FragmentColor.NoticeDialogListenerForColor {

    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mDatabase;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private String tag_frag_color_select ="fragment_color_picker";

    private TextView mTextViewGoalLabel;
    private Button mButtonAddNewGoal;
    private ProgressDialog mProgressDialog;
    private TextView mTextViewSelStartDate;
    private TextView mTextViewSelEndDate;
    private Button mButtonSetStartData;
    private Button mButtonSetEndDate;
    private Button mButtonPickColor;
    private Button mButtonShowColor;

    private Spinner mSpinnerSelectGoalCategory;
    private AdapterSpinnerCustom mAdapterSpinnerCustom;
    private Calendar mCalendar;
    private DatePickerDialog mDatePickerDialogStart;
    private DatePickerDialog mDatePickerDialogEnd;

    private Date goalStartDate;
    private Date goalEndDate;
    private int goalColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goal);
        this.goalColor = R.color.color_goal_white;

       // this.initialiazeFirebaseFunctionality();
        this.initialize_views();
    }

    private void initialize_views ()    {
        // -----------------------------------------------------------------------------------------
        // ----------- TOOLBAR setup ---------------------------------------------------------------
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbarAddGoal);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle (R.string.string_Toolbar_Title_Goal_Add);
        // -----------------------------------------------------------------------------------------
        // ----------- SET start / end DATES for NEW GOAL ------------------------------------------
        mCalendar = Calendar.getInstance();
        mTextViewSelStartDate   = (TextView) findViewById(R.id.textViewEndDatePicked);
        mTextViewSelStartDate.setText( this.dateToString(mCalendar.getTime()) );
        mTextViewSelEndDate     = (TextView) findViewById(R.id.textViewAddGEndDatePick);

        mButtonSetStartData     = (Button)  findViewById(R.id.buttonSetGoalStartDate);
        mButtonSetEndDate       = (Button)  findViewById(R.id.buttonSetGoalEndDate);
        mButtonShowColor        = (Button)  findViewById(R.id.viewSelectedColor);
        mButtonShowColor.setBackgroundTintList( getResources().getColorStateList( this.goalColor) );
        mButtonPickColor        = (Button)  findViewById(R.id.buttonSetGoalPickColor);


        mButtonPickColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new FragmentColor();
                newFragment.show(getSupportFragmentManager(), tag_frag_color_select);
            }
        });

        this.initialize_date_pickers();

        // -----------------------------------------------------------------------------------------
        // ----------- ADD the NEW GOAL ------------------------------------------------------------
        mProgressDialog = new ProgressDialog(this);
        mTextViewGoalLabel = (TextView) findViewById(R.id.textViewAddSubgoalTitle);
        mButtonAddNewGoal = (Button) findViewById(R.id.buttonAddNewGoal);
        mButtonAddNewGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTheGoal();
            }
        });

        mAdapterSpinnerCustom = new AdapterSpinnerCustom(this, __GLOBAL_Const.goal_category_drawable,
                __GLOBAL_Const.goal_category_string);
        mSpinnerSelectGoalCategory = findViewById(R.id.spinnerSelectGoalCategory);
        mSpinnerSelectGoalCategory.setAdapter(mAdapterSpinnerCustom);
        mSpinnerSelectGoalCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {  }
        });
    }

    private void initialiazeFirebaseFunctionality () {
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference().child(mUser.getUid())
                                                    .child( __GLOBAL_Const.child__Datalist__)
                                                    .child( __GLOBAL_Const.child__gListOngoing);
        mDatabaseReference.keepSynced(true);
    }

    private void initialize_date_pickers () {
        int thisDay = mCalendar.get(Calendar.DAY_OF_MONTH);
        int thisMonth = mCalendar.get(Calendar.MONTH);
        int thisYear = mCalendar.get(Calendar.YEAR);
        goalStartDate = new GregorianCalendar(thisYear, thisMonth, thisDay).getTime();


        mButtonSetStartData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendarStartShow = Calendar.getInstance();

//                if (goalStartDate != null && goalEndDate != null)
//                    calendarStartShow.setTime( goalEndDate );
                if (goalStartDate != null )
                    calendarStartShow.setTime( goalStartDate );


                mDatePickerDialogStart = new DatePickerDialog(ActivityGoalAdd.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        process_Date_Start(year, month, dayOfMonth);
                    }
                }, calendarStartShow.get (Calendar.YEAR), calendarStartShow.get (Calendar.MONTH), calendarStartShow.get (Calendar.DAY_OF_MONTH));
                // ---------------------------

                mDatePickerDialogStart.getDatePicker().setMinDate( Calendar.getInstance().getTime().getTime());
                if ( goalStartDate != null )
                    mDatePickerDialogStart.updateDate( calendarStartShow.get(Calendar.YEAR),
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
                Calendar calendarStartingDisable = Calendar.getInstance();

                if ( goalStartDate != null)
                    calendarStartingDisable.setTime ( goalStartDate );

                if ( goalStartDate != null && goalEndDate != null)
                    calendarEndShow.setTime ( goalEndDate );
                else if ( goalStartDate != null && goalEndDate == null)
                    calendarEndShow.setTime ( goalStartDate );

                mDatePickerDialogEnd = new DatePickerDialog(ActivityGoalAdd.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet (DatePicker view, int year, int month, int dayOfMonth) {
                        process_Date_End( year, month, dayOfMonth);
                    }
                }, calendarEndShow.get (Calendar.YEAR), calendarEndShow.get (Calendar.MONTH), calendarEndShow.get (Calendar.DAY_OF_MONTH));

                mDatePickerDialogEnd.getDatePicker().setMinDate( calendarStartingDisable.getTime().getTime() + 86_400_000);
                if ( goalEndDate != null )
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

        goalStartDate = calendarStart.getTime();
        mTextViewSelStartDate.setText( dateToString( goalStartDate ) );

        if ( goalEndDate != null )  {
            if ( ! goalStartDate.before(goalEndDate) ) {
                goalEndDate = null;
                mTextViewSelEndDate.setText(null);
            }
        }
    }

    private void process_Date_End (int year, int month, int dayOfMonth)   {
        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.set (Calendar.YEAR, year);
        calendarEnd.set (Calendar.MONTH, month);
        calendarEnd.set (Calendar.DAY_OF_MONTH, dayOfMonth);

        if ( goalStartDate != null )  {
            if ( goalStartDate.before( calendarEnd.getTime() ) ) {
                goalEndDate = calendarEnd.getTime();
                mTextViewSelEndDate.setText( dateToString( goalEndDate ) );
            }
        }
    }

    private void addTheGoal()   {
        mProgressDialog.setMessage("Adding...");
        mProgressDialog.show();

        String titleVal = mTextViewGoalLabel.getText().toString().trim();

        if ((DataList) getApplicationContext() != null)  {
            if (!TextUtils.isEmpty( titleVal ) &&
                goalStartDate != null &&
                goalEndDate != null ) {

                if (this.areDatesValid()) {
                    if (!this.isGoalTitleDuplicate(titleVal)) {
                        // start uploading ... new goal to server database
                        Goal newGoal = new Goal(titleVal, goalStartDate, goalEndDate, goalColor, (String) mSpinnerSelectGoalCategory.getSelectedItem());

                        ((DataList) getApplicationContext()).getListOngoing().add(newGoal);
                        ((DataList) getApplicationContext()).setNeedsUpdateToFire(true);
                        ((DataList) getApplicationContext()).setDateLastUpdate( Calendar.getInstance().getTime().getTime() );

                        // Go back to previous screen "GOAL" and send the new goal there, to be added to the Data List object
                        //Intent intentBackTo_GoalsScr = new Intent(ActivityGoalAdd.this, ActivityGoals.class);
                        mProgressDialog.dismiss();
                        //startActivity (intentBackTo_GoalsScr);
                        finish();
                    } else {
                        this.showToast("This Goal Exists Already !");
                    }
                }
            }
            else
                this.showToast("Complete All Fields !");
        }
    }

    private void showToast (String toastMessage) {
        mProgressDialog.dismiss();
        Toast.makeText(ActivityGoalAdd.this, toastMessage, Toast.LENGTH_LONG).show();
    }

    private boolean isGoalTitleDuplicate (String title)    {

        boolean foundSomething = false;
        for (Goal goal : ((DataList) getApplicationContext()).getListOngoing() )   {
            if ( title.equalsIgnoreCase( goal.getgLabel() )) {
                foundSomething = true;
                break;
            }
        }
        return foundSomething;
    }

    private boolean areDatesValid ()    {
        if ( goalStartDate.before (goalEndDate) )
            return true;
        else
            return false;
    }

    private String dateToString (Date date)  {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String strDate = dateFormat.format(date);
        return strDate;
    }

    @Override
    public void onClickAddNewTask(DialogFragment dialog, int colorSelected) {
        ((DialogFragment) getSupportFragmentManager().findFragmentByTag( tag_frag_color_select )).dismiss();
        if ( colorSelected != 0) {
            this.goalColor = colorSelected;
            mButtonShowColor.setBackgroundTintList( getResources().getColorStateList( this.goalColor ));
        }
    }

    @Override
    public void onClickCancelAddNewTask(DialogFragment dialog) {
        ((DialogFragment) getSupportFragmentManager().findFragmentByTag( tag_frag_color_select )).dismiss();

    }
}
