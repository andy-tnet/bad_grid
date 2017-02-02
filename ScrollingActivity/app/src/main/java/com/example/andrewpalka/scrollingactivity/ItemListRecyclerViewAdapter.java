package com.example.andrewpalka.scrollingactivity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.Console;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by andrewpalka on 1/24/17.
 */

public class ItemListRecyclerViewAdapter extends RecyclerView.Adapter<ItemListRecyclerViewAdapter
        .ListAdapterViewHolder> {


    private String TAG = ItemListRecyclerViewAdapter.class.getSimpleName();


    private String[] mWeatherData;
    private ArrayList<HashMap<String, String>> mMovieData;

    private View viewReference;

    private static final String IMG_BASE_URL = "http://image.tmdb.org/t/p/w342/";
    /*
     * An on-click handler that we've defined to make it easy for an Activity to interface with
     * our RecyclerView
     */
    private final ListAdapterOnClickHandler mClickHandler;

    /**
     * The interface that receives onClick messages.
     */
    public interface ListAdapterOnClickHandler {
        void onClick(String weatherForDay);
    }

    /**
     * Creates a ForecastAdapter.
     *
     * @param clickHandler The on-click handler for this adapter. This single handler is called
     *                     when an item is clicked.
     */
    public ItemListRecyclerViewAdapter(ListAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    /**
     * Cache of the children views for a forecast list item.
     */
    public class ListAdapterViewHolder extends RecyclerView.ViewHolder {
        ;
        public final TextView mTitleTextView;
        public final ImageView mPosterImageView;

        public ListAdapterViewHolder(View view) {
            super(view);
            mTitleTextView = (TextView) view.findViewById(R.id.tv_title_listed);
            mPosterImageView = (ImageView) view.findViewById(R.id.iv_poster_listed);
//            view.setOnClickListener(this);
        }

        /**
         * This gets called by the child views during a click.
         *
         * @param v The View that was clicked
         */

        /*  @Override
        *   public void onClick(View v) {
        *       int adapterPosition = getAdapterPosition();
        *       String weatherForDay = mWeatherData[adapterPosition];
        *       mClickHandler.onClick(weatherForDay);
        *   }
        */
    }

    @Override
    public ListAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.item_list_content;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;


        viewReference = inflater.inflate(layoutIdForListItem, parent,
                shouldAttachToParentImmediately);
        return new ListAdapterViewHolder(viewReference);
    }

    @Override
    public void onBindViewHolder(ListAdapterViewHolder holder, int position) {
//        String weatherForThisDay = mWeatherData[position];
        HashMap<String, String> hashMap = mMovieData.get(position);

        String title = hashMap.get("title");
        String img = hashMap.get("poster");


        Picasso.with(viewReference.getContext()).load("http://image.tmdb.org/t/p/w342/WLQN5aiQG8wc9SeKwixW7pAR8K.jpg")
                .into(holder.mPosterImageView);

        Log.d(TAG, "onBindViewHolder: " + IMG_BASE_URL + "   AND IMG URL: "+ img);
        Picasso.with(viewReference.getContext())
                .load(IMG_BASE_URL + img)
                .into(holder.mPosterImageView);

        holder.mTitleTextView.setText(title);
    }

    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
//     * @param  The ViewGroup that these ViewHolders are contained within.
//     * @param viewType  If your RecyclerView has more than one type of item (which ours doesn't) you
     *                  can use this viewType integer to provide a different layout. See
     *                  {@link android.support.v7.widget.RecyclerView.Adapter#getItemViewType(int)}
     *                  for more details.
     * @return A new ForecastAdapterViewHolder that holds the View for each list item
     */


    @Override
    public int getItemCount() {

        if (null == mMovieData) return 0;
        return mMovieData.size();
    }

    /**
     * This method is used to set the weather forecast on a ForecastAdapter if we've already
     * created one. This is handy when we get new data from the web but don't want to create a
     * new ForecastAdapter to display it.
     *
//     * @param weatherData The new weather data to be displayed.
     */


    public void setMovieData(ArrayList<HashMap<String, String>> arrayList) {
        mMovieData = arrayList;
        notifyDataSetChanged();
    }
}
