package org.undp_iwomen.iwomen.model.parse;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Date;

/**
 * Created by khinsandar on 4/17/15.
 */
@ParseClassName("Post")
public class Post extends ParseObject {

    String title;
    String content;
    String likes;
    String contentTypes;
    String createdAt;
    String updatedAt;
    String userId;

    public String getTitle() {
        return getString("title");
    }

    public void setTitle(String title) {
        put("title",title);
    }

    public String getContent() {
        return getString("content");
    }

    public void setContent(String content) {
        put("content",content);
    }

    public String getLikes() {
        return getString("likes");
    }

    public void setLikes(String likes) {
        put("likes",likes);
    }

    public String getContentTypes() {
        return getString("contentTypes");
    }

    public void setContentTypes(String contentTypes) {
        put("contentTypes",contentTypes);
    }

    public Date getCreatedAt() {
        return getDate("createdAt");
    }

    public void setCreatedAt(Date createdAt) {
        put("createdAt",createdAt);
    }

    public Date getUpdatedAt() {
        return getDate("updatedAt");
    }

    public void setUpdatedAt(Date updatedAt) {
        put("updatedAt",updatedAt);
    }

    public String getUserId() {
        return getString("userId");
    }

    public void setUserId(String userId) {
        put("userId",ParseObject.createWithoutData("_User", userId));
    }

    /*public void setBusId(String busId) {
        put("BusId", ParseObject.createWithoutData(Bus.class, busId));
    }

    public ParseObject getBusId() {
        return getParseObject("BusId");
    }
    public void setUser(String currentUserID) {
        put("AdminUser", ParseObject.createWithoutData("_User", currentUserID));

    }*/

    public static ParseQuery<Post> getQuery(){
        return ParseQuery.getQuery(Post.class);
    }
}
