package dashboard.core.hpc;

import dashboard.core.model.HashTagVO;
import dashboard.core.utils.GridConstants;
import org.gridgain.grid.lang.GridReducer;
import org.gridgain.grid.streamer.index.GridStreamerIndexEntry;
import org.gridgain.grid.util.lang.GridFunc;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class HashTagReducer implements GridReducer<Collection<GridStreamerIndexEntry<HashTagVO, String, Long>>, Collection<GridStreamerIndexEntry<HashTagVO, String, Long>>> {

    private List<GridStreamerIndexEntry<HashTagVO, String, Long>> sorted = new ArrayList<>();

    @Override
    public boolean collect(@Nullable Collection<GridStreamerIndexEntry<HashTagVO, String, Long>> gridStreamerIndexEntries) {
        if (gridStreamerIndexEntries != null && !gridStreamerIndexEntries.isEmpty()) {
            sorted.addAll(gridStreamerIndexEntries);
        }

        return true;
    }

    @Override
    public Collection<GridStreamerIndexEntry<HashTagVO, String, Long>> reduce() {
        Collections.sort(sorted, new Comparator<GridStreamerIndexEntry<HashTagVO, String, Long>>() {

            @Override
            public int compare(GridStreamerIndexEntry<HashTagVO, String, Long> o1, GridStreamerIndexEntry<HashTagVO, String, Long> o2) {
                return o2.value().compareTo(o1.value());
            }
        });

        return GridFunc.retain(sorted, true, GridConstants.MAX_NUM_RETURNED);
    }
}
