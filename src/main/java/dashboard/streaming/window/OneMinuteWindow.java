package dashboard.streaming.window;

import dashboard.model.HashTagVO;
import dashboard.streaming.index.provider.HashTagIndexProvider;
import org.gridgain.grid.streamer.window.GridStreamerBoundedTimeWindow;


public class OneMinuteWindow extends GridStreamerBoundedTimeWindow<HashTagVO> {

    public OneMinuteWindow() {
        super();

        setName(this.getClass().getName());
        setTimeInterval(1 * 60 * 1000);
        setIndexes(new HashTagIndexProvider());
    }
}
