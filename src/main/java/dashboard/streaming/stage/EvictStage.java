package dashboard.streaming.stage;

import org.gridgain.grid.streamer.GridStreamerWindow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EvictStage {

    private static final Logger log = LoggerFactory.getLogger(EvictStage.class);

    void evict(GridStreamerWindow window) {

        try {
            final int evictionQueueSize = window.evictionQueueSize();
            final int windowSize = window.size();

            window.pollEvicted(evictionQueueSize);


            log.debug("window name: " + window.name() + ", window size: " + windowSize + ", eviction size: " + evictionQueueSize);
        } catch (Exception e) {
            log.error("error clearing evicted HashTagVO from window " + window.name() + "...", e);
        }
    }
}
