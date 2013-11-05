package dashboard.streaming.stage;

import dashboard.model.TweetVO;
import dashboard.utils.GridUtils;
import org.gridgain.grid.Grid;
import org.gridgain.grid.GridException;
import org.gridgain.grid.cache.GridCache;
import org.gridgain.grid.lang.GridPredicate;
import org.gridgain.grid.streamer.GridStreamerContext;
import org.gridgain.grid.streamer.GridStreamerStage;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.twitter.api.Tweet;

import java.util.Collection;
import java.util.Map;


public class AddTweetToDatabaseStage implements GridStreamerStage<Tweet> {

    private final Logger log = LoggerFactory.getLogger(getClass());


    @Override
    public String name() {
        return this.getClass().getSimpleName();
    }

    @Nullable
    @Override
    public Map<String, Collection<?>> run(GridStreamerContext gridStreamerContext, Collection<Tweet> tweets) throws GridException {

        Grid grid = GridUtils.getGrid();

        GridCache<Long, TweetVO> cache = grid.cache(TweetVO.class.getName());
        assert cache != null;

        for (Tweet tweet : tweets) {
            cache.putAsync(tweet.getId(), new TweetVO(tweet), (GridPredicate) null);
        }


        return null;
    }
}
