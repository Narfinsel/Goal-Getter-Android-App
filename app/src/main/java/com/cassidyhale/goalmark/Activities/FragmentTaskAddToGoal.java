package com.cassidyhale.goalmark.Activities;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cassidyhale.goalmark.Model.DataList;
import com.cassidyhale.goalmark.Model.Goal;
import com.cassidyhale.goalmark.Model.Task;
import com.cassidyhale.goalmark.R;

import java.util.Calendar;


public class FragmentTaskAddToGoal extends DialogFragment {

    public interface NoticeDialogListener {
        void onClickAddNewTask(DialogFragment dialog);
        void onClickCancelAddNewTask(DialogFragment dialog);
    }
    NoticeDialogListener mListener;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        final int positionGoal = getArguments().getInt("positionGoal");

        View view = inflater.inflate (R.layout.template_dialog_task_add, null);
        builder.setView (view);
        builder.setMessage(R.string.string_DialogBox_Task_Title);

        final EditText edTextTaskLabel     = view.findViewById(R.id.editTextTaskAddLabel);
        final EditText edTextTaskNumRepeat = view.findViewById(R.id.editTextTaskAddNumRepetitions);

//        builder.setPositiveButton(R.string.string_DialogBox_Task_Add_Button, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        String stringTaskLabel  = edTextTaskLabel.getText().toString().trim();
//                        int numTaskRepetitions  = Integer.parseInt( edTextTaskNumRepeat.getText().toString().trim());
//
//                        if (!TextUtils.isEmpty( stringTaskLabel ) &&
//                                numTaskRepetitions > 0) {
//                            ((Goal) ((DataList) (getActivity()).getApplicationContext())
//                                    .getListOngoing().get(positionGoal))
//                                    .getgListSubGoals().get(positionSubGoal)
//                                    .getsListTasks().add(new Task(stringTaskLabel, numTaskRepetitions));
//                            ((DataList) (getActivity()).getApplicationContext()).setNeedsUpdateToFire(true);
//                            mListener.onClickAddNewTask(FragmentTaskAdd.this);
//                        }
//                    }
//                })
//                .setNegativeButton(R.string.string_DialogBox_Task_Cancel_Button, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        mListener.onClickCancelAddNewTask(FragmentTaskAdd.this);
//                    }
//                });

        Button buttonTaskAdd = view.findViewById( R.id.buttonScheduleTaskDialog);
        buttonTaskAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    String stringTaskLabel  = edTextTaskLabel.getText().toString().trim();
                    String stringTaskCount  = edTextTaskNumRepeat.getText().toString().trim();
                    int numTaskRepetitions = 0;
                    if (!TextUtils.isEmpty( stringTaskCount ))
                        numTaskRepetitions  = Integer.parseInt(stringTaskCount);

                    if (!TextUtils.isEmpty( stringTaskLabel ) &&
                            numTaskRepetitions > 0) {
                        ((DataList) (getActivity()).getApplicationContext())
                                .getListOngoing().get(positionGoal)
                                .getgListTasks().add( new Task( stringTaskLabel, numTaskRepetitions));
                        ((DataList) (getActivity()).getApplicationContext()).setNeedsUpdateToFire(true);
                        ((DataList) (getActivity()).getApplicationContext()).setDateLastUpdate( Calendar.getInstance().getTime().getTime() );
                        mListener.onClickAddNewTask(FragmentTaskAddToGoal.this);
                    }
            }
        });
        Button buttonTaskCancel = view.findViewById( R.id.buttonCancelTaskDialog);
        buttonTaskCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClickCancelAddNewTask(FragmentTaskAddToGoal.this);
            }
        });
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NoticeDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString() + " must implement NoticeDialogListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = null;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(getContext().toString() + " must implement NoticeDialogListener");
        }
    }


}

