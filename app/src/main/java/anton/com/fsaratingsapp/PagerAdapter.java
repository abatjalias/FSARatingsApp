package anton.com.fsaratingsapp;

import android.location.Location;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


class PagerAdapter extends FragmentStatePagerAdapter {
    private LocationListFragment listFragment = new LocationListFragment();
    private LocationMapFragment mapFragment = new LocationMapFragment();

    PagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    /**
     * Return the Fragment associated with a specified position.
     */
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return listFragment;
            case 1:
                return mapFragment;
            default:
                return null;
        }
    }

    /**
     * Return the number of views available.
     */
    @Override
    public int getCount() {
        return 2;
    }

    void setLocation(Location location) {
        listFragment.setLocation(location);
        mapFragment.setLocation(location);
    }
}
