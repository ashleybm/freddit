package ashleybaker.freddit_v1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

/**
 * Shows the details from the post the user selected
 */

public class CommentsFragment extends Fragment {

    private static final String ARGS = "";

    private String postURL;


    /**
     * Empty constructor for newInstance()
     */
    public CommentsFragment() {}

    /**
     * Create a new CommentFragment from a specific URL
     * @param postURL
     * @return
     */
    static CommentsFragment newInstance(@NonNull String postURL) {
        CommentsFragment fragment = new CommentsFragment();
        Bundle args = new Bundle();
        args.putString(ARGS, postURL);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * The 'constructor' for the fragment, sets up important stuff
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            postURL = getArguments().getString(ARGS);
        } else {
            postURL = ""; // TODO: Make this a safe default
        }
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
