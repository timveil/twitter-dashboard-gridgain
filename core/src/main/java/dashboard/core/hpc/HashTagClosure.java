package dashboard.core.hpc;

import dashboard.core.model.HashTagVO;
import org.gridgain.grid.lang.GridClosure;
import org.gridgain.grid.streamer.GridStreamerContext;
import org.gridgain.grid.streamer.GridStreamerWindow;
import org.gridgain.grid.streamer.index.GridStreamerIndex;
import org.gridgain.grid.streamer.index.GridStreamerIndexEntry;

import java.util.Collection;

public class HashTagClosure implements GridClosure<GridStreamerContext, Collection<GridStreamerIndexEntry<HashTagVO, String, Long>>> {

    private String windowName;

    public HashTagClosure(String windowName) {
        this.windowName = windowName;
    }

    @Override
    public Collection<GridStreamerIndexEntry<HashTagVO, String, Long>> apply(GridStreamerContext gridStreamerContext) {

        final GridStreamerWindow<HashTagVO> gridStreamerWindow = gridStreamerContext.window(windowName);

        final GridStreamerIndex<HashTagVO, String, Long> index = gridStreamerWindow.index();

        return index.entries(0);
    }
}
