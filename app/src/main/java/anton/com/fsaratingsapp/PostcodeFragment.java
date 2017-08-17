package anton.com.fsaratingsapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;


public class PostcodeFragment extends ListFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = R.layout.fragment_postcode;

        super.onCreateView(inflater, container, savedInstanceState);

        // Get views by id
        SearchView searchView = (SearchView) view.findViewById(R.id.searchView);

        // Search query listener
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getRestaurants("op=s_postcode&postcode=" + query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return view;
    }
}
