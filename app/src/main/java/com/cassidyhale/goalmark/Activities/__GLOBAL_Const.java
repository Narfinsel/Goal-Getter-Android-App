package com.cassidyhale.goalmark.Activities;

import com.cassidyhale.goalmark.R;

public class __GLOBAL_Const {

    // This is where we write the object:
    // Data / $uid / Data-List

    public static final String APP_NAME                         = "Goal Getter";
    // DEBUGGING
    public static final String TAG_handleUserfb                 = "USERx";
    public static final String child_Data                       = "Data";
    public static final String child__Datalist__                = "Data-List";
    public static final String child__gUserId                   = "userId";
    public static final String child__gDateLastUpdate           = "dateLastUpdate";
    public static final String child__gListCompleted            = "listCompleted";
    public static final String child__gListOngoing              = "listOngoing";
    public static final String child__gSchedule                 = "Schedule";
    public static final String child__needsUpdateToFire         = "needsUpdateToFire";

    public static final String dummy                            = "dummy_goal";

    public static final int default_edit_goal_position          = 99999999;
    public static final int default_edit_subgoal_position       = 99999999;
    public static final int defSubgoalLocus                     = 111111;
    public static final int RESULT_CODE_NEW_TASK                = 5001;
    public static final int REQUEST_CODE_NEW_TASK               = 5101;

    public static final int RESULT_IS_NEW_TASK_ADEDD_IN_SG      = 6001;
    public static final int REQUEST_IS_NEW_TASK_ADEDD_IN_SG     = 6002;

    public static final String PREFS_NAME                       = "com.cassidyhale.goalmark";
    public static final String PREFS_KEY                        = "datalist_shared";
    public static final String PREFS_KEY_CURRENT_WEEK           = "current_week_shared";
    public static final String PREFS_KEY_ALARM_LIST             = "alarm_list_shared";

    // PASSING extra APP > NOTIFICATION
    public static final String extra_alarm_task_name            = "extra_alarm_task_name";
    public static final String extra_alarm_pos_goal             = "extra_alarm_pos_goal";
    public static final String extra_alarm_pos_subgoal          = "extra_alarm_pos_subgoal";
    public static final String extra_alarm_pos_task             = "extra_alarm_pos_task";
    public static final String buttons_acttion_Done             = "DONE";
    public static final String buttons_acttion_SKIPPED          = "SKIPPED";

    // PASSING extra NOTIFICATION > SERVICE
    public static final String extra_service_pos_goal             = "extra_service_pos_goal";
    public static final String extra_service_pos_subgoal          = "extra_service_pos_subgoal";
    public static final String extra_service_pos_task             = "extra_service_pos_task";
    public static final String extra_service_task_done            = "extra_service_task_done";
    public static final String extra_service_task_skipped         = "extra_service_task_skipped";

    public static final  int[] array_colors = { R.color.color_1, R.color.color_2, R.color.color_3, R.color.color_4, R.color.color_5,
                                                R.color.color_6, R.color.color_7, R.color.color_8, R.color.color_9, R.color.color_10,
                                                R.color.color_11, R.color.color_12, R.color.color_13, R.color.color_14, R.color.color_15,
                                                R.color.color_16, R.color.color_17, R.color.color_17    };

    // SELECTING GOALS
    public static final String[] goal_category_string = { "Fitness",  "Well-Being", "Skill Building", "Self-Development", "Wealth", "Business", "Love", "Social", "Travel", "Other"};
    public static final int[] goal_category_drawable =   {  R.drawable.icon_spinner_fitness,
                                                            R.drawable.icon_spinner_wellbeing,
                                                            R.drawable.icon_spinner_skill_building,
                                                            R.drawable.icon_spinner_self_development,
                                                            R.drawable.icon_spinner_wealth,
                                                            R.drawable.icon_spinner_business,
                                                            R.drawable.icon_spinner_love,
                                                            R.drawable.icon_spinner_social,
                                                            R.drawable.icon_spinner_travel,
                                                            R.drawable.icon_spinner_other   };
    }


//    <string-array name="goal_type">
//        <item>Fitness</item>
//        <item>Well-Being</item>
//        <item>Skill Building</item>
//        <item>Self-Development</item>
//        <item>Wealth</item>
//        <item>Business</item>
//        <item>Love</item>
//        <item>Social</item>
//        <item>Travel</item>
//        <item>Other</item>
//    </string-array>

