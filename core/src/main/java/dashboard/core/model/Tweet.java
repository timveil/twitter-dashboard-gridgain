package dashboard.core.model;

import org.gridgain.grid.cache.query.GridCacheQuerySqlField;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Date;
import java.util.Objects;


public class Tweet implements Externalizable {


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

    @GridCacheQuerySqlField
    private boolean geoEnabled;

    @GridCacheQuerySqlField
    private String location;

    @GridCacheQuerySqlField
    private String source;

    // required for Externalizable
    Tweet() {
    }

    public String getGUID() {
        return GUID;
    }

    public void setGUID(String GUID) {
        this.GUID = GUID;
    }

    public long getTweetId() {
        return tweetId;
    }

    public void setTweetId(long tweetId) {
        this.tweetId = tweetId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public boolean isFake() {
        return fake;
    }

    public void setFake(boolean fake) {
        this.fake = fake;
    }

    public boolean isGeoEnabled() {
        return geoEnabled;
    }

    public void setGeoEnabled(boolean geoEnabled) {
        this.geoEnabled = geoEnabled;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        GUID = (String) in.readObject();
        tweetId = in.readLong();
        text = (String) in.readObject();
        createdAt = (Date) in.readObject();
        userId = in.readLong();
        screenName = (String) in.readObject();
        languageCode = (String) in.readObject();
        fake = in.readBoolean();
        geoEnabled = in.readBoolean();
        location = (String) in.readObject();
        source = (String) in.readObject();

    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(GUID);
        out.writeLong(tweetId);
        out.writeObject(text);
        out.writeObject(createdAt);
        out.writeLong(userId);
        out.writeObject(screenName);
        out.writeObject(languageCode);
        out.writeBoolean(fake);
        out.writeBoolean(geoEnabled);
        out.writeObject(location);
        out.writeObject(source);
    }

    @Override
    public int hashCode() {
        return Objects.hash(GUID, tweetId, text, createdAt, userId, screenName, languageCode, fake, geoEnabled, location, source);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Tweet other = (Tweet) obj;
        return Objects.equals(this.GUID, other.GUID) && Objects.equals(this.tweetId, other.tweetId) && Objects.equals(this.text, other.text) && Objects.equals(this.createdAt, other.createdAt) && Objects.equals(this.userId, other.userId) && Objects.equals(this.screenName, other.screenName) && Objects.equals(this.languageCode, other.languageCode) && Objects.equals(this.fake, other.fake) && Objects.equals(this.geoEnabled, other.geoEnabled) && Objects.equals(this.location, other.location) && Objects.equals(this.source, other.source);
    }
}
