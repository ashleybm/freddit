package ashleybaker.freddit_v1;

import ashleybaker.freddit_v1.model.Feed;
import retrofit2.Call;
import retrofit2.http.GET;

public interface RedditAPI {

    String URL = "https://www.reddit.com/r/";

    @GET("earthporn/.rss")
    Call<Feed> getFeed();

}
