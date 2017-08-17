package anton.com.fsaratingsapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    private static final int LOCATION_PERMISSION = 512;

    private android.support.v4.app.FragmentManager fragmentManager;
    private LocationFragment locationFragment = new LocationFragment();
    private NameFragment nameFragment = new NameFragment();
    private PostcodeFragment postcodeFragment = new PostcodeFragment();
    private RecentFragment recentFragment = new RecentFragment();

    private LocationManager locationManager;
    private String locationProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        fragmentManager = getSupportFragmentManager();

        // Initial fragment
        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.container_fragment, locationFragment).commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        final FragmentTransaction transaction = fragmentManager.beginTransaction();

                        switch (item.getItemId()) {
                            case R.id.action_location:
                                transaction.replace(R.id.container_fragment, locationFragment).commit();
                                break;
                            case R.id.action_name:
                                transaction.replace(R.id.container_fragment, nameFragment).commit();
                                break;
                            case R.id.action_postcode:
                                transaction.replace(R.id.container_fragment, postcodeFragment).commit();
                                break;
                            case R.id.action_recent:
                                transaction.replace(R.id.container_fragment, recentFragment).commit();
                                break;
                        }

                        return true;
                    }
                });

        // Create location objects
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationProvider = LocationManager.GPS_PROVIDER;

        // Make sure we have location permissions
        if (checkPermissions()) {
            // We have permission
            listenForLocationUpdates();
        } else {
            // We need to request location permissions
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION }, LOCATION_PERMISSION);
        }
    }

    private boolean checkPermissions() {
        return (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    listenForLocationUpdates();
                }
            }
        }
    }

    private void listenForLocationUpdates() {
        updateLocation();

        try {
            LocationListener locationListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                    updateLocation();
                }

                public void onStatusChanged(String provider, int status, Bundle extras) {}

                public void onProviderEnabled(String provider) {}

                public void onProviderDisabled(String provider) {}
            };

            locationManager.requestLocationUpdates(android.location.LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private void updateLocation() {
        try {
            Location location = locationManager.getLastKnownLocation(locationProvider);

            if (location == null) {
                return;
            }

            locationFragment.setLocation(location);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }
}
