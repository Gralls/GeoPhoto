package com.springer.patryk.geo_photo.screens.map;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.springer.patryk.geo_photo.model.Picture;

/**
 * Created by Patryk on 2017-05-09.
 */

public class ClusterRenderer extends DefaultClusterRenderer<Picture> {
    public ClusterRenderer(Context context, GoogleMap map, ClusterManager<Picture> clusterManager) {
        super(context, map, clusterManager);
    }

    @Override
    protected boolean shouldRenderAsCluster(Cluster<Picture> cluster) {
        return cluster.getSize() > 1;
    }
}
