package com.cassidyhale.goalmark.Activities;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.cassidyhale.goalmark.Model.DataList;


public class ServiceCheckTask extends IntentService {

    public ServiceCheckTask() {
        super("ServiceCheckTask");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Toast.makeText(this, "Service is running!", Toast.LENGTH_LONG).show();
        Log.d(__GLOBAL_Const.TAG_handleUserfb, "Running Intent");
    }
}
