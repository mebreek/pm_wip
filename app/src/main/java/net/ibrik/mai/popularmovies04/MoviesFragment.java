package net.ibrik.mai.popularmovies04;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Mohamad on 11/10/2016.
 */

public class MoviesFragment extends Fragment {
    //private final String TMP_STR_URL = "https://api.themoviedb.org/3/movie/550?api_key=b03e207412a209274acbe55e34899204";
    private final String TMP_STR_URL_POPULAR = "https://api.themoviedb.org/3/movie/popular?api_key=b03e207412a209274acbe55e34899204";
    //private final String TMP_STR_URL_HIGHEST_RATED = "https://api.themoviedb.org/3/movie/top_rated?api_key=b03e207412a209274acbe55e34899204";

    private final String LOG_TAG = MoviesFragment.class.getSimpleName();

    private ArrayAdapter<String> mMoviesArrayAdapter;
    private String [] strArray;

    //constructor
    public MoviesFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_movies_fragment, menu);
        inflater.inflate(R.menu.menu_details, menu);
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragmentplaceholder, container, false);
        strArray = new String[] {"Hello", "There", "How do you do", "star . star"};
        List<String> mMovieArrayList = new ArrayList<String>(Arrays.asList(strArray));

        mMoviesArrayAdapter = new ArrayAdapter<String>(getActivity(),R.layout.list_item_one_movie, R.id.list_item_one_movie_textview,strArray );
        ListView listView = (ListView) rootView.findViewById(
                R.id.listView_movie);
        listView.setAdapter(mMoviesArrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView<?> parent, View view, int position, long id){
                String strMovie = mMoviesArrayAdapter.getItem(position);

                //Toast.makeText(getActivity(), forecast, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(),  DetailsActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, strMovie);
                startActivity(intent);
            }

        });
        return rootView;
    }

    public class MoviesDetails extends AsyncTask<String, Void, String[]> {

        String[] resultStrs;

        @Override
        protected String[] doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String movieJsonStr = null;

            try {
                // Construct the URL for the query
                final String FORECAST_BASE_URL = "https:api.themoviedb.org/3/movie/";
                final String QUERY_PARAM = "?";
                final String M_ID = "550";
                final String API = "api_key";
                final String APIKEY = "=b03e207412a209274acbe55e34899204";

                // query builder as per lesson
                /**            Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                 .appendQueryParameter(QUERY_PARAM, params[0])
                 .appendQueryParameter(FORMAT_PARAM, format)
                 .appendQueryParameter(UNITS_PARAM,units)
                 .appendQueryParameter(DAYS_PARAM,Integer.toString(numDays))
                 .appendQueryParameter(API, APIKEY)
                 .build();
                 **/

                // query builder by Mohamad
                Uri.Builder builtUri = new Uri.Builder();
                builtUri.scheme("https")
                        .authority("api.themoviedb.org")
                        .appendPath("3")
                        .appendPath("movie")
                        .appendQueryParameter("id", params[0]) // the movie ID
                        .appendQueryParameter(API, APIKEY);
                // URL url = new URL (builtUri.toString());
                URL url = new URL(TMP_STR_URL_POPULAR.toString());
                //Log.v(LOG_TAG, "Built URI " + builtUri.toString());
                Log.v(LOG_TAG, "URL " + url.toString());

                // Create the request to the moviedb, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }
                Log.v(LOG_TAG, " doInBackground: \n" + buffer.toString());

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                movieJsonStr = buffer.toString();

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            try {
                return getMoviesDataFromJson(movieJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return resultStrs;
        }

        private String[] getMoviesDataFromJson(String movieJsonStr)
                throws JSONException {
            final String LOG_TAG2 = MoviesFragment.class.getSimpleName();
            // These are the names of the JSON objects that need to be extracted.
            final String TMDB_TITLE = "title";
            final String TMDB_DESCRIPTION = "overview";
            final String TMPB_ID = "id";

            JSONObject movieJson = new JSONObject(movieJsonStr);
            JSONArray moviesArray = movieJson.getJSONArray(TMDB_TITLE);
            JSONArray moviesIDarray = movieJson.getJSONArray(TMPB_ID);

           /* SharedPreferences sharedPrefs =
                    PreferenceManager.getDefaultSharedPreferences(getActivity());
            String unitType = sharedPrefs.getString(
                    getString(R.string.pref_units_key),
                    getString(R.string.pref_units_metric));*/

            for (int i = 0; i < moviesArray.length(); i++) {
                // For now, using the format "Day, description, hi/low"
                String day;
                String description;

                // Get the JSON object representing the day
                JSONObject oneMovieJsonObj = moviesArray.getJSONObject(i);


                // description is in a child array called "weather", which is 1 element long.
                JSONObject oneMovieObject = oneMovieJsonObj.getJSONArray(TMDB_TITLE).getJSONObject(0);
                description = oneMovieObject.getString(TMDB_DESCRIPTION);
            }

            for (String s : resultStrs) {
                Log.v(LOG_TAG2, "Movie entry: " + s);
            }
            return resultStrs;
        }
    }
}