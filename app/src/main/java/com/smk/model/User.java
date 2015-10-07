package com.smk.model;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

@Expose
private String id;
@Expose
private String username;
@Expose
private String email;
@Expose
private String password;
@SerializedName("first_name")
@Expose
private String firstName;
@SerializedName("last_name")
@Expose
private String lastName;
@Expose
private String phone;
@Expose
private Object address;
@Expose
private Object photo;
@Expose
private List<Role> roles = new ArrayList<Role>();

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
* The username
*/
public String getUsername() {
return username;
}

/**
* 
* @param username
* The username
*/
public void setUsername(String username) {
this.username = username;
}

/**
* 
* @return
* The email
*/
public String getEmail() {
return email;
}

/**
* 
* @param email
* The email
*/
public void setEmail(String email) {
this.email = email;
}

/**
* 
* @return
* The password
*/
public String getPassword() {
return password;
}

/**
* 
* @param password
* The password
*/
public void setPassword(String password) {
this.password = password;
}

/**
* 
* @return
* The firstName
*/
public String getFirstName() {
return firstName;
}

/**
* 
* @param firstName
* The first_name
*/
public void setFirstName(String firstName) {
this.firstName = firstName;
}

/**
* 
* @return
* The lastName
*/
public String getLastName() {
return lastName;
}

/**
* 
* @param lastName
* The last_name
*/
public void setLastName(String lastName) {
this.lastName = lastName;
}

/**
* 
* @return
* The phone
*/
public String getPhone() {
return phone;
}

/**
* 
* @param phone
* The phone
*/
public void setPhone(String phone) {
this.phone = phone;
}

/**
* 
* @return
* The address
*/
public Object getAddress() {
return address;
}

/**
* 
* @param address
* The address
*/
public void setAddress(Object address) {
this.address = address;
}

/**
* 
* @return
* The photo
*/
public Object getPhoto() {
return photo;
}

/**
* 
* @param photo
* The photo
*/
public void setPhoto(Object photo) {
this.photo = photo;
}

/**
* 
* @return
* The roles
*/
public List<Role> getRoles() {
return roles;
}

/**
* 
* @param roles
* The roles
*/
public void setRoles(List<Role> roles) {
this.roles = roles;
}

@Override
public String toString() {
	return "User [id=" + id + ", username=" + username + ", email=" + email
			+ ", password=" + password + ", firstName=" + firstName
			+ ", lastName=" + lastName + ", phone=" + phone + ", address="
			+ address + ", photo=" + photo + ", roles=" + roles + "]";
}



}
