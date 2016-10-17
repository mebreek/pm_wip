package net.ibrik.mai.popularmovies04;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

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

/**
 * Created by Mohamad on 11/10/2016.
 */

public class MoviesFragment extends Fragment{
    //private final String TMP_STR_URL = "https://api.themoviedb.org/3/movie/550?api_key=b03e207412a209274acbe55e34899204";
    private final String TMP_STR_URL_POPULAR = "https://api.themoviedb.org/3/movie/popular?api_key=b03e207412a209274acbe55e34899204";
    private final String TMP_STR_URL_HIGHEST_RATED = "https://api.themoviedb.org/3/movie/top_rated?api_key=b03e207412a209274acbe55e34899204";

    private final String LOG_TAG = MoviesFragment.class.getSimpleName();
    private final String LOG_TAG2 = MoviesFragment.FetchMoviesDetails.class.getSimpleName();
    private ArrayAdapter<String> mMoviesArrayAdapter;
    //mMoviesArrayAdapter = new ArrayAdapter<String>(getActivity(),R.layout.list_item_one_movie, R.id.list_item_one_movie_textview, new ArrayList<String> );
    //private String[] strArray;

    //constructor
    public MoviesFragment() {    }

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
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == R.id.action_refresh){
            updateMoviesDetail();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_details, container, false);

        FetchMoviesDetails fetchMoviesDetails = new FetchMoviesDetails();
        fetchMoviesDetails.execute("");

        mMoviesArrayAdapter = new ArrayAdapter<String>(getActivity(),R.layout.list_item_one_movie, R.id.list_item_one_movie_textview,
                new ArrayList<String>());
        GridView gridView = (GridView) rootView.findViewById(
                R.id.container_gv);
        gridView.setAdapter(mMoviesArrayAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String strMovie = mMoviesArrayAdapter.getItem(position);

                //Toast.makeText(getActivity(), forecast, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), DetailsActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, strMovie);
                startActivity(intent);
            }

        });
        return rootView;
    }

    private void updateMoviesDetail() {
        FetchMoviesDetails fetchMoviesDetails = new FetchMoviesDetails();
        SharedPreferences pref_sortby = PreferenceManager.getDefaultSharedPreferences(getContext());
        String sortby = pref_sortby.getString("sortby","1");
        fetchMoviesDetails.execute("550"); //550 movie id test
    }

    @Override
    public void onStart(){
        super.onStart();
        updateMoviesDetail();
    }

    public class FetchMoviesDetails extends AsyncTask<String, Void, String[]> {

        String[] resultStrs;
        private URL url;

        @Override
        protected String[] doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String movieJsonStr = null;


            try {
                // Construct the URL for the query
                final String MOVIE_BASE_URL = "https:api.themoviedb.org/3/movie/";
                final String QUERY_PARAM = "?";
                final String M_ID = "550";
                final String API = "api_key";
                final String APIKEY = "=b03e207412a209274acbe55e34899204";

                // query builder by Mohamad
                Uri.Builder builtUri = new Uri.Builder();
                builtUri.scheme("https")
                        .authority("api.themoviedb.org")
                        .appendPath("3")
                        .appendPath("movie")
                        //.appendQueryParameter("id", params[0]) // the movie ID
                        .appendQueryParameter(API, APIKEY);
                // set the url "sort by" according to settings
                SharedPreferences pref_sortby = PreferenceManager.getDefaultSharedPreferences(getContext());
                String sortby = pref_sortby.getString("sortby","1");
                if (sortby.equals("1")) {
                    // URL url = new URL (builtUri.toString());
                    url = new URL(TMP_STR_URL_POPULAR.toString());
                } else {
                    url = new URL(TMP_STR_URL_HIGHEST_RATED);
                }
                //Log.v(LOG_TAG, "Built URI " + builtUri.toString());
                //Log.v(LOG_TAG, "URL " + url.toString());

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
                //Log.v(LOG_TAG, " doInBackground: \n" + buffer.toString());

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                movieJsonStr = buffer.toString();

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the data, there's no point in attemping
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
            return null;
        }

        @Override
        protected void onPostExecute (String[] result){
            if (result != null) {
                mMoviesArrayAdapter.clear();
                for (String mMovieStr : result){
                    mMoviesArrayAdapter.add(mMovieStr);
                }
            }
        }

        private String[] getMoviesDataFromJson(String movieJsonStr)
                throws JSONException {
            final String LOG_TAG2 = MoviesFragment.FetchMoviesDetails.class.getSimpleName();
            // These are the names of the JSON objects that need to be extracted.
            final String TMDB_TITLE = "title";
            final String TMDB_OVERVIEW = "overview";
            final String TMDB_ID = "id";
            final String TMDB_RESULTS = "results";
            final String TMDB_POSTER_PATH = "poster_path";
            // as per the implementation guide on
            // https://docs.google.com/document/d/1ZlN1fUsCSKuInLECcJkslIqvpKlP7jWL2TP9m6UiA6I/pub?embedded=true#h.easyt9e3rs4y
            // recomendation, we're using w185; other options are "w92", "w154", "w185", "w342", "w500", "w780", or "original".
            final String IMAGE_BASE185_URL = "https://image.tmdb.org/t/p/w185";
            final String IMAGE_BASE92_URL = "https://image.tmdb.org/t/p/w92";
            String strMovieID, strMovieTitle, strMovieOverview, strMoviePosterPath, strImage92URL, strImage185URL;

            JSONObject movieJson = new JSONObject(movieJsonStr);
            JSONArray moviesArray = movieJson.getJSONArray(TMDB_RESULTS);
            //JSONArray moviesIDarray = movieJson.getJSONArray(TMPB_ID);

            //Iterate the jsonArray and print the info of JSONObjects
            String strData;
            // The return string array
            String strReturnArray[] = new String [moviesArray.length()];
            for (int i = 0; i < moviesArray.length(); i++) {
               JSONObject movieObject = moviesArray.getJSONObject(i);

                strMovieID = movieObject.optString(TMDB_ID);
                strMovieTitle = movieObject.optString(TMDB_TITLE);
                strMoviePosterPath = movieObject.optString(TMDB_POSTER_PATH);
                strMovieOverview = movieObject.optString(TMDB_OVERVIEW);
                // build image URL
                // TODO modify the below (IMAGE_BASE_URL) to be more flexible
                //strImage92URL = IMAGE_BASE92_URL + strMoviePosterPath;
                strImage185URL = IMAGE_BASE185_URL + strMoviePosterPath;
                //strData = strImage92URL;
                strData = "\nID: " + strMovieID + ",\nTitle: " + strMovieTitle + ",\nImage: " + strImage185URL + ",\nOverview: " + strMovieOverview;

                //Log.v(LOG_TAG2, "getMoviesDataFromJson: " + strData);
                strReturnArray[i] = strData;
            }
            return strReturnArray;
        }
    }

    // The below code was copied from
    // http://stackoverflow.com/questions/1560788/how-to-check-internet-access-on-android-inetaddress-never-times-out
    // as per the implementation guide found at
    // https://docs.google.com/document/d/1ZlN1fUsCSKuInLECcJkslIqvpKlP7jWL2TP9m6UiA6I/pub?embedded=true#h.bmztf99oydcp
}
