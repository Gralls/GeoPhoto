package com.springer.patryk.geo_photo.cluster_pictures;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
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

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Patryk on 2017-04-08.
 */

public class ClusterFragment extends Fragment {


    @BindView(R.id.picture_remove)
    ImageView removePicture;
    @BindView(R.id.picture_view)
    ImageView pictureImage;
    @BindView(R.id.picture_label)
    TextView pictureDescription;

    private Picture picture;
    public static ClusterFragment newInstance() {
        return new ClusterFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cluster_pictures, null, false);
        ButterKnife.bind(this, view);

        picture = (Picture) getArguments().getSerializable("picture");

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Picasso.with(getContext()).load(picture.getDownloadUrl()).into(pictureImage);
        pictureDescription.setText(picture.getDescription());
    }
}
