package dashboard.streaming.index.updater;

import dashboard.model.TweetVO;
import org.apache.commons.lang.StringUtils;
import org.gridgain.grid.GridException;
import org.gridgain.grid.streamer.index.GridStreamerIndexEntry;
import org.gridgain.grid.streamer.index.GridStreamerIndexUpdater;
import org.jetbrains.annotations.Nullable;


public class TopTweeterCountUpdater implements GridStreamerIndexUpdater<TweetVO, String, Long> {
    @Nullable
    @Override
    public String indexKey(TweetVO tweet) {

        if (StringUtils.isNotBlank(tweet.getScreenName())) {
            return tweet.getScreenName();
        }

        return null;
    }

    @Nullable
    @Override
    public Long initialValue(TweetVO tweet, String s) {
        return 1L;
    }

    @Nullable
    @Override
    public Long onAdded(GridStreamerIndexEntry<TweetVO, String, Long> entry, TweetVO tweet) throws GridException {
        return entry.value() + 1;
    }

    @Nullable
    @Override
    public Long onRemoved(GridStreamerIndexEntry<TweetVO, String, Long> entry, TweetVO tweet) {
        return entry.value() - 1 == 0 ? 1L : entry.value() - 1;
    }
}
