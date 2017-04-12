package com.springer.patryk.geo_photo.map;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.springer.patryk.geo_photo.R;
import com.springer.patryk.geo_photo.model.Picture;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Patryk on 2017-04-08.
 */

public class ClusterAdapter extends RecyclerView.Adapter<ClusterAdapter.ViewHolder> {

    private List<Picture> pictureList;
    private Context mContext;
    private MapFragment.ClusterClicked mCallback;

    public ClusterAdapter(Context mContext, MapFragment.ClusterClicked callback) {
        pictureList = new ArrayList<>();
        this.mContext = mContext;
        mCallback = callback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.picture_item, parent, false);

        ViewHolder holder = new ViewHolder(itemView);

        itemView.setOnClickListener(view -> mCallback.onClusterClickedListener(pictureList.get(holder.getAdapterPosition())));

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Picture picture = pictureList.get(position);
        Picasso.with(mContext).load(picture.getDownloadUrl()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return pictureList.size();
    }

    public void setPictureList(List<Picture> pictureList) {
        this.pictureList = pictureList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;


        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.cluster_picture);
        }
    }

}
