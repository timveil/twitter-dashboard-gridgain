package dashboard.core.streaming.stage;

import dashboard.core.utils.GridConstants;
import org.gridgain.grid.logger.GridLogger;
import org.gridgain.grid.resources.GridLoggerResource;
import org.gridgain.grid.streamer.GridStreamerStage;
import org.gridgain.grid.streamer.GridStreamerWindow;

import java.util.Collection;

public abstract class RemoveFromWindowStage<T> implements GridStreamerStage<T> {

    @GridLoggerResource
    private GridLogger logger;

    void remove(GridStreamerWindow<T> window) {

        try {

            // todo: workaround for bug in GridStreamerBoundedTimeWindow's calculation of evictionQueueSize
            Collection<T> evictedElements = window.pollEvicted(GridConstants.EVICTION_COUNT);

            if (evictedElements != null && !evictedElements.isEmpty()) {
                if (logger.isDebugEnabled()) {
                    logger.debug("evicted " + evictedElements.size() + " from window: " + window.name());
                }
            }


            if (logger.isTraceEnabled()) {
                final int evictionQueueSize = window.evictionQueueSize();
                final int windowSize = window.size();

                logger.trace("window name: " + window.name() + ", window size: " + windowSize + ", eviction size: " + evictionQueueSize);
            }
        } catch (Exception e) {
            logger.error("error clearing evicted event from window " + window.name() + "...", e);
        }
    }
}
