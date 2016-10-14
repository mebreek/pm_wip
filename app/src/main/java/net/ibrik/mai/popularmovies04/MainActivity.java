package net.ibrik.mai.popularmovies04;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.activity_main,new MoviesFragment())
                    .commit();
        }
    }

/*
    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }
*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == R.id.action_settings){
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

 /*   public static class PlaceholderFragment extends Fragment {
        private ArrayAdapter<String> mMovieAdapter;
        private String [] strArray;

        public PlaceholderFragment(){}


        @Override
        public View onCreateView (LayoutInflater inflater, ViewGroup container,
                                  Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragmentplaceholder, container, false);
            strArray = new String[] {"Hello", "there", "how do you do", "star . star"};
            List<String> mMovieArrayList = new ArrayList<String>(Arrays.asList(strArray));

            mMovieAdapter = new ArrayAdapter<String>(getActivity(),R.layout.list_item_one_movie, R.id.list_item_one_movie_textview,strArray );
            ListView listView = (ListView) rootView.findViewById(
                    R.id.listView_movie);
            listView.setAdapter(mMovieAdapter);

            return rootView;
        }
    }*/
}
