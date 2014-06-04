package dashboard.core.streaming.stage;

import dashboard.core.model.Tweet;
import dashboard.core.utils.GridConstants;
import org.gridgain.grid.GridException;
import org.gridgain.grid.streamer.GridStreamerContext;
import org.gridgain.grid.streamer.GridStreamerStage;
import org.gridgain.grid.streamer.GridStreamerWindow;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;


public class AddTweetToWindowsStage extends AddToWindowStage<Tweet> implements GridStreamerStage<Tweet> {

    @Override
    public String name() {
        return this.getClass().getSimpleName();
    }

    @Nullable
    @Override
    public Map<String, Collection<?>> run(GridStreamerContext gridStreamerContext, Collection<Tweet> tweets) throws GridException {

        if (!tweets.isEmpty()) {

            final GridStreamerWindow<Tweet> streamerWindow = gridStreamerContext.window(GridConstants.TOP_TWEETERS_WINDOW);

            add(streamerWindow, tweets);

        }

        return Collections.<String, Collection<?>>singletonMap(AddTweetToDatabaseStage.class.getSimpleName(), tweets);

    }


}
