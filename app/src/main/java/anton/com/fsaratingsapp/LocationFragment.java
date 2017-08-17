package anton.com.fsaratingsapp;

import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Anton on 24/03/2017.
 */

public class LocationFragment extends Fragment {
    private View view;
    private PagerAdapter pagerAdapter;
    private Location location;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_location, container, false);
            TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
            tabLayout.addTab(tabLayout.newTab().setText("List"));
            tabLayout.addTab(tabLayout.newTab().setText("Map"));

            final ViewPager viewPager = (ViewPager) view.findViewById(R.id.pager);
            pagerAdapter = new PagerAdapter(getActivity().getSupportFragmentManager());
            viewPager.setAdapter(pagerAdapter);
            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                /**
                 * Called when a tab enters the selected state.
                 *
                 * @param tab The tab that was selected
                 */
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    viewPager.setCurrentItem(tab.getPosition());
                }

                /**
                 * Called when a tab exits the selected state.
                 *
                 * @param tab The tab that was unselected
                 */
                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                /**
                 * Called when a tab that is already selected is chosen again by the user. Some applications
                 * may use this action to return to the top level of a category.
                 *
                 * @param tab The tab that was reselected.
                 */
                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });

            setLocation(this.location);
        }

        return view;
    }

    public void setLocation(Location location) {
        this.location = location;

        if (pagerAdapter != null) {
            pagerAdapter.setLocation(location);
        }
    }
}
