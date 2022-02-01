package com.cassidyhale.goalmark.Activities;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cassidyhale.goalmark.Adapter.ListAdapterHourly;
import com.cassidyhale.goalmark.Model.DataList;
import com.cassidyhale.goalmark.Model.DataListNoApp;
import com.cassidyhale.goalmark.Model.ItemScheduled;
import com.cassidyhale.goalmark.Model.Schedule;
import com.cassidyhale.goalmark.Model.Task;
import com.cassidyhale.goalmark.R;
import com.google.gson.Gson;

import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FragmentDay extends Fragment {

    private ListView listViewFragmentHours;
    private TextView textViewFragmentDayTitle;
    private TextView textViewFragmentDayDate;
    private LinearLayout mLinearLayoutForDots;
    private ImageView[] mDotsArrayImageView ;
    private ListAdapterHourly listAdapterHourly;

    private Date dateNow;
    private Date dateDay;
    private int dayNumber;
    private String dayLabel;
    private int startingHour = 6;
    private int incrementHours = 2;
    private int totalHours = 16;
    private int clickedHour;
    int mStackLevel = 0;
    public static final int DIALOG_FRAGMENT = 5;
    private ItemScheduled returnedItemScheduled;
    private ItemScheduled beforeClickedItemScheduled;

    public static final FragmentDay newInstance (int dayNumber, String dayLabel)    {
        FragmentDay fragment = new FragmentDay();
        Bundle bundle = new Bundle();
        bundle.putInt( "dayNumber", dayNumber );
        bundle.putString( "dayLabel", dayLabel );
        fragment.setArguments( bundle );
        return fragment;
    }

    public FragmentDay (){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mStackLevel = savedInstanceState.getInt("level");
        }
        if (getArguments() != null) {
            this.dayNumber = getArguments().getInt("dayNumber");    //  0 - 6
            this.dayLabel = getArguments().getString("dayLabel");
            this.dateDay = this.dateOfDay( this.dayNumber);

            Calendar calendar_row = Calendar.getInstance();
            calendar_row.setTime( this.dateDay );
            calendar_row.set( Calendar.HOUR_OF_DAY, Calendar.getInstance().get( Calendar.HOUR_OF_DAY ));
            this.dateNow = calendar_row.getTime();
        }
        this.listAdapterHourly = new ListAdapterHourly( getContext(), this.dateDay, this.startingHour, this.totalHours, this.incrementHours, (this.dayNumber + 1) );
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("level", mStackLevel);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        //Log.d (__GLOBAL_Const.TAG_handleUserfb, "       LIST-ADAPTER-HOURLY   -     ------------------------------------------------------------------------");
        View v = inflater.inflate( R.layout.scheduler_template_day_of_week, container, false);

        mLinearLayoutForDots        = v.findViewById( R.id.linearLayoutDotsScheduler);
        textViewFragmentDayTitle    = v.findViewById( R.id.textViewScheduleDay );
        textViewFragmentDayDate     = v.findViewById( R.id.textViewScheduleDate );
        textViewFragmentDayTitle.setText( this.dayLabel );
        textViewFragmentDayDate.setText( new SimpleDateFormat("DD MM YYYY").format( this.dateOfDay( this.dayNumber)));
                //this.dateForDay(this.dayNumber) );
        createDots( this.dayNumber );

        listViewFragmentHours = v.findViewById(R.id.listViewDaySchedule);
        listViewFragmentHours.setAdapter(listAdapterHourly);
        getActivity().registerForContextMenu( listViewFragmentHours );
        listViewFragmentHours.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setClickedHour( position );
                beforeClickedItemScheduled = (ItemScheduled) listAdapterHourly.getItem( position);
                //Log.d(__GLOBAL_Const.TAG_handleUserfb, "FRAGMENT DAY --- listWork.OnClickItem --- OLD task  :   " + beforeClickedItemScheduled.displayItemScheduledDetails() );
                view.setSelected(true);
                //Log.d(__GLOBAL_Const.TAG_handleUserfb, "FRAGMENT DAY        --      onItemClick     Day = ("+ dayLabel +")=" + (dayNumber+1) +",  Hour = "+ getClickedHour());
                showDialog(dayNumber);
            }
        });
        listViewFragmentHours.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //Log.d( __GLOBAL_Const.TAG_handleUserfb, " TIME for LONG click   :   ");
                return true;
            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {        // A

        if (resultCode == Activity.RESULT_OK) {
            ItemScheduled item_Scheduled_BACK = data.getParcelableExtra("item_schedule");
            if (item_Scheduled_BACK != null && getContext() != null) {
                this.setReturnedItemScheduled (item_Scheduled_BACK);
                //Log.d(__GLOBAL_Const.TAG_handleUserfb, "FRAGMENT DAY --- onActivityResult --- Returned  in FragmentDay {"+ (this.dayNumber+1) +"} for Hour {"+ this.getClickedHour() +"},     ItemScheduled (from Dialog)" + this.getReturnedItemScheduled().displayItemScheduledDetails());
                Date dateScheduledForAlarm = dateForScheduledTasks( this.getClickedHour());
                this.returnedItemScheduled.setDateScheduled( dateScheduledForAlarm );

                Task task_new = Schedule.taskFromItem( ((DataList) getContext().getApplicationContext()), this.returnedItemScheduled );
                Task task_old = Schedule.taskFromItem( ((DataList) getContext().getApplicationContext()), this.beforeClickedItemScheduled );

//                if (task_old != null)
//                    Log.d(__GLOBAL_Const.TAG_handleUserfb, "FRAGMENT DAY --- onActivityResult --- OLD task  :   "+ task_old.getTaskLabel() );
//                Log.d(__GLOBAL_Const.TAG_handleUserfb, "FRAGMENT DAY --- onActivityResult --- NEW task  :   "+ task_new.getTaskLabel() );

                if ( dateScheduledForAlarm.after( Calendar.getInstance().getTime() ) && task_new !=null ) {
                    ((DataList) getContext().getApplicationContext()).getSchedule().updateSchedule( (this.dayNumber +1),
                                                                                                    this.getClickedHour(),
                                                                                                    this.getReturnedItemScheduled() );
                    startAlarmForNotification (dateScheduledForAlarm, task_old, task_new, dayNumber, this.getClickedHour());
                }
                ((DataList) getContext().getApplicationContext()).setNeedsUpdateToFire(true);
                ((DataList) getContext().getApplicationContext()).setDateLastUpdate( Calendar.getInstance().getTime().getTime() );
                this.listAdapterHourly.notifyDataSetChanged();
            }
            else
                Log.d(__GLOBAL_Const.TAG_handleUserfb, "FRAGMENT DAY --- onActivityResult --- !!!! NULL NULL NULL NULL !!!!");

        } else if (resultCode == Activity.RESULT_CANCELED){
            Log.d(__GLOBAL_Const.TAG_handleUserfb, "FRAGMENT DAY --- onActivityResult --- !!!! ACTIVITY RESULT CANCELLED !!!!");
        }
    }

    void showDialog(int type) {
        mStackLevel++;
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        Fragment prev = getActivity().getSupportFragmentManager().findFragmentByTag("new_frag_schedule_importer");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        DialogFragment dialogFrag = FragmentSchedulerImportTask.newInstance(dayNumber);
        dialogFrag.setTargetFragment(this, this.dayNumber);
        dialogFrag.show(ft, "new_frag_schedule_importer");
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0,0,0,"Delete");
        menu.add(0,1,1,"Share");

        Log.d( __GLOBAL_Const.TAG_handleUserfb, " TIME for LONG click   :   ");
        if ( v.getId() == R.id.listViewDaySchedule) {

            AdapterView.AdapterContextMenuInfo info =(AdapterView.AdapterContextMenuInfo)menuInfo;
            MenuItem mnu1=menu.add(0,0,0,"Delete");
            MenuItem mnu2=menu.add(0,1,1,"Share");
//            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
//            menu.setHeaderTitle("TASK");
//            menu.add( Menu.NONE, 0, 0, "GAS");
//            getActivity().getMenuInflater().inflate(R.menu.menu_contextual_scheduler, menu);

//            Log.d( __GLOBAL_Const.TAG_handleUserfb, " TIME for LONG click   :   ");
//            getActivity().getMenuInflater().inflate(R.menu.menu_contextual_scheduler, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        //Log.d( __GLOBAL_Const.TAG_handleUserfb, " LOOOOOOOOOOOOOOOOOOOOOONG ");
        switch ( item.getItemId() ) {
            case R.id.menuOptionRemoveTaskFromScheduler:
                Toast.makeText( getContext(), "LOOOONG", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }


    // ---------------------------- ALARMS --------------------------------------

    private void startAlarmForNotification (Date dateNotification, Task task_old, Task task_new, int day, int hour)  {

        long timemilis = dateNotification.getTime();
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        int request_code = calculate_Request_Code( day, hour);

        if ( (ItemScheduled.compareItemsScheduled_AreTheSame(beforeClickedItemScheduled, returnedItemScheduled) == false) || (task_old == null))  {
            // CANCEL existing alarm at this TIME-stamp
            // get the TASK and REQ CODE
            // and recreate the intent to cancel the pending intent
            Intent intent_old = new Intent( getContext(), AlarmReceiver.class);
            String stringTaskTitle = null;
            if (task_old != null) {
                stringTaskTitle = task_old.getTaskLabel().trim();
                intent_old.putExtra(__GLOBAL_Const.extra_alarm_task_name, stringTaskTitle);
                intent_old.putExtra(__GLOBAL_Const.extra_alarm_pos_goal, beforeClickedItemScheduled.getLocusGoal());
                intent_old.putExtra(__GLOBAL_Const.extra_alarm_pos_goal, beforeClickedItemScheduled.getLocusSubGoal());
                intent_old.putExtra(__GLOBAL_Const.extra_alarm_pos_goal, beforeClickedItemScheduled.getLocusTask());
                PendingIntent.getBroadcast(
                        getContext(),
                        request_code,
                        intent_old,
                        PendingIntent.FLAG_CANCEL_CURRENT).cancel();
            }
            // CREATE NEW pending intent
            Intent intent_new = new Intent( getContext(), AlarmReceiver.class);
            intent_new.putExtra(__GLOBAL_Const.extra_alarm_pos_goal, returnedItemScheduled.getLocusGoal());
            intent_new.putExtra(__GLOBAL_Const.extra_alarm_pos_goal, returnedItemScheduled.getLocusSubGoal());
            intent_new.putExtra(__GLOBAL_Const.extra_alarm_pos_goal, returnedItemScheduled.getLocusTask());
            PendingIntent pendingIntent = this.createNewPendingIntent( getContext(), task_new, request_code, intent_new );

            // SET ALARM
            // based on the Android OS
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                //Log.d(__GLOBAL_Const.TAG_handleUserfb, "FRAGMENT DAY --- startAlarm ---   AlarmManager START NEW setAndAllow 1");
                alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timemilis, pendingIntent);
            }
            else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                //Log.d(__GLOBAL_Const.TAG_handleUserfb, "FRAGMENT DAY --- startAlarm ---   AlarmManager START NEW setAndAllow 2");
                alarmManager.setExact( AlarmManager.RTC_WAKEUP, timemilis, pendingIntent);
            } else {
                //Log.d(__GLOBAL_Const.TAG_handleUserfb, "FRAGMENT DAY --- startAlarm ---   AlarmManager START NEW setAndAllow 3");
                alarmManager.set( AlarmManager.RTC_WAKEUP, timemilis, pendingIntent );
            }
        }
    }

    private int calculate_Request_Code (int day, int hour) {
        int request_code = 0;
        request_code = day * 100 + hour;
        return request_code;
    }

    private PendingIntent createNewPendingIntent (Context context, Task task, int request_code, Intent intent) {

        String stringTaskTitle = null;
        if (task != null)
            stringTaskTitle = task.getTaskLabel().trim();
        intent.putExtra( __GLOBAL_Const.extra_alarm_task_name, stringTaskTitle);

        PendingIntent pendingIntent = PendingIntent.getBroadcast( context,
                                                                request_code,
                                                                intent, 0);
        return pendingIntent;
    }

    // ---------------------------------------------------------------------------------

    private Date dateForScheduledTasks (int position)   {
        // In 2 hour increments
        int hour = this.startingHour + position * this.incrementHours;
        int minute = 0;
        int second = 0;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime( this.dateDay );
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        // In the 2 next minutes
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime( this.dateDay );
//        int minute = calendar.get(Calendar.MINUTE) +2;
//        int second = 0;
//        calendar.set(Calendar.MINUTE, minute);
//        calendar.set(Calendar.SECOND, second);

        return calendar.getTime();
    }

    private Date dateOfDay (int position)    {
        int dayOfWeek = position +1;           // todayIsA: 1 for MONDAY, 2 for TUESDAY, ...., 7 for SUNDAY
        Calendar calendar = Calendar.getInstance();

        int today = 0;
        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.MONDAY:
                today = 1;  break;
            case Calendar.TUESDAY:
                today = 2;  break;
            case Calendar.WEDNESDAY:
                today = 3;  break;
            case Calendar.THURSDAY:
                today = 4;  break;
            case Calendar.FRIDAY:
                today = 5;  break;
            case Calendar.SATURDAY:
                today = 6;  break;
            case Calendar.SUNDAY:
                today = 7;  break;
            default:
                today = 0;  break;
        }
        int n = 0;
        if (dayOfWeek < today) {
            n = dayOfWeek % today;
            n = n - today;
        }
        else if (dayOfWeek >= today)
            n = dayOfWeek - today;
        calendar.add(Calendar.DATE, n);
        return calendar.getTime();
    }

    private void createDots (int current_position) {
        if (mLinearLayoutForDots != null) {
            mLinearLayoutForDots.removeAllViews();
        }
        mDotsArrayImageView = new ImageView[7];
        for (int i = 0; i <= 6; i++) {
            mDotsArrayImageView[i] = new ImageView( getContext() );
            if (i == current_position)
                mDotsArrayImageView[i].setImageResource(R.drawable.dots_active);
            else
                mDotsArrayImageView[i].setImageResource(R.drawable.dots_inactive);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(4, 0, 4, 0);
            mLinearLayoutForDots.addView(mDotsArrayImageView[i], params);
        }
    }

    public int getClickedHour() { return clickedHour; }

    public void setClickedHour(int clickedHour) { this.clickedHour = clickedHour; }

    public ItemScheduled getBeforeClickedItemScheduled() {  return beforeClickedItemScheduled;  }

    public void setBeforeClickedItemScheduled(ItemScheduled beforeClickedItemScheduled) {
        this.beforeClickedItemScheduled = beforeClickedItemScheduled;
    }

    public ItemScheduled getReturnedItemScheduled() { return returnedItemScheduled; }

    public void setReturnedItemScheduled(ItemScheduled returnedItemScheduled) { this.returnedItemScheduled = returnedItemScheduled; }

    // ------------------------ SAVE On EXIT --------------------------------------
    @Override
    public void onPause() {
        super.onPause();
        long dateUpdatedNow = this.establishDateForUpdate();
        ((DataList) getContext().getApplicationContext()).setDateLastUpdate( dateUpdatedNow );
        save_DataList_to_SharedPrefs( (DataList) getContext().getApplicationContext() );
        //save_AlarmList_to_SharedPrefs( this.alarmList);
    }

    private void save_DataList_to_SharedPrefs(DataList datalist) {

        //Log.d(__GLOBAL_Const.TAG_handleUserfb, "FragmentDay - onPause - SaveSL to SHARED");
        DataListNoApp dataListNoApp = new DataListNoApp(
                datalist.getUserId(),
                datalist.getDateLastUpdate(),
                datalist.getListOngoing(),
                datalist.getListCompleted(),
                datalist.getSchedule(),
                datalist.isNeedsUpdateToFire()
        );
        SharedPreferences sharedPreferences;
        SharedPreferences.Editor editor;
        sharedPreferences = getContext().getSharedPreferences( __GLOBAL_Const.PREFS_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson( dataListNoApp);

        editor.putString( __GLOBAL_Const.PREFS_KEY, json);
        editor.commit();
    }

    private long establishDateForUpdate ()  {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime().getTime();
    }


    // ------------------------ BACKUP --------------------------------------

    private String dateForDay (int position)    {
        int dayOfWeek = position +1;           // todayIsA: 1 for MONDAY, 2 for TUESDAY, ...., 7 for SUNDAY
        Calendar calendar = Calendar.getInstance();

        int today = 0;
        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.MONDAY:
                today = 1;  break;
            case Calendar.TUESDAY:
                today = 2;  break;
            case Calendar.WEDNESDAY:
                today = 3;  break;
            case Calendar.THURSDAY:
                today = 4;  break;
            case Calendar.FRIDAY:
                today = 5;  break;
            case Calendar.SATURDAY:
                today = 6;  break;
            case Calendar.SUNDAY:
                today = 7;  break;
            default:
                today = 0;  break;
        }
        int n = 0;
        if (dayOfWeek < today) {
            n = dayOfWeek % today;
            n = n - today;
        }
        else if (dayOfWeek >= today)
            n = dayOfWeek - today;
        calendar.add(Calendar.DATE, n);
        return (new SimpleDateFormat("DD MMM YYYY").format(calendar.getTime()));
    }

    private Intent intent_from_uri (String uriString)   {
        Intent intent = null;
        try {
            intent = Intent.parseUri( uriString, 0);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return intent;
    }

    private String string_intents_uri (Intent intent)    {
        String intentUriString = intent.toUri(0);
        Log.d(__GLOBAL_Const.TAG_handleUserfb, "FRAGMENT DAY --- list_intents_uri ---       "+ intentUriString);
        return intentUriString;
    }

    private void startAlarmForNotification_IT_WORKS (Date dateNotification, Task task_old, Task task_new, int day, int hour)  {

        long timemilis = dateNotification.getTime();
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        int request_code = calculate_Request_Code( day, hour);

        if ( (ItemScheduled.compareItemsScheduled_AreTheSame(beforeClickedItemScheduled, returnedItemScheduled) == false) || (task_old == null))  {
            // CANCEL existing alarm at this TIME-stamp
            // get the TASK and REQ CODE
            // and recreate the intent to cancel the pending intent
            Intent intent_old = new Intent( getContext(), AlarmReceiver.class);
            String stringTaskTitle = null;
            if (task_old != null) {
                stringTaskTitle = task_old.getTaskLabel().trim();
                intent_old.putExtra(__GLOBAL_Const.extra_alarm_task_name, stringTaskTitle);
                PendingIntent.getBroadcast(
                        getContext(),
                        request_code,
                        intent_old,
                        PendingIntent.FLAG_CANCEL_CURRENT).cancel();
            }
            // CREATE NEW pending intent
            Intent intent_new = new Intent( getContext(), AlarmReceiver.class);
            PendingIntent pendingIntent = this.createNewPendingIntent( getContext(), task_new, request_code, intent_new );

            // SET ALARM
            // based on the Android OS
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                //Log.d(__GLOBAL_Const.TAG_handleUserfb, "FRAGMENT DAY --- startAlarm ---   AlarmManager START NEW setAndAllow 1");
                alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timemilis, pendingIntent);
            }
            else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                //Log.d(__GLOBAL_Const.TAG_handleUserfb, "FRAGMENT DAY --- startAlarm ---   AlarmManager START NEW setAndAllow 2");
                alarmManager.setExact( AlarmManager.RTC_WAKEUP, timemilis, pendingIntent);
            } else {
                //Log.d(__GLOBAL_Const.TAG_handleUserfb, "FRAGMENT DAY --- startAlarm ---   AlarmManager START NEW setAndAllow 3");
                alarmManager.set( AlarmManager.RTC_WAKEUP, timemilis, pendingIntent );
            }
        }
    }

    private void startAlarmForNotification_BackUp ()  {
        Calendar calendar = Calendar.getInstance();
        Log.d(__GLOBAL_Const.TAG_handleUserfb, " NOTIFICATION - Current Time    :   " + calendar.getTime().toString());

        calendar.setTimeInMillis( calendar.getTimeInMillis() + 10000);
        Log.d(__GLOBAL_Const.TAG_handleUserfb, " NOTIFICATION - Post on Date    :   " + calendar.getTime().toString());

        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent( getContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast( getContext(), 1, intent, 0);
        alarmManager.setExact( AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    private void startAlarmForNotification_Not_Always_Showing (Date dateNotification)  {

        Calendar calendar = Calendar.getInstance();

        long timemilis = dateNotification.getTime();
        Log.d(__GLOBAL_Const.TAG_handleUserfb, " NOTIFICATION - Current Time    :   " + dateNotification.toString());


        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent( getContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast( getContext(), 1, intent, 0);
        alarmManager.setExact( AlarmManager.RTC_WAKEUP, timemilis, pendingIntent);
    }

    private void startAlarmForNotification_OLD (Date dateNotification, Task task)  {

        Calendar calendar = Calendar.getInstance();

        long timemilis = dateNotification.getTime();

        String stringTaskTitle = task.getTaskLabel().trim();

        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent( getContext(), AlarmReceiver.class);
        intent.putExtra( __GLOBAL_Const.extra_alarm_task_name, stringTaskTitle);

        PendingIntent pendingIntent = PendingIntent.getBroadcast( getContext(), 1, intent, 0);
        //alarmManager.setExact( AlarmManager.RTC_WAKEUP, timemilis, pendingIntent);
        //alarmManager.setWindow( AlarmManager.RTC_WAKEUP, timemilis, 5_000,pendingIntent);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timemilis, pendingIntent);
        }
        else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact( AlarmManager.RTC_WAKEUP, timemilis, pendingIntent);
        } else {
            alarmManager.set( AlarmManager.RTC_WAKEUP, timemilis, pendingIntent );
        }

    }

    private void startAlarmForNotification_OLD_OLD (Date dateNotification, Task task, int day, int hour)  {

        long timemilis = dateNotification.getTime();
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        int request_code = calculate_Request_Code( day, hour);

        // SEARCH and SEE if there is already
        // a pending intent for this DAY, HOUR - and you find one
        // CANCEL it, before adding a new one
        // if an alarm is found, then we CANCEL it. If none is found, then we SKIP to the next step.


        // CREATE NEW pending intent
        // ADD it to the ARRAY
        Intent intent = new Intent( getContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = this.createNewPendingIntent( getContext(), task, request_code, intent );

        // SET ALARM
        // based on the Android OS
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timemilis, pendingIntent);
        }
        else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact( AlarmManager.RTC_WAKEUP, timemilis, pendingIntent);
        } else {
            alarmManager.set( AlarmManager.RTC_WAKEUP, timemilis, pendingIntent );
        }

    }

//    private void startAlarmForNotification_Uri (Date dateNotification, Task task, int day, int hour)  {
//
//        long timemilis = dateNotification.getTime();
//        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
//        int request_code = calculate_Request_Code( day, hour);
//        Log.d(__GLOBAL_Const.TAG_handleUserfb, "FRAGMENT DAY --- request_code --- "+ request_code);
//
//        // SEARCH and SEE if there is already
//        // a pending intent for this DAY, HOUR - and you find one
//        // CANCEL it, before adding a new one
//        String string_uri_intent_found = this.alarmList.find_Intent( request_code );
//        if ( string_uri_intent_found != null) {
////            Log.d(__GLOBAL_Const.TAG_handleUserfb, "FRAGMENT DAY --- startAlarmNotification --- IF, cancel alarm");
////            Log.d(__GLOBAL_Const.TAG_handleUserfb, "FRAGMENT DAY --- startAlarmNotification --- INTENT FOUND");
//            Intent intent_to_Delete = this.intent_from_uri( string_uri_intent_found );
//            if ( intent_to_Delete != null) {
//                alarmManager.cancel(this.createNewPendingIntent(getContext(), null, request_code, intent_to_Delete));
//                this.alarmList.remove_Intent(request_code);
//            }
//        }
//        // if an alarm is found, then we CANCEL it. If none is found, then we SKIP to the next step.
//
//
//        // CREATE NEW pending intent
//        // ADD it to the ARRAY
//        Intent intent = new Intent( getContext(), AlarmReceiver.class);
//        Log.d(__GLOBAL_Const.TAG_handleUserfb, "FRAGMENT DAY --- startAlarmNotification --- INTENT ");
//        String uri_intent = this.string_intents_uri( intent );
//        PendingIntent pendingIntent = this.createNewPendingIntent( getContext(), task, request_code, intent );
//        this.alarmList.add_Intent( uri_intent, request_code );
//
//        // SET ALARM
//        // based on the Android OS
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timemilis, pendingIntent);
//        }
//        else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
//            alarmManager.setExact( AlarmManager.RTC_WAKEUP, timemilis, pendingIntent);
//        } else {
//            alarmManager.set( AlarmManager.RTC_WAKEUP, timemilis, pendingIntent );
//        }
//
//    }

}
