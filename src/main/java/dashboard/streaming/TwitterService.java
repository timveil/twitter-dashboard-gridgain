package dashboard.streaming;

import dashboard.model.HashtagSummary;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: timveil
 * Date: 8/29/13
 * Time: 1:50 PM
 * To change this template use File | Settings | File Templates.
 */
public interface TwitterService {

    void ingest(int duration);

    List<HashtagSummary> getHashtagSummary(StreamerWindow window);
}
