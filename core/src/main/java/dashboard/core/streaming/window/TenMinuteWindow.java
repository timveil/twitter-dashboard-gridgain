package dashboard.core.streaming.window;

import dashboard.core.model.HashTagVO;
import dashboard.core.streaming.index.provider.HashTagIndexProvider;
import org.gridgain.grid.streamer.window.GridStreamerBoundedTimeWindow;


public class TenMinuteWindow extends GridStreamerBoundedTimeWindow<HashTagVO> {

    public TenMinuteWindow() {
        super();

        setName(this.getClass().getName());
        setTimeInterval(10 * 60 * 1000);
        setIndexes(new HashTagIndexProvider());
    }
}
