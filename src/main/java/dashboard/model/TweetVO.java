package dashboard.model;

import org.gridgain.grid.cache.query.GridCacheQuerySqlField;
import org.springframework.social.twitter.api.Tweet;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: timveil
 * Date: 8/27/13
 * Time: 5:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class TweetVO {


    @GridCacheQuerySqlField(unique = true)
    private final long id;

    @GridCacheQuerySqlField
    private final String text;


    private final Date createdAt;
    private final String fromUser;

    public TweetVO(Tweet tweet) {
        this.id = tweet.getId();
        this.text = tweet.getText();
        this.createdAt = tweet.getCreatedAt();
        this.fromUser = tweet.getFromUser();
    }

    public long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public String getFromUser() {
        return fromUser;
    }
}
