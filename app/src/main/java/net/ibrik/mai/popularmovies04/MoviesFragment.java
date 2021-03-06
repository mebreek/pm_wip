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

/**
 * Created by Mohamad on 11/10/2016.
 */

public class MoviesFragment extends Fragment{
    private final String TMP_STR_URL_POPULAR = "https://api.themoviedb.org/3/movie/popular?api_key=" + APIKEY;
    private final String TMP_STR_URL_HIGHEST_RATED = "https://api.themoviedb.org/3/movie/top_rated?api_key="+  APIKEY;
    private final String LOG_TAG = MoviesFragment.class.getSimpleName();
    private final String LOG_TAG2 = MoviesFragment.FetchMoviesDetails.class.getSimpleName();
    private ArrayAdapter<String> mMoviesArrayAdapter;

    String [] strMoviesID;
    String [] strMoviesPosterPath92;
    String [] strMoviesPosterPath185;
    String [] strMoviesOverview;
    String [] strMoviesTitle;
    String [] strMovieSTRING;


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
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_details, container, false);

        FetchMoviesDetails fetchMoviesDetails = new FetchMoviesDetails();
        fetchMoviesDetails.execute("");

//        //mMoviesArrayAdapter = new ArrayAdapter<>(getActivity(),R.layout.list_item_one_movie, R.id.list_item_one_movie_textview,
//        //       new ArrayList<String>());
//        GridView gridView = (GridView) getActivity().findViewById(R.id.container_gv);
//        //if (strMovieSTRING != null) {
//            CustomGridViewAdapter customGridViewAdapter = new CustomGridViewAdapter(getContext(), strMovieSTRING);
//            gridView.setAdapter(customGridViewAdapter);
//
//            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    //String strMovie = customGridViewAdapter.getItem(position);
//                    String strMovie = " todel string";
//                    //Toast.makeText(getActivity(), forecast, Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(getActivity(), DetailsActivity.class)
//                            .putExtra(Intent.EXTRA_TEXT, strMovie)
//                            .putExtra("movieid",strMoviesID[position])
//                            .putExtra("movietitle",strMoviesTitle[position])
//                            .putExtra("movieoverview", strMoviesOverview[position])
//                            .putExtra("movieposterpath", strMoviesPosterPath185[position]);
//                    //intent.putExtra()
//                    startActivity(intent);
//                }
//            });
//        //}
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
/*                mMoviesArrayAdapter.clear();
                for (String strMovieDetail : result){
                    mMoviesArrayAdapter.add(strMovieDetail);*/

                GridView gridView = (GridView) getActivity().findViewById(R.id.container_gv);
                //if (strMovieSTRING != null) {
                CustomGridViewAdapter customGridViewAdapter = new CustomGridViewAdapter(getContext(), strMovieSTRING);
                gridView.setAdapter(customGridViewAdapter);

                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //String strMovie = customGridViewAdapter.getItem(position);
                        String strMovie = " todel string";
                        //Toast.makeText(getActivity(), forecast, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(), DetailsActivity.class)
                                .putExtra(Intent.EXTRA_TEXT, strMovie)
                                .putExtra("movieid", strMoviesID[position])
                                .putExtra("movietitle", strMoviesTitle[position])
                                .putExtra("movieoverview", strMoviesOverview[position])
                                .putExtra("movieposterpath", strMoviesPosterPath185[position]);
                        //intent.putExtra()
                        startActivity(intent);
                    }
                });
            }
        }



        private String[] getMoviesDataFromJson(String movieJsonStr)
                throws JSONException {
            final String LOG_TAG2 = MoviesFragment.FetchMoviesDetails.class.getSimpleName();
            // These are the JSON objects attributes that need to be extracted.
            final String TMDB_TITLE = "title";
            final String TMDB_OVERVIEW = "overview";
            final String TMDB_ID = "id";
            final String TMDB_RESULTS = "results";
            final String TMDB_POSTER_PATH = "poster_path";
            final String TMDB_BACKDROP_PATH="backdrop_path";
            final String TMDB_VOTE_COUNT="vote_count";
            final String TMDB_VOTE_AVG="vote_average";
            final String TMDB_ADULT="adult";
            final String TMDB_VIDEO="video";
            final String TMDB_RELEASED="release_date";
            final String TMDB_POPULARITY="popularity";


            // as per the implementation guide on
            // https://docs.google.com/document/d/1ZlN1fUsCSKuInLECcJkslIqvpKlP7jWL2TP9m6UiA6I/pub?embedded=true#h.easyt9e3rs4y
            // recomendation, we're using w185; other options are "w92", "w154", "w185", "w342", "w500", "w780", or "original".
            final String IMAGE_BASE185_URL = "https://image.tmdb.org/t/p/w185";
            final String IMAGE_BASE92_URL = "https://image.tmdb.org/t/p/w92";
            String strImage92URL, strImage185URL;

            JSONObject movieJson = new JSONObject(movieJsonStr);
            JSONArray moviesArray = movieJson.getJSONArray(TMDB_RESULTS);
            //JSONArray moviesIDarray = movieJson.getJSONArray(TMPB_ID);

            //Iterate the jsonArray and print the info of JSONObjects
            String strData;
            // The return string array
            String strReturnArray[] = new String [moviesArray.length()];
            //initialize String arrays
            strMoviesID = new String [moviesArray.length()];
            strMoviesTitle = new String [moviesArray.length()];
            strMoviesPosterPath92 = new String [moviesArray.length()];
            strMoviesPosterPath185= new String [moviesArray.length()];
            strMoviesOverview = new String [moviesArray.length()];
            strMovieSTRING = new String[moviesArray.length()];

            for (int i = 0; i < moviesArray.length(); i++) {
               JSONObject movieObject = moviesArray.getJSONObject(i);

                //strImage92URL = IMAGE_BASE92_URL + strMoviesPosterPath[i];
                //strImage185URL = IMAGE_BASE185_URL + strMoviesPosterPath[i];

                strMoviesID [i] = movieObject.optString(TMDB_ID);
                strMoviesTitle[i] = movieObject.optString(TMDB_TITLE);
                strMoviesPosterPath92[i] = IMAGE_BASE92_URL + movieObject.optString(TMDB_POSTER_PATH);
                strMoviesOverview[i] = movieObject.optString(TMDB_OVERVIEW);
                strMoviesPosterPath185[i] = IMAGE_BASE185_URL +movieObject.optString(TMDB_POSTER_PATH);

                strData = strMoviesTitle [i] + "," + strMoviesPosterPath92[i] ;
                strMovieSTRING [i]= strData;
                //strData = "\nID: " + strMoviesID [i] + ",\nTitle: " + strMoviesTitle[i] + ",\nImage: " + IMAGE_BASE185_URL + ",\nOverview: " + strMoviesOverview[i];

                //Log.v(LOG_TAG2, "getMoviesDataFromJson: " + strData);
                strReturnArray[i] = strData;
            }
            return strReturnArray;
        }
    }
}
