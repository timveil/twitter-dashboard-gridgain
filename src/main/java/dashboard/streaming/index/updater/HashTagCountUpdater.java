package dashboard.streaming.index.updater;

import org.gridgain.grid.GridException;
import org.gridgain.grid.streamer.index.GridStreamerIndexEntry;
import org.gridgain.grid.streamer.index.GridStreamerIndexUpdater;
import org.jetbrains.annotations.Nullable;
import org.springframework.social.twitter.api.HashTagEntity;

/**
 * Created with IntelliJ IDEA.
 * User: timveil
 * Date: 11/1/13
 * Time: 4:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class HashTagCountUpdater implements GridStreamerIndexUpdater<HashTagEntity, String, Long> {

    @Nullable
    @Override
    public String indexKey(HashTagEntity hashTagEntity) {
        return hashTagEntity.getText();
    }

    @Nullable
    @Override
    public Long initialValue(HashTagEntity hashTagEntity, String s) {
        return 1l;
    }

    @Nullable
    @Override
    public Long onAdded(GridStreamerIndexEntry<HashTagEntity, String, Long> entry, HashTagEntity hashTagEntity) throws GridException {
        return entry.value() + 1;
    }

    @Nullable
    @Override
    public Long onRemoved(GridStreamerIndexEntry<HashTagEntity, String, Long> entry, HashTagEntity hashTagEntity) {
        return entry.value() - 1 == 0 ? 1L : entry.value() - 1;
    }
}
