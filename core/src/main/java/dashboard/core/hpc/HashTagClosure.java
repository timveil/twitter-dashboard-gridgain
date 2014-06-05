package dashboard.core.hpc;

import dashboard.core.model.HashTag;
import org.gridgain.grid.lang.GridClosure;
import org.gridgain.grid.streamer.GridStreamerContext;
import org.gridgain.grid.streamer.GridStreamerWindow;
import org.gridgain.grid.streamer.index.GridStreamerIndex;
import org.gridgain.grid.streamer.index.GridStreamerIndexEntry;

import java.util.ArrayList;
import java.util.Collection;

public class HashTagClosure implements GridClosure<GridStreamerContext, Collection<GridStreamerIndexEntry<HashTag, String, Long>>> {

    private String windowName;

    public HashTagClosure(String windowName) {
        this.windowName = windowName;
    }

    @Override
    public Collection<GridStreamerIndexEntry<HashTag, String, Long>> apply(GridStreamerContext gridStreamerContext) {

        final GridStreamerWindow<HashTag> gridStreamerWindow = gridStreamerContext.window(windowName);

        final GridStreamerIndex<HashTag, String, Long> index = gridStreamerWindow.index();

        return new ArrayList<>(index.entries(0));
    }
}
