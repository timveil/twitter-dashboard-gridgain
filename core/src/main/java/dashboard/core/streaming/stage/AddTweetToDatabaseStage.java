package dashboard.core.streaming.stage;

import dashboard.core.model.Tweet;
import org.gridgain.grid.GridException;
import org.gridgain.grid.cache.GridCache;
import org.gridgain.grid.lang.GridPredicate;
import org.gridgain.grid.streamer.GridStreamerContext;
import org.gridgain.grid.streamer.GridStreamerStage;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;


public class AddTweetToDatabaseStage implements GridStreamerStage<Tweet> {

    @Override
    public String name() {
        return this.getClass().getSimpleName();
    }

    @Nullable
    @Override
    public Map<String, Collection<?>> run(final GridStreamerContext gridStreamerContext, Collection<Tweet> tweets) throws GridException {

        if (!tweets.isEmpty()) {

            final GridCache<String, Tweet> tweetCache = gridStreamerContext.projection().grid().cache(Tweet.class.getName());

            for (Tweet tweet : tweets) {
                tweetCache.putxAsync(tweet.getGUID(), tweet, (GridPredicate) null);
            }
        }

        return null;
    }

}
