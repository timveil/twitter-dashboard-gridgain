package dashboard.core.streaming.stage;

import org.gridgain.grid.logger.GridLogger;
import org.gridgain.grid.resources.GridLoggerResource;
import org.gridgain.grid.streamer.GridStreamerStage;
import org.gridgain.grid.streamer.GridStreamerWindow;

import java.util.Collection;

public abstract class AddToWindowStage<T> implements GridStreamerStage<T> {

    @GridLoggerResource
    private GridLogger logger;

    void add(GridStreamerWindow<T> window, Collection<T> events) {
        try {
            boolean success = window.enqueueAll(events);

            if (!success) {
                logger.warning("problem adding events to window");
            }

        } catch (Exception e) {
            logger.error("error adding all events to window " + window.name() + "...", e);
        }
    }
}
