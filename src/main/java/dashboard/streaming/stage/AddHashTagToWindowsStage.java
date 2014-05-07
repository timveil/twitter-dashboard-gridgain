package dashboard.streaming.stage;

import dashboard.model.HashTagVO;
import dashboard.model.TweetVO;
import dashboard.streaming.window.FiveMinuteWindow;
import dashboard.streaming.window.OneMinuteWindow;
import dashboard.streaming.window.TenMinuteWindow;
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


public class AddHashTagToWindowsStage extends EnqueueStage<HashTagVO> implements GridStreamerStage<TweetVO> {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public String name() {
        return this.getClass().getSimpleName();
    }

    @Nullable
    @Override
    public Map<String, Collection<?>> run(GridStreamerContext gridStreamerContext, Collection<TweetVO> tweets) throws GridException {

        if (!tweets.isEmpty()) {

            final GridStreamerWindow<HashTagVO> oneMinute = gridStreamerContext.window(OneMinuteWindow.class.getName());

            final GridStreamerWindow<HashTagVO> fiveMinute = gridStreamerContext.window(FiveMinuteWindow.class.getName());

            final GridStreamerWindow<HashTagVO> tenMinute = gridStreamerContext.window(TenMinuteWindow.class.getName());

            for (TweetVO tweet : tweets) {
                if (tweet.hasHashTags()) {
                    final List<HashTagVO> hashTags = tweet.getHashTags();

                    enqueue(oneMinute, hashTags);
                    enqueue(fiveMinute, hashTags);
                    enqueue(tenMinute, hashTags);
                }

            }
        }

        return Collections.<String, Collection<?>>singletonMap(gridStreamerContext.nextStageName(), tweets);

    }


}
