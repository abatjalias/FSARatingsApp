package anton.com.fsaratingsapp;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by Anton on 24/03/2017.
 */

class RestaurantMarker implements ClusterItem {
    private final LatLng mPosition;
    private final String mTitle;
    private final String mSnippet;
    private final int mIcon;

    RestaurantMarker(double lat, double lng, String title, String snippet, int icon) {
        mPosition = new LatLng(lat, lng);
        mTitle = title;
        mSnippet = snippet;
        mIcon = icon;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public String getSnippet() {
        return mSnippet;
    }

    int getIcon() {
        return mIcon;
    }
}
