package dashboard.utils;


import org.gridgain.grid.Grid;
import org.gridgain.grid.GridFactory;

public class GridUtils {

    public static final String GRID_NAME = "twitter-grid";
    public static final String STREAMER_NAME = "twitter-sample-stream";
    public static final String TOTAL_TWEETS = "totalTweets";
    public static final String TOTAL_TWEETS_NO_HASH_TAGS = "totalTweetsNoHashTags";

    public static Grid getGrid() {
        return GridFactory.grid(GRID_NAME);
    }
}
