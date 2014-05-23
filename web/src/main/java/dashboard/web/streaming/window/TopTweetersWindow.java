package dashboard.web.streaming.window;

import dashboard.web.model.TweetVO;
import dashboard.web.streaming.index.provider.TopTweeterIndexProvider;
import org.gridgain.grid.streamer.window.GridStreamerUnboundedWindow;


public class TopTweetersWindow extends GridStreamerUnboundedWindow<TweetVO> {

    public TopTweetersWindow() {
        super();

        setName(this.getClass().getName());
        setIndexes(new TopTweeterIndexProvider());
    }
}
