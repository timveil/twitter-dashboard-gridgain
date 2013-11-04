package dashboard.model;

import org.gridgain.grid.cache.query.GridCacheQuerySqlField;
import org.springframework.social.twitter.api.Tweet;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Date;


public class TweetVO implements Externalizable {


    @GridCacheQuerySqlField(unique = true)
    private long id;

    @GridCacheQuerySqlField(index = true)
    private String text;

    @GridCacheQuerySqlField
    private Date createdAt;

    @GridCacheQuerySqlField
    private long userId;

    @GridCacheQuerySqlField
    private String screenName;

    @GridCacheQuerySqlField
    private String languageCode;

    // required for Externalizable
    public TweetVO() {
    }

    public TweetVO(Tweet tweet) {
        this.id = tweet.getId();
        this.text = tweet.getText();
        this.createdAt = tweet.getCreatedAt();
        this.screenName = tweet.getUser().getScreenName();
        this.userId = tweet.getUser().getId();
        this.languageCode = tweet.getLanguageCode();
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


    public Long getUserId() {
        return userId;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeLong(id);
        out.writeObject(text);
        out.writeObject(createdAt);
        out.writeLong(userId);
        out.writeObject(screenName);
        out.writeObject(languageCode);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.id = in.readLong();
        this.text = (String) in.readObject();
        this.createdAt = (Date) in.readObject();
        this.screenName = (String) in.readObject();
        this.languageCode = (String) in.readObject();
        this.userId = in.readLong();
    }
}
