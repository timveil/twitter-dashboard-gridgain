package dashboard.core.model;


import org.gridgain.grid.cache.affinity.GridCacheAffinityKeyMapped;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Objects;

public class HashTagKey implements Externalizable {

    private String hashTagId;

    @GridCacheAffinityKeyMapped
    private String tweetId;

    HashTagKey() {
    }

    public HashTagKey(String hashTagId, String tweetId) {
        this.hashTagId = hashTagId;
        this.tweetId = tweetId;
    }


    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        hashTagId = (String) in.readObject();
        tweetId = (String) in.readObject();
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(hashTagId);
        out.writeObject(tweetId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hashTagId, tweetId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final HashTagKey other = (HashTagKey) obj;
        return Objects.equals(this.hashTagId, other.hashTagId) && Objects.equals(this.tweetId, other.tweetId);
    }
}
