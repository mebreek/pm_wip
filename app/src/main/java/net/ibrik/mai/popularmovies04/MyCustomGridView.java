package net.ibrik.mai.popularmovies04;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * Created by Mohamad on 17/10/2016.
 */


public class MyCustomGridView extends Activity {

    private Integer[] mThumbIds;



    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GridView gridview = (GridView) findViewById(R.id.container_gv);
        gridview.setAdapter(new MyCustomAdapter(this));
        gridview.setNumColumns(4);
    }

    public class MyCustomAdapter extends BaseAdapter {
        // code viewed online at
        // http://www.coderzheaven.com/2012/02/29/custom-gridview-in-android-a-simple-example/
        private Context mContext;

        public MyCustomAdapter(Context c) {
            mContext = c;
        }

        @Override
        public int getCount() {
            return mThumbIds.length;
        }

        @Override
        public Object getItem(int arg0) {
            return mThumbIds[arg0];
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View grid;

            if (convertView == null) {
                grid = new View(mContext);
                LayoutInflater inflater = getLayoutInflater();
                grid = inflater.inflate(R.layout.mygrid_layout, parent, false);
            } else {
                grid = (View) convertView;
            }

            ImageView imageView = (ImageView) grid.findViewById(R.id.moviethumbnail);
            imageView.setImageResource(mThumbIds[position]);

            return grid;
        }
    }
}

