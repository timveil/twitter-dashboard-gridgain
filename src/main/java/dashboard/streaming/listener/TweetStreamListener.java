package dashboard.streaming.listener;

import com.google.common.collect.Lists;
import org.gridgain.grid.GridException;
import org.gridgain.grid.streamer.GridStreamer;
import org.springframework.social.twitter.api.Tweet;

import java.util.List;

public class TweetStreamListener extends BaseListener {

    public static final int MULTIPLIER = 55;

    private GridStreamer streamer;

    public TweetStreamListener(GridStreamer streamer) {
        this.streamer = streamer;
    }

    @Override
    public void onTweet(Tweet tweet) {


        List<Tweet> tweets = Lists.newArrayList();
        tweets.add(tweet);

        for (int i = 0; i < MULTIPLIER; i++) {
            final Tweet fakeTweet = new Tweet(tweet.getId() + (i + 1), tweet.getText() + "-FAKE", tweet.getCreatedAt(), tweet.getFromUser(), null, tweet.getToUserId(), tweet.getFromUserId(), tweet.getLanguageCode(), tweet.getSource());
            fakeTweet.setUser(tweet.getUser());
            fakeTweet.setEntities(tweet.getEntities());

            tweets.add(fakeTweet);
        }

        try {
            streamer.addEvents(tweets);
        } catch (GridException e) {
            log.error("error adding Tweet to streamer... " + e);
        }

    }

}
