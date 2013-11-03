package dashboard.streaming;

import org.gridgain.grid.GridException;
import org.gridgain.grid.streamer.GridStreamer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.twitter.api.StreamDeleteEvent;
import org.springframework.social.twitter.api.StreamListener;
import org.springframework.social.twitter.api.StreamWarningEvent;
import org.springframework.social.twitter.api.Tweet;

public class HashtagStreamListener implements StreamListener {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private GridStreamer streamer;

    public HashtagStreamListener(GridStreamer streamer) {
        this.streamer = streamer;
    }

    @Override
    public void onTweet(Tweet tweet) {

        try {

            if (tweet.getEntities() != null && tweet.getEntities().getHashTags() != null) {

                streamer.addEvents(tweet.getEntities().getHashTags());

            } else {
                log.warn("tweet had no hash tags");
            }
        } catch (GridException e) {
            log.error("error adding event... " + e);
        }

    }

    @Override
    public void onDelete(StreamDeleteEvent deleteEvent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onLimit(int numberOfLimitedTweets) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onWarning(StreamWarningEvent warningEvent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
