package com.smk.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Comment {

@Expose
private Integer id;
@SerializedName("post_id")
@Expose
private Integer postId;
@SerializedName("user_id")
@Expose
private Integer userId;
@Expose
private String comment;
@SerializedName("created_at")
@Expose
private String createdAt;
@SerializedName("updated_at")
@Expose
private String updatedAt;
@Expose
private User user;

/**
* 
* @return
* The id
*/
public Integer getId() {
return id;
}

/**
* 
* @param id
* The id
*/
public void setId(Integer id) {
this.id = id;
}

/**
* 
* @return
* The postId
*/
public Integer getPostId() {
return postId;
}

/**
* 
* @param postId
* The post_id
*/
public void setPostId(Integer postId) {
this.postId = postId;
}

/**
* 
* @return
* The userId
*/
public Integer getUserId() {
return userId;
}

/**
* 
* @param userId
* The user_id
*/
public void setUserId(Integer userId) {
this.userId = userId;
}

/**
* 
* @return
* The comment
*/
public String getComment() {
return comment;
}

/**
* 
* @param comment
* The comment
*/
public void setComment(String comment) {
this.comment = comment;
}

/**
* 
* @return
* The createdAt
*/
public String getCreatedAt() {
return createdAt;
}

/**
* 
* @param createdAt
* The created_at
*/
public void setCreatedAt(String createdAt) {
this.createdAt = createdAt;
}

/**
* 
* @return
* The updatedAt
*/
public String getUpdatedAt() {
return updatedAt;
}

/**
* 
* @param updatedAt
* The updated_at
*/
public void setUpdatedAt(String updatedAt) {
this.updatedAt = updatedAt;
}

/**
* 
* @return
* The user
*/
public User getUser() {
return user;
}

/**
* 
* @param user
* The user
*/
public void setUser(User user) {
this.user = user;
}

}