package dashboard.model;


public class TopTweeterSummary {

    private String screenName;
    private long count;

    public TopTweeterSummary(String screenName, long count) {
        this.screenName = screenName;
        this.count = count;
    }

    public String getScreenName() {
        return screenName;
    }

    public long getCount() {
        return count;
    }
}
