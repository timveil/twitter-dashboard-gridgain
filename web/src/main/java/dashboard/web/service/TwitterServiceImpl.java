package dashboard.web.service;

import com.google.common.collect.Lists;
import dashboard.core.hpc.HashTagClosure;
import dashboard.core.hpc.HashTagReducer;
import dashboard.core.hpc.TweetClosure;
import dashboard.core.hpc.TweetReducer;
import dashboard.core.model.HashTag;
import dashboard.core.model.Tweet;
import dashboard.core.twitter.TweetStreamListener;
import dashboard.core.utils.GridConstants;
import dashboard.core.utils.GridUtils;
import dashboard.web.model.KeyValuePair;
import org.apache.commons.lang.StringUtils;
import org.gridgain.grid.Grid;
import org.gridgain.grid.GridException;
import org.gridgain.grid.cache.GridCache;
import org.gridgain.grid.cache.datastructures.GridCacheAtomicSequence;
import org.gridgain.grid.cache.datastructures.GridCacheDataStructures;
import org.gridgain.grid.cache.query.GridCacheQuery;
import org.gridgain.grid.streamer.GridStreamer;
import org.gridgain.grid.streamer.index.GridStreamerIndexEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.social.twitter.api.Stream;
import org.springframework.social.twitter.api.StreamListener;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
public class TwitterServiceImpl implements TwitterService {


    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private Twitter twitter;


    @Override
    @Async
    public void ingest(int duration, int multiplier) {

        Stream sampleStream = null;

        final Grid grid = GridUtils.getGrid();

        final GridStreamer tweetStreamer = grid.streamer(GridConstants.STREAMER_NAME);

        try {
            List<StreamListener> listeners = Lists.newArrayList();
            listeners.add(new TweetStreamListener(tweetStreamer, multiplier));

            sampleStream = twitter.streamingOperations().sample(listeners);

            Thread.sleep(duration);

        } catch (InterruptedException e) {
            log.error("stream thread interrupted...", e);
        } finally {
            log.debug("closing stream");

            if (tweetStreamer != null) {
                tweetStreamer.reset();
                tweetStreamer.resetMetrics();

            }

            if (sampleStream != null) {
                sampleStream.close();
            }
        }
    }

    @Override
    public List<KeyValuePair> getHashTagSummary(String windowName) {

        final Grid grid = GridUtils.getGrid();

        final GridStreamer streamer = grid.streamer(GridConstants.STREAMER_NAME);

        List<KeyValuePair> results = Lists.newArrayList();

        try {

            Collection<GridStreamerIndexEntry<HashTag, String, Long>> reduceResults = streamer.context().reduce(new HashTagClosure(windowName), new HashTagReducer());

            for (GridStreamerIndexEntry<HashTag, String, Long> entry : reduceResults) {
                results.add(new KeyValuePair(StringUtils.abbreviate(entry.key(), 20), NumberFormat.getNumberInstance().format(entry.value())));
            }

        } catch (GridException e) {
            log.error("grid exception occurred...", e);
        }

        return results;

    }


    @Override
    public List<KeyValuePair> getTopTweeters() {
        final Grid grid = GridUtils.getGrid();

        final GridStreamer streamer = grid.streamer(GridConstants.STREAMER_NAME);

        List<KeyValuePair> results = Lists.newArrayList();

        try {

            Collection<GridStreamerIndexEntry<Tweet, String, Long>> reduceResults = streamer.context().reduce(new TweetClosure(), new TweetReducer());

            for (GridStreamerIndexEntry<Tweet, String, Long> entry : reduceResults) {
                results.add(new KeyValuePair(StringUtils.abbreviate(entry.key(), 20), NumberFormat.getNumberInstance().format(entry.value())));
            }

        } catch (GridException e) {
            log.error("grid exception occurred...", e);
        }

        return results;

    }

    @Override
    public long getTotalTweets() {

        try {
            final Grid grid = GridUtils.getGrid();

            final GridCacheDataStructures dataStructures = grid.cache(GridConstants.ATOMIC_CACHE).dataStructures();
            final GridCacheAtomicSequence seq = dataStructures.atomicSequence(GridConstants.TOTAL_TWEETS, 0, true);

            assert seq != null;

            return seq.get();
        } catch (GridException e) {
            log.error("error getting total tweets", e);
        }

        return 0L;

    }

    @Override
    public long getTotalHashTags() {
        try {
            final Grid grid = GridUtils.getGrid();

            final GridCacheDataStructures dataStructures = grid.cache(GridConstants.ATOMIC_CACHE).dataStructures();
            final GridCacheAtomicSequence seq = dataStructures.atomicSequence(GridConstants.TOTAL_HASH_TAGS, 0, true);

            assert seq != null;

            return seq.get();
        } catch (GridException e) {
            log.error("error getting total hash tags", e);
        }

        return 0L;
    }

    @Override
    public List<Tweet> findTweets(String text, String screenName) {

        final Grid grid = GridUtils.getGrid();

        final GridCache<String, Tweet> cache = grid.cache(Tweet.class.getName());

        List<Tweet> tweets = Lists.newArrayList();

        try {

            List<String> parameters = Lists.newArrayList();
            StringBuilder s = new StringBuilder("fake = 'false'");

            if (StringUtils.isNotBlank(text)) {
                s.append(" and text like ?");
                parameters.add("%" + text + "%");
            }
            if (StringUtils.isNotBlank(screenName)) {
                s.append(" and screenName = ?");
                parameters.add(screenName);
            }

            s.append(" limit 100");

            final String sql = s.toString();

            if (log.isDebugEnabled()) {
                log.debug("findTweets sql [" + sql + "]");
            }

            GridCacheQuery<Map.Entry<String, Tweet>> query = cache.queries().createSqlQuery(Tweet.class, sql);

            final Collection<Map.Entry<String, Tweet>> searchResults = query.execute(parameters.toArray()).get();

            for (Map.Entry<String, Tweet> entry : searchResults) {
                tweets.add(entry.getValue());
            }

        } catch (GridException e) {
            log.error("error getting tweets with text [" + text + "]", e);
        }


        return tweets;
    }

}
