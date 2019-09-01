package com.kanjanawit.imagengine;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    //Constant
    private static final int COLUMNS_NUMBERS = 5;
    //private variable
    RecyclerImageViewAdapter mRycyclerImageViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setting up recyclerview
        RecyclerView recyclerView = findViewById(R.id.main_recyclerview);
        recyclerView.setLayoutManager(new GridLayoutManager(this, COLUMNS_NUMBERS));
        // TODO Change from fake data to real data
        mRycyclerImageViewAdapter = new RecyclerImageViewAdapter(this, FakeData.getImageData(this));
        recyclerView.setAdapter(mRycyclerImageViewAdapter);


    }
}
