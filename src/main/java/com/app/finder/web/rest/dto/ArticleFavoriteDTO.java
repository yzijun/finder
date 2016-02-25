package com.app.finder.web.rest.dto;

import java.time.ZonedDateTime;

import com.app.finder.domain.Article;
import com.app.finder.domain.ArticleFavorite;
import com.app.finder.domain.User;

/**
 * 文章收藏DTO
 *
 */
public class ArticleFavoriteDTO {

	private Long id;

	private ZonedDateTime createdDate;

	// 所属用户
	private User user;

	// 所属文章
	private Article article;

	// 文章的收藏数
	private Integer countArticleSaveAid;

	public ArticleFavoriteDTO(ArticleFavorite articleFavorite, Integer countArticleSaveAid) {
		this(articleFavorite.getId(), articleFavorite.getCreatedDate(), articleFavorite.getUser(),
				articleFavorite.getArticle(), countArticleSaveAid);
	}

	public ArticleFavoriteDTO(Long id, ZonedDateTime createdDate, User user, Article article,
			Integer countArticleSaveAid) {
		this.id = id;
		this.createdDate = createdDate;
		this.user = user;
		this.article = article;
		this.countArticleSaveAid = countArticleSaveAid;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ZonedDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(ZonedDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Article getArticle() {
		return article;
	}

	public void setArticle(Article article) {
		this.article = article;
	}

	public Integer getCountArticleSaveAid() {
		return countArticleSaveAid;
	}

	public void setCountArticleSaveAid(Integer countArticleSaveAid) {
		this.countArticleSaveAid = countArticleSaveAid;
	}

	@Override
	public String toString() {
		return "ArticleFavoriteDTO{" + "id=" + id + ", createdDate='" + createdDate + "'" + ", countArticleSaveAid='"
				+ countArticleSaveAid + "'" + '}';
	}
}
