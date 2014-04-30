package dashboard.streaming.stage;

import dashboard.model.HashTagVO;
import dashboard.model.TweetVO;
import dashboard.streaming.window.FifteenMinuteWindow;
import dashboard.streaming.window.FiveMinuteWindow;
import dashboard.streaming.window.SixtyMinuteWindow;
import org.gridgain.grid.GridException;
import org.gridgain.grid.streamer.GridStreamerContext;
import org.gridgain.grid.streamer.GridStreamerStage;
import org.gridgain.grid.streamer.GridStreamerWindow;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;


public class RemoveHashTagFromWindowsStage extends EvictStage implements GridStreamerStage<TweetVO>  {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public String name() {
        return this.getClass().getSimpleName();
    }

    @Nullable
    @Override
    public Map<String, Collection<?>> run(GridStreamerContext gridStreamerContext, Collection<TweetVO> tweets) throws GridException {

        final GridStreamerWindow<HashTagVO> fiveMinuteWindow = gridStreamerContext.window(FiveMinuteWindow.class.getName());
        evict(fiveMinuteWindow);

        final GridStreamerWindow<HashTagVO> fifteenMinuteWindow = gridStreamerContext.window(FifteenMinuteWindow.class.getName());
        evict(fifteenMinuteWindow);

        final GridStreamerWindow<HashTagVO> sixtyMinuteWindow = gridStreamerContext.window(SixtyMinuteWindow.class.getName());
        evict(sixtyMinuteWindow);

        return Collections.<String, Collection<?>>singletonMap(gridStreamerContext.nextStageName(), tweets);

    }


}
