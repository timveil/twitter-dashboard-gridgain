package dashboard.model;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import org.gridgain.grid.cache.query.GridCacheQuerySqlField;
import org.springframework.social.twitter.api.HashTagEntity;
import org.springframework.social.twitter.api.Tweet;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Date;
import java.util.List;
import java.util.UUID;


public class TweetVO implements Externalizable {


    @GridCacheQuerySqlField(unique = true, index = true)
    private String GUID;

    @GridCacheQuerySqlField
    private long tweetId;

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
    private boolean fake;

    private List<HashTagVO> hashTags;


    // required for Externalizable
    public TweetVO() {
    }

    public TweetVO(Tweet tweet, boolean fake) {
        this.GUID = UUID.randomUUID().toString();
        this.tweetId = tweet.getId();
        this.text = tweet.getText();
        this.createdAt = tweet.getCreatedAt();
        this.screenName = tweet.getUser().getScreenName();
        this.userId = tweet.getUser().getId();
        this.languageCode = tweet.getLanguageCode();
        this.fake = fake;

        if (tweet.hasTags()) {
            hashTags = Lists.newArrayList();

            for (HashTagEntity entity : tweet.getEntities().getHashTags()) {
                hashTags.add(new HashTagVO(this.GUID, entity));
            }
        }

    }

    public String getGUID() {
        return GUID;
    }

    public long getTweetId() {
        return tweetId;
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

    public List<HashTagVO> getHashTags() {
        return hashTags;
    }

    public boolean isFake() {
        return fake;
    }

    public boolean hasHashTags() {
        return hashTags != null && !hashTags.isEmpty();
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeLong(tweetId);
        out.writeObject(text);
        out.writeObject(GUID);
        out.writeObject(createdAt);
        out.writeLong(userId);
        out.writeObject(screenName);
        out.writeObject(languageCode);
        out.writeBoolean(fake);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.tweetId = in.readLong();
        this.GUID = (String) in.readObject();
        this.text = (String) in.readObject();
        this.createdAt = (Date) in.readObject();
        this.screenName = (String) in.readObject();
        this.languageCode = (String) in.readObject();
        this.userId = in.readLong();
        this.fake = in.readBoolean();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(GUID, tweetId, text, createdAt, userId, screenName, languageCode, fake, hashTags);
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
        return Objects.equal(this.GUID, other.GUID) && Objects.equal(this.tweetId, other.tweetId) && Objects.equal(this.text, other.text) && Objects.equal(this.createdAt, other.createdAt) && Objects.equal(this.userId, other.userId) && Objects.equal(this.screenName, other.screenName) && Objects.equal(this.languageCode, other.languageCode) && Objects.equal(this.fake, other.fake) && Objects.equal(this.hashTags, other.hashTags);
    }
}
