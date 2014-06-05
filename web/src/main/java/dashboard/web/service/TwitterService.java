package dashboard.web.service;

import dashboard.core.model.Tweet;
import dashboard.web.model.KeyValuePair;

import java.util.List;


public interface TwitterService {

    void ingest(int duration, int multiplier);

    List<KeyValuePair> getHashTagSummary(String windowName);

    List<KeyValuePair> getTopTweeters();

    long getTotalTweets();

    long getTotalHashTags();

    List<Tweet> findTweets(String text, String screenName);
}
