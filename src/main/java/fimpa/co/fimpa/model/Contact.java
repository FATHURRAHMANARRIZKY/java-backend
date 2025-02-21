package fimpa.co.fimpa.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "contact")
public class Contact {
     @Id
    private String id;
    private String instagramProfile;
    private String facebookProfile;
    private String twitterProfile;
    private String tiktokProfile;
    
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getInstagramProfile() {
        return instagramProfile;
    }
    public void setInstagramProfile(String instagramProfile) {
        this.instagramProfile = instagramProfile;
    }
    public String getFacebookProfile() {
        return facebookProfile;
    }
    public void setFacebookProfile(String facebookProfile) {
        this.facebookProfile = facebookProfile;
    }
    public String getTwitterProfile() {
        return twitterProfile;
    }
    public void setTwitterProfile(String twitterProfile) {
        this.twitterProfile = twitterProfile;
    }
    public String getTiktokProfile() {
        return tiktokProfile;
    }
    public void setTiktokProfile(String tiktokProfile) {
        this.tiktokProfile = tiktokProfile;
    }

    
}
