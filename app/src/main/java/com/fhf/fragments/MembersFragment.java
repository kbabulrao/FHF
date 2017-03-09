package com.fhf.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fhf.R;
import com.fhf.adapters.EventsAdapter;
import com.fhf.adapters.MembersAdapter;

/**
 * Created by santosh on 2/16/2017.
 */

public class MembersFragment extends BaseFragment {

    RecyclerView rvMemberslist;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_members, container, false);

        findViews(view);

        rvMemberslist.setAdapter(new MembersAdapter(getContext()));

        return view;
    }

    void findViews(View view) {
        rvMemberslist = (RecyclerView) view.findViewById(R.id.rv_memberslist);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        rvMemberslist.setLayoutManager(layoutManager);
        rvMemberslist.setNestedScrollingEnabled(true);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
