package com.ly.pojo.vo;

public class PublisherVideo {
    
	public UsersVO publisher;
	public boolean userLikeVideo;
	public com.ly.pojo.vo.UsersVO getPublisher() {
		return publisher;
	}
	public void setPublisher(com.ly.pojo.vo.UsersVO publisher) {
		this.publisher = publisher;
	}
	public boolean isUserLikeVideo() {
		return userLikeVideo;
	}
	public void setUserLikeVideo(boolean userLikeVideo) {
		this.userLikeVideo = userLikeVideo;
	}
}