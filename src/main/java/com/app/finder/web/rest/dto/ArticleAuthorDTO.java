package com.app.finder.web.rest.dto;

import java.util.List;

import org.springframework.data.domain.Page;

import com.app.finder.domain.Article;
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
	// 默认 文章分页数据DTO
	private List<HomePageDataDTO> pageDataDTO;
	// 文章分页数据
	private Page<Article> pageData;
	
	public ArticleAuthorDTO(User user, Integer articleNum, Integer commentNum, Integer favoriteNum,
							List<HomePageDataDTO> pageDataDTO, Page<Article> pageData) {
		this.user = user;
		this.articleNum = articleNum;
		this.commentNum = commentNum;
		this.favoriteNum = favoriteNum;
		this.pageDataDTO = pageDataDTO;
		this.pageData = pageData;
	}
	
	public ArticleAuthorDTO(List<HomePageDataDTO> pageDataDTO, Page<Article> pageData) {
		this.pageDataDTO = pageDataDTO;
		this.pageData = pageData;
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
	
	public List<HomePageDataDTO> getPageDataDTO() {
		return pageDataDTO;
	}

	public void setPageDataDTO(List<HomePageDataDTO> pageDataDTO) {
		this.pageDataDTO = pageDataDTO;
	}

	public Page<Article> getPageData() {
		return pageData;
	}

	public void setPageData(Page<Article> pageData) {
		this.pageData = pageData;
	}

	@Override
	public String toString() {
		return "ArticleAuthorDetailDTO [user=" + user + ", articleNum=" + articleNum + ", commentNum=" + commentNum
				+ ", favoriteNum=" + favoriteNum + "]";
	}
	
}
