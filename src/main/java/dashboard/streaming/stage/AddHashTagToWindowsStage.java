package dashboard.streaming.stage;

import dashboard.utils.StreamerWindow;
import org.gridgain.grid.GridException;
import org.gridgain.grid.streamer.GridStreamerContext;
import org.gridgain.grid.streamer.GridStreamerStage;
import org.gridgain.grid.streamer.GridStreamerWindow;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.twitter.api.HashTagEntity;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;


public class AddHashTagToWindowsStage implements GridStreamerStage<HashTagEntity> {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public String name() {
        return this.getClass().getSimpleName();
    }

    @Nullable
    @Override
    public Map<String, Collection<?>> run(GridStreamerContext gridStreamerContext, Collection<HashTagEntity> hashTagEntities) throws GridException {

        addToWindow(gridStreamerContext, hashTagEntities, StreamerWindow.FIVE_MIN);
        addToWindow(gridStreamerContext, hashTagEntities, StreamerWindow.FIFTEEN_MIN);
        addToWindow(gridStreamerContext, hashTagEntities, StreamerWindow.SIXTY_MIN);

        return Collections.<String, Collection<?>>singletonMap(gridStreamerContext.nextStageName(), hashTagEntities);

    }

    private void addToWindow(GridStreamerContext context, Collection<HashTagEntity> hashTagEntities, StreamerWindow window) {
        final GridStreamerWindow<HashTagEntity> streamerWindow = context.window(window.name());
        assert streamerWindow != null;


        for (HashTagEntity hashTag : hashTagEntities) {
            try {
                streamerWindow.enqueue(hashTag);
            } catch (Exception e) {
                log.error("error adding hashTag [" + hashTag.getText() + "] to window " + window + "...", e);
            }
        }


        final int evictionSize = streamerWindow.evictionQueueSize();

        if (evictionSize > 0) {

            if (log.isTraceEnabled()) {
                log.trace("eviction queue size in window " + window + " is " + evictionSize);
            }

            try {
                streamerWindow.clearEvicted();
            } catch (Exception e) {
                log.error("error clearing evicted hashTags from window " + window + "...", e);
            }
        }

    }
}
