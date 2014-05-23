package dashboard.web.streaming.index.provider;

import dashboard.web.model.HashTagVO;
import dashboard.web.streaming.index.updater.HashTagCountUpdater;
import org.gridgain.grid.streamer.index.tree.GridStreamerTreeIndexProvider;


public class HashTagIndexProvider extends GridStreamerTreeIndexProvider<HashTagVO, String, Long> {

    public HashTagIndexProvider() {
        super();

        this.setName(this.getClass().getName());

        this.setUpdater(new HashTagCountUpdater());
    }
}
