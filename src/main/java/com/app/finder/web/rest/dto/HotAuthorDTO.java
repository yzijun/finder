package com.app.finder.web.rest.dto;

import com.app.finder.domain.User;

/**
 * 活跃作者(文章数最多)数据DTO
 *
 */
public class HotAuthorDTO {

	// 活跃作者(文章数最多)
	private User author;

	// 文章数量
	private Integer articleNum;

	public HotAuthorDTO(User author, Integer articleNum) {
		this.author = author;
		this.articleNum = articleNum;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public Integer getArticleNum() {
		return articleNum;
	}

	public void setArticleNum(Integer articleNum) {
		this.articleNum = articleNum;
	}

	@Override
	public String toString() {
		return "HotAuthorDTO [author=" + author + ", articleNum=" + articleNum + "]";
	}

}
