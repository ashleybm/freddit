package ashleybaker.freddit_v1;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import androidx.annotation.NonNull;
import ashleybaker.freddit_v1.model.Feed;
import ashleybaker.freddit_v1.model.entry.Entry;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class CommentListAdapter {

    private static final String ARGS = "";

    private String postURL;

    private final String BASE_URL = "https://www.reddit.com/r/";
    private String currentFeed;
    private String TAG = "CommentsFragment :";

    private List<Comment> comment;
    private ListView listViewComments;

    /**
     * Empty constructor for newInstance()
     */
    public CommentsFragment() {}

    /**
     * Create a new CommentFragment from a specific URL
     * @param post
     * @return
     */
    static CommentsFragment newInstance(@NonNull Post post) {
        CommentsFragment fragment = new CommentsFragment();
        Bundle args = new Bundle();
        args.putString(ARGS, post.getPostURL());
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

        listViewComments = findViewById(R.id.commentsListView);

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

        try{
            String[] splitURL = postURL.split(BASE_URL);
            currentFeed = splitURL[1];
            Log.d(TAG, "onCreateView: currentFeed: " + currentFeed);
        }
        catch(ArrayIndexOutOfBoundsException e){
            //CATCHES POSTS THAT IT CAN'T ACCESS
            Log.e(TAG, "onCreateView: ArrayIndexOutOfBoundsException: " + e.getMessage());
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();

        RedditAPI redditAPI = retrofit.create(RedditAPI.class);

        Call<Feed> call = redditAPI.getFeed();

        call.enqueue(new Callback<Feed>() {
            @Override
            public void onResponse(Call<Feed> call, Response<Feed> response) {
                Log.e(TAG, "onResponse: Server Response: " + response.toString());

                List<Entry> entrys = response.body().getEntrys();
                for(int i = 0; i < entrys.size(); i++){
                    Log.d(TAG, "onResponse : entry: " + entrys.get(i).toString() + "\n");

                    ExtractXML extractXML = new ExtractXML(entrys.get(i).getContent(), "<div class=\"md\"><p>","</p>");
                    List<String> comments = extractXML.start();

                    try{
                        comment.add(new Comment(
                                comments.get(0),
                                entrys.get(i).getAuthor().getName(),
                                entrys.get(i).getUpdated(),
                                entrys.get(i).getId()
                        ));
                    }
                    catch(IndexOutOfBoundsException e){
                        comment.add(new Comment(
                                "Error Reading Content",
                                "None",
                                "None",
                                "None"
                        ));
                    }

                    catch(NullPointerException e){
                        comment.add(new Comment(
                                comments.get(0),
                                "None",
                                entrys.get(i).getUpdated(),
                                entrys.get(i).getId()
                        ));
                    }

                }

                listViewComments = (ListView) findViewById(R.id.commentsListView);

            }

            @Override
            public void onFailure(Call<Feed> call, Throwable t) {
                Log.e(TAG, "onFailure: Unable to retrieve RSS: " + t.getMessage());
                Toast.makeText(getActivity(), "An Error Occurred", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
