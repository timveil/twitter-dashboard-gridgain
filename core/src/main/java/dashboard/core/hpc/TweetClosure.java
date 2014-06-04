package dashboard.core.hpc;

import dashboard.core.model.Tweet;
import dashboard.core.utils.GridConstants;
import org.gridgain.grid.lang.GridClosure;
import org.gridgain.grid.streamer.GridStreamerContext;
import org.gridgain.grid.streamer.GridStreamerWindow;
import org.gridgain.grid.streamer.index.GridStreamerIndex;
import org.gridgain.grid.streamer.index.GridStreamerIndexEntry;

import java.util.Collection;

public class TweetClosure implements GridClosure<GridStreamerContext, Collection<GridStreamerIndexEntry<Tweet, String, Long>>> {

    @Override
    public Collection<GridStreamerIndexEntry<Tweet, String, Long>> apply(GridStreamerContext gridStreamerContext) {
        final GridStreamerWindow<Tweet> gridStreamerWindow = gridStreamerContext.window(GridConstants.TOP_TWEETERS_WINDOW);

        final GridStreamerIndex<Tweet, String, Long> index = gridStreamerWindow.index();

        return index.entries(0);
    }
}
