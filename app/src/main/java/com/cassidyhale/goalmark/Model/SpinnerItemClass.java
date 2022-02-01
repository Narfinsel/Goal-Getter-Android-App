package com.cassidyhale.goalmark.Model;

public class SpinnerItemClass {

    private String mGoalType;
    private int mItemIcon;

    public SpinnerItemClass(String goalType, int itemIcon) {
        mGoalType = goalType;
        mItemIcon = itemIcon;
    }

    public String getGoalType() {
        return mGoalType;
    }
    public int getItemIcon() {
        return mItemIcon;
    }

    public void setGoalType(String goalType) {
        mGoalType = goalType;
    }
    public void setItemIcon(int itemIcon) {
        mItemIcon = itemIcon;
    }


}
