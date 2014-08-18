package dashboard.core.streaming.stage;

import dashboard.core.model.Tweet;
import dashboard.core.utils.GridConstants;
import org.gridgain.grid.Grid;
import org.gridgain.grid.GridException;
import org.gridgain.grid.cache.datastructures.GridCacheAtomicSequence;
import org.gridgain.grid.cache.datastructures.GridCacheDataStructures;
import org.gridgain.grid.streamer.GridStreamerContext;
import org.gridgain.grid.streamer.GridStreamerWindow;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;


public class AddTweetToWindowsStage extends AddToWindowStage<Tweet> {

    @Override
    public String name() {
        return this.getClass().getSimpleName();
    }

    @Nullable
    @Override
    public Map<String, Collection<?>> run(GridStreamerContext gridStreamerContext, Collection<Tweet> tweets) throws GridException {

        if (!tweets.isEmpty()) {

            final GridStreamerWindow<Tweet> streamerWindow = gridStreamerContext.window(GridConstants.TOP_TWEETERS_WINDOW);

            add(streamerWindow, tweets);

            incrementTotalCount(gridStreamerContext, tweets);

        }

        return Collections.<String, Collection<?>>singletonMap(AddTweetToDatabaseStage.class.getSimpleName(), tweets);

    }

    private void incrementTotalCount(GridStreamerContext gridStreamerContext, Collection<Tweet> tweets) {

        try {
            final Grid grid = gridStreamerContext.projection().grid();
            final GridCacheDataStructures dataStructures = grid.cache(GridConstants.ATOMIC_CACHE).dataStructures();
            final GridCacheAtomicSequence seq = dataStructures.atomicSequence(GridConstants.TOTAL_TWEETS, 0, true);

            assert seq != null;

            seq.addAndGet(tweets.size());

        } catch (GridException e) {
            logger.error("error incrementing total tweets", e);
        }

    }


}
