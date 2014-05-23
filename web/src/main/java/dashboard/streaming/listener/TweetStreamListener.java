package dashboard.streaming.listener;

import dashboard.model.TweetVO;
import org.gridgain.grid.GridException;
import org.gridgain.grid.streamer.GridStreamer;
import org.springframework.social.twitter.api.Tweet;

public class TweetStreamListener extends BaseListener {


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

}
