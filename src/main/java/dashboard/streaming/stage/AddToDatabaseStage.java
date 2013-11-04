package dashboard.streaming.stage;

import dashboard.model.TweetVO;
import org.gridgain.grid.Grid;
import org.gridgain.grid.GridException;
import org.gridgain.grid.GridFactory;
import org.gridgain.grid.cache.GridCache;
import org.gridgain.grid.streamer.GridStreamerContext;
import org.gridgain.grid.streamer.GridStreamerStage;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.twitter.api.Tweet;

import java.util.Collection;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: timveil
 * Date: 11/3/13
 * Time: 7:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class AddToDatabaseStage implements GridStreamerStage<Tweet> {

    private final Logger log = LoggerFactory.getLogger(getClass());


    @Override
    public String name() {
        return this.getClass().getSimpleName();
    }

    @Nullable
    @Override
    public Map<String, Collection<?>> run(GridStreamerContext gridStreamerContext, Collection<Tweet> tweets) throws GridException {

        Grid grid = GridFactory.grid("twitter-grid");

        GridCache<Long, TweetVO> cache = grid.cache(TweetVO.class.getName());

        for (Tweet tweet : tweets) {
            cache.put(tweet.getId(), new TweetVO(tweet));
        }


        return null;
    }
}
