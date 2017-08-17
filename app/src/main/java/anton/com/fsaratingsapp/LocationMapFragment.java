package anton.com.fsaratingsapp;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import java.util.ArrayList;

/**
 * Created by Anton on 24/03/2017.
 */

public class LocationMapFragment extends RestaurantFragment {
    private MapView mapView;
    private ArrayList<Restaurant> restaurants;
    private ClusterManager<RestaurantMarker> mClusterManager;
    private GoogleMap gMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location_map, container, false);

        mapView = (MapView) view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        mapView.onResume();

        MapsInitializer.initialize(getContext());

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                gMap = googleMap;
                mClusterManager = new ClusterManager<>(getContext(), googleMap);

                // Use the cluster manager's listeners
                googleMap.setOnCameraIdleListener(mClusterManager);
                googleMap.setOnMarkerClickListener(mClusterManager);

                // Add zoom controls
                googleMap.getUiSettings().setZoomControlsEnabled(true);

                // Set custom cluster renderer
                RestaurantClusterRenderer clusterRenderer = new RestaurantClusterRenderer(getContext(), googleMap, mClusterManager);
                clusterRenderer.setMinClusterSize(3);   // How many markers before a cluster can be made
                mClusterManager.setRenderer(clusterRenderer);

                // Set custom info window adapter
                googleMap.setInfoWindowAdapter(new RestaurantInfoWindow());

                addMarkers();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    public void setLocation(Location location) {
        getRestaurants("op=s_loc&lat=" + location.getLatitude() + "&long=" + location.getLongitude());
    }

    public void updateRestaurants(ArrayList<Restaurant> restaurants) {
        this.restaurants = restaurants;

        addMarkers();
    }

    private void addMarkers() {
        // Clear current markers
        mClusterManager.clearItems();

        if (restaurants != null) {
            boolean centered = false;

            // Add markers to map
            for (int i = 0; i < restaurants.size(); i++) {
                Restaurant restaurant = restaurants.get(i);
                int rating = Integer.parseInt(restaurant.getRating());

                if (rating >= 0 && rating <= 5) {
                    // Create a custom marker
                    RestaurantMarker marker = new RestaurantMarker(
                            Double.parseDouble(restaurant.getLatitude()),
                            Double.parseDouble(restaurant.getLongitude()),
                            restaurant.getName(),
                            restaurant.getAddressLine1() + "\n" + restaurant.getAddressLine2() + "\n" + restaurant.getAddressLine3() + "\n" + restaurant.getPostcode(),
                            getResources().getIdentifier("marker" + rating, "drawable", getContext().getPackageName()));
                    mClusterManager.addItem(marker);

                    // If this is the first restaurant, move and center the camera to it
                    if (!centered) {
                        centered = true;
                        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 17));
                    }
                }
            }
        }
    }

    private class RestaurantInfoWindow implements GoogleMap.InfoWindowAdapter {
        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {
            // Inflate custom layout
            View view = getActivity().getLayoutInflater().inflate(R.layout.window_info, null);

            // Get text views from layout
            TextView title = (TextView) view.findViewById(R.id.info_title);
            TextView snippet = (TextView) view.findViewById(R.id.info_snippet);

            // Set text
            title.setText(marker.getTitle());
            snippet.setText(marker.getSnippet());

            return view;
        }
    }

    private class RestaurantClusterRenderer extends DefaultClusterRenderer<RestaurantMarker> {
        RestaurantClusterRenderer(Context context, GoogleMap map, ClusterManager<RestaurantMarker> clusterManager) {
            super(context, map, clusterManager);
        }

        @Override
        protected void onBeforeClusterItemRendered(RestaurantMarker item, MarkerOptions markerOptions) {
            markerOptions.icon(BitmapDescriptorFactory.fromResource(item.getIcon()));
            markerOptions.anchor(0.5f, 0.5f);

            super.onBeforeClusterItemRendered(item, markerOptions);
        }
    }
}
