package dashboard.core.streaming.stage;

import dashboard.core.model.HashTagVO;
import dashboard.core.model.TweetVO;
import dashboard.core.utils.GridConstants;
import org.gridgain.grid.GridException;
import org.gridgain.grid.streamer.GridStreamerContext;
import org.gridgain.grid.streamer.GridStreamerStage;
import org.gridgain.grid.streamer.GridStreamerWindow;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;


public class RemoveHashTagFromWindowsStage extends RemoveFromWindowStage<HashTagVO> implements GridStreamerStage<TweetVO>  {

    @Override
    public String name() {
        return this.getClass().getSimpleName();
    }

    @Nullable
    @Override
    public Map<String, Collection<?>> run(GridStreamerContext gridStreamerContext, Collection<TweetVO> tweets) throws GridException {

        final GridStreamerWindow<HashTagVO> oneMinute = gridStreamerContext.window(GridConstants.ONE_MINUTE_WINDOW);
        remove(oneMinute);

        final GridStreamerWindow<HashTagVO> fiveMinute = gridStreamerContext.window(GridConstants.FIVE_MINUTE_WINDOW);
        remove(fiveMinute);

        final GridStreamerWindow<HashTagVO> tenMinute = gridStreamerContext.window(GridConstants.TEN_MINUTE_WINDOW);
        remove(tenMinute);

        return null;

    }


}
