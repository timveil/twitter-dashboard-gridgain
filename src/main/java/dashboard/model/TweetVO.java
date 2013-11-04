package dashboard.model;

import com.google.common.base.Objects;
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

    @GridCacheQuerySqlField(index = true)
    private String screenName;

    @GridCacheQuerySqlField
    private String languageCode;

    @GridCacheQuerySqlField(index = true)
    private int hashTagCount;

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

        if (tweet.getEntities() != null && tweet.getEntities().getHashTags() != null) {
            this.hashTagCount = tweet.getEntities().getHashTags().size();
        }
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

    public int getHashTagCount() {
        return hashTagCount;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeLong(id);
        out.writeObject(text);
        out.writeObject(createdAt);
        out.writeLong(userId);
        out.writeObject(screenName);
        out.writeObject(languageCode);
        out.writeInt(hashTagCount);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.id = in.readLong();
        this.text = (String) in.readObject();
        this.createdAt = (Date) in.readObject();
        this.screenName = (String) in.readObject();
        this.languageCode = (String) in.readObject();
        this.userId = in.readLong();
        this.hashTagCount = in.readInt();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, text, createdAt, userId, screenName, languageCode, hashTagCount);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final TweetVO other = (TweetVO) obj;
        return Objects.equal(this.id, other.id) && Objects.equal(this.text, other.text) && Objects.equal(this.createdAt, other.createdAt) && Objects.equal(this.userId, other.userId) && Objects.equal(this.screenName, other.screenName) && Objects.equal(this.languageCode, other.languageCode) && Objects.equal(this.hashTagCount, other.hashTagCount);
    }
}
