package dashboard.core.streaming.window;

import dashboard.core.model.TweetVO;
import dashboard.core.streaming.index.provider.TopTweeterIndexProvider;
import org.gridgain.grid.streamer.window.GridStreamerUnboundedWindow;


public class TopTweetersWindow extends GridStreamerUnboundedWindow<TweetVO> {

    public TopTweetersWindow() {
        super();

        setName(this.getClass().getName());
        setIndexes(new TopTweeterIndexProvider());
    }
}
