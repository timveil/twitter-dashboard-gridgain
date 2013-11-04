package dashboard.service;

import com.google.common.collect.Lists;
import dashboard.model.KeyValuePair;
import dashboard.model.TweetVO;
import dashboard.streaming.listener.HashTagStreamListener;
import dashboard.streaming.listener.TweetStreamListener;
import dashboard.utils.GridUtils;
import dashboard.utils.Streamer;
import dashboard.utils.StreamerIndex;
import dashboard.utils.StreamerWindow;
import org.apache.commons.lang.StringUtils;
import org.gridgain.grid.Grid;
import org.gridgain.grid.GridException;
import org.gridgain.grid.cache.GridCache;
import org.gridgain.grid.lang.GridClosure;
import org.gridgain.grid.lang.GridFunc;
import org.gridgain.grid.lang.GridReducer0;
import org.gridgain.grid.streamer.GridStreamer;
import org.gridgain.grid.streamer.GridStreamerContext;
import org.gridgain.grid.streamer.GridStreamerWindow;
import org.gridgain.grid.streamer.index.GridStreamerIndex;
import org.gridgain.grid.streamer.index.GridStreamerIndexEntry;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.social.twitter.api.HashTagEntity;
import org.springframework.social.twitter.api.Stream;
import org.springframework.social.twitter.api.StreamListener;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.util.*;

@Service
public class TwitterServiceImpl implements TwitterService {

    public static final int MAX_NUM_RETURNED = 5;

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private Twitter twitter;


    @Override
    @Async
    public void ingest(int duration) {

        Stream sampleStream = null;

        Grid grid = GridUtils.getGrid();

        final GridStreamer hashTagStreamer = grid.streamer(Streamer.HASHTAGS.name());
        final GridStreamer tweetStreamer = grid.streamer(Streamer.TWEETS.name());

        try {
            List<StreamListener> listeners = Lists.newArrayList();
            listeners.add(new HashTagStreamListener(hashTagStreamer));
            listeners.add(new TweetStreamListener(tweetStreamer));

            sampleStream = twitter.streamingOperations().sample(listeners);

            Thread.sleep(duration);

        } catch (InterruptedException e) {
            log.error("stream thread interrupted...", e);
        } finally {
            log.debug("closing stream");

            if (tweetStreamer != null) {
                tweetStreamer.reset();
            }

            if (hashTagStreamer != null) {
                hashTagStreamer.reset();
            }

            if (sampleStream != null) {
                sampleStream.close();
            }
        }
    }

    @Override
    public List<KeyValuePair> getHashTagSummary(final StreamerWindow window) {

        Grid grid = GridUtils.getGrid();

        final GridStreamer streamer = grid.streamer(Streamer.HASHTAGS.name());

        assert streamer != null;

        List<KeyValuePair> results = Lists.newArrayList();


        final GridClosure<GridStreamerContext, Collection<GridStreamerIndexEntry<HashTagEntity, String, Long>>> gridClosure = new GridClosure<GridStreamerContext, Collection<GridStreamerIndexEntry<HashTagEntity, String, Long>>>() {

            @Override
            public Collection<GridStreamerIndexEntry<HashTagEntity, String, Long>> apply(GridStreamerContext gridStreamerContext) {

                final GridStreamerWindow<HashTagEntity> gridStreamerWindow = gridStreamerContext.window(window.name());

                assert gridStreamerWindow != null;

                final GridStreamerIndex<HashTagEntity, String, Long> index = gridStreamerWindow.index(StreamerIndex.HASHTAG_COUNT.name());

                return index.entries(0);

            }
        };

        final GridReducer0<Collection<GridStreamerIndexEntry<HashTagEntity, String, Long>>> gridReducer = new GridReducer0<Collection<GridStreamerIndexEntry<HashTagEntity, String, Long>>>() {
            private List<GridStreamerIndexEntry<HashTagEntity, String, Long>> sorted = new ArrayList<>();


            @Override
            public boolean collect(@Nullable Collection<GridStreamerIndexEntry<HashTagEntity, String, Long>> gridStreamerIndexEntries) {
                if (gridStreamerIndexEntries != null && !gridStreamerIndexEntries.isEmpty()) {
                    sorted.addAll(gridStreamerIndexEntries);
                }

                return true;
            }

            @Override
            public Collection<GridStreamerIndexEntry<HashTagEntity, String, Long>> apply() {
                Collections.sort(sorted, new Comparator<GridStreamerIndexEntry<HashTagEntity, String, Long>>() {

                    @Override
                    public int compare(GridStreamerIndexEntry<HashTagEntity, String, Long> o1, GridStreamerIndexEntry<HashTagEntity, String, Long> o2) {
                        return o2.value().compareTo(o1.value());
                    }
                });

                return GridFunc.retain(sorted, true, MAX_NUM_RETURNED);
            }
        };

        try {

            Collection<GridStreamerIndexEntry<HashTagEntity, String, Long>> reduceResults = streamer.context().reduce(gridClosure, gridReducer);

            for (GridStreamerIndexEntry<HashTagEntity, String, Long> entry : reduceResults) {
                results.add(new KeyValuePair(StringUtils.abbreviate(entry.key(), 50), NumberFormat.getNumberInstance().format(entry.value())));
            }

        } catch (GridException e) {
            log.error("grid exception occurred...", e);
        }

        return results;

    }


    @Override
    public List<KeyValuePair> getTopTweeters() {

        Grid grid = GridUtils.getGrid();

        GridCache<Long, TweetVO> cache = grid.cache(TweetVO.class.getName());
        assert cache != null;

        List<KeyValuePair> topTweeters = Lists.newArrayList();

        try {
            final Collection<List<Object>> results = cache.createFieldsQuery("select screenName, count(*) from TweetVO group by screenName having count(*) > 0 order by count(*) desc limit ?")
                    .queryArguments(MAX_NUM_RETURNED)
                    .execute(grid)
                    .get();

            for (List<Object> result : results) {
                final String screenName = (String) result.get(0);
                final Long count = (Long) result.get(1);
                topTweeters.add(new KeyValuePair(StringUtils.abbreviate(screenName, 50), NumberFormat.getNumberInstance().format(count)));
            }

        } catch (GridException e) {
            log.error("error executing top tweeters query", e);
        }

        return topTweeters;


    }

    @Override
    public long getTotalTweets() {

        Grid grid = GridUtils.getGrid();

        GridCache<Long, TweetVO> cache = grid.cache(TweetVO.class.getName());
        assert cache != null;

        try {
           Long queryResult = (Long)cache.createFieldsQuery("select count(id) from TweetVO")
                    .executeSingleField(grid)
                    .get();

            if (queryResult != null) {
                return queryResult;
            }
        } catch (GridException e) {
           log.error("error getting total tweets", e);
        }

        return 0L;
    }

    @Override
    public long getTotalTweetsWithHashTag() {
        Grid grid = GridUtils.getGrid();

        GridCache<Long, TweetVO> cache = grid.cache(TweetVO.class.getName());
        assert cache != null;

        try {
            Long queryResult = (Long)cache.createFieldsQuery("select count(id) from TweetVO where hashTagCount > 0")
                    .executeSingleField(grid)
                    .get();

            if (queryResult != null) {
                return queryResult;
            }

        } catch (GridException e) {
            log.error("error getting tweets with hashtags", e);
        }

        return 0L;
    }

}
