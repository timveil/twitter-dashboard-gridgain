package dashboard.streaming.stage;

import dashboard.model.HashTagVO;
import dashboard.model.TweetVO;
import dashboard.utils.GridUtils;
import org.gridgain.grid.Grid;
import org.gridgain.grid.GridException;
import org.gridgain.grid.GridFuture;
import org.gridgain.grid.cache.GridCache;
import org.gridgain.grid.lang.GridInClosure;
import org.gridgain.grid.streamer.GridStreamerContext;
import org.gridgain.grid.streamer.GridStreamerStage;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
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

            GridCache<String, TweetVO> tweetCache = grid.cache(TweetVO.class.getName());
            assert tweetCache != null;

            GridCache<String, HashTagVO> hashTagCache = grid.cache(HashTagVO.class.getName());
            assert hashTagCache != null;

            for (final TweetVO tweet : tweets) {


                final GridFuture<Boolean> putTweetFuture = tweetCache.putxAsync(tweet.getGUID(), tweet);

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

                        hashTagCache.putxAsync(hashTagVO.getGUID(), hashTagVO);

                    }
                }
            }
        }

        return null;
    }
}
