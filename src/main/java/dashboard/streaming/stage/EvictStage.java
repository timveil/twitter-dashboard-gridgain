package dashboard.streaming.stage;

import dashboard.utils.GridUtils;
import org.gridgain.grid.streamer.GridStreamerWindow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EvictStage {

    private static final Logger log = LoggerFactory.getLogger(EvictStage.class);

    void evict(GridStreamerWindow window) {

        try {
            final int evictionQueueSize = window.evictionQueueSize();
            final int windowSize = window.size();

            // todo: workaround for bug in timebound windows calculation of evictionQueueSize
            final int pollCount = evictionQueueSize == windowSize ? GridUtils.EVICTION_COUNT : evictionQueueSize;

            window.pollEvicted(pollCount);

            if (log.isTraceEnabled()) {
                log.trace("window name: " + window.name() + ", window size: " + windowSize + ", eviction size: " + evictionQueueSize + ", poll count: " + pollCount);
            }
        } catch (Exception e) {
            log.error("error clearing evicted HashTagVO from window " + window.name() + "...", e);
        }
    }
}
