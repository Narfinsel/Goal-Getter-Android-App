package com.cassidyhale.goalmark.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;

public class SubGoal implements Parcelable {

    private String sLabel;
    private Date sDateStart;
    private Date sDateEnd;
    private boolean sIsCompleted;
    private ArrayList<Task> sListTasks;


    public SubGoal ()   {
        this.sListTasks = new ArrayList<>();
        this.sIsCompleted = false;
    }

    public SubGoal (String sLabel, Date sDateStart, Date sDateEnd)   {
        this();
        this.sLabel = sLabel;
        this.sDateStart = sDateStart;
        this.sDateEnd = sDateEnd;
    }

    // ------------------------ Getters and Setter ------------------------
    public String getsLabel() {
        return sLabel;
    }
    public Date getsDateStart() {
        return sDateStart;
    }
    public Date getsDateEnd() {
        return sDateEnd;
    }
    public boolean issIsCompleted() {
        return sIsCompleted;
    }
    public ArrayList<Task> getsListTasks() {
        return sListTasks;
    }

    public void setsLabel(String sLabel) {
        this.sLabel = sLabel;
    }
    public void setsDateStart(Date sDateStart) {
        this.sDateStart = sDateStart;
    }
    public void setsDateEnd(Date sDateEnd) {
        this.sDateEnd = sDateEnd;
    }
    public void setsIsCompleted(boolean sIsCompleted) {
        this.sIsCompleted = sIsCompleted;
    }
    public void setsListTasks(ArrayList<Task> sListTasks) {
        this.sListTasks = sListTasks;
    }

    // ------------------------- Parcelable -------------------------------

    public static final Creator<SubGoal> CREATOR = new Creator<SubGoal>() {
        @Override
        public SubGoal createFromParcel(Parcel in) {
            return new SubGoal(in);
        }

        @Override
        public SubGoal[] newArray(int size) {
            return new SubGoal[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    protected SubGoal(Parcel in) {
        sLabel = in.readString();
        sDateStart = (Date) in.readSerializable();
        sDateEnd = (Date) in.readSerializable();
        sIsCompleted = in.readByte() != 0;
        sListTasks = in.createTypedArrayList(Task.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(sLabel);
        dest.writeSerializable(sDateStart);
        dest.writeSerializable(sDateEnd);
        dest.writeByte((byte) (sIsCompleted ? 1 : 0));
        dest.writeTypedList(sListTasks);
    }


}
