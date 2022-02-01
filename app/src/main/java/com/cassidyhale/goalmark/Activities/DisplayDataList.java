package com.cassidyhale.goalmark.Activities;

import android.util.Log;

import com.cassidyhale.goalmark.Model.DataList;
import com.cassidyhale.goalmark.Model.Goal;

import java.util.List;

public class DisplayDataList {

    public static void displayDl (DataList dataList, String activStr)  {

        if (dataList != null) {
            StringBuilder strUid = new StringBuilder("");
            StringBuilder strLiOng = new StringBuilder("List-Ongoing: ");
            StringBuilder strLiComp = new StringBuilder("List-Complet: ");

            strUid.append( dataList.getUserId() );
            for (Object goal : dataList.getListOngoing() )  {
                strLiOng.append( " " + ((Goal) goal).getgLabel() );
            }
            for (Object goal : dataList.getListCompleted() )  {
                strLiComp.append( " " + ((Goal) goal).getgLabel() );
            }

            Log.d(__GLOBAL_Const.TAG_handleUserfb, activStr + " DDL - " + strUid);
            Log.d(__GLOBAL_Const.TAG_handleUserfb, activStr + " DDL - " + strLiOng);
            Log.d(__GLOBAL_Const.TAG_handleUserfb, activStr + " DDL - " + strLiComp);
        }
        else
            Log.d(__GLOBAL_Const.TAG_handleUserfb, activStr + " DDL - datalist is null");
    }
}
