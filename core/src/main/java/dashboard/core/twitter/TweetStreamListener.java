package dashboard.core.twitter;

import dashboard.core.model.TweetVO;
import org.gridgain.grid.GridException;
import org.gridgain.grid.streamer.GridStreamer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.twitter.api.StreamDeleteEvent;
import org.springframework.social.twitter.api.StreamListener;
import org.springframework.social.twitter.api.StreamWarningEvent;
import org.springframework.social.twitter.api.Tweet;

public class TweetStreamListener implements StreamListener {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private GridStreamer streamer;

    private int multiplier;

    public TweetStreamListener(GridStreamer streamer, int multiplier) {
        this.streamer = streamer;
        this.multiplier = multiplier;
    }

    @Override
    public void onTweet(Tweet tweet) {

        addEvent(tweet, false);


        for (int i = 0; i < multiplier; i++) {
            final Tweet fakeTweet = new Tweet(0, tweet.getText(), tweet.getCreatedAt(), tweet.getFromUser(), tweet.getProfileImageUrl(), tweet.getToUserId(), tweet.getFromUserId(), tweet.getLanguageCode(), tweet.getSource());
            fakeTweet.setUser(tweet.getUser());
            fakeTweet.setEntities(tweet.getEntities());

            addEvent(fakeTweet, true);

        }

    }

    private void addEvent(Tweet tweet, boolean fake) {
        try {
            streamer.addEvent(new TweetVO(tweet, fake));
        } catch (GridException e) {
            log.error("error adding Tweet to streamer... ", e);
        }
    }

    @Override
    public void onDelete(StreamDeleteEvent deleteEvent) {
        if (log.isTraceEnabled()) {
            log.trace("onDelete called, but not implemented");
        }
    }

    @Override
    public void onLimit(int numberOfLimitedTweets) {
        if (log.isWarnEnabled()) {
            log.warn("onLimit called, but not implemented");
        }
    }

    @Override
    public void onWarning(StreamWarningEvent warningEvent) {
        if (log.isWarnEnabled()) {
            log.warn("onWarning called, but not implemented");
        }
    }

}
