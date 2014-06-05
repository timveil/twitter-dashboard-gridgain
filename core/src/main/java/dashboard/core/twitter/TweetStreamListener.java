package dashboard.core.twitter;

import dashboard.core.model.HashTagFactory;
import dashboard.core.model.TweetFactory;
import dashboard.core.streaming.stage.AddHashTagToWindowsStage;
import dashboard.core.streaming.stage.AddTweetToWindowsStage;
import org.gridgain.grid.GridException;
import org.gridgain.grid.streamer.GridStreamer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.twitter.api.*;

public class TweetStreamListener implements StreamListener {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private GridStreamer streamer;

    private int multiplier;

    public TweetStreamListener(GridStreamer streamer, int multiplier) {
        this.streamer = streamer;
        this.multiplier = multiplier;
    }

    @Override
    public void onTweet(org.springframework.social.twitter.api.Tweet tweet) {

        addTweetToStreamer(tweet, false);

        if (multiplier > 0) {
            for (int i = 0; i<multiplier; i++) {
                addTweetToStreamer(tweet, true);
            }
        }

    }

    private void addTweetToStreamer(Tweet tweet, boolean fake) {
        try {
            final dashboard.core.model.Tweet ggTweet = TweetFactory.create(tweet, fake);

            streamer.addEventToStage(AddTweetToWindowsStage.class.getSimpleName(), ggTweet);

            for (HashTagEntity entity : tweet.getEntities().getHashTags()) {
                streamer.addEventToStage(AddHashTagToWindowsStage.class.getSimpleName(), HashTagFactory.create(ggTweet, entity));
            }


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
