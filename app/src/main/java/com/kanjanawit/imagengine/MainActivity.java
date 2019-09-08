package com.kanjanawit.imagengine;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {
    //Constant
    private static final int COLUMNS_NUMBERS = 3;
    RecyclerImageViewAdapter mRecyclerImageViewAdapter;
    //private variable
    private RecyclerView mRecyclerView;
    private ProgressBar mLoadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PermissionCheck.GetExternalReadStoragePermission(this); //check permission to load picture

        mRecyclerView = findViewById(R.id.main_recyclerview);
        mLoadingBar = findViewById(R.id.loading_progressbar);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, COLUMNS_NUMBERS));
        refreshGridView();
    }

    public void refreshGridView() {
        AsyncTask backgroundWork = new AsyncTask() {
            /**
             * Runs on the UI thread before {@link #doInBackground}.
             *
             * @see #onPostExecute
             * @see #doInBackground
             */
            @Override
            protected void onPreExecute() {
                mRecyclerView.setVisibility(View.GONE);
                mLoadingBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected Object doInBackground(Object[] objects) {
                mRecyclerImageViewAdapter = new RecyclerImageViewAdapter(getApplicationContext(), DatabaseConnection.getAllImageIds(getApplicationContext()));
                return null;
            }

            /**
             * <p>Runs on the UI thread after {@link #doInBackground}. The
             * specified result is the value returned by {@link #doInBackground}.</p>
             *
             * <p>This method won't be invoked if the task was cancelled.</p>
             *
             * @param o The result of the operation computed by {@link #doInBackground}.
             * @see #onPreExecute
             * @see #doInBackground
             * @see #onCancelled(Object)
             */
            @Override
            protected void onPostExecute(Object o) {
                mRecyclerView.setAdapter(mRecyclerImageViewAdapter);
                mRecyclerView.setVisibility(View.VISIBLE);
                mLoadingBar.setVisibility(View.GONE);
            }
        };
        backgroundWork.execute();
    }
}
