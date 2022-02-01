package com.cassidyhale.goalmark.Model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.cassidyhale.goalmark.Activities.__GLOBAL_Const;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataListNoApp implements Parcelable { //

    public String userId;
    public long dateLastUpdate;
    public List<Goal> listOngoing;
    public List<Goal> listCompleted;
    public Schedule schedule;
    private boolean needsUpdateToFire;


    // ------------------------- CONSTRUCTORS -------------------------------
    public DataListNoApp() {
       this("");
    }

    public DataListNoApp(String userId) {
        this.userId = userId;
        this.dateLastUpdate = Calendar.getInstance().getTime().getTime();
        this.listOngoing = new ArrayList<>();
        this.listCompleted = new ArrayList<>();
        this.schedule = new Schedule();
        this.needsUpdateToFire = false;

    }

    public DataListNoApp(String userId, long dateLastUpdate, List<Goal> listOngoing, List<Goal> listCompleted, Schedule schedule, boolean needsUpdate) {
        this.userId = userId;
        this.dateLastUpdate = dateLastUpdate;
        this.listOngoing = listOngoing;
        this.listCompleted = listCompleted;
        this.schedule = schedule;
        this.needsUpdateToFire = needsUpdate;
    }


    // --------------------- SETTER & GETTERS -----------------------------
    public String getUserId() {
        return userId;
    }
    public long getDateLastUpdate() {   return dateLastUpdate;  }
    public List<Goal> getListOngoing() {
        return listOngoing;
    }
    public List<Goal> getListCompleted() {
        return listCompleted;
    }
    public Schedule getSchedule() {
        return schedule;
    }
    public void setNeedsUpdateToFire(boolean needsUpdateToFire) {   this.needsUpdateToFire = needsUpdateToFire; }

    public void setUserId (String userId) {
        this.userId = userId;
    }
    public void setDateLastUpdate(long dateLastUpdate){ this.dateLastUpdate = dateLastUpdate;   }
    public void setListOngoing (List<Goal> listOngoing) {
        this.listOngoing = listOngoing;
    }
    public void setListCompleted (List<Goal> listCompleted) {
        this.listCompleted = listCompleted;
    }
    public void setSchedule (Schedule schedule) {
        this.schedule = schedule;
    }
    public boolean isNeedsUpdateToFire() {
        return needsUpdateToFire;
    }


    public void displayToString ()  {
        StringBuilder liOnCom = new StringBuilder("DATALIST = ");

        liOnCom.append("     >>> ONGOING: ");
        if (this.listOngoing != null)
            for (Goal goal : this.listOngoing)
                liOnCom.append(" ").append(goal.getgLabel());
        else
            liOnCom.append("-- null object--");


        liOnCom.append("     >>> COMPLETED: ");
        if (this.listOngoing != null)
            for (Goal goal : this.listCompleted)
                liOnCom.append(" ").append(goal.getgLabel());
        else
            liOnCom.append("-- null object--");

        liOnCom.append("     >>> UEID: ").append(this.userId);
        liOnCom.append("     >>> NEED_UPDATA: ").append(this.needsUpdateToFire);
        Log.d(__GLOBAL_Const.TAG_handleUserfb, liOnCom.toString());
    }

    public void displayScheduleToString ()  {
        StringBuilder stringSchedule = new StringBuilder();
        stringSchedule.append("Mon:   ");
        for (int i =0; i< this.schedule.getArray_1_Mon().size(); i++)    {
            ItemScheduled item = this.schedule.getArray_1_Mon().get(i);
            int p1 = item.getLocusGoal();
            int p2 = item.getLocusSubGoal();
            int p3 = item.getLocusTask();
            stringSchedule.append( getListOngoing().get(p1).getgListSubGoals().get(p2).getsListTasks().get(p3).getTaskLabel());
        }
        stringSchedule.append("\n");

        stringSchedule.append("Tue:   ");
        for (int i =0; i< this.schedule.getArray_2_Tue().size(); i++)    {
            ItemScheduled item = this.schedule.getArray_2_Tue().get(i);
            int p1 = item.getLocusGoal();
            int p2 = item.getLocusSubGoal();
            int p3 = item.getLocusTask();
            stringSchedule.append( getListOngoing().get(p1).getgListSubGoals().get(p2).getsListTasks().get(p3).getTaskLabel());
        }
        stringSchedule.append("\n");

        stringSchedule.append("Wed:   ");
        for (int i =0; i< this.schedule.getArray_3_Wed().size(); i++)    {
            ItemScheduled item = this.schedule.getArray_3_Wed().get(i);
            int p1 = item.getLocusGoal();
            int p2 = item.getLocusSubGoal();
            int p3 = item.getLocusTask();
            stringSchedule.append( getListOngoing().get(p1).getgListSubGoals().get(p2).getsListTasks().get(p3).getTaskLabel());
        }
        stringSchedule.append("\n");

        stringSchedule.append("Thu:   ");
        for (int i =0; i< this.schedule.getArray_4_Thur().size(); i++)    {
            ItemScheduled item = this.schedule.getArray_4_Thur().get(i);
            int p1 = item.getLocusGoal();
            int p2 = item.getLocusSubGoal();
            int p3 = item.getLocusTask();
            stringSchedule.append( getListOngoing().get(p1).getgListSubGoals().get(p2).getsListTasks().get(p3).getTaskLabel());
        }
        stringSchedule.append("\n");

        stringSchedule.append("Fri:   ");
        for (int i =0; i< this.schedule.getArray_5_Fri().size(); i++)    {
            ItemScheduled item = this.schedule.getArray_5_Fri().get(i);
            int p1 = item.getLocusGoal();
            int p2 = item.getLocusSubGoal();
            int p3 = item.getLocusTask();
            stringSchedule.append( getListOngoing().get(p1).getgListSubGoals().get(p2).getsListTasks().get(p3).getTaskLabel());
        }
        stringSchedule.append("\n");

        stringSchedule.append("Sat:   ");
        for (int i =0; i< this.schedule.getArray_6_Sat().size(); i++)    {
            ItemScheduled item = this.schedule.getArray_6_Sat().get(i);
            int p1 = item.getLocusGoal();
            int p2 = item.getLocusSubGoal();
            int p3 = item.getLocusTask();
            stringSchedule.append( getListOngoing().get(p1).getgListSubGoals().get(p2).getsListTasks().get(p3).getTaskLabel());
        }
        stringSchedule.append("\n");

        stringSchedule.append("Sun:   ");
        for (int i =0; i< this.schedule.getArray_7_Sun().size(); i++)    {
            ItemScheduled item = this.schedule.getArray_7_Sun().get(i);
            int p1 = item.getLocusGoal();
            int p2 = item.getLocusSubGoal();
            int p3 = item.getLocusTask();
            stringSchedule.append( getListOngoing().get(p1).getgListSubGoals().get(p2).getsListTasks().get(p3).getTaskLabel());
        }
        stringSchedule.append("\n");

        System.out.print( stringSchedule.toString() );
    }

    public void displayScheduleForDay (int day)    {

        ArrayList<ItemScheduled> array;
        switch(day) {
            case 1: array = this.getSchedule().getArray_1_Mon(); break;
            case 2: array = this.getSchedule().getArray_2_Tue(); break;
            case 3: array = this.getSchedule().getArray_3_Wed(); break;
            case 4: array = this.getSchedule().getArray_4_Thur(); break;
            case 5: array = this.getSchedule().getArray_5_Fri(); break;
            case 6: array = this.getSchedule().getArray_6_Sat(); break;
            case 7: array = this.getSchedule().getArray_7_Sun(); break;
            default: array = null; break;
        }
        Log.d(__GLOBAL_Const.TAG_handleUserfb, "---------------------------------------------------------------------------------- ");
        if (array != null) {
            for (int i = 0; i < array.size(); i++) {
                Log.d(__GLOBAL_Const.TAG_handleUserfb, "    DAY = " + day + "     :   " + array.get(i).displayItemScheduledDetails());
            }
        }
        else    {
            Log.d(__GLOBAL_Const.TAG_handleUserfb, "    DAY = " + day + "     :   Array is NULL");
        }
        Log.d(__GLOBAL_Const.TAG_handleUserfb, "---------------------------------------------------------------------------------- ");
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put( __GLOBAL_Const.child__gUserId, userId);
        result.put( __GLOBAL_Const.child__gDateLastUpdate, dateLastUpdate);
        result.put( __GLOBAL_Const.child__gListOngoing, listOngoing);
        result.put( __GLOBAL_Const.child__gListCompleted, listCompleted);
        result.put( __GLOBAL_Const.child__gSchedule, schedule);
        result.put( __GLOBAL_Const.child__needsUpdateToFire, needsUpdateToFire);
        return result;
    }

    public static DataListNoApp fromMap (Map<String, Object> map )   {
        DataListNoApp createdDataList = new DataListNoApp(
                (String)    map.get( __GLOBAL_Const.child__gUserId ),
                (long)      map.get( __GLOBAL_Const.child__gDateLastUpdate ),
                (List<Goal>) map.get( __GLOBAL_Const.child__gListOngoing ),
                (List<Goal>) map.get( __GLOBAL_Const.child__gListCompleted ),
                (Schedule)   map.get( __GLOBAL_Const.child__gSchedule),
                (boolean)    map.get ( __GLOBAL_Const.child__needsUpdateToFire )
        );
        return createdDataList;
    }


    // ------------------------- Parcelable -------------------------------
    public static final Creator<DataListNoApp> CREATOR = new Creator<DataListNoApp>() {
        @Override
        public DataListNoApp createFromParcel(Parcel in) {
            return new DataListNoApp(in);
        }

        @Override
        public DataListNoApp[] newArray(int size) {
            return new DataListNoApp[size];
        }
    };

    protected DataListNoApp(Parcel in) {
        userId = in.readString();
        dateLastUpdate = in.readLong();
        listOngoing = in.createTypedArrayList(Goal.CREATOR);
        listCompleted = in.createTypedArrayList(Goal.CREATOR);
        schedule = (Schedule) in.readSerializable();
        needsUpdateToFire = in.readByte() != 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeLong (dateLastUpdate);
        dest.writeTypedList(listOngoing);
        dest.writeTypedList(listCompleted);
        dest.writeSerializable(schedule);
        dest.writeByte((byte) (needsUpdateToFire ? 1 : 0));
    }

}
