package dashboard.streaming;

import org.gridgain.grid.Grid;
import org.gridgain.grid.GridException;
import org.gridgain.grid.streamer.GridStreamer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.twitter.api.*;

public class TwitterSampleListener implements StreamListener {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private Grid grid;

    public TwitterSampleListener(Grid grid) {
        this.grid = grid;
    }

    @Override
    public void onTweet(Tweet tweet) {
        GridStreamer streamer = grid.streamer("twitter-popular-hashtags");

        try {
            assert streamer != null;

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
