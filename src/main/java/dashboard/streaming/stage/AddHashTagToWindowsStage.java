package dashboard.streaming.stage;

import dashboard.model.HashTagVO;
import dashboard.model.TweetVO;
import dashboard.streaming.window.FifteenMinuteWindow;
import dashboard.streaming.window.FiveMinuteWindow;
import dashboard.streaming.window.SixtyMinuteWindow;
import org.gridgain.grid.GridException;
import org.gridgain.grid.streamer.GridStreamerContext;
import org.gridgain.grid.streamer.GridStreamerStage;
import org.gridgain.grid.streamer.GridStreamerWindow;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;


public class AddHashTagToWindowsStage implements GridStreamerStage<TweetVO> {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public String name() {
        return this.getClass().getSimpleName();
    }

    @Nullable
    @Override
    public Map<String, Collection<?>> run(GridStreamerContext gridStreamerContext, Collection<TweetVO> tweets) throws GridException {

        addToWindow(gridStreamerContext, tweets, FiveMinuteWindow.class);
        addToWindow(gridStreamerContext, tweets, FifteenMinuteWindow.class);
        addToWindow(gridStreamerContext, tweets, SixtyMinuteWindow.class);

        return Collections.<String, Collection<?>>singletonMap(gridStreamerContext.nextStageName(), tweets);

    }

    private void addToWindow(GridStreamerContext context, Collection<TweetVO> tweets, Class window) {
        final GridStreamerWindow<HashTagVO> streamerWindow = context.window(window.getName());
        assert streamerWindow != null;


        for (TweetVO tweet : tweets) {

            if (tweet.hasHashTags()) {
                final List<HashTagVO> hashTags = tweet.getHashTags();

                try {
                    boolean success = streamerWindow.enqueueAll(hashTags);

                    if (!success) {
                        log.warn("problem adding hashtags to queue");
                    }

                } catch (Exception e) {
                    log.error("error adding hashTags to window " + window + "...", e);
                }
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
