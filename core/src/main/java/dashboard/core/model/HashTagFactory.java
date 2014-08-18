package dashboard.core.model;


import org.springframework.social.twitter.api.HashTagEntity;

import java.util.UUID;

public class HashTagFactory {

    public static HashTag create(Tweet tweet, HashTagEntity hashHasTagEntity){
        HashTag hashTag = new HashTag();
        hashTag.setGUID(UUID.randomUUID().toString());
        hashTag.setTweetGUID(tweet.getGUID());
        hashTag.setText(hashHasTagEntity.getText());

        return hashTag;
    }

}
