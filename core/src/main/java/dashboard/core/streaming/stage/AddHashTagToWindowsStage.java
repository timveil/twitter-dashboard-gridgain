package dashboard.core.streaming.stage;

import dashboard.core.model.HashTagVO;
import dashboard.core.model.TweetVO;
import dashboard.core.utils.GridUtils;
import org.gridgain.grid.GridException;
import org.gridgain.grid.streamer.GridStreamerContext;
import org.gridgain.grid.streamer.GridStreamerStage;
import org.gridgain.grid.streamer.GridStreamerWindow;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;


public class AddHashTagToWindowsStage extends AddToWindowStage<HashTagVO> implements GridStreamerStage<TweetVO> {

    @Override
    public String name() {
        return this.getClass().getSimpleName();
    }

    @Nullable
    @Override
    public Map<String, Collection<?>> run(GridStreamerContext gridStreamerContext, Collection<TweetVO> tweets) throws GridException {

        if (!tweets.isEmpty()) {

            final GridStreamerWindow<HashTagVO> oneMinute = gridStreamerContext.window(GridUtils.ONE_MINUTE_WINDOW);

            final GridStreamerWindow<HashTagVO> fiveMinute = gridStreamerContext.window(GridUtils.FIVE_MINUTE_WINDOW);

            final GridStreamerWindow<HashTagVO> tenMinute = gridStreamerContext.window(GridUtils.TEN_MINUTE_WINDOW);

            for (TweetVO tweet : tweets) {
                if (tweet.hasHashTags()) {
                    final List<HashTagVO> hashTags = tweet.getHashTags();

                    add(oneMinute, hashTags);
                    add(fiveMinute, hashTags);
                    add(tenMinute, hashTags);
                }

            }
        }

        return Collections.<String, Collection<?>>singletonMap(gridStreamerContext.nextStageName(), tweets);

    }


}
