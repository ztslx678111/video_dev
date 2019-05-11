package cn.hncu.pojo.vo;

public class PublisherVideo {
	public UsersVo publisher;//视频创建者
	public boolean userLikeVideo;//是否喜欢该视频
	public UsersVo getPublisher() {
		return publisher;
	}
	public void setPublisher(UsersVo publisher) {
		this.publisher = publisher;
	}
	public boolean isUserLikeVideo() {
		return userLikeVideo;
	}
	public void setUserLikeVideo(boolean userLikeVideo) {
		this.userLikeVideo = userLikeVideo;
	}
}
