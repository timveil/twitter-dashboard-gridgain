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

        final GridStreamerWindow<HashTagEntity> last5Minutes = gridStreamerContext.window(StreamerWindow.FIVE_MIN.name());
        assert last5Minutes != null;

        final GridStreamerWindow<HashTagEntity> last15Minutes = gridStreamerContext.window(StreamerWindow.FIFTEEN_MIN.name());
        assert last15Minutes != null;

        final GridStreamerWindow<HashTagEntity> last60Minutes = gridStreamerContext.window(StreamerWindow.SIXTY_MIN.name());
        assert last60Minutes != null;

        for (HashTagEntity hashTag : hashTagEntities) {
            last5Minutes.enqueue(hashTag);
            last15Minutes.enqueue(hashTag);
            last60Minutes.enqueue(hashTag);
        }

        last5Minutes.clearEvicted();
        last15Minutes.clearEvicted();
        last60Minutes.clearEvicted();


        return Collections.<String, Collection<?>>singletonMap(gridStreamerContext.nextStageName(), hashTagEntities);

    }
}
