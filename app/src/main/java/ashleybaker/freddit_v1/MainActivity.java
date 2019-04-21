package ashleybaker.freddit_v1;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    private FragmentManager fragmentManager;
    private Fragment activeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
