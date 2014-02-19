package dashboard.service;

import dashboard.model.KeyValuePair;
import dashboard.model.TweetVO;

import java.util.List;


public interface TwitterService {

    void ingest(int duration);

    List<KeyValuePair> getHashTagSummary(Class window);

    List<KeyValuePair> getTopTweeters();

    long getTotalTweets();

    long getTotalTweetsWithHashTag();

    List<TweetVO> findTweets(String text, String screenName);
}
