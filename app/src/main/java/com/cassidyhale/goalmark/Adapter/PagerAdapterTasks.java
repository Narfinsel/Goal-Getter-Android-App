package com.cassidyhale.goalmark.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PagerAdapterTasks extends PagerAdapter {

    private int[] layout;
    private LayoutInflater mLayoutInflater;
    private Context mContext;

    public PagerAdapterTasks(int[] layout, Context context) {
        this.layout = layout;
        this.mContext = context;
        mLayoutInflater = (LayoutInflater) context.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = mLayoutInflater.inflate( layout[position], container, false);
        container.addView (view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        View view = (View) object;
        container.removeView (view);
    }

    @Override
    public int getCount() {
        return layout.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return (view == o);
    }
}
