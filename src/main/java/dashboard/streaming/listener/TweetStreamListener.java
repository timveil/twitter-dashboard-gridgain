package dashboard.streaming.listener;

import com.google.common.collect.Lists;
import dashboard.model.TweetVO;
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


        List<TweetVO> tweets = Lists.newArrayList();
        tweets.add(new TweetVO(tweet, false));

        for (int i = 0; i < MULTIPLIER; i++) {
            final Tweet fakeTweet = new Tweet(0, tweet.getText(), tweet.getCreatedAt(), tweet.getFromUser(), tweet.getProfileImageUrl(), tweet.getToUserId(), tweet.getFromUserId(), tweet.getLanguageCode(), tweet.getSource());
            fakeTweet.setUser(tweet.getUser());
            fakeTweet.setEntities(tweet.getEntities());

            tweets.add(new TweetVO(fakeTweet, true));
        }

        try {
            streamer.addEvents(tweets);
        } catch (GridException e) {
            log.error("error adding Tweet to streamer... " + e);
        }

    }

}
