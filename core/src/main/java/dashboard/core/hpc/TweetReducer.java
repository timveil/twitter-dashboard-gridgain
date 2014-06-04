package dashboard.core.hpc;

import dashboard.core.model.TweetVO;
import dashboard.core.utils.GridConstants;
import org.gridgain.grid.lang.GridReducer;
import org.gridgain.grid.streamer.index.GridStreamerIndexEntry;
import org.gridgain.grid.util.lang.GridFunc;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class TweetReducer implements GridReducer<Collection<GridStreamerIndexEntry<TweetVO, String, Long>>, Collection<GridStreamerIndexEntry<TweetVO, String, Long>>> {
    private List<GridStreamerIndexEntry<TweetVO, String, Long>> sorted = new ArrayList<>();


    @Override
    public boolean collect(@Nullable Collection<GridStreamerIndexEntry<TweetVO, String, Long>> gridStreamerIndexEntries) {
        if (gridStreamerIndexEntries != null && !gridStreamerIndexEntries.isEmpty()) {
            sorted.addAll(gridStreamerIndexEntries);
        }

        return true;
    }

    @Override
    public Collection<GridStreamerIndexEntry<TweetVO, String, Long>> reduce() {
        Collections.sort(sorted, new Comparator<GridStreamerIndexEntry<TweetVO, String, Long>>() {

            @Override
            public int compare(GridStreamerIndexEntry<TweetVO, String, Long> o1, GridStreamerIndexEntry<TweetVO, String, Long> o2) {
                return o2.value().compareTo(o1.value());
            }
        });

        return GridFunc.retain(sorted, true, GridConstants.MAX_NUM_RETURNED);
    }
}
