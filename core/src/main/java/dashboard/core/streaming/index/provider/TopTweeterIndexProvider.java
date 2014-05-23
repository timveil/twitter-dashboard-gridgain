package dashboard.core.streaming.index.provider;

import dashboard.core.model.TweetVO;
import dashboard.core.streaming.index.updater.TopTweeterCountUpdater;
import org.gridgain.grid.streamer.index.tree.GridStreamerTreeIndexProvider;


public class TopTweeterIndexProvider extends GridStreamerTreeIndexProvider<TweetVO, String, Long> {

    public TopTweeterIndexProvider() {
        super();

        this.setName(this.getClass().getName());

        this.setUpdater(new TopTweeterCountUpdater());
    }
}
