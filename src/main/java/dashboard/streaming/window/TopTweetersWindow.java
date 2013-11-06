package dashboard.streaming.window;

import dashboard.model.TweetVO;
import dashboard.streaming.index.provider.TopTweeterIndexProvider;
import org.gridgain.grid.streamer.window.GridStreamerUnboundedWindow;


public class TopTweetersWindow extends GridStreamerUnboundedWindow<TweetVO> {

    public TopTweetersWindow() {
        super();

        setName(this.getClass().getName());
        setIndexes(new TopTweeterIndexProvider());
    }
}
