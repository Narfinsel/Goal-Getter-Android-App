package com.cassidyhale.goalmark.Model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import java.io.Serializable;
import java.util.ArrayList;
import static com.cassidyhale.goalmark.Activities.__GLOBAL_Const.defSubgoalLocus;

public class Schedule implements Parcelable, Serializable {

    private int weekInYear;
    private int hourGranularity;
    private int hourCount;
    private ArrayList<ItemScheduled> array_1_Mon;
    private ArrayList<ItemScheduled> array_2_Tue;
    private ArrayList<ItemScheduled> array_3_Wed;
    private ArrayList<ItemScheduled> array_4_Thur;
    private ArrayList<ItemScheduled> array_5_Fri;
    private ArrayList<ItemScheduled> array_6_Sat;
    private ArrayList<ItemScheduled> array_7_Sun;

    private int arrayHourSize;

    // ------------------------- CONSTRUCTORS -------------------------------
    public Schedule()   {
        this.hourGranularity = 2;
        this.hourCount = 16;
        this.arrayHourSize = this.calculateHourArraySizeForFilling();
        this.array_1_Mon = new ArrayList<>();
        this.array_2_Tue = new ArrayList<>();
        this.array_3_Wed = new ArrayList<>();
        this.array_4_Thur = new ArrayList<>();
        this.array_5_Fri = new ArrayList<>();
        this.array_6_Sat = new ArrayList<>();
        this.array_7_Sun = new ArrayList<>();
        this.fill_all_7_array_with_nulls();
    }

    public Schedule(int weekInYear,
                    int hourGranularity,
                    int hourCount,
                    ArrayList<ItemScheduled> array_1_Mon,
                    ArrayList<ItemScheduled> array_2_Tue,
                    ArrayList<ItemScheduled> array_3_Wed,
                    ArrayList<ItemScheduled> array_4_Thur,
                    ArrayList<ItemScheduled> array_5_Fri,
                    ArrayList<ItemScheduled> array_6_Sat,
                    ArrayList<ItemScheduled> array_7_Sun) {
        this.weekInYear = weekInYear;
        this.hourGranularity = hourGranularity;
        this.hourCount = hourCount;
        this.array_1_Mon = array_1_Mon;     this.fill_any_array_with_nulls( this.array_1_Mon );
        this.array_2_Tue = array_2_Tue;     this.fill_any_array_with_nulls( this.array_2_Tue );
        this.array_3_Wed = array_3_Wed;     this.fill_any_array_with_nulls( this.array_3_Wed );
        this.array_4_Thur = array_4_Thur;   this.fill_any_array_with_nulls( this.array_4_Thur );
        this.array_5_Fri = array_5_Fri;     this.fill_any_array_with_nulls( this.array_5_Fri );
        this.array_6_Sat = array_6_Sat;     this.fill_any_array_with_nulls( this.array_6_Sat );
        this.array_7_Sun = array_7_Sun;     this.fill_any_array_with_nulls( this.array_7_Sun );
        // this.fill_all_7_array_with_nulls();
    }

    public void updateSchedule (int dayInWeek, int positionTime, ItemScheduled itemScheduled)   {

        if ( positionTime <= this.arrayHourSize)
        switch ( dayInWeek ) {
            case 1:
//                if (positionTime >= array_1_Mon.size())
//                    this.fill_array_with_null_till_position(array_1_Mon, positionTime);
                this.array_1_Mon.set( positionTime, itemScheduled);
                break;
            case 2:
//                if (positionTime >= array_2_Tue.size())
//                    this.fill_array_with_null_till_position(array_2_Tue, positionTime);
                this.array_2_Tue.set( positionTime, itemScheduled);
                break;
            case 3:
//                if (positionTime >= array_3_Wed.size())
//                    this.fill_array_with_null_till_position(array_3_Wed, positionTime);
                this.array_3_Wed.set( positionTime, itemScheduled);
                break;
            case 4:
//                if (positionTime >= array_4_Thur.size())
//                    this.fill_array_with_null_till_position(array_4_Thur, positionTime);
                this.array_4_Thur.set( positionTime, itemScheduled);
                break;
            case 5:
//                if (positionTime >= array_5_Fri.size())
//                    this.fill_array_with_null_till_position(array_5_Fri, positionTime);
                this.array_5_Fri.set( positionTime, itemScheduled);
                break;
            case 6:
//                if (positionTime >= array_6_Sat.size())
//                    this.fill_array_with_null_till_position(array_6_Sat, positionTime);
                this.array_6_Sat.set( positionTime, itemScheduled);
                break;
            case 7:
//                if (positionTime >= array_7_Sun.size())
//                    this.fill_array_with_null_till_position(array_7_Sun, positionTime);
                this.array_7_Sun.set( positionTime, itemScheduled);
                break;
             default:
                break;
        }
    }

    public ItemScheduled getScheduledItemForDayAndHour (int dayInWeek, int positionTime) {
        ItemScheduled itemScheduled;
        switch ( dayInWeek ) {
            case 1:
                itemScheduled = this.array_1_Mon.get( positionTime );
                break;
            case 2:
                itemScheduled = this.array_2_Tue.get( positionTime );
                break;
            case 3:
                itemScheduled = this.array_3_Wed.get( positionTime );
                break;
            case 4:
                itemScheduled = this.array_4_Thur.get( positionTime );
                break;
            case 5:
                itemScheduled = this.array_5_Fri.get( positionTime );
                break;
            case 6:
                itemScheduled = this.array_6_Sat.get( positionTime );
                break;
            case 7:
                itemScheduled = this.array_7_Sun.get( positionTime );
                break;
            default:
                itemScheduled = null;
                break;
        }
//        if (itemScheduled != null)
//            Log.d("Userx", "    ItemSchedule [ " + dayInWeek + " ][ " + positionTime + " ]    :   " + this.taskFromItem(  ) );
        return itemScheduled;
    }

    private int calculateHourArraySizeForFilling () {
        int count = this.hourCount / this.hourGranularity + 1;
        return count;
    }

    public void reset_scheduler_for_new_week ()  {
        this.reset_array( this.array_1_Mon );
        this.reset_array( this.array_2_Tue );
        this.reset_array( this.array_3_Wed );
        this.reset_array( this.array_4_Thur );
        this.reset_array( this.array_5_Fri );
        this.reset_array( this.array_6_Sat );
        this.reset_array( this.array_7_Sun );
    }

    private void reset_array (ArrayList<ItemScheduled> arrayList) {
        for (int i=0; i < arrayList.size(); i++)
            arrayList.set(i, new ItemScheduled());
    }

    private void fill_array_with_null_till_position (ArrayList<ItemScheduled> array, int position)  {
        for (int i=array.size(); i<=position; i++)
            array.add(i, new ItemScheduled());
    }

    private void fill_all_7_array_with_nulls () {
        this.fill_any_array_with_nulls( this.array_1_Mon );
        this.fill_any_array_with_nulls( this.array_2_Tue );
        this.fill_any_array_with_nulls( this.array_3_Wed );
        this.fill_any_array_with_nulls( this.array_4_Thur );
        this.fill_any_array_with_nulls( this.array_5_Fri );
        this.fill_any_array_with_nulls( this.array_6_Sat );
        this.fill_any_array_with_nulls( this.array_7_Sun );
    }

    private void fill_any_array_with_nulls (ArrayList<ItemScheduled> array)   {
        for (int i=0; i < this.arrayHourSize; i++)
            if (i >= array.size())
                array.add(new ItemScheduled());
    }

    // --------------------- SETTER & GETTERS -----------------------------
    public int getWeekInYear () { return weekInYear; }
    public int getHourCount ()  { return hourCount; }
    public int getHourGranularity () { return hourGranularity; }
    public ArrayList<ItemScheduled> getArray_1_Mon() {
        return array_1_Mon;
    }
    public ArrayList<ItemScheduled> getArray_2_Tue() {
        return array_2_Tue;
    }
    public ArrayList<ItemScheduled> getArray_3_Wed() {
        return array_3_Wed;
    }
    public ArrayList<ItemScheduled> getArray_4_Thur() {
        return array_4_Thur;
    }
    public ArrayList<ItemScheduled> getArray_5_Fri() {
        return array_5_Fri;
    }
    public ArrayList<ItemScheduled> getArray_6_Sat() {
        return array_6_Sat;
    }
    public ArrayList<ItemScheduled> getArray_7_Sun() {
        return array_7_Sun;
    }
    public int getArrayHourSize() {
        return arrayHourSize;
    }

    public void setWeekInYear(int weekInYear) {
        this.weekInYear = weekInYear;
    }
    public void setHourCount(int hourCount) {
        this.hourCount = hourCount;
    }
    public void setHourGranularity(int hourGranularity) {
        this.hourGranularity = hourGranularity;
    }
    public void setArray_1_Mon(ArrayList<ItemScheduled> array_1_Mon) { this.array_1_Mon = array_1_Mon;  }
    public void setArray_2_Tue(ArrayList<ItemScheduled> array_2_Tue) { this.array_2_Tue = array_2_Tue;  }
    public void setArray_3_Wed(ArrayList<ItemScheduled> array_3_Wed) { this.array_3_Wed = array_3_Wed;  }
    public void setArray_4_Thur(ArrayList<ItemScheduled> array_4_Thur) { this.array_4_Thur = array_4_Thur; }
    public void setArray_5_Fri(ArrayList<ItemScheduled> array_5_Fri) { this.array_5_Fri = array_5_Fri; }
    public void setArray_6_Sat(ArrayList<ItemScheduled> array_6_Sat) { this.array_6_Sat = array_6_Sat; }
    public void setArray_7_Sun(ArrayList<ItemScheduled> array_7_Sun) { this.array_7_Sun = array_7_Sun; }

    // ----------------------- Test and Print -----------------------------
    public void test_print_array ()   {
        StringBuilder str = new StringBuilder();
        str.append("\n");
        str.append("\n     DAY - "+ 1 +" -       ");
        for (int i=0; i<array_1_Mon.size(); i++)
               str.append("("+ array_1_Mon.get(i).getLocusGoal() +", "
                             + array_1_Mon.get(i).getLocusSubGoal() +", "
                             + array_1_Mon.get(i).getLocusTask() +")   ");

        str.append("\n     DAY - "+ 2 +" -       ");
        for (int i=0; i<array_2_Tue.size(); i++)
            str.append("("+ array_2_Tue.get(i).getLocusGoal() +", "
                          + array_2_Tue.get(i).getLocusSubGoal() +", "
                          + array_2_Tue.get(i).getLocusTask() +")   ");
        str.append("\n     DAY - "+ 3 +" -       ");
        for (int i=0; i<array_3_Wed.size(); i++)
            str.append("("+ array_3_Wed.get(i).getLocusGoal() +", "
                          + array_3_Wed.get(i).getLocusSubGoal() +", "
                          + array_3_Wed.get(i).getLocusTask() +")   ");

        str.append("\n     DAY - "+ 4 +" -       ");
        for (int i=0; i<array_4_Thur.size(); i++)
            str.append("("  + array_4_Thur.get(i).getLocusGoal() +", "
                            + array_4_Thur.get(i).getLocusSubGoal() +", "
                            + array_4_Thur.get(i).getLocusTask() +")   ");

        str.append("\n     DAY - "+ 5 +" -       ");
        for (int i=0; i<array_5_Fri.size(); i++)
            str.append("("  + array_5_Fri.get(i).getLocusGoal() +", "
                            + array_5_Fri.get(i).getLocusSubGoal() +", "
                            + array_5_Fri.get(i).getLocusTask() +")   ");

        str.append("\n     DAY - "+ 6 +" -       ");
        for (int i=0; i<array_6_Sat.size(); i++)
            str.append("("  + array_6_Sat.get(i).getLocusGoal() +", "
                            + array_6_Sat.get(i).getLocusSubGoal() +", "
                            + array_6_Sat.get(i).getLocusTask() +")   ");

        str.append("\n     DAY - "+ 7 +" -       ");
        for (int i=0; i<array_7_Sun.size(); i++)
            str.append("("  + array_7_Sun.get(i).getLocusGoal() +", "
                            + array_7_Sun.get(i).getLocusSubGoal() +", "
                            + array_7_Sun.get(i).getLocusTask() +")   ");
        str.append("\n");
        Log.d("Userx", "Schedule: " + str.toString());
    }

    public void test_print_scheduler (DataList dataList) {
        int length = this.getArrayHourSize();
        String format = "%-20s %20s %20s %20s %20s %20s %20s %n";
        StringBuilder stringBuilder = new StringBuilder();

        //((ActivityScheduler) mContext).getSchedule().getArray_1_Mon()

        String string;
        for (int i=0; i< length; i++ ) {
            string = String.format(format,
                    this.test_print_if_tasks_exists(    dataList,   this.getArray_1_Mon(), i),
                    this.test_print_if_tasks_exists(    dataList,   this.getArray_2_Tue(), i),
                    this.test_print_if_tasks_exists(    dataList,   this.getArray_3_Wed(), i),
                    this.test_print_if_tasks_exists(    dataList,   this.getArray_4_Thur(), i),
                    this.test_print_if_tasks_exists(    dataList,   this.getArray_5_Fri(), i),
                    this.test_print_if_tasks_exists(    dataList,   this.getArray_6_Sat(), i),
                    this.test_print_if_tasks_exists(    dataList,   this.getArray_7_Sun(), i));
            stringBuilder.append(string);
        }
        Log.d("Userx", stringBuilder.toString());
    }

    private String test_print_if_tasks_exists (DataList dataList, ArrayList<ItemScheduled> arrayList, int position) {
        String print;
        ItemScheduled itemScheduled = arrayList.get(position);
        Task task = this.taskFromItem( dataList, itemScheduled );
        if (task == null)
            print = " ------- ";
        else
            print = task.getTaskLabel().trim();
        return print;
    }

    public static Task taskFromItem (DataList dataList, ItemScheduled itemScheduled) {
        if (dataList != null && itemScheduled != null) {
            int g = itemScheduled.getLocusGoal();
            int s = itemScheduled.getLocusSubGoal();
            int t = itemScheduled.getLocusTask();
            Task task;
            if (g >=0 && s >=0 && s!=defSubgoalLocus && t >=0 ) {
                task = dataList.getListOngoing().get(g)
                        .getgListSubGoals().get(s)
                        .getsListTasks().get(t);
            }
            else if ( s==defSubgoalLocus) {
                task = dataList
                        .getListOngoing().get( g )
                        .getgListTasks().get( t );
            }
            else
                task = null;
            return task;
        }
        else
            return null;
    }   // private

    // ------------------------- Parcelable -------------------------------
    public static final Creator<Schedule> CREATOR = new Creator<Schedule>() {
        @Override
        public Schedule createFromParcel(Parcel in) {
            return new Schedule(in);
        }

        @Override
        public Schedule[] newArray(int size) {
            return new Schedule[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(weekInYear);
        dest.writeInt(hourGranularity);
        dest.writeInt(hourCount);
        dest.writeTypedList(array_1_Mon);
        dest.writeTypedList(array_2_Tue);
        dest.writeTypedList(array_3_Wed);
        dest.writeTypedList(array_4_Thur);
        dest.writeTypedList(array_5_Fri);
        dest.writeTypedList(array_6_Sat);
        dest.writeTypedList(array_7_Sun);
    }

    protected Schedule(Parcel in) {
        weekInYear = in.readInt();
        hourGranularity = in.readInt();
        hourCount = in.readInt();
        array_1_Mon = in.createTypedArrayList(ItemScheduled.CREATOR);
        array_2_Tue = in.createTypedArrayList(ItemScheduled.CREATOR);
        array_3_Wed = in.createTypedArrayList(ItemScheduled.CREATOR);
        array_4_Thur = in.createTypedArrayList(ItemScheduled.CREATOR);
        array_5_Fri = in.createTypedArrayList(ItemScheduled.CREATOR);
        array_6_Sat = in.createTypedArrayList(ItemScheduled.CREATOR);
        array_7_Sun = in.createTypedArrayList(ItemScheduled.CREATOR);
        this.fill_all_7_array_with_nulls();
    }


}
