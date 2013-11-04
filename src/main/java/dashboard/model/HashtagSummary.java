package dashboard.model;

/**
 * Created with IntelliJ IDEA.
 * User: timveil
 * Date: 11/3/13
 * Time: 2:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class HashtagSummary {

    private String hashtag;
    private long count;

    public HashtagSummary(String hashtag, long count) {
        this.hashtag = hashtag;
        this.count = count;
    }

    public String getHashtag() {
        return hashtag;
    }

    public long getCount() {
        return count;
    }
}
