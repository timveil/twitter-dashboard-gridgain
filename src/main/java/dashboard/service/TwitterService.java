package dashboard.service;

import dashboard.model.HashTagSummary;
import dashboard.utils.StreamerWindow;

import java.util.List;


public interface TwitterService {

    void ingest(int duration);

    List<HashTagSummary> getHashTagSummary(StreamerWindow window);
}
