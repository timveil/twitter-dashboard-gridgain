package dashboard.streaming.window;

import dashboard.streaming.index.provider.TopTweeterIndexProvider;
import org.gridgain.grid.streamer.window.GridStreamerUnboundedWindow;
import org.springframework.social.twitter.api.Tweet;


public class TopTweetersWindow extends GridStreamerUnboundedWindow<Tweet> {

    public TopTweetersWindow() {
        super();

        setName(this.getClass().getName());
        setIndexes(new TopTweeterIndexProvider());
    }
}
