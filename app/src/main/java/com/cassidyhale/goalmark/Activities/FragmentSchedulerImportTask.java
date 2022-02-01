package com.cassidyhale.goalmark.Activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;

import com.cassidyhale.goalmark.Adapter.ExpandedListSchedulingTaskAdapter;
import com.cassidyhale.goalmark.Model.ItemScheduled;
import com.cassidyhale.goalmark.R;

public class FragmentSchedulerImportTask extends DialogFragment {


    ItemScheduled mItemScheduledSelected;
    private int day;

    public static FragmentSchedulerImportTask newInstance (int forDay)  {
        FragmentSchedulerImportTask fragment = new FragmentSchedulerImportTask();
        Bundle bundle =  new Bundle();
        bundle.putInt( "day", forDay );
        fragment.setArguments( bundle );
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        this.day = getArguments().getInt("day");

        View view = inflater.inflate (R.layout.scheduler_frag_layout_import_task, null);
        builder.setView (view);
        builder.setMessage(R.string.string_DialogBox_Schedule_Task);

        Button buttonScheduleTask   = view.findViewById( R.id.buttonScheduleTaskDialog);
        Button buttonCancelSchedule = view.findViewById( R.id.buttonCancelScheduleTaskDialog);

        ExpandableListView expandableListView = view.findViewById( R.id.expandableListViewForSchedulingTasks);
        final ExpandedListSchedulingTaskAdapter expandableListAdapter = new ExpandedListSchedulingTaskAdapter( getActivity() ); // getContext()
        //expandableListAdapter.test_Print_ArrayGS();
        expandableListView.setAdapter( expandableListAdapter );

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                ItemScheduled itemScheduled = (ItemScheduled) expandableListAdapter.getChild(groupPosition, childPosition);
                setItemScheduledForFragment ( itemScheduled );
                return true;
            }
        });
        expandableListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return false;
            }
        });

        // ----------------------- CALL-BACK ----------------------------
        buttonScheduleTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getIntent().putExtra("item_schedule", getItemScheduledFromFragment() );
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, getActivity().getIntent());
                dismiss();
            }
        });

        buttonCancelSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_CANCELED, getActivity().getIntent());
                dismiss();
            }
        });
        return builder.create();
    }

    public ItemScheduled getItemScheduledFromFragment () {
        return mItemScheduledSelected;
    }

    public void setItemScheduledForFragment (ItemScheduled itemScheduledSelected) {
        mItemScheduledSelected = itemScheduledSelected;
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        // Verify that the host activity implements the callback interface
//        try {
//            // Instantiate the NoticeDialogListener so we can send events to the host
//            //mListener = (FragmentSchedulerImportTask.NoticeDialogListener) ((ActivityScheduler) context).getViewPager().getAdapter();
//            //mListener = (FragmentSchedulerImportTask.NoticeDialogListener) getFragmentManager().getFragments().get( day );
//            // mListener = (FragmentSchedulerImportTask.NoticeDialogListener) context;
//        } catch (ClassCastException e) {
//            // The activity doesn't implement the interface, throw exception
//            throw new ClassCastException(context.toString() + " must implement NoticeDialogListener"); // getActivity()
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        try {
//            // Instantiate the NoticeDialogListener so we can send events to the host
//            mListener = null;
//        } catch (ClassCastException e) {
//            // The activity doesn't implement the interface, throw exception
//            throw new ClassCastException(getContext().toString() + " must implement NoticeDialogListener");
//        }
//    }

}
