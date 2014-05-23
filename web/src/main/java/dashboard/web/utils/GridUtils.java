package dashboard.web.utils;


import org.gridgain.grid.Grid;
import org.gridgain.grid.GridGain;

public class GridUtils {

    public static final String GRID_NAME = "twitter-grid";
    public static final String STREAMER_NAME = "twitter-sample-stream";
    public static final String TOTAL_TWEETS = "totalTweets";
    public static final String TOTAL_TWEETS_NO_HASH_TAGS = "totalTweetsNoHashTags";

    public static final int EVICTION_COUNT = 1000;

    public static Grid getGrid() {
        return GridGain.grid(GRID_NAME);
    }
}
