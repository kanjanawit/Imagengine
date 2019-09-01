package com.kanjanawit.imagengine;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Parcel;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

class RecyclerImageViewAdapter extends RecyclerView.Adapter<RecyclerImageViewAdapter.ViewHolder> {
    public static final String URI_EXTRA = "uri_detail";

    private LayoutInflater mLayoutInflater;
    private Uri[] mImageUri;

    public RecyclerImageViewAdapter(@NonNull Context context, Uri[] uris) {
        mLayoutInflater = LayoutInflater.from(context);
        mImageUri = uris;
    }

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.recycler_image_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.listItemImageView.setImageURI(mImageUri[position]);
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return mImageUri.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView listItemImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            listItemImageView = itemView.findViewById(R.id.item_image_imageview);
            //set onclicklistener
            itemView.setOnClickListener(this);
        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Context context = v.getContext();
            // TODO Open Detail Activity and send mImageUri[adapterPosition].
            Intent intent = new Intent(context, DetailImageActivity.class);
            intent.putExtra(URI_EXTRA,mImageUri[adapterPosition].toString());
            context.startActivity(intent);
        }
    }
}
