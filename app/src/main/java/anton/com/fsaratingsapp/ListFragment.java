package anton.com.fsaratingsapp;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

public class ListFragment extends RestaurantFragment {
    protected View view;
    protected ListView listView;
    protected ListAdapter listAdapter;
    protected int layout;
    protected ArrayList<Restaurant> restaurants;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view == null) {
            view = inflater.inflate(layout, container, false);

            // Get views by id
            listView = (ListView) view.findViewById(R.id.listView);

            // Create list adapter
            listAdapter = new ListAdapter(getContext());
            listView.setAdapter(listAdapter);
            listAdapter.setRestaurants(restaurants);
        }

        return view;
    }

    public void updateRestaurants(ArrayList<Restaurant> restaurants) {
        this.restaurants = restaurants;

        if (listAdapter != null) {
            listAdapter.setRestaurants(restaurants);
        }
    }
}
