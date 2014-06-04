package dashboard.core.hpc;

import dashboard.core.model.HashTag;
import dashboard.core.utils.GridConstants;
import org.gridgain.grid.lang.GridReducer;
import org.gridgain.grid.streamer.index.GridStreamerIndexEntry;
import org.gridgain.grid.util.lang.GridFunc;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class HashTagReducer implements GridReducer<Collection<GridStreamerIndexEntry<HashTag, String, Long>>, Collection<GridStreamerIndexEntry<HashTag, String, Long>>> {

    private List<GridStreamerIndexEntry<HashTag, String, Long>> sorted = new ArrayList<>();

    @Override
    public boolean collect(@Nullable Collection<GridStreamerIndexEntry<HashTag, String, Long>> gridStreamerIndexEntries) {
        if (gridStreamerIndexEntries != null && !gridStreamerIndexEntries.isEmpty()) {
            sorted.addAll(gridStreamerIndexEntries);
        }

        return true;
    }

    @Override
    public Collection<GridStreamerIndexEntry<HashTag, String, Long>> reduce() {
        Collections.sort(sorted, new Comparator<GridStreamerIndexEntry<HashTag, String, Long>>() {

            @Override
            public int compare(GridStreamerIndexEntry<HashTag, String, Long> o1, GridStreamerIndexEntry<HashTag, String, Long> o2) {
                return o2.value().compareTo(o1.value());
            }
        });

        return GridFunc.retain(sorted, true, GridConstants.MAX_NUM_RETURNED);
    }
}
