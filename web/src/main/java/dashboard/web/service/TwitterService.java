package dashboard.web.service;

import dashboard.web.model.KeyValuePair;
import dashboard.web.model.TweetVO;

import java.util.List;


public interface TwitterService {

    void ingest(int duration);

    List<KeyValuePair> getHashTagSummary(Class window);

    List<KeyValuePair> getTopTweeters();

    long getTotalTweets();

    long getTotalTweetsWithHashTag();

    List<TweetVO> findTweets(String text, String screenName);
}
