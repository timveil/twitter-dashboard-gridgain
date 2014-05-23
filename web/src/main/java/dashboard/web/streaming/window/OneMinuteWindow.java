package dashboard.web.streaming.window;

import dashboard.web.model.HashTagVO;
import dashboard.web.streaming.index.provider.HashTagIndexProvider;
import org.gridgain.grid.streamer.window.GridStreamerBoundedTimeWindow;


public class OneMinuteWindow extends GridStreamerBoundedTimeWindow<HashTagVO> {

    public OneMinuteWindow() {
        super();

        setName(this.getClass().getName());
        setTimeInterval(1 * 60 * 1000);
        setIndexes(new HashTagIndexProvider());
    }
}
