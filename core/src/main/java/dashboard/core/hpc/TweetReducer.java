package dashboard.core.hpc;

import dashboard.core.model.Tweet;
import dashboard.core.utils.GridConstants;
import org.gridgain.grid.lang.GridReducer;
import org.gridgain.grid.streamer.index.GridStreamerIndexEntry;
import org.gridgain.grid.util.lang.GridFunc;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class TweetReducer implements GridReducer<Collection<GridStreamerIndexEntry<Tweet, String, Long>>, Collection<GridStreamerIndexEntry<Tweet, String, Long>>> {
    private List<GridStreamerIndexEntry<Tweet, String, Long>> sorted = new ArrayList<>();


    @Override
    public boolean collect(@Nullable Collection<GridStreamerIndexEntry<Tweet, String, Long>> gridStreamerIndexEntries) {
        if (gridStreamerIndexEntries != null && !gridStreamerIndexEntries.isEmpty()) {
            sorted.addAll(gridStreamerIndexEntries);
        }

        return true;
    }

    @Override
    public Collection<GridStreamerIndexEntry<Tweet, String, Long>> reduce() {
        Collections.sort(sorted, new Comparator<GridStreamerIndexEntry<Tweet, String, Long>>() {

            @Override
            public int compare(GridStreamerIndexEntry<Tweet, String, Long> o1, GridStreamerIndexEntry<Tweet, String, Long> o2) {
                return o2.value().compareTo(o1.value());
            }
        });

        return GridFunc.retain(sorted, true, GridConstants.MAX_NUM_RETURNED);
    }
}
