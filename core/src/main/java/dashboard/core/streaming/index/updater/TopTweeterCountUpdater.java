package dashboard.core.streaming.index.updater;

import dashboard.core.model.Tweet;
import org.apache.commons.lang3.StringUtils;
import org.gridgain.grid.GridException;
import org.gridgain.grid.streamer.index.GridStreamerIndexEntry;
import org.gridgain.grid.streamer.index.GridStreamerIndexUpdater;
import org.jetbrains.annotations.Nullable;


public class TopTweeterCountUpdater implements GridStreamerIndexUpdater<Tweet, String, Long> {
    @Nullable
    @Override
    public String indexKey(Tweet tweet) {

        if (StringUtils.isNotBlank(tweet.getScreenName())) {
            return tweet.getScreenName();
        }

        return null;
    }

    @Nullable
    @Override
    public Long initialValue(Tweet tweet, String s) {
        return 1L;
    }

    @Nullable
    @Override
    public Long onAdded(GridStreamerIndexEntry<Tweet, String, Long> entry, Tweet tweet) throws GridException {
        return entry.value() + 1;
    }

    @Nullable
    @Override
    public Long onRemoved(GridStreamerIndexEntry<Tweet, String, Long> entry, Tweet tweet) {
        return entry.value() - 1 == 0 ? 1L : entry.value() - 1;
    }
}
