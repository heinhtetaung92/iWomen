package com.smk.model;

import com.google.gson.annotations.Expose;

public class Role {

@Expose
private String id;
@Expose
private String slug;
@Expose
private String name;
@Expose
private Permissions permissions;

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
* The slug
*/
public String getSlug() {
return slug;
}

/**
* 
* @param slug
* The slug
*/
public void setSlug(String slug) {
this.slug = slug;
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
* The permissions
*/
public Permissions getPermissions() {
return permissions;
}

/**
* 
* @param permissions
* The permissions
*/
public void setPermissions(Permissions permissions) {
this.permissions = permissions;
}

}