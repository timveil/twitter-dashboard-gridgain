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

        if (!tweets.isEmpty()) {

            final GridStreamerWindow<TweetVO> streamerWindow = gridStreamerContext.window(TopTweetersWindow.class.getName());
            assert streamerWindow != null;

            addToWindow(tweets, streamerWindow);
        }

        return Collections.<String, Collection<?>>singletonMap(gridStreamerContext.nextStageName(), tweets);

    }

    private void addToWindow(Collection<TweetVO> tweets, GridStreamerWindow<TweetVO> window) {

        try {
            boolean success = window.enqueueAll(tweets);

            if (!success) {
                log.warn("problem adding all TweetVO to queue");
            }

        } catch (Exception e) {
            log.error("error adding all TweetVO to window " + window.name() + "...", e);
        }
    }

    private void evict(GridStreamerWindow<TweetVO> window) {
        try {
            window.clearEvicted();
        } catch (Exception e) {
            log.error("error clearing evicted TweetVO from window " + window.name() + "...", e);
        }
    }
}
