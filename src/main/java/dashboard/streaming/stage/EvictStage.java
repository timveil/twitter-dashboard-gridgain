package dashboard.streaming.stage;

import dashboard.utils.GridUtils;
import org.gridgain.grid.streamer.GridStreamerWindow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EvictStage {

    private static final Logger log = LoggerFactory.getLogger(EvictStage.class);

    void evict(GridStreamerWindow window) {

        try {

            // todo: workaround for bug in GridStreamerBoundedTimeWindow's calculation of evictionQueueSize
            window.pollEvicted(GridUtils.EVICTION_COUNT);

            if (log.isTraceEnabled()) {
                final int evictionQueueSize = window.evictionQueueSize();
                final int windowSize = window.size();

                log.trace("window name: " + window.name() + ", window size: " + windowSize + ", eviction size: " + evictionQueueSize);
            }
        } catch (Exception e) {
            log.error("error clearing evicted HashTagVO from window " + window.name() + "...", e);
        }
    }
}
