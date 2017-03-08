package com.fhf.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fhf.R;
import com.fhf.adapters.holders.EventsHolder;
import com.fhf.adapters.holders.LoadingViewHolder;

import java.util.ArrayList;

/**
 * Created by santosh on 3/8/2017.
 */

public class EventsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private ArrayList<String> eventsArray;
    private Context context;
    boolean isDataOver;

    public EventsAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_ITEM;
//        return eventsArray.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(context).inflate(R.layout.row_event, parent, false);
            return new EventsHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            System.out.println("in loading");
            View view = LayoutInflater.from(context).inflate(R.layout.loadmorelayout, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    public int lastDigit(int number) {
        return number % 10;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof EventsHolder) {

        } else if (holder instanceof LoadingViewHolder) {

            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
            if (isDataOver) {
                if (getItemCount() > 10) {
                    loadingViewHolder.LB_msz.setText("No data left");
                } else {
                    loadingViewHolder.LB_msz.setText("");
                }
                loadingViewHolder.progressBar.setVisibility(View.GONE);
            } else {
                loadingViewHolder.progressBar.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return 10;
    }

}
