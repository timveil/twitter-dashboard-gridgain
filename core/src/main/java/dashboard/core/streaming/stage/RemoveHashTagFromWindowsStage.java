package dashboard.core.streaming.stage;

import dashboard.core.model.HashTagVO;
import dashboard.core.model.TweetVO;
import dashboard.core.utils.GridUtils;
import org.gridgain.grid.GridException;
import org.gridgain.grid.streamer.GridStreamerContext;
import org.gridgain.grid.streamer.GridStreamerStage;
import org.gridgain.grid.streamer.GridStreamerWindow;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;


public class RemoveHashTagFromWindowsStage extends RemoveFromWindowStage<HashTagVO> implements GridStreamerStage<TweetVO>  {

    @Override
    public String name() {
        return this.getClass().getSimpleName();
    }

    @Nullable
    @Override
    public Map<String, Collection<?>> run(GridStreamerContext gridStreamerContext, Collection<TweetVO> tweets) throws GridException {

        final GridStreamerWindow<HashTagVO> oneMinute = gridStreamerContext.window(GridUtils.ONE_MINUTE_WINDOW);
        remove(oneMinute);

        final GridStreamerWindow<HashTagVO> fiveMinute = gridStreamerContext.window(GridUtils.FIVE_MINUTE_WINDOW);
        remove(fiveMinute);

        final GridStreamerWindow<HashTagVO> tenMinute = gridStreamerContext.window(GridUtils.TEN_MINUTE_WINDOW);
        remove(tenMinute);

        return Collections.<String, Collection<?>>singletonMap(gridStreamerContext.nextStageName(), tweets);

    }


}
