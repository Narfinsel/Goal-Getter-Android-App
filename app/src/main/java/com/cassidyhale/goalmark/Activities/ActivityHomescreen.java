package com.cassidyhale.goalmark.Activities;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.cassidyhale.goalmark.Model.DataList;
import com.cassidyhale.goalmark.Model.DataListNoApp;
import com.cassidyhale.goalmark.Model.Goal;
import com.cassidyhale.goalmark.Model.Schedule;
import com.cassidyhale.goalmark.R;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityHomescreen extends AppCompatActivity {

    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mDatabase;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private DataList mSharedDataList;
    private DataList dataList_from_Firebase;
    private boolean isFirstTimeSignIn_PutSHtoFb = false;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    private final String __PROVIDER_Facebook__ = "facebook.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_homescreen);
        mSharedDataList = (DataList) getApplicationContext();

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

        if (this.isInternetConnection() && isLoggedIn) {
            this.initialize_Datalist_For_Facebook_User();
        }
        else if( this.isInternetConnection() == false || isLoggedIn == false)   {
            if (read_DataList_from_SharedPrefs() != null)    {
                DataList dataListFromSharedPref = read_DataList_from_SharedPrefs();
                setDataListToApplicationContext( dataListFromSharedPref, false);
            }
        }
        this.initializeViews();
    }

    @Override
    protected void onStart() { super.onStart(); }

    @Override
    protected void onResume() {
        super.onResume();
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

        if (  ((DataList) getApplicationContext()).isNeedsUpdateToFire() )  {
            DataList dataList = (DataList) getApplicationContext();
            if (isLoggedIn) {
                //Log.d(__GLOBAL_Const.TAG_handleUserfb, "    OnStart -   ..................................... Writing to Firebase");
                mDatabaseReference.child(__GLOBAL_Const.child__Datalist__).setValue(dataList.toMap());
            }
            save_DataList_to_SharedPrefs( dataList );
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initializeViews (){
        Toolbar myToolbar = (Toolbar) findViewById(R.id.ToolbarHomeScr);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle (R.string.string_Toolbar_Title_Home);


        mDrawerLayout= (DrawerLayout) findViewById(R.id.drawerHomescreen);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                                R.string.string_NavDrawer_MenuItem_Guide,
                                R.string.string_NavDrawer_MenuItem_Settings);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigationViewHomeScreen);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                            // set item as selected to persist highlight
                        menuItem.setChecked(true);
                            // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                            // Add code here to update the UI based on the item selected
                            // For example, swap UI fragments here
                        if ( menuItem.getItemId() == R.id.menu_item_walkthrough)    {
                            changeActivityWalkthrough();
                        }
                        if ( menuItem.getItemId() == R.id.menu_item_weekly_schedule)    {
                            changeActivityScheduler();
                        }
                        if ( menuItem.getItemId() == R.id.menu_item_goals)    {
                            changeActivityGoals();
                        }
                        if ( menuItem.getItemId() == R.id.menu_item_sign_out) {
                            signOutOffFacebookApp();
                        }
                        if ( menuItem.getItemId() == R.id.menu_item_settings) {
                            if ( getApplicationContext() != null )
                                if ( read_DataList_from_SharedPrefs() != null )
                                    read_DataList_from_SharedPrefs().displayDateLastUpdated();
                                //((DataList) getApplicationContext()).displayAll();
                        }
                        return true;
                    }
                });

    }

    private void initialize_Datalist_For_Facebook_User () {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

        if (isLoggedIn) {
            mAuth = FirebaseAuth.getInstance();
            mUser = mAuth.getCurrentUser();
            mDatabase = FirebaseDatabase.getInstance();
            if (mUser != null) {
                mDatabaseReference = mDatabase.getReference(__GLOBAL_Const.child_Data).child(mUser.getUid());
                //Log.d("USERx", "HOMEscreen -- initFirebFuncionality - DB-RRF-KEY: " + mDatabaseReference.getKey());
                this.valueEventListenerForDataList();
                mDatabaseReference.keepSynced(true);
            }
        }
    }

    private void valueEventListenerForDataList() {
        if ( mUser != null)  {
            mDatabaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // create new Datalist in Firebase Realtine, if the user is NEW signed in
                    if ( ! dataSnapshot.hasChild( __GLOBAL_Const.child__Datalist__ ) )  {
                            // New User - Create new Data List object
                        String keyDL = mDatabaseReference.child(__GLOBAL_Const.child__Datalist__).getKey();
                        String uid = mUser.getUid();
                        List<Goal> listOngoing = new ArrayList<>();
                            listOngoing.add(new Goal( __GLOBAL_Const.dummy ));
                        List<Goal> listCompleted = new ArrayList<>();
                            listCompleted.add(new Goal( __GLOBAL_Const.dummy ));
                        Schedule schedule = new Schedule();

                        DataList newDataList = new DataList( uid, establishDateForUpdate(), listOngoing, listCompleted, schedule, false);

                        Map<String, Object> mappedData = newDataList.toMap();
                        Map<String, Object> mapToPutInFireBase = new HashMap<>();

                        mapToPutInFireBase.put( keyDL, mappedData);
                        mDatabaseReference.updateChildren( mapToPutInFireBase );

                        if (read_DataList_from_SharedPrefs() != null) {
                            //  Log.d(__GLOBAL_Const.TAG_handleUserfb, "OnDataChange - New DL from Scratch !!!");
                            isFirstTimeSignIn_PutSHtoFb = true;
                        }
                    }
                    else {
                        // Upload Datalist modifications to Firebade Realtime
                        // only if needs update

                        GenericTypeIndicator<List<Goal>> t = new GenericTypeIndicator<List<Goal>>() {
                        };
                        String useId = dataSnapshot.child(__GLOBAL_Const.child__Datalist__)
                                .child(__GLOBAL_Const.child__gUserId).getValue(String.class);

                        long dateLast = (long) dataSnapshot.child(__GLOBAL_Const.child__Datalist__)
                                .child(__GLOBAL_Const.child__gDateLastUpdate).getValue();

                        List<Goal> listGlOngo = dataSnapshot.child(__GLOBAL_Const.child__Datalist__)
                                .child(__GLOBAL_Const.child__gListOngoing).getValue(t);
                        if (listGlOngo == null)
                            listGlOngo = new ArrayList<Goal>();

                        List<Goal> listGlComp = dataSnapshot.child(__GLOBAL_Const.child__Datalist__)
                                .child(__GLOBAL_Const.child__gListCompleted).getValue(t);
                        if (listGlComp == null)
                            listGlComp = new ArrayList<Goal>();
                        Schedule schedule = dataSnapshot.child(__GLOBAL_Const.child__Datalist__)
                                .child(__GLOBAL_Const.child__gSchedule).getValue(Schedule.class);

                        dataList_from_Firebase = new DataList(useId,
                                dateLast,
                                listGlOngo,
                                listGlComp,
                                schedule,
                                false);
                        DataList dataListFor_APPCONTEXT = null;
                        DataList dataList_from_Shared = read_DataList_from_SharedPrefs();
                        boolean isWriteToFireBase_OnStart;

                        if ( dataList_from_Shared != null)    {
                            //Log.d(__GLOBAL_Const.TAG_handleUserfb, "    HOMESCREEN  -   valueEventListenr - NO read_DataList_from_SharedPrefs() == null");

                            if ( dataList_from_Firebase.getDateLastUpdate() < dataList_from_Shared.getDateLastUpdate() ) {
                                /*Log.d(__GLOBAL_Const.TAG_handleUserfb, "    HOMESCREEN  -   SH datalist is NEWER");
                                Log.d(__GLOBAL_Const.TAG_handleUserfb, "    HOMESCREEN  -   SH datalist is NEWER    FB - "+ dataList_from_Firebase.getDateLastUpdate());
                                Log.d(__GLOBAL_Const.TAG_handleUserfb, "    HOMESCREEN  -   SH datalist is NEWER    SH - "+ dataList_from_Shared.getDateLastUpdate());  */
                                //  SH data-list is NEWER
                                //  Use SH for Application Context
                                isWriteToFireBase_OnStart = true;
                                dataListFor_APPCONTEXT = dataList_from_Shared;
                            }
                            else {
                                /*Log.d(__GLOBAL_Const.TAG_handleUserfb, "    HOMESCREEN  -   FB datalist is NEWER");
                                Log.d(__GLOBAL_Const.TAG_handleUserfb, "    HOMESCREEN  -   FB datalist is NEWER    FB - "+ dataList_from_Firebase.getDateLastUpdate());
                                Log.d(__GLOBAL_Const.TAG_handleUserfb, "    HOMESCREEN  -   FB datalist is NEWER    SH - "+ dataList_from_Shared.getDateLastUpdate());  */
                                //  FB data-list is NEWER
                                //  Use FB for Application Context
                                isWriteToFireBase_OnStart = false;
                                dataListFor_APPCONTEXT = dataList_from_Firebase;
                                //  Write FB DL to shared prefs
                                save_DataList_to_SharedPrefs( dataList_from_Firebase );
                            }
                        }
                        else {
                            // if (dataList_from_Shared == null)
                            // if there is no shared prefs DATALIST
                            //Log.d(__GLOBAL_Const.TAG_handleUserfb, "    HOMESCREEN  -   NO SH exists !");
                            isWriteToFireBase_OnStart = false;
                            dataListFor_APPCONTEXT = dataList_from_Firebase;
                            save_DataList_to_SharedPrefs( dataList_from_Firebase );
                        }
                        if ( dataList_from_Shared != null && isFirstTimeSignIn_PutSHtoFb == true) {
                            //Log.d(__GLOBAL_Const.TAG_handleUserfb, "    HOMESCREEN  -   New Connection - using SH as APP_CTX !");
                            isWriteToFireBase_OnStart = true;
                            dataListFor_APPCONTEXT = dataList_from_Shared;
                            setDataListToApplicationContext(dataListFor_APPCONTEXT, true);
                            isFirstTimeSignIn_PutSHtoFb = false;
                        }
                        setDataListToApplicationContext(dataListFor_APPCONTEXT, isWriteToFireBase_OnStart);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void setDataListToApplicationContext( DataList dataListFor_APPCONTEXT, boolean doesNeedUpdate)  {

        //Log.d(__GLOBAL_Const.TAG_handleUserfb, "    HOMESCREEN  -  in   setDataListToApplicationContext() ");
        if ( dataListFor_APPCONTEXT.getListOngoing().size() > 0) {
            Goal testFirstOngoingGoal = dataListFor_APPCONTEXT.getListOngoing().get(0);
            if (testFirstOngoingGoal != null && testFirstOngoingGoal.getgLabel().equals(__GLOBAL_Const.dummy)) {
                dataListFor_APPCONTEXT.getListOngoing().remove(0);
            }
        }
        if ( dataListFor_APPCONTEXT.getListCompleted().size() > 0) {
            Goal testFirstCompletedGoal = dataListFor_APPCONTEXT.getListCompleted().get(0);
            if (testFirstCompletedGoal != null && testFirstCompletedGoal.getgLabel().equals(__GLOBAL_Const.dummy)) {
                dataListFor_APPCONTEXT.getListCompleted().remove(0);
            }
        }
        ((DataList) getApplicationContext()).setListCompleted( dataListFor_APPCONTEXT.getListCompleted());
        ((DataList) getApplicationContext()).setListOngoing( dataListFor_APPCONTEXT.getListOngoing());
        ((DataList) getApplicationContext()).setSchedule( dataListFor_APPCONTEXT.getSchedule());
        ((DataList) getApplicationContext()).setDateLastUpdate( dataListFor_APPCONTEXT.getDateLastUpdate());
        ((DataList) getApplicationContext()).setUserId( dataListFor_APPCONTEXT.getUserId());
        ((DataList) getApplicationContext()).setNeedsUpdateToFire( doesNeedUpdate );

        /*
        DataList dataListFor_APPCONTEXT;

        if (read_DataList_from_SharedPrefs() != null )    {
            // if shared prefs DATALIST exists
            Log.d(__GLOBAL_Const.TAG_handleUserfb, "    HOMESCREEN  -  read_DataList_from_SharedPrefs() != null ");
            DataList dataList_SH = read_DataList_from_SharedPrefs();
            long dateFrom_FB = dataList_FB.getDateLastUpdate();
            //dataList_SH.displayAll();
            Log.d(__GLOBAL_Const.TAG_handleUserfb, "    HOMESCREEN  -   dataList_SH.getDateLastUpdate() :   "+ dataList_SH.getDateLastUpdate());

            if ( dataList_FB.getDateLastUpdate() < dataList_SH.getDateLastUpdate() ) {
                //  SH data-list is NEWER
                //  Use SH for Application Context
                Log.d(__GLOBAL_Const.TAG_handleUserfb, "    HOMESCREEN  -   FB older that SH");
                dataListFor_APPCONTEXT = dataList_SH;
                //  Write SH DL to server
                //write_DataList_to_Server( dataList_SH );
            }
            else {
                //  FB data-list is NEWER
                //  Use FB for Application Context
                Log.d(__GLOBAL_Const.TAG_handleUserfb, "    HOMESCREEN  -   FB not older than SH");
                dataListFor_APPCONTEXT = dataList_FB;
                //  Write FB DL to shared prefs
                save_DataList_to_SharedPrefs( dataList_FB );
            }
        }
        else {
            // if there is no shared prefs DATALIST
            Log.d(__GLOBAL_Const.TAG_handleUserfb, "    HOMESCREEN  -   NO SH exists !");
            dataListFor_APPCONTEXT = dataList_FB;
            save_DataList_to_SharedPrefs(dataList_FB);
        }

        ((DataList) getApplicationContext()).setListCompleted( dataListFor_APPCONTEXT.getListCompleted());
        ((DataList) getApplicationContext()).setListOngoing( dataListFor_APPCONTEXT.getListOngoing());
        ((DataList) getApplicationContext()).setSchedule( dataListFor_APPCONTEXT.getSchedule());
        ((DataList) getApplicationContext()).setDateLastUpdate( dataListFor_APPCONTEXT.getDateLastUpdate());
        ((DataList) getApplicationContext()).setUserId( dataListFor_APPCONTEXT.getUserId());
        ((DataList) getApplicationContext()).setNeedsUpdateToFire( false );
        */
    }

    private void signOutOffFacebookApp()   {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        if (isLoggedIn) {
            startActivity( new Intent(ActivityHomescreen.this, ActivityLoginMain.class) );
            finish();
            LoginManager.getInstance().logOut();
        }
        else    {
            startActivity( new Intent(ActivityHomescreen.this, ActivityLoginMain.class) );
            finish();
        }
    }

    private void changeActivityWalkthrough ()   {
        Intent intentGoTo_WalkthrScr = new Intent (ActivityHomescreen.this, ActivityWalkthrough.class);
        startActivity( intentGoTo_WalkthrScr );
    }

    private void changeActivityGoals ()   {
        Intent intentGoTo_GoalsScr = new Intent (ActivityHomescreen.this, ActivityGoals.class);
        startActivity( intentGoTo_GoalsScr );
    }

    private void changeActivityScheduler ()   {
        Intent intentGoTo_Schedule = new Intent (ActivityHomescreen.this, ActivityScheduler.class);
        startActivity( intentGoTo_Schedule );
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item){

        if (mToggle.onOptionsItemSelected( item ))  {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isInternetConnection()  {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if (    connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState()   == NetworkInfo.State.CONNECTED)
            //we are connected to a network
            connected = true;
        else
            connected = false;
        return connected;
    }

    private void save_DataList_to_SharedPrefs(DataList datalist) {

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
        sharedPreferences = getSharedPreferences( __GLOBAL_Const.PREFS_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson( dataListNoApp);

        editor.putString( __GLOBAL_Const.PREFS_KEY, json);
        editor.commit();
    }

    private DataList read_DataList_from_SharedPrefs() {

        DataListNoApp dataListNoApp;
        SharedPreferences sharedPreferences;
        sharedPreferences = getSharedPreferences( __GLOBAL_Const.PREFS_NAME, Context.MODE_PRIVATE);

        if (sharedPreferences.contains( __GLOBAL_Const.PREFS_KEY)) {
            String jsonFavorites = sharedPreferences.getString( __GLOBAL_Const.PREFS_KEY, null);
            Gson gson = new Gson();
            dataListNoApp = gson.fromJson (jsonFavorites, DataListNoApp.class);
            DataList dataList = new DataList (
                    dataListNoApp.getUserId(),
                    dataListNoApp.getDateLastUpdate(),
                    dataListNoApp.getListOngoing(),
                    dataListNoApp.getListCompleted(),
                    dataListNoApp.getSchedule(),
                    dataListNoApp.isNeedsUpdateToFire() );
            return dataList;

        } else
            return null;
    }

    private void clear_SharedPrefferences() {
        if (read_DataList_from_SharedPrefs() != null) {
            Log.d(__GLOBAL_Const.TAG_handleUserfb, "    ....... Clearing Shared Preferences");
            //getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit().clear();
            SharedPreferences.Editor editor = getSharedPreferences(__GLOBAL_Const.PREFS_NAME, Context.MODE_PRIVATE).edit();
            editor.clear();
            editor.commit();
        }
        else
            Log.d(__GLOBAL_Const.TAG_handleUserfb, "    ....... C L E A R E D ............");
    }

    private long establishDateForUpdate ()  {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime().getTime();
    }

    // ------------------------ BACKUP -----------------------------
    private void valueEventListenerForDataList_BACKUP() {
        if ( mUser != null)  {
            mDatabaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // create new Datalist in Firebase Realtine, if the user is NEW signed in
                    if ( ! dataSnapshot.hasChild( __GLOBAL_Const.child__Datalist__ ) )  {
                        // New User - Create new Data List object
                        String keyDL = mDatabaseReference.child(__GLOBAL_Const.child__Datalist__).getKey();
                        String uid = mUser.getUid();
                        List<Goal> listOngoing = new ArrayList<>();
                        listOngoing.add(new Goal( __GLOBAL_Const.dummy ));
                        List<Goal> listCompleted = new ArrayList<>();
                        listCompleted.add(new Goal( __GLOBAL_Const.dummy ));
                        Schedule schedule = new Schedule();

                        DataList newDataList = new DataList( uid, establishDateForUpdate(), listOngoing, listCompleted, schedule, false);

                        Map<String, Object> mappedData = newDataList.toMap();
                        Map<String, Object> mapToPutInFireBase = new HashMap<>();

                        mapToPutInFireBase.put( keyDL, mappedData);
                        mDatabaseReference.updateChildren( mapToPutInFireBase );
                    }
                    else {
                        // Upload Datalist modifications to Firebade Realtime
                        // only if needs update
                        //if ( ((DataList) getApplicationContext()).isNeedsUpdateToFire()  ) {
//                            if ( ! dataSnapshot.child(__GLOBAL_Const.child__Datalist__).hasChild(__GLOBAL_Const.child__gSchedule))  {
//                                Schedule schedule = new Schedule();
//                            }

                        GenericTypeIndicator<List<Goal>> t = new GenericTypeIndicator<List<Goal>>() { };
                        String useId = dataSnapshot.child(__GLOBAL_Const.child__Datalist__)
                                .child(__GLOBAL_Const.child__gUserId).getValue(String.class);

                        long dateLast = (long) dataSnapshot.child(__GLOBAL_Const.child__Datalist__)
                                .child(__GLOBAL_Const.child__gDateLastUpdate).getValue();

                        List<Goal> listGlOngo = dataSnapshot.child(__GLOBAL_Const.child__Datalist__)
                                .child(__GLOBAL_Const.child__gListOngoing).getValue(t);
                        if (listGlOngo == null)
                            listGlOngo = new ArrayList<Goal>();

                        List<Goal> listGlComp = dataSnapshot.child(__GLOBAL_Const.child__Datalist__)
                                .child(__GLOBAL_Const.child__gListCompleted).getValue(t);
                        if (listGlComp == null)
                            listGlComp = new ArrayList<Goal>();
                        Schedule schedule = dataSnapshot.child(__GLOBAL_Const.child__Datalist__)
                                .child(__GLOBAL_Const.child__gSchedule).getValue(Schedule.class);

                        DataList mDataList = new DataList( useId,
                                dateLast,
                                listGlOngo,
                                listGlComp,
                                schedule,
                                false);

                        ((DataList) getApplicationContext()).setListCompleted(mDataList.getListCompleted());
                        ((DataList) getApplicationContext()).setListOngoing(mDataList.getListOngoing());
                        ((DataList) getApplicationContext()).setSchedule(mDataList.getSchedule());
                        ((DataList) getApplicationContext()).setDateLastUpdate(mDataList.getDateLastUpdate());
                        ((DataList) getApplicationContext()).setUserId(mDataList.getUserId());
                        ((DataList) getApplicationContext()).setNeedsUpdateToFire(false);

                        if (((DataList) getApplicationContext()).getListOngoing().size() > 0) {
                            Goal testFirstOngoingGoal = ((DataList) getApplicationContext()).getListOngoing().get(0);
                            if (testFirstOngoingGoal != null && testFirstOngoingGoal.getgLabel().equals(__GLOBAL_Const.dummy)) {
                                ((DataList) getApplicationContext()).getListOngoing().remove(0);
                            }
                        }
                        if (((DataList) getApplicationContext()).getListCompleted().size() > 0) {
                            Goal testFirstCompletedGoal = ((Goal) ((DataList) getApplicationContext()).getListCompleted().get(0));
                            if (testFirstCompletedGoal != null && testFirstCompletedGoal.getgLabel().equals(__GLOBAL_Const.dummy)) {
                                ((DataList) getApplicationContext()).getListCompleted().remove(0);
                            }
                        }
                        // to change alteration to this method:
                        // 1. DataList newDataList = new DataList( uid, listOngoing, listCompleted, false);                     line 136
                        // 2. Incorporate IF statement: if ( ((DataList) getApplicationContext()).isNeedsUpdateToFire()  ) {    line 147
                        // 3. ((DataList) getApplicationContext()).setNeedsUpdateToFire(mDataList.isNeedsUpdateToFire());       line 171

                        save_DataList_to_SharedPrefs( (DataList) getApplicationContext() );
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void initialiazeFirebaseFunctionality () {
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        if (mUser != null) {
            mDatabaseReference = mDatabase.getReference(__GLOBAL_Const.child_Data).child(mUser.getUid());
            //Log.d("USERx", "HOMEscreen -- initFirebFuncionality - DB-RRF-KEY: " + mDatabaseReference.getKey());
            this.valueEventListenerForDataList();
            mDatabaseReference.keepSynced(true);
        }
    }

}
