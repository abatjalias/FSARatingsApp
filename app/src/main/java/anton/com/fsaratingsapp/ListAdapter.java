package anton.com.fsaratingsapp;


import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

class ListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Restaurant> restaurants;
    private static LayoutInflater inflater;

    ListAdapter(Context context) {
        this.context = context;
        this.restaurants = new ArrayList<>();
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    void setRestaurants(ArrayList<Restaurant> restaurants) {
        // Update the data
        this.restaurants = restaurants;

        // Check if restaurants is null
        if (this.restaurants == null) {
            this.restaurants = new ArrayList<>();
        }

        // Trigger a visual update
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return restaurants.size();
    }

    @Override
    public Object getItem(int position) {
        return restaurants.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(restaurants.get(position).getId());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Restaurant restaurant = restaurants.get(position);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_list, parent, false);
        }

        TextView nameView = (TextView) convertView.findViewById(R.id.name);
        TextView addressView = (TextView) convertView.findViewById(R.id.address);
        TextView distanceView = (TextView) convertView.findViewById(R.id.distance);
        TextView ratingText = (TextView) convertView.findViewById(R.id.ratingText);
        ImageView ratingImage = (ImageView) convertView.findViewById(R.id.rating);

        nameView.setText(restaurant.getName());
        addressView.setText(
                restaurant.getAddressLine1() + "\n" +
                        restaurant.getAddressLine2() + "\n" +
                        restaurant.getAddressLine3() + "\n" +
                        restaurant.getPostcode()
        );
        if (restaurant.getDistance() == null) {
            distanceView.setText("");
        } else {
            distanceView.setText(String.format("%.2f", Double.parseDouble(restaurant.getDistance())) + "km away");
        }

        int rating = Integer.parseInt(restaurant.getRating());

        if (rating == -1) {
            ratingImage.setVisibility(View.GONE);
            ratingText.setTextColor(ContextCompat.getColor(context, R.color.exempt));
            ratingText.setText(R.string.exempt);
            ratingText.setVisibility(View.VISIBLE);
        } else {
            int id = context.getResources().getIdentifier("rating" + rating, "drawable", context.getPackageName());

            if (id == 0) {
                ratingImage.setVisibility(View.GONE);
                ratingText.setText(restaurant.getRating());
                ratingText.setVisibility(View.VISIBLE);

                switch (rating) {
                    case 0:
                        ratingText.setTextColor(ContextCompat.getColor(context, R.color.urgentImprovementNecessary));
                        break;
                    case 1:
                        ratingText.setTextColor(ContextCompat.getColor(context, R.color.majorImprovementNecessary));
                        break;
                    case 2:
                        ratingText.setTextColor(ContextCompat.getColor(context, R.color.improvementNecessary));
                        break;
                    case 3:
                        ratingText.setTextColor(ContextCompat.getColor(context, R.color.generallySatisfactory));
                        break;
                    case 4:
                        ratingText.setTextColor(ContextCompat.getColor(context, R.color.good));
                        break;
                    case 5:
                        ratingText.setTextColor(ContextCompat.getColor(context, R.color.veryGood));
                        break;
                }
            } else {
                ratingImage.setImageResource(id);
                ratingImage.setVisibility(View.VISIBLE);
                ratingText.setVisibility(View.GONE);
            }
        }

        return convertView;
    }
}
