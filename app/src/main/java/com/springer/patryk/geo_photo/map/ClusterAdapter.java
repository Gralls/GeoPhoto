package com.springer.patryk.geo_photo.map;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ViewAnimator;

import com.springer.patryk.geo_photo.R;
import com.springer.patryk.geo_photo.model.Picture;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Patryk on 2017-04-08.
 */

class ClusterAdapter extends RecyclerView.Adapter<ClusterAdapter.ViewHolder> {

    private List<Picture> pictureList;
    private Context mContext;
    private BottomSheetPictureClickedListener mCallback;

    ClusterAdapter(Context mContext, BottomSheetPictureClickedListener callback) {
        pictureList = new ArrayList<>();
        this.mContext = mContext;
        mCallback = callback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.picture_item, parent, false);

        ViewHolder holder = new ViewHolder(itemView);

        itemView.setOnClickListener(item ->
                mCallback.onPictureClick(
                        pictureList.get(holder.getAdapterPosition())
                )
        );

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Picture picture = pictureList.get(position);
        holder.animator.setDisplayedChild(0);
        Picasso.with(mContext).load(picture.getDownloadUrl()).into(holder.imageView, new Callback() {
            @Override
            public void onSuccess() {
                holder.animator.setDisplayedChild(1);
            }

            @Override
            public void onError() {

            }
        });
    }

    @Override
    public int getItemCount() {
        return pictureList.size();
    }

    void setPictureList(List<Picture> pictureList) {
        this.pictureList = pictureList;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        ViewAnimator animator;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.cluster_picture);
            animator = (ViewAnimator) itemView.findViewById(R.id.picture_item_animator);
        }
    }

    public interface BottomSheetPictureClickedListener {
        void onPictureClick(Picture picture);
    }

}
