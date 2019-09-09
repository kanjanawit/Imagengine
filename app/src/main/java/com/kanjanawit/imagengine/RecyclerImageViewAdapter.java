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
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class RecyclerImageViewAdapter extends RecyclerView.Adapter<RecyclerImageViewAdapter.ViewHolder> {
    public static final String URI_EXTRA = "uri_detail";

    private ContentResolver mContentResolver;
    private LayoutInflater mLayoutInflater;
    private ArrayList<String> mImageIds = new ArrayList<String>();

    public RecyclerImageViewAdapter(@NonNull Context context, ArrayList<String> ids) {
        mContentResolver = context.getContentResolver();
        mLayoutInflater = LayoutInflater.from(context);
        mImageIds = ids;
    }

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.recycler_image_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Bitmap bitmap = MediaStore.Images.Thumbnails.getThumbnail(mContentResolver, Integer.parseInt(mImageIds.get(position)), MediaStore.Images.Thumbnails.MICRO_KIND, null);
        holder.listItemImageView.setImageBitmap(bitmap);
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return mImageIds.size();
    }

    public String getImageIdAtPosition(int position) {
        return mImageIds.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        ImageView listItemImageView;
        ConstraintLayout listItemLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            listItemImageView = itemView.findViewById(R.id.item_image_imageview);
            listItemLayout = itemView.findViewById(R.id.item_layout);
            //set onclicklistener
            itemView.setOnClickListener(this);
            listItemLayout.setOnCreateContextMenuListener(this);
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
            intent.putExtra(URI_EXTRA, mImageIds.get(adapterPosition));
            context.startActivity(intent);
        }

        /**
         * Called when the context menu for this view is being built. It is not
         * safe to hold onto the menu after this method returns.
         *
         * @param menu     The context menu that is being built
         * @param v        The view for which the context menu is being built
         * @param menuInfo Extra information about the item for which the
         *                 context menu should be shown. This information will vary
         */
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Choose Option");
            new MenuInflater(MyApplication.getAppContext()).inflate(R.menu.main_activity_context_menu, menu);
            menu.findItem(R.id.gridmenu_command_delete).setOnMenuItemClickListener(this);
            menu.findItem(R.id.gridmenu_command_edit).setOnMenuItemClickListener(this);
        }

        /**
         * Called when a menu item has been invoked.  This is the first code
         * that is executed; if it returns true, no other callbacks will be
         * executed.
         *
         * @param item The menu item that was invoked.
         * @return Return true to consume this click and prevent others from
         * executing.
         */
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (item.getItemId() == R.id.gridmenu_command_edit) {
                return true;
            } else if (item.getItemId() == R.id.gridmenu_command_delete) {
                int deletePosition = getAdapterPosition();
                DatabaseConnection.deleteImage(MyApplication.getAppContext(), getImageIdAtPosition(deletePosition));
                //notify the recycler to update
                mImageIds.remove(deletePosition);
                notifyItemRemoved(deletePosition);
                return true;
            }
            return false;
        }
    }
}
