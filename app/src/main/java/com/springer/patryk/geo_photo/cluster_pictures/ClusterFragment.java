package com.springer.patryk.geo_photo.cluster_pictures;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.springer.patryk.geo_photo.R;
import com.springer.patryk.geo_photo.model.Picture;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Patryk on 2017-04-08.
 */

public class ClusterFragment extends Fragment {

    @BindView(R.id.cluster_recycler)
    RecyclerView recyclerView;

    public static ClusterFragment newInstance() {
        return new ClusterFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cluster_pictures, null, false);
        ButterKnife.bind(this, view);

        List<Picture> pictre = (List<Picture>) getArguments().getSerializable("s");
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        // recyclerView.setAdapter(new ClusterAdapter(pictre,getContext()));
        return view;
    }
}
