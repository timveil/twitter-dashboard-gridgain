package dashboard.streaming.stage;

import dashboard.streaming.window.TopTweetersWindow;
import org.gridgain.grid.GridException;
import org.gridgain.grid.streamer.GridStreamerContext;
import org.gridgain.grid.streamer.GridStreamerStage;
import org.gridgain.grid.streamer.GridStreamerWindow;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.twitter.api.Tweet;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;


public class AddTweetToWindowsStage implements GridStreamerStage<Tweet> {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public String name() {
        return this.getClass().getSimpleName();
    }

    @Nullable
    @Override
    public Map<String, Collection<?>> run(GridStreamerContext gridStreamerContext, Collection<Tweet> tweets) throws GridException {

        addToWindow(gridStreamerContext, tweets, TopTweetersWindow.class);

        return Collections.<String, Collection<?>>singletonMap(gridStreamerContext.nextStageName(), tweets);

    }

    private void addToWindow(GridStreamerContext context, Collection<Tweet> tweets, Class window) {
        final GridStreamerWindow<Tweet> streamerWindow = context.window(window.getName());
        assert streamerWindow != null;


        for (Tweet tweet : tweets) {

            if (tweet.hasTags()) {
                try {
                    streamerWindow.enqueue(tweet);
                } catch (Exception e) {
                    log.error("error adding tweet [" + tweet.getId() + "] to window " + window + "...", e);
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
                log.error("error clearing evicted tweet from window " + window + "...", e);
            }
        }

    }
}
