package ashleybaker.freddit_v1;

import android.content.SharedPreferences;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import ashleybaker.freddit_v1.model.Feed;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

import ashleybaker.freddit_v1.model.entry.Entry;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static final String URL = "https://www.reddit.com/r/";

    private DrawerLayout drawerLayout;

    private FragmentManager fragmentManager;
    private Fragment activeFragment;

    private static final String PREFS_NAME = "prefs";
    private static final String PREF_DARK_THEME = "dark_theme";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean useDarkTheme = preferences.getBoolean(PREF_DARK_THEME, false);

        if(useDarkTheme){
            setTheme(R.style.AppTheme_Dark_NoActionBar);
        }

        setContentView(R.layout.activity_main);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();

        RedditAPI redditAPI = retrofit.create(RedditAPI.class);

        Call<Feed> call = redditAPI.getFeed();

        call.enqueue(new Callback<Feed>() {
            @Override
            public void onResponse(Call<Feed> call, Response<Feed> response) {
                //Log.d(TAG, "onResponse: feed: " + response.body().toString());
                Log.e(TAG, "onResponse: Server Response: " + response.toString());

                List<Entry> entrys = response.body().getEntrys();
                Log.d(TAG, "onResponse: entrys: " + response.body().getEntrys());

                //Log.d(TAG, "onResponse: author: " + entrys.get(0).getAuthor());
                //Log.d(TAG, "onResponse: updated: " + entrys.get(0).getUpdated());
                //Log.d(TAG, "onResponse: title: " + entrys.get(0).getTitle());

                ArrayList<Post> posts = new ArrayList<Post>();
                for(int i = 0; i < entrys.size(); i++){
                    ExtractXML extractXML_ahref = new ExtractXML(entrys.get(0).getContent(), "<a href=");
                    List<String> postContent = extractXML_ahref.start();

                    ExtractXML extractXML_imgsrc = new ExtractXML(entrys.get(0).getContent(), "<img src=\"");

                    try{
                        postContent.add(extractXML_imgsrc.findThumbNail());
                    }
                    catch(NullPointerException e){
                        postContent.add(null);
                        Log.e(TAG, "onResponse: NullPointerException(thumbnail):" + e.getMessage());
                    }
                    catch(IndexOutOfBoundsException e){
                        postContent.add(null);
                        Log.e(TAG, "onResponse: IndexOutOfBoundsException(thumbnail):" + e.getMessage());
                    }

                    int lastPosition = postContent.size() - 1;
                    posts.add(new Post(
                            entrys.get(i).getTitle(),
                            entrys.get(i).getAuthor().getName(),
                            entrys.get(i).getUpdated(),
                            postContent.get(0),
                            postContent.get(lastPosition)
                            )
                    );

                    for(int j = 0; j < posts.size(); j++){
                        Log.d(TAG, "onResponse: \n " +
                                "PostURL: " + posts.get(j).getPostURL() + "\n " +
                                "ThumbnailURL: " +  posts.get(j).getThumbnailURL() + "\n " +
                                "Title: " + posts.get(j).getTitle() + "\n " +
                                "Author: " + posts.get(j).getAuthor() + "\n " +
                                "updated: " + posts.get(j).getDate_updated() + "\n ");
                    }
                }
            }

            @Override
            public void onFailure(Call<Feed> call, Throwable t) {
                Log.e(TAG, "onFailure: Unable to retrieve RSS: " + t.getMessage());
                Toast.makeText(MainActivity.this, "An Error Occured", Toast.LENGTH_SHORT).show();
            }
        });

        fragmentManager = getSupportFragmentManager();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        drawerLayout.closeDrawers();

                        // Swap out Fragment
                        Fragment newFragment = null;
                        switch (menuItem.getItemId()) {

                            // TODO: Add our fragments here when the user selects an item in the nav drawer

                            case R.id.settings :
                                newFragment = new SettingsFragment();
                                break;
                            default : break;
                        }

                        if(newFragment != null) {
                            fragmentManager.beginTransaction().replace(R.id.fragmentHolder, newFragment).commit();
                            activeFragment = newFragment;
                        }

                        return true;
                    }
                });

        // Add the default fragment
        Fragment fragment = new SettingsFragment();
        fragmentManager.beginTransaction().replace(R.id.fragmentHolder, fragment).commit();
        activeFragment = fragment;
    }

    /**
     * Switches theme to dark if preference is set. Will restart application
     * @param darkTheme Use dark theme
     */
    public void toggleTheme(boolean darkTheme) {
        Toast.makeText(this, "App restarting to apply theme", Toast.LENGTH_LONG).show();

        Intent intent = getIntent();
        finish();

        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
