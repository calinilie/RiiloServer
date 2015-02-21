package com.prototype.bll.entities;

public class DistanceFromPost{
	
	private double distance;
	private Post post;
	
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	public Post getPost() {
		return post;
	}
	public void setPost(Post post) {
		this.post = post;
	}
	
	@Override
	public String toString() {
		return String.format("d: %s \t pId: %s \r\n", this.distance+"", this.post.getPostId()+"");
	}
}