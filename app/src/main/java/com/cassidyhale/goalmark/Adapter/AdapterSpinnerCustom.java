package com.cassidyhale.goalmark.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.cassidyhale.goalmark.R;



public class AdapterSpinnerCustom extends ArrayAdapter<String> {
    private String[] spinnerItemText;
    private int[]    spinnerItemIcon;

    public AdapterSpinnerCustom(Context context, int[] iconDrawables, String[] spinnerItem)    {
        super(context, 0, spinnerItem);
        this.spinnerItemIcon = iconDrawables;
        this.spinnerItemText = spinnerItem;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView (int position, View convertView, ViewGroup parent)    {
        if (convertView == null)    {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.template_spinner_goals, parent, false);
        }
        ImageView imageViewIcon = convertView.findViewById(R.id.imageViewSpinnerItem);
        TextView textViewSpinner = convertView.findViewById(R.id.textViewSpinnerItem);
        String currentItem = getItem(position);

        if (currentItem != null) {
            imageViewIcon.setImageResource(spinnerItemIcon[position]);
            textViewSpinner.setText(spinnerItemText[position]);
        }
        return convertView;
    }
}
