package dashboard.streaming.window;

import dashboard.streaming.index.provider.HashTagIndexProvider;
import org.gridgain.grid.streamer.window.GridStreamerBoundedTimeWindow;
import org.springframework.social.twitter.api.HashTagEntity;


public class FifteenMinuteWindow extends GridStreamerBoundedTimeWindow<HashTagEntity> {

    public FifteenMinuteWindow() {
        super();

        setName(this.getClass().getName());
        setTimeInterval(15 * 60 * 1000);
        setIndexes(new HashTagIndexProvider());
    }
}
