package com.fhf.adapters.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fhf.R;

/**
 * Created by santosh on 3/9/2017.
 */

public class EventsHolder extends RecyclerView.ViewHolder {

    private ImageView ivEvent;
    private TextView tvEventTitle;
    private TextView tvEventDate;
    private TextView tvEventDesc;

    private void findViews(View rootView) {
        ivEvent = (ImageView) rootView.findViewById(R.id.iv_event);
        tvEventDate = (TextView) rootView.findViewById(R.id.tv_event_date);
        tvEventTitle = (TextView) rootView.findViewById(R.id.tv_event_title);
        tvEventDesc = (TextView) rootView.findViewById(R.id.tv_event_desc);
    }

    public EventsHolder(View rootView) {
        super(rootView);
        findViews(rootView);
    }
}
