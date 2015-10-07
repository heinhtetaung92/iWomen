package com.smk.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Permissions {

@SerializedName("user.create")
@Expose
private Boolean userCreate;
@SerializedName("user.update")
@Expose
private Boolean userUpdate;
@SerializedName("user.view")
@Expose
private Boolean userView;
@SerializedName("user.delete")
@Expose
private Boolean userDelete;

/**
* 
* @return
* The userCreate
*/
public Boolean getUserCreate() {
return userCreate;
}

/**
* 
* @param userCreate
* The user.create
*/
public void setUserCreate(Boolean userCreate) {
this.userCreate = userCreate;
}

/**
* 
* @return
* The userUpdate
*/
public Boolean getUserUpdate() {
return userUpdate;
}

/**
* 
* @param userUpdate
* The user.update
*/
public void setUserUpdate(Boolean userUpdate) {
this.userUpdate = userUpdate;
}

/**
* 
* @return
* The userView
*/
public Boolean getUserView() {
return userView;
}

/**
* 
* @param userView
* The user.view
*/
public void setUserView(Boolean userView) {
this.userView = userView;
}

/**
* 
* @return
* The userDelete
*/
public Boolean getUserDelete() {
return userDelete;
}

/**
* 
* @param userDelete
* The user.delete
*/
public void setUserDelete(Boolean userDelete) {
this.userDelete = userDelete;
}

}