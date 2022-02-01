package com.cassidyhale.goalmark.Activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cassidyhale.goalmark.Adapter.AdapterSpinnerCustom;
import com.cassidyhale.goalmark.Model.DataList;
import com.cassidyhale.goalmark.Model.Goal;
import com.cassidyhale.goalmark.Model.SubGoal;
import com.cassidyhale.goalmark.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ActivityGoalsEdit extends AppCompatActivity implements FragmentColor.NoticeDialogListenerForColor {

    private String tag_frag_color_select ="fragment_color_picker";

    private TextView mTextViewEditGoalLabel;
    private Button mButtonSaveChangesGoal;
    private ProgressDialog mProgressDialog;
    private TextView mTextViewEditStartDate;
    private TextView mTextViewEditEndDate;
    private Button mButtonEditStartDate;
    private Button mButtonEditEndDate;
    private Button mButtonEditChangeColor;
    private Button mButtonShowEditedColor;

    private Spinner mSpinnerEditGoalCategory;
    private AdapterSpinnerCustom mAdapterSpinnerCustom;
    private Calendar mCalendar;
    private DatePickerDialog mDatePickerDialogStart;
    private DatePickerDialog mDatePickerDialogEnd;

    private int goalPosition;
    private Goal goalEdited;

    private Date goalStartDate;
    private Date goalEndDate;
    private int goalColor;
    private Date goalOldStartDate;
    private Date goalOldEndDate;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_goal);

        this.goalPosition = getIntent().getIntExtra("positionGoal", __GLOBAL_Const.default_edit_goal_position);
        this.goalEdited = ((DataList) getApplicationContext()).getListOngoing().get( this.goalPosition );
        this.goalStartDate = this.goalEdited.getgDateStart();
        this.goalEndDate = this.goalEdited.getgDateEnd();
        this.goalColor = this.goalEdited.getGoalColor();

        this.goalOldStartDate = this.goalEdited.getgDateStart();
        this.goalOldEndDate = this.goalEdited.getgDateEnd();

        this.initialize_views();
        this.initialize_value_fields_for_goal();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //this.test_have_subgoal_date_updated_properly();
    }

    private void initialize_views ()    {
        // -----------------------------------------------------------------------------------------
        // ----------- TOOLBAR setup ---------------------------------------------------------------
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbarEditGoal);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle (R.string.string_Toolbar_Title_Goal_Edit);
        // -----------------------------------------------------------------------------------------
        // ----------- SET start / end DATES for NEW GOAL ------------------------------------------
        mCalendar = Calendar.getInstance();
        mTextViewEditStartDate  = (TextView) findViewById(R.id.textViewEditGStartDatePicked);
        mTextViewEditEndDate    = (TextView) findViewById(R.id.textViewEditGEndDatePicked);

        mButtonEditStartDate    = (Button)  findViewById(R.id.buttonEdtGoalStartDate);
        mButtonEditEndDate      = (Button)  findViewById(R.id.buttonEditGoalEndDate);

        mButtonEditChangeColor  = (Button)  findViewById(R.id.buttonEditGoalPickColor);
        mButtonEditChangeColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new FragmentColor();
                newFragment.show(getSupportFragmentManager(), tag_frag_color_select);
            }
        });
        mButtonShowEditedColor  = (Button)  findViewById(R.id.viewEditSelectedColor);

        this.initialize_date_pickers();

        // -----------------------------------------------------------------------------------------
        // ----------- EDIT the GOAL ---------------------------------------------------------------
        mProgressDialog = new ProgressDialog(this);
        mTextViewEditGoalLabel = (TextView) findViewById(R.id.textViewEditGoalTitle);
        mButtonSaveChangesGoal = (Button) findViewById(R.id.buttonSaveChangesGoal);
        mButtonSaveChangesGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTheGoal();
            }
        });

        mAdapterSpinnerCustom = new AdapterSpinnerCustom(this, __GLOBAL_Const.goal_category_drawable,
                __GLOBAL_Const.goal_category_string);
        mSpinnerEditGoalCategory = findViewById(R.id.spinnerEditGoalCategory);
        mSpinnerEditGoalCategory.setAdapter(mAdapterSpinnerCustom);
        mSpinnerEditGoalCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {  }
        });
    }

    private void initialize_value_fields_for_goal ()    {
        this.mTextViewEditGoalLabel.setText( this.goalEdited.getgLabel() );
        this.mTextViewEditStartDate.setText( dateToString( this.goalEdited.getgDateStart() ));
        this.mTextViewEditEndDate.setText( dateToString( this.goalEdited.getgDateEnd() ));
        this.mButtonShowEditedColor.setBackgroundTintList( getResources().getColorStateList( this.goalColor ));

        // SET Category Apropriatly
        int categoryPosition = __GLOBAL_Const.default_edit_goal_position;
        for (int i=0; i < __GLOBAL_Const.goal_category_string.length; i++)
            if (  this.goalEdited.getgCathegory().equals( __GLOBAL_Const.goal_category_string[i] )   )
                categoryPosition = i;
        this.mSpinnerEditGoalCategory.setSelection( categoryPosition );
    }

    private void initialize_date_pickers () {
        int thisDay = mCalendar.get(Calendar.DAY_OF_MONTH);
        int thisMonth = mCalendar.get(Calendar.MONTH);
        int thisYear = mCalendar.get(Calendar.YEAR);
        //goalStartDate = new GregorianCalendar(thisYear, thisMonth, thisDay).getTime();

        mButtonEditStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendarStartShow = Calendar.getInstance();

                if (goalStartDate != null )
                    calendarStartShow.setTime( goalStartDate );


                mDatePickerDialogStart = new DatePickerDialog(ActivityGoalsEdit.this, new DatePickerDialog.OnDateSetListener() {
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

        mButtonEditEndDate.setOnClickListener(new View.OnClickListener() {
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

                mDatePickerDialogEnd = new DatePickerDialog(ActivityGoalsEdit.this, new DatePickerDialog.OnDateSetListener() {
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
        mTextViewEditStartDate.setText( dateToString( goalStartDate ) );

        if ( goalEndDate != null )  {
            if ( ! goalStartDate.before(goalEndDate) ) {
                goalEndDate = null;
                mTextViewEditEndDate.setText(null);
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
                mTextViewEditEndDate.setText( dateToString( goalEndDate ) );
            }
        }
    }

    private void editTheGoal()   {
        mProgressDialog.setMessage("Updating...");
        mProgressDialog.show();

        String titleVal = mTextViewEditGoalLabel.getText().toString().trim();

        if ( getApplicationContext() != null)  {
            if (!TextUtils.isEmpty( titleVal ) &&
                    goalStartDate != null &&
                    goalEndDate != null ) {

                if (this.areDatesValid()) {
                    if (!this.isGoalTitleDuplicate(titleVal)) {
                        // start uploading ... new goal to server database

                        ((DataList) getApplicationContext()).getListOngoing().get( this.goalPosition ).setgLabel( titleVal );
                        ((DataList) getApplicationContext()).getListOngoing().get( this.goalPosition ).setgDateStart( this.goalStartDate );
                        ((DataList) getApplicationContext()).getListOngoing().get( this.goalPosition ).setgDateEnd( this.goalEndDate );
                        ((DataList) getApplicationContext()).getListOngoing().get( this.goalPosition ).setGoalColor( this.goalColor );
                        String selectedCategory = ((DataList) getApplicationContext()).getListOngoing().get( this.goalPosition ).getgCathegory();
                        ((DataList) getApplicationContext()).getListOngoing().get( this.goalPosition ).setgCathegory( (String) mSpinnerEditGoalCategory.getSelectedItem() );
                        if ( selectedCategory != ((String) mSpinnerEditGoalCategory.getSelectedItem() ) ) {
                            ((DataList) getApplicationContext()).getListOngoing().get(this.goalPosition).setResourceBackroundId();
                        }
                        ((DataList) getApplicationContext()).setNeedsUpdateToFire( true );
                        ((DataList) getApplicationContext()).setDateLastUpdate( Calendar.getInstance().getTime().getTime() );
                        this.update_subgoals_start_and__dates();

                        //this.test_have_subgoal_date_updated_properly();

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
        Toast.makeText(ActivityGoalsEdit.this, toastMessage, Toast.LENGTH_LONG).show();
    }

    private boolean isGoalTitleDuplicate (String title)    {

        boolean foundSomething = false;
        for ( int i=0; i < ((DataList) getApplicationContext()).getListOngoing().size(); i++ )   {
            if ( i != this.goalPosition &&
                    title.equalsIgnoreCase( ((DataList) getApplicationContext()).getListOngoing().get(i).getgLabel() )) {
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
        String strDate;
        if (date != null)
            strDate = dateFormat.format(date);
        else
            strDate = "XXXXXX";
        return strDate;
    }

    private void update_subgoals_start_and__dates () {
        int num_subgoals = ((DataList) getApplicationContext()).getListOngoing().get( this.goalPosition ).getgListSubGoals().size();
        SubGoal subGoal_current;
        Goal goal_current = ((DataList) getApplicationContext()).getListOngoing().get( this.goalPosition );

        for (int i=0; i < num_subgoals; i++)    {
            subGoal_current = ((DataList) getApplicationContext()).getListOngoing().get( this.goalPosition ).getgListSubGoals().get( i );

            if( (subGoal_current.getsDateStart() == null) || goal_current.getgDateStart().after( subGoal_current.getsDateStart() )      ) {

                // Log.d( __GLOBAL_Const.TAG_handleUserfb, "\n\t\tUPDATE:    "+ dateToString( goal_current.getgDateStart() ) + "     AFTER   " + dateToString( subGoal_current.getsDateStart() )    );
                ((DataList) getApplicationContext()).getListOngoing().get(this.goalPosition).getgListSubGoals().get(i).setsDateStart(goal_current.getgDateStart());

                if(     ((DataList) getApplicationContext()).getListOngoing().get(this.goalPosition).getgListSubGoals().get(i).getsDateStart().after(
                        ((DataList) getApplicationContext()).getListOngoing().get(this.goalPosition).getgListSubGoals().get(i).getsDateEnd() ) )    {

                    Date newSubGoalEndDate = new Date();
                    newSubGoalEndDate.setTime( goal_current.getgDateEnd().getTime() );
                    ((DataList) getApplicationContext()).getListOngoing().get(this.goalPosition).getgListSubGoals().get(i).setsDateEnd( newSubGoalEndDate );
                }
            }

            if( (subGoal_current.getsDateEnd() == null) || goal_current.getgDateEnd().before( subGoal_current.getsDateEnd() )      ) {

                // Log.d( __GLOBAL_Const.TAG_handleUserfb, "\n\t\tUPDATE:    "+ dateToString( goal_current.getgDateEnd() ) + "     BEFORE   " + dateToString( subGoal_current.getsDateEnd() )    );
                ((DataList) getApplicationContext()).getListOngoing().get(this.goalPosition).getgListSubGoals().get(i).setsDateEnd (goal_current.getgDateEnd());

                if(     ((DataList) getApplicationContext()).getListOngoing().get(this.goalPosition).getgListSubGoals().get(i).getsDateStart().after(
                        ((DataList) getApplicationContext()).getListOngoing().get(this.goalPosition).getgListSubGoals().get(i).getsDateEnd() ) )    {

                    Date newSubGoalStartDate = new Date();
                    newSubGoalStartDate.setTime( goal_current.getgDateStart().getTime() );
                    ((DataList) getApplicationContext()).getListOngoing().get(this.goalPosition).getgListSubGoals().get(i).setsDateStart( newSubGoalStartDate );
                }
            }
        }
    }

    private void test_have_subgoal_date_updated_properly()  {
        int num_subgoals = ((DataList) getApplicationContext()).getListOngoing().get( this.goalPosition ).getgListSubGoals().size();
        StringBuilder str = new StringBuilder();
        str.append("\n\n");
        str.append("\nGOAL <"+this.goalPosition+">    "+ ((DataList) getApplicationContext()).getListOngoing().get( this.goalPosition ).getgLabel() +" has these subgoals: \n");

        str.append(  "\t\t\t\t\t\t\t\tSTART: "+dateToString(    ((DataList) getApplicationContext()).getListOngoing().get( this.goalPosition ).getgDateStart()    ) +
                        "\t     END: "+dateToString(    ((DataList) getApplicationContext()).getListOngoing().get( this.goalPosition ).getgDateEnd()    ) +
                        "\n");

        for (int i=0; i < num_subgoals; i++)    {
            str.append("\t\tSUB-goal <"+ i +">  "+ ((DataList) getApplicationContext()).getListOngoing().get( this.goalPosition ).getgListSubGoals().get(i).getsLabel() +
                    "\t\t START: "+dateToString(    ((DataList) getApplicationContext()).getListOngoing().get( this.goalPosition ).getgListSubGoals().get(i).getsDateStart()    ) +
                    "\t     END: "+dateToString(    ((DataList) getApplicationContext()).getListOngoing().get( this.goalPosition ).getgListSubGoals().get(i).getsDateEnd()    ) +
                    "\n");
        }
        Log.d(__GLOBAL_Const.TAG_handleUserfb, str.toString());
    }

    @Override
    public void onClickAddNewTask(DialogFragment dialog, int colorSelected) {
        ((DialogFragment) getSupportFragmentManager().findFragmentByTag( tag_frag_color_select )).dismiss();
        if ( colorSelected != 0) {
            this.goalColor = colorSelected;
            mButtonShowEditedColor.setBackgroundTintList( getResources().getColorStateList( this.goalColor ));
        }
    }

    @Override
    public void onClickCancelAddNewTask(DialogFragment dialog) {
        ((DialogFragment) getSupportFragmentManager().findFragmentByTag( tag_frag_color_select )).dismiss();

    }


}
