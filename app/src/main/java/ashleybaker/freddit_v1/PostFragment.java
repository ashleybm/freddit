package ashleybaker.freddit_v1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

/**
 * Shows the details from the post the user selected
 */

public class PostFragment extends Fragment {

    /**
     * The 'constructor' for the fragment, sets up important stuff
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    /**
     * Called after onCreate(), sets up the view of the fragment
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return The view for the fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rss_feed_fragment, container, false);

        return view;
    }
}
