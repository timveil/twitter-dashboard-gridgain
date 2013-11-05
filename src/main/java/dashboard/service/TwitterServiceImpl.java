package dashboard.service;

import com.google.common.collect.Lists;
import dashboard.model.KeyValuePair;
import dashboard.streaming.index.provider.HashTagIndexProvider;
import dashboard.streaming.index.provider.TopTweeterIndexProvider;
import dashboard.streaming.listener.TweetStreamListener;
import dashboard.streaming.window.TopTweetersWindow;
import dashboard.utils.GridUtils;
import org.apache.commons.lang.StringUtils;
import org.gridgain.grid.Grid;
import org.gridgain.grid.GridException;
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
import org.springframework.social.twitter.api.*;
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

        final GridStreamer tweetStreamer = grid.streamer(GridUtils.STREAMER_NAME);

        try {
            List<StreamListener> listeners = Lists.newArrayList();
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

            if (sampleStream != null) {
                sampleStream.close();
            }
        }
    }

    @Override
    public List<KeyValuePair> getHashTagSummary(final Class window) {

        Grid grid = GridUtils.getGrid();

        final GridStreamer streamer = grid.streamer(GridUtils.STREAMER_NAME);

        assert streamer != null;

        List<KeyValuePair> results = Lists.newArrayList();


        final GridClosure<GridStreamerContext, Collection<GridStreamerIndexEntry<HashTagEntity, String, Long>>> gridClosure = new GridClosure<GridStreamerContext, Collection<GridStreamerIndexEntry<HashTagEntity, String, Long>>>() {

            @Override
            public Collection<GridStreamerIndexEntry<HashTagEntity, String, Long>> apply(GridStreamerContext gridStreamerContext) {

                final GridStreamerWindow<HashTagEntity> gridStreamerWindow = gridStreamerContext.window(window.getName());

                assert gridStreamerWindow != null;

                final GridStreamerIndex<HashTagEntity, String, Long> index = gridStreamerWindow.index(HashTagIndexProvider.class.getName());

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

        final GridStreamer streamer = grid.streamer(GridUtils.STREAMER_NAME);

        assert streamer != null;

        List<KeyValuePair> results = Lists.newArrayList();


        final GridClosure<GridStreamerContext, Collection<GridStreamerIndexEntry<Tweet, String, Long>>> gridClosure = new GridClosure<GridStreamerContext, Collection<GridStreamerIndexEntry<Tweet, String, Long>>>() {

            @Override
            public Collection<GridStreamerIndexEntry<Tweet, String, Long>> apply(GridStreamerContext gridStreamerContext) {

                final GridStreamerWindow<Tweet> gridStreamerWindow = gridStreamerContext.window(TopTweetersWindow.class.getName());

                assert gridStreamerWindow != null;

                final GridStreamerIndex<Tweet, String, Long> index = gridStreamerWindow.index(TopTweeterIndexProvider.class.getName());

                return index.entries(0);

            }
        };

        final GridReducer0<Collection<GridStreamerIndexEntry<Tweet, String, Long>>> gridReducer = new GridReducer0<Collection<GridStreamerIndexEntry<Tweet, String, Long>>>() {
            private List<GridStreamerIndexEntry<Tweet, String, Long>> sorted = new ArrayList<>();


            @Override
            public boolean collect(@Nullable Collection<GridStreamerIndexEntry<Tweet, String, Long>> gridStreamerIndexEntries) {
                if (gridStreamerIndexEntries != null && !gridStreamerIndexEntries.isEmpty()) {
                    sorted.addAll(gridStreamerIndexEntries);
                }

                return true;
            }

            @Override
            public Collection<GridStreamerIndexEntry<Tweet, String, Long>> apply() {
                Collections.sort(sorted, new Comparator<GridStreamerIndexEntry<Tweet, String, Long>>() {

                    @Override
                    public int compare(GridStreamerIndexEntry<Tweet, String, Long> o1, GridStreamerIndexEntry<Tweet, String, Long> o2) {
                        return o2.value().compareTo(o1.value());
                    }
                });

                return GridFunc.retain(sorted, true, MAX_NUM_RETURNED);
            }
        };

        try {

            Collection<GridStreamerIndexEntry<Tweet, String, Long>> reduceResults = streamer.context().reduce(gridClosure, gridReducer);

            for (GridStreamerIndexEntry<Tweet, String, Long> entry : reduceResults) {
                results.add(new KeyValuePair(StringUtils.abbreviate(entry.key(), 50), NumberFormat.getNumberInstance().format(entry.value())));
            }

        } catch (GridException e) {
            log.error("grid exception occurred...", e);
        }

        return results;

    }

    @Override
    public long getTotalTweets() {

        Grid grid = GridUtils.getGrid();

        final GridStreamer streamer = grid.streamer(GridUtils.STREAMER_NAME);

        assert streamer != null;

        return (Long) streamer.context().localSpace().get(GridUtils.TOTAL_TWEETS);

    }

    @Override
    public long getTotalTweetsWithHashTag() {
        Grid grid = GridUtils.getGrid();

        final GridStreamer streamer = grid.streamer(GridUtils.STREAMER_NAME);

        assert streamer != null;

        return (Long) streamer.context().localSpace().get(GridUtils.TOTAL_TWEETS_NO_HASH_TAGS);
    }

}
