package com.cassidyhale.goalmark.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Task implements Parcelable {

    private String taskLabel;
    private int taskNumOfTimesDone;
    private int taskNumRepetitions;
    private double taskCompletion;
    private boolean taskIsDone;

    public Task ()  { }

    public Task (String taskLabel, int taskRepetitions) {
        this.taskLabel = taskLabel;
        this.taskNumOfTimesDone = 0;
        this.taskNumRepetitions = taskRepetitions;
        this.taskCompletion = 0.0;
        this.taskIsDone = false;
    }

    public void checkOffTask () {
        this.increaseTaskNumOfTimesDone();
        if ( this.taskNumOfTimesDone < this.taskNumRepetitions )    {
            this.taskCompletion += ( 100.0 / this.taskNumRepetitions);
        }
        else if ( this.taskNumOfTimesDone == this.taskNumRepetitions )  {
            this.taskCompletion = 100.0;
        }
    }

    public String getTaskLabel() {
        return taskLabel;
    }
    public int getTaskNumOfTimesDone() {
        return taskNumOfTimesDone;
    }
    public int getTaskNumRepetitions() {
        return taskNumRepetitions;
    }
    public double getTaskCompletion() {
        return taskCompletion;
    }
    public boolean isTaskIsDone() {
        return taskIsDone;
    }

    public void setTaskLabel(String taskLabel) {
        this.taskLabel = taskLabel;
    }
    public void increaseTaskNumOfTimesDone ()   {
        this.taskNumOfTimesDone += 1;
    }
    public void setTaskNumRepetitions(int taskRepetead) {
        this.taskNumRepetitions = taskRepetead;
    }
    public void setTaskCompletion(double taskCompletion) {
        this.taskCompletion = taskCompletion;
        if (this.taskCompletion == 100.0)
            this.taskIsDone = true;
        else
            this.taskIsDone = false;
    }


    // ------------------------- Parcelable -------------------------------

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    protected Task(Parcel in) {
        taskLabel = in.readString();
        taskNumOfTimesDone = in.readInt();
        taskNumRepetitions = in.readInt();
        taskCompletion = in.readDouble();
        taskIsDone = in.readByte() != 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(taskLabel);
        dest.writeInt(taskNumOfTimesDone);
        dest.writeInt(taskNumRepetitions);
        dest.writeDouble(taskCompletion);
        dest.writeByte((byte) (taskIsDone ? 1 : 0));
    }
}
