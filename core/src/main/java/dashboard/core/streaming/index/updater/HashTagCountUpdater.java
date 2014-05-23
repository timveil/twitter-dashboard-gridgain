package dashboard.core.streaming.index.updater;

import dashboard.core.model.HashTagVO;
import org.gridgain.grid.GridException;
import org.gridgain.grid.streamer.index.GridStreamerIndexEntry;
import org.gridgain.grid.streamer.index.GridStreamerIndexUpdater;
import org.jetbrains.annotations.Nullable;


public class HashTagCountUpdater implements GridStreamerIndexUpdater<HashTagVO, String, Long> {

    @Nullable
    @Override
    public String indexKey(HashTagVO hashTag) {
        return hashTag.getText();
    }

    @Nullable
    @Override
    public Long initialValue(HashTagVO hashTag, String s) {
        return 1l;
    }

    @Nullable
    @Override
    public Long onAdded(GridStreamerIndexEntry<HashTagVO, String, Long> entry, HashTagVO hashTag) throws GridException {
        return entry.value() + 1;
    }

    @Nullable
    @Override
    public Long onRemoved(GridStreamerIndexEntry<HashTagVO, String, Long> entry, HashTagVO hashTag) {
        return entry.value() - 1 == 0 ? 1L : entry.value() - 1;
    }
}
