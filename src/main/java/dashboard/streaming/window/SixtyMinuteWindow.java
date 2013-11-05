package dashboard.streaming.window;

import dashboard.streaming.index.provider.HashTagIndexProvider;
import org.gridgain.grid.streamer.window.GridStreamerBoundedTimeWindow;
import org.springframework.social.twitter.api.HashTagEntity;


public class SixtyMinuteWindow extends GridStreamerBoundedTimeWindow<HashTagEntity> {

    public SixtyMinuteWindow() {
        super();

        setName(this.getClass().getName());
        setTimeInterval(60 * 60 * 1000);
        setIndexes(new HashTagIndexProvider());
    }
}
