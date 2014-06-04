package dashboard.core.hpc;

import dashboard.core.model.TweetVO;
import dashboard.core.utils.GridConstants;
import org.gridgain.grid.lang.GridClosure;
import org.gridgain.grid.streamer.GridStreamerContext;
import org.gridgain.grid.streamer.GridStreamerWindow;
import org.gridgain.grid.streamer.index.GridStreamerIndex;
import org.gridgain.grid.streamer.index.GridStreamerIndexEntry;

import java.util.Collection;

public class TweetClosure implements GridClosure<GridStreamerContext, Collection<GridStreamerIndexEntry<TweetVO, String, Long>>> {

    @Override
    public Collection<GridStreamerIndexEntry<TweetVO, String, Long>> apply(GridStreamerContext gridStreamerContext) {
        final GridStreamerWindow<TweetVO> gridStreamerWindow = gridStreamerContext.window(GridConstants.TOP_TWEETERS_WINDOW);

        final GridStreamerIndex<TweetVO, String, Long> index = gridStreamerWindow.index();

        return index.entries(0);
    }
}
