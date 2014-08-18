package dashboard.core.streaming.stage;

import dashboard.core.model.HashTag;
import dashboard.core.utils.GridConstants;
import org.gridgain.grid.GridException;
import org.gridgain.grid.streamer.GridStreamerContext;
import org.gridgain.grid.streamer.GridStreamerWindow;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;


public class RemoveHashTagFromWindowsStage extends RemoveFromWindowStage<HashTag> {

    @Override
    public String name() {
        return this.getClass().getSimpleName();
    }

    @Nullable
    @Override
    public Map<String, Collection<?>> run(GridStreamerContext gridStreamerContext, Collection<HashTag> hashTags) throws GridException {

        final GridStreamerWindow<HashTag> oneMinute = gridStreamerContext.window(GridConstants.ONE_MINUTE_WINDOW);
        remove(oneMinute);

        final GridStreamerWindow<HashTag> fiveMinute = gridStreamerContext.window(GridConstants.FIVE_MINUTE_WINDOW);
        remove(fiveMinute);

        final GridStreamerWindow<HashTag> tenMinute = gridStreamerContext.window(GridConstants.TEN_MINUTE_WINDOW);
        remove(tenMinute);

        return null;

    }


}
