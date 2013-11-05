package dashboard.streaming.window;

import dashboard.streaming.index.provider.HashTagIndexProvider;
import org.gridgain.grid.streamer.window.GridStreamerBoundedTimeWindow;
import org.springframework.social.twitter.api.HashTagEntity;


public class FiveMinuteWindow extends GridStreamerBoundedTimeWindow<HashTagEntity> {

    public FiveMinuteWindow() {
        super();

        setName(this.getClass().getName());
        setTimeInterval(5 * 60 * 1000);
        setIndexes(new HashTagIndexProvider());
    }
}
