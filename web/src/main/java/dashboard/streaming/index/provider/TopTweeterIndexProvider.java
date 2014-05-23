package dashboard.streaming.index.provider;

import dashboard.model.TweetVO;
import dashboard.streaming.index.updater.TopTweeterCountUpdater;
import org.gridgain.grid.streamer.index.tree.GridStreamerTreeIndexProvider;


public class TopTweeterIndexProvider extends GridStreamerTreeIndexProvider<TweetVO, String, Long> {

    public TopTweeterIndexProvider() {
        super();

        this.setName(this.getClass().getName());

        this.setUpdater(new TopTweeterCountUpdater());
    }
}
