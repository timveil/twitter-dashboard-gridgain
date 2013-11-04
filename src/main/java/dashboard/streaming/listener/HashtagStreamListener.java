package dashboard.streaming.listener;

import org.gridgain.grid.GridException;
import org.gridgain.grid.streamer.GridStreamer;
import org.springframework.social.twitter.api.Entities;
import org.springframework.social.twitter.api.Tweet;

public class HashTagStreamListener extends BaseListener {

    private GridStreamer streamer;

    public HashTagStreamListener(GridStreamer streamer) {
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
            log.error("error adding HashTag to streamer... " + e);
        }

    }

}
