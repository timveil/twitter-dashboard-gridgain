package dashboard.model;


public class HashTagSummary {

    private String hashTag;
    private long count;

    public HashTagSummary(String hashTag, long count) {
        this.hashTag = hashTag;
        this.count = count;
    }

    public String getHashTag() {
        return hashTag;
    }

    public long getCount() {
        return count;
    }
}
