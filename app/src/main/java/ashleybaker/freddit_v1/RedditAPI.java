import retrofit2.http.GET;

public interface RedditAPI {

    String URL = "https://www.reddit.com/r/";

    public interface GitHubService {
        @GET("incorgnito/.rss")
        Call<Sub> getSub();
    }
}
