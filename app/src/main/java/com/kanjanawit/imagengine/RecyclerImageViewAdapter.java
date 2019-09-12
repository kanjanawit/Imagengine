package com.kanjanawit.imagengine;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

class RecyclerImageViewAdapter extends RecyclerView.Adapter<RecyclerImageViewAdapter.ViewHolder> implements Filterable {
    static final String IMAGEDATA_EXTRA = "imagedata_extra";
    private ContentResolver mContentResolver;
    private LayoutInflater mLayoutInflater;
    private ArrayList<ImageData> mImageDatas = new ArrayList<ImageData>();
    private ArrayList<ImageData> mFullImageDatas = new ArrayList<ImageData>();
    private Filter displayNameFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<ImageData> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(mFullImageDatas);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (ImageData imageData : mFullImageDatas) {
                    if (imageData.getDisplayName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(imageData);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mImageDatas.clear();
            mImageDatas.addAll((List<ImageData>) results.values);
            notifyDataSetChanged();
        }
    };

    RecyclerImageViewAdapter(@NonNull Context context, ArrayList<ImageData> imageDatas) {
        mContentResolver = context.getContentResolver();
        mLayoutInflater = LayoutInflater.from(context);
        mImageDatas = imageDatas;
        mFullImageDatas = new ArrayList<ImageData>(imageDatas);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = MediaStore.Images.Thumbnails.getThumbnail(mContentResolver, Integer.parseInt(mImageDatas.get(position).getImageId()), MediaStore.Images.Thumbnails.MICRO_KIND, null);
                holder.listItemImageView.setImageBitmap(bitmap);
            }
        }).run();
    }

    @Override
    public int getItemCount() {
        return mImageDatas.size();
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.recycler_image_item, parent, false);
        return new ViewHolder(view);
    }

    private ImageData getImageDataAtPosition(int position) {
        return mImageDatas.get(position);
    }

    public void sortByName() {
        Collections.sort(mImageDatas, new Comparator<ImageData>() {
            @Override
            public int compare(ImageData o1, ImageData o2) {
                return o1.getDisplayName().compareTo(o2.getDisplayName());
            }
        });
        notifyDataSetChanged();
    }

    public void sortByDateTaken() {
        Collections.sort(mImageDatas, new Comparator<ImageData>() {
            @Override
            public int compare(ImageData o1, ImageData o2) {
                return o1.getDateTaken().compareTo(o2.getDateTaken());
            }
        });
        notifyDataSetChanged();
    }

    public void sortByDateAdded() {
        Collections.sort(mImageDatas, new Comparator<ImageData>() {
            @Override
            public int compare(ImageData o1, ImageData o2) {
                return o1.getDateAdded().compareTo(o2.getDateAdded());
            }
        });
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        ImageView listItemImageView;
        ConstraintLayout listItemLayout;

        ViewHolder(View itemView) {
            super(itemView);
            listItemImageView = itemView.findViewById(R.id.item_image_imageview);
            listItemLayout = itemView.findViewById(R.id.item_layout);
            //set onclicklistener
            itemView.setOnClickListener(this);
            listItemLayout.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Context context = v.getContext();
            // TODO Open Detail Activity and send ImageData at position.
            Intent intent = new Intent(context, DetailImageActivity.class);
            intent.putExtra(IMAGEDATA_EXTRA, mImageDatas.get(adapterPosition));
            context.startActivity(intent);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Choose Option");
            new MenuInflater(MyApplication.getAppContext()).inflate(R.menu.main_activity_context_menu, menu);
            menu.findItem(R.id.gridmenu_command_delete).setOnMenuItemClickListener(this);
            menu.findItem(R.id.gridmenu_command_edit).setOnMenuItemClickListener(this);
        }


        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (item.getItemId() == R.id.gridmenu_command_edit) {
                return true;
            } else if (item.getItemId() == R.id.gridmenu_command_delete) {
                int deletePosition = getAdapterPosition();
                DatabaseConnection.deleteImage(MyApplication.getAppContext(), getImageDataAtPosition(deletePosition));
                //notify the recycler to update
                mImageDatas.remove(deletePosition);
                notifyItemRemoved(deletePosition);
                return true;
            }
            return false;
        }
    }

    @Override
    public Filter getFilter() {
        return displayNameFilter;
    }
}
