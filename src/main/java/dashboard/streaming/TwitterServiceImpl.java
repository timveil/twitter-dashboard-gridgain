package dashboard.streaming;

import com.google.common.collect.Lists;
import dashboard.model.HashtagAggregate;
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

import java.util.*;

@Service
public class TwitterServiceImpl implements TwitterService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private Twitter twitter;

    @Autowired
    private Grid grid;


    @Override
    @Async
    public void ingest(int duration) {

        log.debug("duration of sample will be " + duration + " milliseconds.");

        Stream userStream = null;

        final GridStreamer streamer = grid.streamer("twitter-popular-hashtags");

        assert streamer != null;

        try {
            List<StreamListener> listeners = Lists.newArrayList();
            listeners.add(new HashtagStreamListener(streamer));

            userStream = twitter.streamingOperations().sample(listeners);

            if (duration != 0) {
                Thread.sleep(duration);
            }

        } catch (InterruptedException e) {
            log.error("stream thread interrupted...", e);
        } finally {
            log.debug("closing stream");

            streamer.reset();


            if (userStream != null) {
                userStream.close();
            }
        }
    }

    @Override
    public List<HashtagAggregate> getHashtagAgregate() {

        final GridStreamer streamer = grid.streamer("twitter-popular-hashtags");

        assert streamer != null;

        List<HashtagAggregate> results = Lists.newArrayList();

        try {
            // Send reduce query to all 'popular-numbers' streamers
            // running on local and remote noes.
            Collection<GridStreamerIndexEntry<HashTagEntity, String, Long>> col = streamer.context().reduce(
                    // This closure will execute on remote nodes.
                    new GridClosure<GridStreamerContext, Collection<GridStreamerIndexEntry<HashTagEntity, String, Long>>>() {

                        @Override
                        public Collection<GridStreamerIndexEntry<HashTagEntity, String, Long>> apply(GridStreamerContext gridStreamerContext) {

                            final GridStreamerWindow<HashTagEntity> last5MinutesWindow = gridStreamerContext.window("last5Minutes");

                            assert last5MinutesWindow != null;

                            final GridStreamerIndex<HashTagEntity, String, Long> last5MinutesIndex = last5MinutesWindow.index("hashTagCount");

                            return last5MinutesIndex.entries(0);

                        }
                    },
                    // The reducer will always execute locally, on the same node
                    // that submitted the query.
                    new GridReducer0<Collection<GridStreamerIndexEntry<HashTagEntity, String, Long>>>() {
                        private List<GridStreamerIndexEntry<HashTagEntity, String, Long>> sorted = new ArrayList<>();


                        @Override
                        public boolean collect(@Nullable Collection<GridStreamerIndexEntry<HashTagEntity, String, Long>> gridStreamerIndexEntries) {
                            if (gridStreamerIndexEntries != null && !gridStreamerIndexEntries.isEmpty()) {
                                // Add result from remote node to sorted set.
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

                            // Take only first POPULAR_NUMBERS_CNT values from final sorted set.
                            return GridFunc.retain(sorted, true, 10);
                        }
                    }
            );

            for (GridStreamerIndexEntry<HashTagEntity, String, Long> cntr : col) {
                results.add(new HashtagAggregate(cntr.key(), cntr.value()));
            }

        } catch (GridException e) {
            e.printStackTrace();
        }

        return results;

    }
}
