package dashboard.core.streaming.index.provider;

import dashboard.core.model.HashTagVO;
import dashboard.core.streaming.index.updater.HashTagCountUpdater;
import org.gridgain.grid.streamer.index.tree.GridStreamerTreeIndexProvider;


public class HashTagIndexProvider extends GridStreamerTreeIndexProvider<HashTagVO, String, Long> {

    public HashTagIndexProvider() {
        super();

        this.setName(this.getClass().getName());

        this.setUpdater(new HashTagCountUpdater());
    }
}
