package dashboard.streaming.listener;

import org.gridgain.grid.GridException;
import org.gridgain.grid.streamer.GridStreamer;
import org.springframework.social.twitter.api.Tweet;

public class TweetStreamListener  extends BaseListener {

    private GridStreamer streamer;

    public TweetStreamListener(GridStreamer streamer) {
        this.streamer = streamer;
    }

    @Override
    public void onTweet(Tweet tweet) {

        try {
            streamer.addEvent(tweet);
        } catch (GridException e) {
            log.error("error adding Tweet to streamer... " + e);
        }

    }

}
