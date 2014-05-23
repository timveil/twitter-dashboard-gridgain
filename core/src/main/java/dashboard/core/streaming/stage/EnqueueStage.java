package dashboard.core.streaming.stage;

import org.gridgain.grid.streamer.GridStreamerWindow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

public class EnqueueStage<T> {

    private static final Logger log = LoggerFactory.getLogger(EnqueueStage.class);


    void enqueue(GridStreamerWindow<T> window, Collection<T> events) {
        try {
            boolean success = window.enqueueAll(events);

            if (!success) {
                log.warn("problem adding events to window");
            }

        } catch (Exception e) {
            log.error("error adding all events to window " + window.name() + "...", e);
        }
    }
}
