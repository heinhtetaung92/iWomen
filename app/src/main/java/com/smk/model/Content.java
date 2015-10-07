package com.smk.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Content {

@SerializedName("short_content")
@Expose
private String shortContent;
@SerializedName("short_content_mm")
@Expose
private String shortContentMm;
@Expose
private String content;
@SerializedName("content_mm")
@Expose
private String contentMm;

/**
* 
* @return
* The shortContent
*/
public String getShortContent() {
return shortContent;
}

/**
* 
* @param shortContent
* The short_content
*/
public void setShortContent(String shortContent) {
this.shortContent = shortContent;
}

/**
* 
* @return
* The shortContentMm
*/
public String getShortContentMm() {
return shortContentMm;
}

/**
* 
* @param shortContentMm
* The short_content_mm
*/
public void setShortContentMm(String shortContentMm) {
this.shortContentMm = shortContentMm;
}

/**
* 
* @return
* The content
*/
public String getContent() {
return content;
}

/**
* 
* @param content
* The content
*/
public void setContent(String content) {
this.content = content;
}

/**
* 
* @return
* The contentMm
*/
public String getContentMm() {
return contentMm;
}

/**
* 
* @param contentMm
* The content_mm
*/
public void setContentMm(String contentMm) {
this.contentMm = contentMm;
}

@Override
public String toString() {
	return " {\"shortContent\":\"" + shortContent + "\", shortContentMm\":\""
			+ shortContentMm + "\", content\":\"" + content
			+ "\", contentMm\":\"" + contentMm + "}";
}





}