package com.app.finder.web.rest.dto;

import com.app.finder.domain.Article;

/**
 * 查询文章带有关联关系分页列表数据DTO
 *
 */
public class PageArticleDataDTO {

	private Article article;

	// 文章评论数
	private Integer commentNum;

	// 文章收获喜欢数
	private Integer favoriteNum;
	
	public PageArticleDataDTO(Article article, Integer commentNum, Integer favoriteNum) {
		this.article = article;
		this.commentNum = commentNum;
		this.favoriteNum = favoriteNum;
	}

	public Article getArticle() {
		return article;
	}

	public void setArticle(Article article) {
		this.article = article;
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

}
