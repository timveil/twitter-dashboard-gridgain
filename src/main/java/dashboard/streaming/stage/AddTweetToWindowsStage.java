package dashboard.streaming.stage;

import dashboard.model.TweetVO;
import dashboard.streaming.window.TopTweetersWindow;
import org.gridgain.grid.GridException;
import org.gridgain.grid.streamer.GridStreamerContext;
import org.gridgain.grid.streamer.GridStreamerStage;
import org.gridgain.grid.streamer.GridStreamerWindow;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;


public class AddTweetToWindowsStage implements GridStreamerStage<TweetVO> {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public String name() {
        return this.getClass().getSimpleName();
    }

    @Nullable
    @Override
    public Map<String, Collection<?>> run(GridStreamerContext gridStreamerContext, Collection<TweetVO> tweets) throws GridException {

        addToWindow(gridStreamerContext, tweets, TopTweetersWindow.class);

        return Collections.<String, Collection<?>>singletonMap(gridStreamerContext.nextStageName(), tweets);

    }

    private void addToWindow(GridStreamerContext context, Collection<TweetVO> tweets, Class window) {
        final GridStreamerWindow<TweetVO> streamerWindow = context.window(window.getName());
        assert streamerWindow != null;

        try {
            boolean success = streamerWindow.enqueueAll(tweets);

            if (!success) {
                log.warn("problem adding tweets to queue");
            }

        } catch (Exception e) {
            log.error("error adding tweets to window " + window + "...", e);
        }


        final int evictionSize = streamerWindow.evictionQueueSize();

        if (evictionSize > 0) {

            if (log.isTraceEnabled()) {
                log.trace("eviction queue size in window " + window + " BEFORE EVICTION is " + evictionSize);
            }

            try {
                streamerWindow.clearEvicted();
            } catch (Exception e) {
                log.error("error clearing evicted tweet from window " + window + "...", e);
            }

            if (log.isTraceEnabled()) {
                log.trace("eviction queue size in window " + window + " AFTER EVICTION is " + streamerWindow.evictionQueueSize());
            }
        }

    }
}
