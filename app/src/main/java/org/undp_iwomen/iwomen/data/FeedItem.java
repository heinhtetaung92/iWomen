package org.undp_iwomen.iwomen.data;

public class FeedItem {




	private String post_obj_id;
	String post_title ;
	String post_content;
	String post_like ;
	String post_img_path ;
	String post_content_type;
	String post_content_user_id ;
	String post_content_user_name;
	String post_content_user_img_path ;

	String status;
	String created_at ;
	String updated_at;

	public FeedItem() {
	}


	public FeedItem(String post_obj_id, String post_title, String post_content, String post_like,
					String post_img_path, String post_content_type, String post_content_user_id,
					String post_content_user_name , String post_content_user_img_path ,
					String status , String created_at , String updated_at) {
		super();
		this.post_obj_id = post_obj_id;
		this.post_title = post_title;
		this.post_content = post_content;
		this.post_like = post_like;
		this.post_img_path = post_img_path;
		this.post_content_type = post_content_type;
		this.post_content_user_id = post_content_user_id;
		this.post_content_user_name = post_content_user_name;
		this.post_content_user_img_path = post_content_user_img_path;
		this.status = status;
		this.created_at = created_at;
		this.updated_at = updated_at;
	}


	public String getPost_obj_id() {
		return post_obj_id;
	}

	public void setPost_obj_id(String post_obj_id) {
		this.post_obj_id = post_obj_id;
	}

	public String getPost_title() {
		return post_title;
	}

	public void setPost_title(String post_title) {
		this.post_title = post_title;
	}

	public String getPost_content() {
		return post_content;
	}

	public void setPost_content(String post_content) {
		this.post_content = post_content;
	}

	public String getPost_like() {
		return post_like;
	}

	public void setPost_like(String post_like) {
		this.post_like = post_like;
	}

	public String getPost_img_path() {
		return post_img_path;
	}

	public void setPost_img_path(String post_img_path) {
		this.post_img_path = post_img_path;
	}

	public String getPost_content_type() {
		return post_content_type;
	}

	public void setPost_content_type(String post_content_type) {
		this.post_content_type = post_content_type;
	}

	public String getPost_content_user_id() {
		return post_content_user_id;
	}

	public void setPost_content_user_id(String post_content_user_id) {
		this.post_content_user_id = post_content_user_id;
	}

	public String getPost_content_user_name() {
		return post_content_user_name;
	}

	public void setPost_content_user_name(String post_content_user_name) {
		this.post_content_user_name = post_content_user_name;
	}

	public String getPost_content_user_img_path() {
		return post_content_user_img_path;
	}

	public void setPost_content_user_img_path(String post_content_user_img_path) {
		this.post_content_user_img_path = post_content_user_img_path;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public String getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}
}
