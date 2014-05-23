package dashboard.web.streaming.window;

import dashboard.web.model.HashTagVO;
import dashboard.web.streaming.index.provider.HashTagIndexProvider;
import org.gridgain.grid.streamer.window.GridStreamerBoundedTimeWindow;


public class TenMinuteWindow extends GridStreamerBoundedTimeWindow<HashTagVO> {

    public TenMinuteWindow() {
        super();

        setName(this.getClass().getName());
        setTimeInterval(10 * 60 * 1000);
        setIndexes(new HashTagIndexProvider());
    }
}
