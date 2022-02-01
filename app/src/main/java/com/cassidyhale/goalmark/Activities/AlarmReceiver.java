package com.cassidyhale.goalmark.Activities;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.cassidyhale.goalmark.Model.DataList;
import com.cassidyhale.goalmark.R;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // PENDING INTENT - TAP on NOTIFICATION OPEN APP
        Intent intentStartActivity = new Intent( context, ActivityHomescreen.class);
        intent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent_OpenApp = PendingIntent.getActivity( context, 0, intentStartActivity, 0);



//        BroadcastReceiver broadcastReceiverCheckTask = new CheckTaskReceiver();
//
//        Intent intentUpdateTaskStatus = new Intent( context, CheckTaskReceiver.class);
//        IntentFilter filter = new IntentFilter();
//        filter.addCategory(Intent.CATEGORY_DEFAULT);
//        intentUpdateTaskStatu
//        context.registerReceiver(broadcastReceiverCheckTask, filter);
//
//
//        intentUpdateTaskStatus.setAction(ACTION_SNOOZE);
//        intentUpdateTaskStatus.putExtra(EXTRA_NOTIFICATION_ID, 0);
//        PendingIntent snoozePendingIntent =
//                PendingIntent.getBroadcast(context, 0, intentUpdateTaskStatus, 0);

        // PENDING INTENT - PRESS C_H_E_C_K / YES
        Intent intentCheckTask = new Intent( context, ServiceCheckTask.class);
        int pos_goal = intent.getIntExtra( __GLOBAL_Const.extra_alarm_pos_goal,     -1);
        int pos_sub  = intent.getIntExtra( __GLOBAL_Const.extra_alarm_pos_subgoal,  -1);
        int pos_task = intent.getIntExtra( __GLOBAL_Const.extra_alarm_pos_task,     -1);
        boolean wasTaskDone = true;
        intentCheckTask.putExtra( __GLOBAL_Const.extra_service_pos_goal,    pos_goal);
        intentCheckTask.putExtra( __GLOBAL_Const.extra_service_pos_subgoal, pos_sub);
        intentCheckTask.putExtra( __GLOBAL_Const.extra_service_pos_task,    pos_task);
        PendingIntent pendingIntent_Check_Done =
                PendingIntent.getBroadcast( context, 0, intentCheckTask, 0);

        // CREATE TEXT for NOTIFICATION
        String alarmTitle = "NEW TASK";
        String alarmContent = "Decide now - "
                            + intent.getStringExtra( __GLOBAL_Const.extra_alarm_task_name )
                            + ". Start in... 5, 4, 3, 2, 1.";

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from( context );
        Notification notification = new NotificationCompat.Builder( context, DataList.CHANNEl_ID_1)
                .setSmallIcon( R.drawable.ic_notification_goal_getter)
                .setContentTitle( alarmTitle )
                .setContentText( alarmContent )
                .setPriority( NotificationCompat.PRIORITY_HIGH)
                .setCategory( NotificationCompat.CATEGORY_ALARM)
                .setContentIntent( pendingIntent_OpenApp )
                .setAutoCancel(true)
                .setLights(Color.MAGENTA,3000,3000)
                .addAction( R.drawable.icon_action_buttun_notification_pased,
                            context.getString(R.string.stringNotificationActionButtonDone),
                            pendingIntent_Check_Done)
                .build();
        notificationManagerCompat.notify(1, notification);
    }
}



/*
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Intent intentStartActivity = new Intent( context, ActivityHomescreen.class);
        intent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity( context, 0, intentStartActivity, 0);

        String alarmTitle = "TASK -  " + intent.getStringExtra( __GLOBAL_Const.extra_alarm_task_name );
        String alarmContent = "Get up and begin now ... 5, 4, 3, 2, 1.";

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from( context );
        Notification notification = new NotificationCompat.Builder( context, DataList.CHANNEl_ID_1)
                .setSmallIcon( R.drawable.ic_background)
                .setContentTitle( alarmTitle )
                .setContentText( alarmContent )
                .setPriority( NotificationCompat.PRIORITY_HIGH)
                .setCategory( NotificationCompat.CATEGORY_ALARM)
                .setContentIntent( pendingIntent )
                .setAutoCancel(true)
                .setLights(Color.MAGENTA,3000,3000)
                .build();
        notificationManagerCompat.notify(1, notification);
    }
}


*/







/*  ------ WORKING BACKUP
    @Override
    public void onReceive(Context context, Intent intent) {

        Intent intentStartActivity = new Intent( context, ActivityHomescreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity( context, 0, intentStartActivity, 0);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from( context );
        Notification notification = new NotificationCompat.Builder( context, DataList.CHANNEl_ID_1)
                .setSmallIcon( R.drawable.ic_launcher_foreground)
                .setContentTitle( "Not Title")
                .setContentText( "Not Content Text")
                .setPriority( NotificationCompat.PRIORITY_HIGH)
                .setCategory( NotificationCompat.CATEGORY_ALARM)
                .setContentIntent( pendingIntent )
                .setAutoCancel(true)
                .build();
        notificationManagerCompat.notify(1, notification);
    }

*/