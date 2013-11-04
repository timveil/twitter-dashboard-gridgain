package dashboard.streaming;

import dashboard.model.HashTagSummary;

import java.util.List;


public interface TwitterService {

    void ingest(int duration);

    List<HashTagSummary> getHashTagSummary(StreamerWindow window);
}
