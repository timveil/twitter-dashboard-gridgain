package dashboard.streaming.stage;

import dashboard.utils.GridUtils;
import org.gridgain.grid.GridException;
import org.gridgain.grid.streamer.GridStreamerContext;
import org.gridgain.grid.streamer.GridStreamerStage;
import org.jetbrains.annotations.Nullable;
import org.springframework.social.twitter.api.Tweet;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;


public class CountTweetsStage implements GridStreamerStage<Tweet> {

    @Override
    public String name() {
        return this.getClass().getSimpleName();
    }

    @Nullable
    @Override
    public Map<String, Collection<?>> run(GridStreamerContext gridStreamerContext, Collection<Tweet> tweets) throws GridException {

        Long currentTotal = (Long) gridStreamerContext.localSpace().get(GridUtils.TOTAL_TWEETS);

        if (currentTotal == null) {
            currentTotal = 0L;
        }

        gridStreamerContext.localSpace().put(GridUtils.TOTAL_TWEETS, currentTotal += tweets.size());

        Long noHashTags = (Long) gridStreamerContext.localSpace().get(GridUtils.TOTAL_TWEETS_NO_HASH_TAGS);

        if (noHashTags == null) {
            noHashTags = 0L;
        }

        for (Tweet tweet : tweets) {
            if (tweet.hasTags()) {
                noHashTags++;
            }
        }


        gridStreamerContext.localSpace().put(GridUtils.TOTAL_TWEETS_NO_HASH_TAGS, noHashTags);

        return Collections.<String, Collection<?>>singletonMap(gridStreamerContext.nextStageName(), tweets);
    }
}
