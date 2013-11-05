package dashboard.streaming.index.provider;

import dashboard.streaming.index.updater.HashTagCountUpdater;
import org.gridgain.grid.streamer.index.tree.GridStreamerTreeIndexProvider;
import org.springframework.social.twitter.api.HashTagEntity;


public class HashTagIndexProvider extends GridStreamerTreeIndexProvider<HashTagEntity, String, Long> {

    public HashTagIndexProvider() {
        super();

        this.setName(this.getClass().getName());

        this.setUpdater(new HashTagCountUpdater());
    }
}
