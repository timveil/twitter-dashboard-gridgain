package dashboard.streaming.window;

import dashboard.model.HashTagVO;
import dashboard.streaming.index.provider.HashTagIndexProvider;
import org.gridgain.grid.streamer.window.GridStreamerBoundedTimeWindow;


public class FifteenMinuteWindow extends GridStreamerBoundedTimeWindow<HashTagVO> {

    public FifteenMinuteWindow() {
        super();

        setName(this.getClass().getName());
        setTimeInterval(15 * 60 * 1000);
        setIndexes(new HashTagIndexProvider());
    }
}
