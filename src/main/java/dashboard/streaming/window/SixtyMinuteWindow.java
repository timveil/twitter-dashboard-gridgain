package dashboard.streaming.window;

import dashboard.model.HashTagVO;
import dashboard.streaming.index.provider.HashTagIndexProvider;
import org.gridgain.grid.streamer.window.GridStreamerBoundedTimeWindow;


public class SixtyMinuteWindow extends GridStreamerBoundedTimeWindow<HashTagVO> {

    public SixtyMinuteWindow() {
        super();

        setName(this.getClass().getName());
        setTimeInterval(60 * 60 * 1000);
        setIndexes(new HashTagIndexProvider());
    }
}
