package com.app.finder.web.rest.dto;

import com.app.finder.domain.User;

/**
 * 用户收获喜欢数 (收获喜欢数最多的数据)DTO
 *
 */
public class HotFavoriteDTO {

	// 作者收获喜欢数最多
	private User author;

	// 收获喜欢数
	private Integer favoriteNum;
	
	public HotFavoriteDTO(User author, Integer favoriteNum) {
		this.author = author;
		this.favoriteNum = favoriteNum;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public Integer getFavoriteNum() {
		return favoriteNum;
	}

	public void setFavoriteNum(Integer favoriteNum) {
		this.favoriteNum = favoriteNum;
	}

	@Override
	public String toString() {
		return "HotFavoriteDTO [author=" + author + ", favoriteNum=" + favoriteNum + "]";
	}
}
