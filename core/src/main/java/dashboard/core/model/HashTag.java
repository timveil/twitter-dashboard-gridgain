package dashboard.core.model;

import org.gridgain.grid.cache.query.GridCacheQuerySqlField;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Objects;


public class HashTag implements Externalizable {

    @GridCacheQuerySqlField(unique = true)
    private String GUID;

    @GridCacheQuerySqlField
    private String text;

    @GridCacheQuerySqlField
    private String tweetGUID;

    HashTag() {
    }

    public String getGUID() {
        return GUID;
    }

    public void setGUID(String GUID) {
        this.GUID = GUID;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTweetGUID() {
        return tweetGUID;
    }

    public void setTweetGUID(String tweetGUID) {
        this.tweetGUID = tweetGUID;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(tweetGUID);
        out.writeObject(text);
        out.writeObject(GUID);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        tweetGUID = (String)in.readObject();
        text = (String)in.readObject();
        GUID = (String)in.readObject();
    }

    @Override
    public int hashCode() {
        return Objects.hash(GUID, text, tweetGUID);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final HashTag other = (HashTag) obj;
        return Objects.equals(this.GUID, other.GUID) && Objects.equals(this.text, other.text) && Objects.equals(this.tweetGUID, other.tweetGUID);
    }
}
