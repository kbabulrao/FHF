package com.fhf.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;

import com.fhf.R;
import com.fhf.adapters.FullScreenImageAdapter;

/**
 * Created by sbogi on 3/9/2017.
 */

public class FullScreenViewActivity extends BaseActivity {
    private int[] mImageResources = {
            R.mipmap.welcome_bg,
            R.mipmap.ic_launcher,
            R.mipmap.welcome_bg,
            R.mipmap.ic_launcher,
            R.mipmap.welcome_bg
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_view);

        int position = getIntent().getIntExtra("position", 0);
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(new FullScreenImageAdapter(this, mImageResources));
        viewPager.setCurrentItem(position);
    }
}
