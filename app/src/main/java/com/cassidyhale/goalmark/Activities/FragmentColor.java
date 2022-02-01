package com.cassidyhale.goalmark.Activities;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.support.v7.widget.GridLayout;
import android.widget.ImageView;
import com.cassidyhale.goalmark.R;

import java.util.ArrayList;


public class FragmentColor extends DialogFragment {

    private GridLayout gridLayoutColorPicker;
    private int numberColumnsGridLayout = 5;
    private int colorSelected;
    private ArrayList<FloatingActionButton> arrayFloatingButton;

    public interface NoticeDialogListenerForColor {
        void onClickAddNewTask(DialogFragment dialog, int colorSelected);
        void onClickCancelAddNewTask(DialogFragment dialog);
    }
    NoticeDialogListenerForColor mListener;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        this.arrayFloatingButton = new ArrayList();

        View view = inflater.inflate (R.layout.template_dialog_color_picker, null);
        builder.setView (view);
        builder.setMessage(R.string.strDialogTitlePickColor);

        gridLayoutColorPicker              = view.findViewById(R.id.gridLayoutColorPicker);
        this.colorSelected = 0; //this.array_colors[0];
        this.populate_GridLayout();

        Button buttonTaskAdd = view.findViewById( R.id.buttonScheduleTaskDialog);
        buttonTaskAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClickAddNewTask(FragmentColor.this, colorSelected);
            }
        });
        Button buttonTaskCancel = view.findViewById( R.id.buttonCancelTaskDialog);
        buttonTaskCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClickCancelAddNewTask(FragmentColor.this);
            }
        });
        return builder.create();
    }

    private void populate_GridLayout () {

        gridLayoutColorPicker.removeAllViews();

        int colorsTotal = __GLOBAL_Const.array_colors.length;
        int columnCount = this.numberColumnsGridLayout;
        int rowCount = colorsTotal / columnCount + 1;
        gridLayoutColorPicker.setColumnCount(columnCount);
        gridLayoutColorPicker.setRowCount(rowCount);

        for (int i = 0, c = 0, r = 0; i < colorsTotal; i++, c++) {
            if (c == columnCount) {
                c = 0;
                r++;
            }
            FloatingActionButton floatingColorButton = new FloatingActionButton( getContext() );
            floatingColorButton.setClickable(true);
            floatingColorButton.setLongClickable(false);
            floatingColorButton.setId(9990 + i);
            //floatingColorButton.setSize(0);
            floatingColorButton.setBackgroundTintList( getResources().getColorStateList ( __GLOBAL_Const.array_colors[i]) );

            floatingColorButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int pos = v.getId();
                    pos -= 9990;
                    colorSelected = __GLOBAL_Const.array_colors[pos];

                    for (int k=0; k < __GLOBAL_Const.array_colors.length; k++)
                        if (k != pos)
                            arrayFloatingButton.get(k).setImageResource(R.drawable.icon_none);

                    ((FloatingActionButton) v).setImageResource(R.drawable.icon_check);
                    ((FloatingActionButton) v).setScaleType(ImageView.ScaleType.CENTER);
                }
            });
            GridLayout.Spec rowSpan = GridLayout.spec(GridLayout.UNDEFINED, 1);
            GridLayout.Spec colspan = GridLayout.spec(GridLayout.UNDEFINED, 1);
            GridLayout.LayoutParams gridParam = new GridLayout.LayoutParams(rowSpan, colspan);
            gridParam.width = 140;
            gridParam.height = 140;
            gridParam.setMargins(15,15,15,15);

            gridLayoutColorPicker.addView( floatingColorButton, gridParam);
            arrayFloatingButton.add( floatingColorButton );
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NoticeDialogListenerForColor) context;
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

