package ashleybaker.freddit_v1;

import androidx.fragment.app.Fragment;

/**
 * Interface used when a fragment needs to be shown, but we want to be able to return to the previous
 * fragment.
 *
 * Really just a way to make sure the main activity is the one doing fragment managing, and to reduce
 * code copying
 */

interface AddFragmentToBackStack {

    /**
     * Add a given fragment to the back stack
     * @param fragment The fragment we want to show
     */
    void addFragmentToBackStack(Fragment fragment);
}
