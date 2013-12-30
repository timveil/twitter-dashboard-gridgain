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

        if (!tweets.isEmpty()) {

            final GridStreamerWindow<HashTagVO> fiveMinuteWindow = gridStreamerContext.window(FiveMinuteWindow.class.getName());
            assert fiveMinuteWindow != null;

            final GridStreamerWindow<HashTagVO> fifteenMinuteWindow = gridStreamerContext.window(FifteenMinuteWindow.class.getName());
            assert fifteenMinuteWindow != null;

            final GridStreamerWindow<HashTagVO> sixtyMinuteWindow = gridStreamerContext.window(SixtyMinuteWindow.class.getName());
            assert sixtyMinuteWindow != null;


            for (TweetVO tweet : tweets) {
                if (tweet.hasHashTags()) {
                    final List<HashTagVO> hashTags = tweet.getHashTags();

                    enqueue(hashTags, fiveMinuteWindow);
                    enqueue(hashTags, fifteenMinuteWindow);
                    enqueue(hashTags, sixtyMinuteWindow);
                }

            }


            evict(fiveMinuteWindow);
            evict(fifteenMinuteWindow);
            evict(sixtyMinuteWindow);

        }


        return Collections.<String, Collection<?>>singletonMap(gridStreamerContext.nextStageName(), tweets);

    }

    private void enqueue(List<HashTagVO> hashTags, GridStreamerWindow<HashTagVO> window) {
        try {
            boolean success = window.enqueueAll(hashTags);

            if (!success) {
                log.warn("problem adding all HashTagVO to window");
            }

        } catch (Exception e) {
            log.error("error adding all HashTagVO to window " + window.name() + "...", e);
        }
    }


    private void evict(GridStreamerWindow<HashTagVO> window) {

        try {
            window.clearEvicted();
        } catch (Exception e) {
            log.error("error clearing evicted HashTagVO from window " + window.name() + "...", e);
        }
    }
}
