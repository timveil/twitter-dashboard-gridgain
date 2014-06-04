package dashboard.web.service;

import dashboard.core.model.Tweet;
import dashboard.web.model.KeyValuePair;

import java.util.List;


public interface TwitterService {

    void ingest(int duration);

    List<KeyValuePair> getHashTagSummary(String windowName);

    List<KeyValuePair> getTopTweeters();

    long getTotalTweets();

    long getTotalTweetsWithHashTag();

    List<Tweet> findTweets(String text, String screenName);
}
