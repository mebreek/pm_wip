package net.ibrik.mai.popularmovies04;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by Mohamad on 12/10/2016.
 */


public class CustomGridViewAdapter extends BaseAdapter {

    private Context context;
    private final String[] gridValues;
    private String strMoviesTitles[];
    private String strMoviesPosters[];

    // This is a contructor
    public CustomGridViewAdapter(Context context, String[] gridValues) {
        this.context = context;
        this.gridValues = gridValues;
        if (gridValues != null){
            strMoviesTitles=getTitles(gridValues);
            strMoviesPosters = getImageURLs(gridValues);
        }
    }

    // getView method call depends on gridValues.length;
    @Override
    public int getCount() {
        return gridValues.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

        if (view == null) {
            gridView = new View(context);

            // get layout
            gridView = inflater.inflate(R.layout.list_item_one_movie, null);

            //set value to textview
            TextView textView = (TextView) gridView.findViewById(R.id.list_item_one_movie_textview);
            textView.setText(strMoviesTitles[position]);

            //set image (test)
            ImageView imageView = (ImageView) gridView.findViewById(R.id.list_item_one_movie_imageview);
            String strImageURL = strMoviesPosters[position];//"https://image.tmdb.org/t/p/w185/5N20rQURev5CNDcMjHVUZhpoCNC.jpg";
            Picasso.with(context).load(strImageURL).into(imageView);
        } else {
            gridView = (View) view;
        }
        return gridView;
    }

    private String [] getTitles (String [] strMovies){
        String [] strMovieT = new String [strMovies.length];
        String strTmp = "";
        int i;
        // if a comma is provided, get the second part of the [] string
        for (i=0; i<strMovies.length; i++){
            strTmp=strMovies[i].substring(0,strMovies[i].indexOf(","));
            strTmp=strTmp.replaceAll("'","\\\\'");
            if (strTmp==null){strTmp="Empty";}
            strMovieT[i]= strTmp;
        }
    return strMovieT;
    }

    private String [] getImageURLs (String [] strMovies){
        String [] strMovieIURL = new String [strMovies.length];
        int i;
        for (i=0; i<strMovies.length; i++){
            strMovieIURL[i]=strMovies[i].substring(strMovies[i].indexOf(",")+1,strMovies[i].length());
        }
        return strMovieIURL;
    }
}

