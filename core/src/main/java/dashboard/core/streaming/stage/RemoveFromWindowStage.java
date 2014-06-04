package dashboard.core.streaming.stage;

import dashboard.core.utils.GridConstants;
import org.gridgain.grid.streamer.GridStreamerWindow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

public class RemoveFromWindowStage<T> {

    private static final Logger log = LoggerFactory.getLogger(RemoveFromWindowStage.class);

    void remove(GridStreamerWindow<T> window) {

        try {

            // todo: workaround for bug in GridStreamerBoundedTimeWindow's calculation of evictionQueueSize
            Collection<T> evictedElements = window.pollEvicted(GridConstants.EVICTION_COUNT);

            if (evictedElements != null && !evictedElements.isEmpty()) {
                log.debug("evicted " + evictedElements.size() + " from window: " + window.name());
            }

            if (log.isTraceEnabled()) {
                final int evictionQueueSize = window.evictionQueueSize();
                final int windowSize = window.size();

                log.trace("window name: " + window.name() + ", window size: " + windowSize + ", eviction size: " + evictionQueueSize);
            }
        } catch (Exception e) {
            log.error("error clearing evicted event from window " + window.name() + "...", e);
        }
    }
}
