package com.cassidyhale.goalmark.Model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.cassidyhale.goalmark.Activities.__GLOBAL_Const;
import com.cassidyhale.goalmark.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.Random;

public class Goal implements Parcelable {

    private String gLabel;
    private String gCathegory;
    private Date gDateStart;
    private Date gDateEnd;
    private int resourceBackroundId;
    private int goalColor;
    private boolean gIsCompleted;
    private ArrayList<SubGoal> gListSubGoals;
    private ArrayList<Task> gListTasks;


    private int[] array_goal_category_fitness       = {R.drawable.goal_type_fitness_1, R.drawable.goal_type_fitness_2, R.drawable.goal_type_fitness_3,
                                                        R.drawable.goal_type_fitness_4, R.drawable.goal_type_fitness_5};
    private int[] array_goal_category_skillbuild    = {R.drawable.goal_type_learning_1, R.drawable.goal_type_learning_2, R.drawable.goal_type_learning_3,
                                                        R.drawable.goal_type_learning_4, R.drawable.goal_type_learning_5};
    private int[] array_goal_category_other         = {R.drawable.goal_type_other_1};

    public Goal ()  {
        this.gListSubGoals = new ArrayList<>();
        this.gListTasks = new ArrayList<>();
    }

    public Goal(String gLabel) {
        this();
        this.gLabel = gLabel;
        this.gCathegory = "Other";
        this.gDateStart = new Date();
        this.gDateEnd   = new Date();
        this.gIsCompleted = false;
        this.resourceBackroundId =  this.chooseBackroundForGoal();
    }

    public Goal(String gLabel, Date gDateStart, Date gDateEnd, int goalColor, String gCathegory) {
        this();
        this.gLabel = gLabel;
        this.gCathegory = gCathegory;
        this.gDateStart = gDateStart;
        this.gDateEnd   = gDateEnd;
        this.gIsCompleted = false;
        this.goalColor = goalColor;
        this.resourceBackroundId =  this.chooseBackroundForGoal();
    }

    public void setgLabel(String gLabel) {
        this.gLabel = gLabel;
    }
    public void setgCathegory(String gCathegory) {
        this.gCathegory = gCathegory;
    }
    public void setgDateStart(Date gDateStart) { this.gDateStart = gDateStart;  }
    public void setgDateEnd(Date gDateEnd) {
        this.gDateEnd = gDateEnd;
    }
    public void setgIsCompleted(boolean gIsCompleted) {
        this.gIsCompleted = gIsCompleted;
    }
    public void setGoalColor(int goalColor) {  this.goalColor = goalColor;    }
    public void setgListSubGoals(ArrayList<SubGoal> gListSubGoals) {    this.gListSubGoals = gListSubGoals; }
    public void setgListTasks(ArrayList<Task> gListTasks) {
        this.gListTasks = gListTasks;
    }
    public void setResourceBackroundId(int resourceBackroundId) {   this.resourceBackroundId = resourceBackroundId; }
    public void setResourceBackroundId()    { this.resourceBackroundId =  this.chooseBackroundForGoal();    }

    public String getgLabel() {
        return gLabel;
    }
    public String getgCathegory() {
        return gCathegory;
    }
    public Date getgDateStart() {
        return gDateStart;
    }
    public Date getgDateEnd() {
        return gDateEnd;
    }
    public boolean isgIsCompleted() {
        return gIsCompleted;
    }
    public int getGoalColor() { return goalColor;  }
    public ArrayList<SubGoal> getgListSubGoals() {
        return gListSubGoals;
    }
    public ArrayList<Task> getgListTasks() {
        return gListTasks;
    }
    public int getResourceBackroundId() {
        return resourceBackroundId;
    }


    private int chooseBackroundForGoal ()   {
        int rand;
        switch ( this.gCathegory )  {
            case "Fitness":
                rand = new Random().nextInt( array_goal_category_fitness.length );
                return array_goal_category_fitness[ rand ];
            case "Skill Building":
                rand = new Random().nextInt( array_goal_category_skillbuild.length );
                return array_goal_category_skillbuild[ rand ];
            case "Well-Being":
            case "Self-Development":
            case "Wealth":
            case "Business":
            case "Love":
            case "Social":
            case "Travel":
            case "Other":
            default:
                rand = new Random().nextInt( array_goal_category_other.length );
                return array_goal_category_other[ rand ];
        }
    }

    public void displaySubgoalsToString ()  {
        StringBuilder liOnCom = new StringBuilder("GOAL = ");
        liOnCom.append(this.getgLabel());
        liOnCom.append("    >>> Count: ");
        liOnCom.append(this.getgListSubGoals().size());
        liOnCom.append("    >>> SUB-goals: ");
        if (this.getgListSubGoals() != null)
            for (SubGoal subGoal : this.getgListSubGoals())
                liOnCom.append(" ").append(subGoal.getsLabel());
        else
            liOnCom.append("-- null object--");

        Log.d(__GLOBAL_Const.TAG_handleUserfb, liOnCom.toString());
    }

    // ------------------------- Parcelable -------------------------------
    public static final Creator<Goal> CREATOR = new Creator<Goal>() {
        @Override
        public Goal createFromParcel(Parcel in) {
            return new Goal(in);
        }

        @Override
        public Goal[] newArray(int size) {
            return new Goal[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    protected Goal(Parcel in) {
        gLabel = in.readString();
        gCathegory = in.readString();
        gDateStart = (Date) in.readSerializable();
        gDateEnd = (Date) in.readSerializable();
        gIsCompleted = in.readByte() != 0;
        resourceBackroundId = in.readInt();
        goalColor = in.readInt();
        gListSubGoals = in.createTypedArrayList(SubGoal.CREATOR);
        gListTasks = in.createTypedArrayList(Task.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(gLabel);
        dest.writeString(gCathegory);
        dest.writeSerializable(gDateStart);
        dest.writeSerializable(gDateEnd);
        dest.writeByte((byte) (gIsCompleted ? 1 : 0));
        dest.writeInt(resourceBackroundId);
        dest.writeInt(goalColor);
        dest.writeTypedList(gListSubGoals);
        dest.writeTypedList(gListTasks);
    }


}
