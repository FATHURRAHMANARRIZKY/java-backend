package fimpa.co.fimpa.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "contact")
public class Contact {
     @Id
    private String id;
    private String name;
    private String mediasocial;
    
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getMediasocial() {
        return mediasocial;
    }
    public void setMediasocial(String mediasocial) {
        this.mediasocial = mediasocial;
    }    
}
