package com.springer.patryk.geo_photo.screens.pictures_list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.springer.patryk.geo_photo.R;
import com.springer.patryk.geo_photo.model.Picture;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Patryk on 2017-07-16.
 */

public class PicturesListAdapter extends RecyclerView.Adapter<PicturesListAdapter.ViewHolder> {

    private List<Picture> mPictureList;
    private Context mContext;

    PicturesListAdapter(Context context, List<Picture> list) {
        mContext = context;
        mPictureList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pictures_list_item, parent, false);

        return new PicturesListAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Picture picture = mPictureList.get(position);
        Picasso.with(mContext)
                .load(picture.getDownloadUrl())
                .placeholder(R.drawable.progress_animation)
                .into(holder.picture);
    }

    @Override
    public int getItemCount() {
        return mPictureList.size();
    }

    void setPictureList(List<Picture> pictureList) {
        this.mPictureList = pictureList;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView picture;
        TextView text;
        TextView owner;
        ImageView delete;
        ImageView navigation;

        ViewHolder(View itemView) {
            super(itemView);
            picture = (ImageView) itemView.findViewById(R.id.pictures_list_item_image);
            text = (TextView) itemView.findViewById(R.id.pictures_list_item_text);
            owner = (TextView) itemView.findViewById(R.id.pictures_list_item_owner);
            delete = (ImageView) itemView.findViewById(R.id.pictures_list_item_delete);
            navigation = (ImageView) itemView.findViewById(R.id.pictures_list_item_navigation);
            setOnDeleteClick();
            setOnNavigationClick();
        }

        private void setOnDeleteClick() {
            delete.setOnClickListener(view -> {
            });
        }

        private void setOnNavigationClick() {
            navigation.setOnClickListener(view -> {
            });
        }
    }
}
