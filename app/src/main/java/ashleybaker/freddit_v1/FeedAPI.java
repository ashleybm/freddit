package ashleybaker.freddit_v1;

import retrofit2.http.GET;

public interface FeedAPI {

    String BASE_URL = "https://www.reddit.com/r/";

    @GET("earthporn/.rss")
    Call<Feed> getFeed();
}
