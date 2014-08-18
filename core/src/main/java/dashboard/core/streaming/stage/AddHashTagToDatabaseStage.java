package dashboard.core.streaming.stage;

import dashboard.core.model.HashTag;
import dashboard.core.model.HashTagKey;
import org.gridgain.grid.GridException;
import org.gridgain.grid.cache.GridCache;
import org.gridgain.grid.lang.GridPredicate;
import org.gridgain.grid.streamer.GridStreamerContext;
import org.gridgain.grid.streamer.GridStreamerStage;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;


public class AddHashTagToDatabaseStage implements GridStreamerStage<HashTag> {

    @Override
    public String name() {
        return this.getClass().getSimpleName();
    }

    @Nullable
    @Override
    public Map<String, Collection<?>> run(final GridStreamerContext gridStreamerContext, Collection<HashTag> hashTags) throws GridException {

        if (!hashTags.isEmpty()) {

            final GridCache<HashTagKey, HashTag> hashTagCache = gridStreamerContext.projection().grid().cache(HashTag.class.getName());

            for (HashTag hashTag : hashTags) {
                hashTagCache.putxAsync(new HashTagKey(hashTag.getGUID(), hashTag.getTweetGUID()), hashTag, (GridPredicate) null);
            }

        }

        return Collections.<String, Collection<?>>singletonMap(RemoveHashTagFromWindowsStage.class.getSimpleName(), hashTags);
    }

}
