package com.app.finder.web.rest.dto;

import com.app.finder.domain.User;

/*
 * 文章作者详细DTO
 */
public class ArticleAuthorDTO {
	// 作者
	private User user;
	// 发表文章数
	private Integer articleNum;
	// 评论数
	private Integer commentNum;
	// 收获喜欢数
	private Integer favoriteNum;
	
	public ArticleAuthorDTO(User user, Integer articleNum, Integer commentNum, Integer favoriteNum) {
		this.user = user;
		this.articleNum = articleNum;
		this.commentNum = commentNum;
		this.favoriteNum = favoriteNum;
	}
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Integer getArticleNum() {
		return articleNum;
	}
	public void setArticleNum(Integer articleNum) {
		this.articleNum = articleNum;
	}
	public Integer getCommentNum() {
		return commentNum;
	}
	public void setCommentNum(Integer commentNum) {
		this.commentNum = commentNum;
	}
	public Integer getFavoriteNum() {
		return favoriteNum;
	}
	public void setFavoriteNum(Integer favoriteNum) {
		this.favoriteNum = favoriteNum;
	}
	
	@Override
	public String toString() {
		return "ArticleAuthorDetailDTO [user=" + user + ", articleNum=" + articleNum + ", commentNum=" + commentNum
				+ ", favoriteNum=" + favoriteNum + "]";
	}
	
}
