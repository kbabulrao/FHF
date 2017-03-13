package com.fhf.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.fhf.R;
import com.fhf.adapters.CustomGridViewAdapter;

/**
 * Created by sbogi on 3/13/2017.
 */

public class GalleryImagesActivity extends BaseActivity {

    GridView gridView;

    String[] gridViewString = {
            "Alram", "Android", "Mobile", "Website", "Profile", "WordPress",
            "Alram", "Android", "Mobile", "Website", "Profile", "WordPress",
    };
    int[] gridViewImageId = {
            R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher,
            R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher,
            R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_gallery);

        findViews();

        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setVisibility(View.VISIBLE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Event Images");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        CustomGridViewAdapter adapterViewAndroid = new CustomGridViewAdapter(this, gridViewString, gridViewImageId);

        gridView.setAdapter(adapterViewAndroid);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int i, long id) {
//                Toast.makeText(GalleryImagesActivity.this, "GridView Item: " + gridViewString[+i], Toast.LENGTH_LONG).show();
                Intent intent = new Intent(GalleryImagesActivity.this, FullScreenViewActivity.class);
                intent.putExtra("position", (int) view.getTag());
                startActivity(intent);
            }
        });

    }

    void findViews() {
        gridView = (GridView) findViewById(R.id.grid_images);
    }
}
