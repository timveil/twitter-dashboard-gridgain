package dashboard.core.streaming.index.updater;

import dashboard.core.model.HashTag;
import org.gridgain.grid.GridException;
import org.gridgain.grid.streamer.index.GridStreamerIndexEntry;
import org.gridgain.grid.streamer.index.GridStreamerIndexUpdater;
import org.jetbrains.annotations.Nullable;


public class HashTagCountUpdater implements GridStreamerIndexUpdater<HashTag, String, Long> {

    @Nullable
    @Override
    public String indexKey(HashTag hashTag) {
        return hashTag.getText();
    }

    @Nullable
    @Override
    public Long initialValue(HashTag hashTag, String s) {
        return 1l;
    }

    @Nullable
    @Override
    public Long onAdded(GridStreamerIndexEntry<HashTag, String, Long> entry, HashTag hashTag) throws GridException {
        return entry.value() + 1;
    }

    @Nullable
    @Override
    public Long onRemoved(GridStreamerIndexEntry<HashTag, String, Long> entry, HashTag hashTag) {
        return entry.value() - 1 == 0 ? 1L : entry.value() - 1;
    }
}
