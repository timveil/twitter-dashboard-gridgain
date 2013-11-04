package dashboard.streaming;

import com.google.common.collect.Lists;
import dashboard.model.HashTagSummary;
import dashboard.streaming.listener.HashTagStreamListener;
import dashboard.streaming.listener.TweetStreamListener;
import org.gridgain.grid.Grid;
import org.gridgain.grid.GridException;
import org.gridgain.grid.GridFactory;
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

import java.util.*;

@Service
public class TwitterServiceImpl implements TwitterService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private Twitter twitter;


    @Override
    @Async
    public void ingest(int duration) {

        Stream sampleStream = null;

        Grid grid = GridFactory.grid("twitter-grid");

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
    public List<HashTagSummary> getHashTagSummary(final StreamerWindow window) {

        Grid grid = GridFactory.grid("twitter-grid");

        final GridStreamer streamer = grid.streamer(Streamer.HASHTAGS.name());

        assert streamer != null;

        List<HashTagSummary> results = Lists.newArrayList();

        try {
            Collection<GridStreamerIndexEntry<HashTagEntity, String, Long>> reduceResults = streamer.context().reduce(
                    new GridClosure<GridStreamerContext, Collection<GridStreamerIndexEntry<HashTagEntity, String, Long>>>() {

                        @Override
                        public Collection<GridStreamerIndexEntry<HashTagEntity, String, Long>> apply(GridStreamerContext gridStreamerContext) {

                            final GridStreamerWindow<HashTagEntity> gridStreamerWindow = gridStreamerContext.window(window.name());

                            assert gridStreamerWindow != null;

                            final GridStreamerIndex<HashTagEntity, String, Long> index = gridStreamerWindow.index(StreamerIndex.HASHTAG_COUNT.name());

                            return index.entries(0);

                        }
                    },
                    new GridReducer0<Collection<GridStreamerIndexEntry<HashTagEntity, String, Long>>>() {
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

                            return GridFunc.retain(sorted, true, 10);
                        }
                    }
            );

            for (GridStreamerIndexEntry<HashTagEntity, String, Long> entry : reduceResults) {
                results.add(new HashTagSummary(entry.key(), entry.value()));
            }

        } catch (GridException e) {
            log.error("grid exception occurred...", e);
        }

        return results;

    }
}
