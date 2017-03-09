package com.fhf.adapters.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fhf.R;

/**
 * Created by santosh on 3/9/2017.
 */

public class MembersHolder extends RecyclerView.ViewHolder {

    private TextView tvMembersCount;
    private TextView tvMemberName;

    private void findViews(View rootView) {
        tvMembersCount = (TextView) rootView.findViewById(R.id.tv_member_count);
        tvMemberName = (TextView) rootView.findViewById(R.id.tv_member_name);
    }

    public MembersHolder(View rootView) {
        super(rootView);
        findViews(rootView);
    }

    public void setData(int position) {
        tvMembersCount.setText(position + "");
    }
}
