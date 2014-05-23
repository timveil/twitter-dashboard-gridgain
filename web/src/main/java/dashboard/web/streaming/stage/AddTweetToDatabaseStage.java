package dashboard.web.streaming.stage;

import dashboard.web.model.HashTagVO;
import dashboard.web.model.TweetVO;
import dashboard.web.utils.GridUtils;
import org.gridgain.grid.Grid;
import org.gridgain.grid.GridException;
import org.gridgain.grid.GridFuture;
import org.gridgain.grid.cache.GridCache;
import org.gridgain.grid.lang.GridInClosure;
import org.gridgain.grid.lang.GridPredicate;
import org.gridgain.grid.streamer.GridStreamerContext;
import org.gridgain.grid.streamer.GridStreamerStage;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;


public class AddTweetToDatabaseStage implements GridStreamerStage<TweetVO> {

    private final Logger log = LoggerFactory.getLogger(getClass());


    @Override
    public String name() {
        return this.getClass().getSimpleName();
    }

    @Nullable
    @Override
    public Map<String, Collection<?>> run(final GridStreamerContext gridStreamerContext, Collection<TweetVO> tweets) throws GridException {

        if (!tweets.isEmpty()) {

            Grid grid = GridUtils.getGrid();

            for (final TweetVO tweet : tweets) {
                putInCache(gridStreamerContext, grid, tweet);
            }
        }

        return Collections.<String, Collection<?>>singletonMap(gridStreamerContext.nextStageName(), tweets);
    }

    private void putInCache(final GridStreamerContext gridStreamerContext, Grid grid, final TweetVO tweet) {

        final GridCache<String, TweetVO> tweetCache = grid.cache(TweetVO.class.getName());

        final GridCache<String, HashTagVO> hashTagCache = grid.cache(HashTagVO.class.getName());

        final GridFuture<Boolean> putTweetFuture = tweetCache.putxAsync(tweet.getGUID(), tweet, (GridPredicate)null);

        putTweetFuture.listenAsync(new GridInClosure<GridFuture<Boolean>>() {
            @Override
            public void apply(GridFuture<Boolean> future) {

                if (future.isDone()) {

                    Long currentTotal = (Long) gridStreamerContext.localSpace().get(GridUtils.TOTAL_TWEETS);

                    if (currentTotal == null) {
                        currentTotal = 0L;
                    }

                    gridStreamerContext.localSpace().put(GridUtils.TOTAL_TWEETS, currentTotal += 1);

                    Long noHashTags = (Long) gridStreamerContext.localSpace().get(GridUtils.TOTAL_TWEETS_NO_HASH_TAGS);

                    if (noHashTags == null) {
                        noHashTags = 0L;
                    }


                    if (tweet.hasHashTags()) {
                        noHashTags += 1;
                    }

                    gridStreamerContext.localSpace().put(GridUtils.TOTAL_TWEETS_NO_HASH_TAGS, noHashTags);
                }
            }
        });

        if (tweet.hasHashTags()) {

            for (HashTagVO hashTagVO : tweet.getHashTags()) {

                hashTagCache.putxAsync(hashTagVO.getGUID(), hashTagVO, (GridPredicate)null);

            }
        }
    }
}
