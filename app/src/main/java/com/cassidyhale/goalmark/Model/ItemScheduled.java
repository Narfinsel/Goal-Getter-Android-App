package com.cassidyhale.goalmark.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class ItemScheduled implements Parcelable {

    private Date dateScheduled;
    private int locusGoal;
    private int locusSubGoal;
    private int locusTask;

    public ItemScheduled () {
       this (-1, -1, -1);
    }

    public ItemScheduled(int locusGoal, int locusSubGoal, int locusTask) {
        this.dateScheduled = null;
        this.locusGoal = locusGoal;
        this.locusSubGoal = locusSubGoal;
        this.locusTask = locusTask;
    }

    public String displayItemScheduledDetails ()    {
        StringBuilder str = new StringBuilder();
        str.append(" ( " + this.getDateScheduled() + ", "
                        + this.getLocusGoal() + ", "
                        + this.getLocusSubGoal() + ", "
                        + this.getLocusTask() + " )");
        return str.toString();
    }

    public static boolean compareItemsScheduled_AreTheSame (ItemScheduled itemScheduled_o, ItemScheduled itemScheduled_n)  {
        boolean same;
        if (( itemScheduled_o == null) || ( itemScheduled_n == null))
            same = false;
        else if ( ( itemScheduled_o.getLocusGoal())     == ( itemScheduled_n.getLocusGoal()) &&
                  ( itemScheduled_o.getLocusSubGoal())  == ( itemScheduled_n.getLocusSubGoal()) &&
                  ( itemScheduled_o.getLocusTask())     == ( itemScheduled_n.getLocusTask()) )
            same = true;
        else
            same = false;
        return same;
    }


    public Date getDateScheduled() {
        return dateScheduled;
    }
    public int getLocusGoal() {
        return locusGoal;
    }
    public int getLocusSubGoal() {
        return locusSubGoal;
    }
    public int getLocusTask() {
        return locusTask;
    }

    public void setDateScheduled(Date dateScheduled) {
        this.dateScheduled = dateScheduled;
    }
    public void setLocusGoal(int locusGoal) {
        this.locusGoal = locusGoal;
    }
    public void setLocusSubGoal(int locusSubGoal) {
        this.locusSubGoal = locusSubGoal;
    }
    public void setLocusTask(int locusTask) {
        this.locusTask = locusTask;
    }


    // ------------------------- Parcelable -------------------------------

    public static final Creator<ItemScheduled> CREATOR = new Creator<ItemScheduled>() {
        @Override
        public ItemScheduled createFromParcel(Parcel in) {
            return new ItemScheduled(in);
        }

        @Override
        public ItemScheduled[] newArray(int size) {
            return new ItemScheduled[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    protected ItemScheduled(Parcel in) {
        dateScheduled = (Date) in.readSerializable();
        locusGoal = in.readInt();
        locusSubGoal = in.readInt();
        locusTask = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(dateScheduled);
        dest.writeInt(locusGoal);
        dest.writeInt(locusSubGoal);
        dest.writeInt(locusTask);
    }
}
