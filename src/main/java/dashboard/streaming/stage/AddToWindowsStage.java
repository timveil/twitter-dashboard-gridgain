package dashboard.streaming.stage;

import dashboard.streaming.StreamerWindow;
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


public class AddToWindowsStage implements GridStreamerStage<HashTagEntity> {

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
            } catch (GridException e) {
                log.error("error adding hashTag to window...", e);
            }
        }

        try {
            streamerWindow.clearEvicted();
        } catch (GridException e) {
            log.error("error clearing evicted hashTag from window...", e);
        }

    }
}
