package com.smk.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Author {

@Expose
private String id;
@Expose
private String name;
@Expose
private String image;
@Expose
private String about;
@SerializedName("about_mm")
@Expose
private String aboutMm;
@SerializedName("created_at")
@Expose
private String createdAt;
@SerializedName("updated_at")
@Expose
private String updatedAt;

/**
* 
* @return
* The id
*/
public String getId() {
return id;
}

/**
* 
* @param id
* The id
*/
public void setId(String id) {
this.id = id;
}

/**
* 
* @return
* The name
*/
public String getName() {
return name;
}

/**
* 
* @param name
* The name
*/
public void setName(String name) {
this.name = name;
}

/**
* 
* @return
* The image
*/
public String getImage() {
return image;
}

/**
* 
* @param image
* The image
*/
public void setImage(String image) {
this.image = image;
}

/**
* 
* @return
* The about
*/
public String getAbout() {
return about;
}

/**
* 
* @param about
* The about
*/
public void setAbout(String about) {
this.about = about;
}

/**
* 
* @return
* The aboutMm
*/
public String getAboutMm() {
return aboutMm;
}

/**
* 
* @param aboutMm
* The about_mm
*/
public void setAboutMm(String aboutMm) {
this.aboutMm = aboutMm;
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

}