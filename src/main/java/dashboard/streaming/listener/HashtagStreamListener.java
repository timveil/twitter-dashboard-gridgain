package dashboard.streaming.listener;

import org.gridgain.grid.GridException;
import org.gridgain.grid.streamer.GridStreamer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.twitter.api.*;

public class HashtagStreamListener implements StreamListener {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private GridStreamer streamer;

    public HashtagStreamListener(GridStreamer streamer) {
        this.streamer = streamer;
    }

    @Override
    public void onTweet(Tweet tweet) {

        try {

            final Entities entities = tweet.getEntities();

            if (entities != null && entities.getHashTags() != null && !entities.getHashTags().isEmpty()) {
                streamer.addEvents(entities.getHashTags());
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
