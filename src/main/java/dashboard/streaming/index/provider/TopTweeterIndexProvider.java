package dashboard.streaming.index.provider;

import dashboard.streaming.index.updater.TopTweeterCountUpdater;
import org.gridgain.grid.streamer.index.tree.GridStreamerTreeIndexProvider;
import org.springframework.social.twitter.api.Tweet;


public class TopTweeterIndexProvider extends GridStreamerTreeIndexProvider<Tweet, String, Long> {

    public TopTweeterIndexProvider() {
        super();

        this.setName(this.getClass().getName());

        this.setUpdater(new TopTweeterCountUpdater());
    }
}
