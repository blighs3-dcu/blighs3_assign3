/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.flavor;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.util.Log;


import java.util.ArrayList;

/**
 * {@link FlavorActivity} shows a list of Android platform releases.
 * For each release, display the name, version number, and image.
 */
public class FlavorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flavor);

        // Create an ArrayList of AndroidFlavor objects
        final ArrayList<AndroidFlavor> androidFlavors = new ArrayList<AndroidFlavor>();
        androidFlavors.add(new AndroidFlavor(getString(R.string.leinster), getString(R.string.first), R.drawable.leinster_rugby));
        androidFlavors.add(new AndroidFlavor(getString(R.string.munster), getString(R.string.second), R.drawable.munster_rugby));
        androidFlavors.add(new AndroidFlavor(getString(R.string.ulster), getString(R.string.third), R.drawable.ulster_rugby));
        androidFlavors.add(new AndroidFlavor(getString(R.string.connacht), getString(R.string.fourth), R.drawable.connacht_rugby));


        // Create an {@link AndroidFlavorAdapter}, whose data source is a list of
        // {@link AndroidFlavor}s. The adapter knows how to create list item views for each item
        // in the list.
        AndroidFlavorAdapter flavorAdapter = new AndroidFlavorAdapter(this, androidFlavors);

        // Get a reference to the ListView, and attach the adapter to the listView.
        final ListView listView = (ListView) findViewById(R.id.listview_flavor);
        listView.setAdapter(flavorAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {

                Toast.makeText(FlavorActivity.this, (androidFlavors.get(i).getVersionName()), Toast.LENGTH_SHORT).show();
            }



});

    }}


