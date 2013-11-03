package dashboard.streaming.stage;

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


public class AddToTimeBoundedWindowStage implements GridStreamerStage<HashTagEntity> {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public String name() {
        return this.getClass().getSimpleName();
    }

    @Nullable
    @Override
    public Map<String, Collection<?>> run(GridStreamerContext gridStreamerContext, Collection<HashTagEntity> hashTagEntities) throws GridException {

        final GridStreamerWindow<HashTagEntity> last5Minutes = gridStreamerContext.window("last5Minutes");
        assert last5Minutes != null;

        final GridStreamerWindow<HashTagEntity> last15Minutes = gridStreamerContext.window("last15Minutes");
        assert last15Minutes != null;

        final GridStreamerWindow<HashTagEntity> last60Minutes = gridStreamerContext.window("last60Minutes");
        assert last60Minutes != null;

        for (HashTagEntity hashTag : hashTagEntities) {

            last5Minutes.enqueue(hashTag);
            last15Minutes.enqueue(hashTag);
            last60Minutes.enqueue(hashTag);

            log.debug("hash tag [" + hashTag.getText() + "]");
        }

        last5Minutes.pollEvictedAll();
        last15Minutes.pollEvictedAll();
        last60Minutes.pollEvictedAll();


        return Collections.<String, Collection<?>>singletonMap(gridStreamerContext.nextStageName(), hashTagEntities);

    }
}
