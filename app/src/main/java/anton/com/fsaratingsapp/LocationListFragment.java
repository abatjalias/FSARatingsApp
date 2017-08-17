package anton.com.fsaratingsapp;

import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class LocationListFragment extends ListFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = R.layout.fragment_location_list;

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void setLocation(Location location) {
        getRestaurants("op=s_loc&lat=" + location.getLatitude() + "&long=" + location.getLongitude());
    }
}
