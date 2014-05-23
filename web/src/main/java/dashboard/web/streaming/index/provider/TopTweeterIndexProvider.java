package dashboard.web.streaming.index.provider;

import dashboard.web.model.TweetVO;
import dashboard.web.streaming.index.updater.TopTweeterCountUpdater;
import org.gridgain.grid.streamer.index.tree.GridStreamerTreeIndexProvider;


public class TopTweeterIndexProvider extends GridStreamerTreeIndexProvider<TweetVO, String, Long> {

    public TopTweeterIndexProvider() {
        super();

        this.setName(this.getClass().getName());

        this.setUpdater(new TopTweeterCountUpdater());
    }
}
