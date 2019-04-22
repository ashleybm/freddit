package ashleybaker.freddit_v1;

import android.content.SharedPreferences;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.preference.PreferenceManager;

public class MainActivity extends AppCompatActivity implements AddFragmentToBackStack {

    private DrawerLayout drawerLayout;

    private FragmentManager fragmentManager;
    private Fragment activeFragment;

    private static final String PREF_DARK_THEME = "dark_theme";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean useDarkTheme = preferences.getBoolean(PREF_DARK_THEME, false);

        if(useDarkTheme){
            setTheme(R.style.AppTheme_Dark_NoActionBar);
        }

        setContentView(R.layout.activity_main);

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
                            case R.id.nav_pick_subreddit :
                                newFragment = new RSSFeedFragment();
                                break;
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
        Fragment fragment = new RSSFeedFragment();
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

    /**
     * Show the given fragment, adding it to the back stack
     * @param fragment The fragment we want to show
     */
    @Override
    public void addFragmentToBackStack(Fragment fragment) {
        fragmentManager.beginTransaction()
                .replace(R.id.fragmentHolder, fragment)
                .addToBackStack(null)
                .commit();
        activeFragment = fragment;
    }
}
