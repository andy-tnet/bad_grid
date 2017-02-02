package com.example.andrewpalka.scrollingactivity;

import android.content.Context;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;

import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements ItemListRecyclerViewAdapter
        .ListAdapterOnClickHandler{



    private EditText emailText;
    private TextView responseView;
    private ProgressBar progressBar;
    private RecyclerView mRecyclerView;
    private ItemListRecyclerViewAdapter mRecyclerViewAdapter;
    private TextView mErrorMessageDisplay;
    private Boolean swapSwitch;



    private String TAG = MainActivity.class.getSimpleName();

    private ArrayList<HashMap<String, String>> itemList;

    static final String API_KEY = "3b4b5ac626ad4e48";
    static final String API_URL = "https://api.fullcontact.com/v2/person.json?";

    private static final String BASE_URL =
            "http://api.themoviedb.org/3/movie/";

    private static  final String YOUR_API_KEY =
            "22f14a55c63ee4e1b8eab56217d509a8";

    final static String POPULAR_PARAM = "popular?api_key=";
    final static String RATINGS_PARAM = "top_rated?api_key=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        swapSwitch = false;
        itemList = new ArrayList<>();


        /*
         * Using findViewById, we get a reference to our RecyclerView from xml. This allows us to
         * do things like set the adapter of the RecyclerView and toggle the visibility.
         */
        mRecyclerView = (RecyclerView) findViewById(R.id.item_list);

        /* This TextView is used to display errors and will be hidden if there are no errors */
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);


        GridLayoutManager layoutManager = new GridLayoutManager(this,3);

        mRecyclerView.setLayoutManager(layoutManager);

        /*
         * Use this setting to improve performance if you know that changes in content do not
         * change the child layout size in the RecyclerView
         */
        mRecyclerView.setHasFixedSize(true);

        mRecyclerViewAdapter = new ItemListRecyclerViewAdapter(this);




        mRecyclerView.setAdapter(mRecyclerViewAdapter);

        int spacingInPixels = Utils.dpToPx(12);
        //calls and inits custom ItemDecoration class
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));

        /*
         * The mRecyclerViewAdapter is responsible for linking our movie data with the Views that
         * will end up displaying our movie image and text data.
         */


        progressBar = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        loadWeatherData();


    }


    private boolean performCheck() {
//        Context context = getBaseContext();
//
//        ConnectivityManager cm =
//                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
//
//        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
//        boolean isConnected = activeNetwork != null &&
//                activeNetwork.isConnectedOrConnecting();
//
//        return isConnected;
        
        return true;

    }

    private void loadWeatherData() {
        showMovieDataView();

        if(performCheck()) {
            new RetrieveFeedTask().execute();
        } else {
            showErrorMessage();
        }
    }

    private void showMovieDataView() {
        /* First, make sure the error is invisible */
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, make sure the weather data is visible */
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        /* First, hide the currently visible data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {

        private int mSpacing;

        public SpacesItemDecoration(int spacing) { mSpacing = spacing; }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView recyclerView,
                                   RecyclerView.State state) {
            outRect.left = mSpacing;
            outRect.top = mSpacing;
            outRect.right = mSpacing;
            outRect.bottom = mSpacing;
        }
    }

    class RetrieveFeedTask extends AsyncTask<Void, Void, Void> {

        private Exception exception;
        private String email;

        @Override protected void onPreExecute() {
            super.onPreExecute();

            progressBar.setVisibility(View.VISIBLE);

            if (!swapSwitch) {
                setTitle("Most Popular");

            } else {
                setTitle("Highest Rated");

            }

        }

        @Override protected Void doInBackground(Void... arg0) {

            // Simple validation of URL

            HttpHandler sh = new HttpHandler();

            String url;
            if (!swapSwitch) {
                Log.d(TAG, "doInBackground: " +swapSwitch.toString());
                url = BASE_URL + POPULAR_PARAM + YOUR_API_KEY;
            } else {
                Log.d(TAG, "doInBackground: " +swapSwitch.toString());
                url = BASE_URL + RATINGS_PARAM + YOUR_API_KEY;
            }

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray results = jsonObj.getJSONArray("results");

                    // looping through All Contacts
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject r = results.getJSONObject(i);
                        String title = r.getString("title");
                        String poster = r.getString("poster_path");
                        String release = r.getString("release_date");
                        String review = r.getString("vote_average");
                        String overview = r.getString("overview");

                        HashMap<String, String> result = new HashMap<>();
                        result.put("title",title);
                        result.put("poster",poster);
                        result.put("release",release);
                        result.put("review",review);
                        result.put("overview",overview);



                        //Adds the manually parsed result hashmap to the itemList instance variable
                        itemList.add(result);
                    }
                } catch (final Exception e) {

                    Log.e(TAG, "JSON parsing error: " + e.getMessage());


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });
                }

                } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }
//            }
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progressBar.setVisibility(View.GONE);

            if(itemList.isEmpty()) {
                showErrorMessage();

            } else {
                showMovieDataView();
                mRecyclerViewAdapter.setMovieData(itemList);
            }




            // TODO: check this.exception
            // TODO: do something with the feed

//            try {
//                JSONObject object = (JSONObject) new JSONTokener(response).nextValue();
//                String requestID = object.getString("requestId");
//                int likelihood = object.getInt("likelihood");
//                JSONArray photos = object.getJSONArray("photos");
//                .
//                .
//                .
//                .
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
        }


    }

    @Override
    public void onClick(String weatherForDay) {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.menu_main, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            itemList.clear();
            mRecyclerViewAdapter.setMovieData(null);
            loadWeatherData();
            return true;
        }

        // COMPLETED (2) Launch the map when the map menu item is clicked
        if (id == R.id.action_swap) {

            swapSwitch = !swapSwitch;
            Log.d(TAG, "onOptionsItemSelected: " + swapSwitch.toString());



            mRecyclerViewAdapter  = new ItemListRecyclerViewAdapter(this);
            mRecyclerView.setAdapter(mRecyclerViewAdapter);
            itemList.clear();
            mRecyclerViewAdapter.setMovieData(null);



            loadWeatherData();




            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
