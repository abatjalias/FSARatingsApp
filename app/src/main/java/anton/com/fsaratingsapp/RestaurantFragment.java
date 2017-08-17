package anton.com.fsaratingsapp;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by Anton on 24/03/2017.
 */

public class RestaurantFragment extends Fragment {
    protected void getRestaurants(String query) {
        URL url = null;
        try {
            url = new URL("http://sandbox.kriswelsh.com/hygieneapi/hygiene.php?" + query);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        new ContactServer(this).execute(url);
    }

    private class ContactServer extends AsyncTask<URL, Void, ArrayList<Restaurant>> {
        Fragment parent;

        ContactServer(Fragment parent) {
            this.parent = parent;
        }

        @Override
        protected ArrayList<Restaurant> doInBackground(URL... params) {
            URL url = params[0];
            URLConnection connection;
            InputStreamReader streamReader;
            ArrayList<Restaurant> restaurants = new ArrayList<>();

            try {
                connection = url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

            try {
                streamReader = new InputStreamReader((connection.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

            BufferedReader reader = new BufferedReader(streamReader);

            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    JSONArray ja = new JSONArray(line);
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject object = (JSONObject) ja.get(i);
                        Restaurant restaurant = new Restaurant();

                        // Set restaurant properties
                        restaurant.setId(object.getString("id"));
                        restaurant.setName(object.getString("BusinessName"));
                        restaurant.setAddressLine1(object.getString("AddressLine1"));
                        restaurant.setAddressLine2(object.getString("AddressLine2"));
                        restaurant.setAddressLine3(object.getString("AddressLine3"));
                        restaurant.setPostcode(object.getString("PostCode"));
                        restaurant.setRating(object.getString("RatingValue"));
                        restaurant.setRatingDate(object.getString("RatingDate"));
                        restaurant.setLatitude(object.getString("Latitude"));
                        restaurant.setLongitude(object.getString("Longitude"));
                        if (object.has("DistanceKM")) {
                            restaurant.setDistance(object.getString("DistanceKM"));
                        }

                        // Add to list
                        restaurants.add(restaurant);
                    }
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return restaurants;
        }

        @Override
        protected void onPostExecute (ArrayList<Restaurant> result) {
            if (this.parent instanceof ListFragment) {
                ((ListFragment) this.parent).updateRestaurants(result);
            } else if (this.parent instanceof LocationMapFragment) {
                ((LocationMapFragment) this.parent).updateRestaurants(result);
            }
        }
    }
}
