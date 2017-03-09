package com.fhf.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fhf.R;
import com.fhf.activities.EventDetailsActivity;
import com.fhf.adapters.EventsAdapter;
import com.fhf.utils.Utils;

import java.util.ArrayList;

/**
 * Created by santosh on 2/16/2017.
 */

public class EventsFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    RecyclerView rvEventsList;
    SwipeRefreshLayout mSwipeRefresh;
    TextView tvNoEvents;
    EventsAdapter eventsAdapter;
    private ArrayList<String> eventsArray = new ArrayList<>();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events, container, false);

        findViews(view);
        updateAdapter();

//        mSwipeRefresh.setRefreshing(true);
        eventsArray.add(null);

        rvEventsList.addOnItemTouchListener(
                new Utils.RecyclerItemClickListener(getActivity(), new Utils.RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // do whatever
                        Intent eventDetails = new Intent(getActivity(), EventDetailsActivity.class);
                        eventDetails.putExtra("eventObj", eventsArray.get(0));
                        startActivity(eventDetails);
                    }
                })
        );
        return view;
    }

    void findViews(View view) {

        rvEventsList = (RecyclerView) view.findViewById(R.id.rv_events_list);
        tvNoEvents = (TextView) view.findViewById(R.id.tv_no_events);

        mSwipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipeTopList);
        mSwipeRefresh.setOnRefreshListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        rvEventsList.setLayoutManager(layoutManager);
        rvEventsList.setNestedScrollingEnabled(true);

    }

    void updateAdapter() {
        if (eventsArray != null) {

            if (eventsArray.size() == 0) {
                tvNoEvents.setVisibility(View.VISIBLE);
                tvNoEvents.setText("No Events");
            } else {
                tvNoEvents.setVisibility(View.GONE);
            }

            if (eventsAdapter == null) {
                eventsAdapter = new EventsAdapter(getActivity());
                rvEventsList.setAdapter(eventsAdapter);
            } else {
                rvEventsList.getRecycledViewPool().clear();
                eventsAdapter.notifyDataSetChanged();
            }

        } else {
            tvNoEvents.setVisibility(View.VISIBLE);
            tvNoEvents.setText("Something went wrong...Please try again");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onRefresh() {

    }
}
