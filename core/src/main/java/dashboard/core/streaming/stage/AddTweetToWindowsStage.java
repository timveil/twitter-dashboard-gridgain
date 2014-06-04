package dashboard.core.streaming.stage;

import dashboard.core.model.TweetVO;
import dashboard.core.utils.GridUtils;
import org.gridgain.grid.GridException;
import org.gridgain.grid.streamer.GridStreamerContext;
import org.gridgain.grid.streamer.GridStreamerStage;
import org.gridgain.grid.streamer.GridStreamerWindow;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;


public class AddTweetToWindowsStage extends AddToWindowStage<TweetVO> implements GridStreamerStage<TweetVO> {

    @Override
    public String name() {
        return this.getClass().getSimpleName();
    }

    @Nullable
    @Override
    public Map<String, Collection<?>> run(GridStreamerContext gridStreamerContext, Collection<TweetVO> tweets) throws GridException {

        if (!tweets.isEmpty()) {

            final GridStreamerWindow<TweetVO> streamerWindow = gridStreamerContext.window(GridUtils.TOP_TWEETERS_WINDOW);

            add(streamerWindow, tweets);

        }

        return Collections.<String, Collection<?>>singletonMap(gridStreamerContext.nextStageName(), tweets);

    }


}
