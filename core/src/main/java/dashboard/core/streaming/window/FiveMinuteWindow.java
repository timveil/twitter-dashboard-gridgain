package dashboard.core.streaming.window;

import dashboard.core.model.HashTagVO;
import dashboard.core.streaming.index.provider.HashTagIndexProvider;
import org.gridgain.grid.streamer.window.GridStreamerBoundedTimeWindow;


public class FiveMinuteWindow extends GridStreamerBoundedTimeWindow<HashTagVO> {

    public FiveMinuteWindow() {
        super();

        setName(this.getClass().getName());
        setTimeInterval(5 * 60 * 1000);
        setIndexes(new HashTagIndexProvider());
    }
}
