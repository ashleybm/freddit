package ashleybaker.freddit_v1;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import ashleybaker.freddit_v1.model.Feed;
import ashleybaker.freddit_v1.model.entry.Entry;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/**
 * Displays the Reddit RSS feed in a cardview
 */

public class RSSFeedFragment extends Fragment implements ListView.OnItemClickListener {

    private static final String TAG = "RSSFeedFragment";

    private static final String URL = "https://www.reddit.com/r/";

    private AddFragmentToBackStack parentActivity;

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
        final View view = inflater.inflate(R.layout.rss_feed_fragment, container, false);

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
                    ExtractXML extractXML_ahref = new ExtractXML(entrys.get(i).getContent(), "<a href=");
                    List<String> postContent = extractXML_ahref.start();

                    ExtractXML extractXML_imgsrc = new ExtractXML(entrys.get(i).getContent(), "<img src=");

                    try{
                        postContent.add(extractXML_imgsrc.start().get(0));
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

                    //for(int j = 0; j < posts.size(); j++){
                    //  Log.d(TAG, "onResponse: \n " +
                    //        "PostURL: " + posts.get(j).getPostURL() + "\n " +
                    //      "ThumbnailURL: " +  posts.get(j).getThumbnailURL() + "\n " +
                    //    "Title: " + posts.get(j).getTitle() + "\n " +
                    //  "Author: " + posts.get(j).getAuthor() + "\n " +
                    //"updated: " + posts.get(j).getDate_updated() + "\n ");
                    //}

                    ListView listView = (ListView) view.findViewById(R.id.listView);
                    CustomListAdapter customListAdapter = new CustomListAdapter(getActivity(), R.layout.card_layout_main, posts);
                    listView.setAdapter(customListAdapter);
                }
            }

            @Override
            public void onFailure(Call<Feed> call, Throwable t) {
                Log.e(TAG, "onFailure: Unable to retrieve RSS: " + t.getMessage());
                Toast.makeText(getActivity(), "An Error Occured", Toast.LENGTH_SHORT).show();
            }
        });

        ListView listView = (ListView) view.findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AddFragmentToBackStack) {
            parentActivity = (AddFragmentToBackStack) context;
        } else {
            throw new RuntimeException(context.toString()
                    + "  must implement AddFragmentToBackStack");
        }
    }

    /**
     * Do things when the user clicks on something in the list
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getActivity(), "You touched " + position, Toast.LENGTH_SHORT).show();

        // TODO: create new PostFragment and add to back stack
    }
}
