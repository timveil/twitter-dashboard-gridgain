package dashboard.service;

import dashboard.model.KeyValuePair;
import dashboard.utils.StreamerWindow;

import java.util.List;


public interface TwitterService {

    void ingest(int duration);

    List<KeyValuePair> getHashTagSummary(StreamerWindow window);

    List<KeyValuePair> getTopTweeters();
    long getTotalTweets();
    long getTotalTweetsWithHashTag();
}
