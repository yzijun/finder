package com.app.finder.web.rest.dto;

import java.time.ZonedDateTime;

import com.app.finder.domain.Article;
import com.app.finder.domain.ArticleCategory;
import com.app.finder.domain.User;

/**
 * 首页分页列表数据DTO
 *
 */
public class HomePageDataDTO {

	private Long id;

	// 文章标题
	private String title;

	// 显示在文章前的第一张图
	private byte[] firstImg;

	// 图片类型
	private String firstImgContentType;

	// 文章内容
	private String content;

	// 文章默认被发布(可能有非法不允许发布)
	private boolean published = true;

	// 文章浏览次数
	private Integer pageView;

	private ZonedDateTime createdDate;

	private User user;

	private ArticleCategory articleCategory;

	// 美化时间 12分钟前
	private String prettyTime;

	// 文章评论数
	private Integer commentNum;

	// 文章收藏数
	private Integer favoriteNum;

	public HomePageDataDTO(Article article, String prettyTime, Integer commentNum, Integer favoriteNum) {
		this(article.getId(), article.getTitle(), article.getFirstImg(), article.getFirstImgContentType(),
				article.getContent(), article.isPublished(), article.getPageView(), article.getCreatedDate(),
				article.getUser(), article.getArticleCategory(), prettyTime, commentNum, favoriteNum);
	}

	public HomePageDataDTO(Long id, String title, byte[] firstImg, String firstImgContentType, String content,
			boolean published, Integer pageView, ZonedDateTime createdDate, User user, ArticleCategory articleCategory,
			String prettyTime, Integer commentNum, Integer favoriteNum) {
		this.id = id;
		this.title = title;
		this.firstImg = firstImg;
		this.firstImgContentType = firstImgContentType;
		this.content = content;
		this.published = published;
		this.pageView = pageView;
		this.createdDate = createdDate;
		this.user = user;
		this.articleCategory = articleCategory;
		this.prettyTime = prettyTime;
		this.commentNum = commentNum;
		this.favoriteNum = favoriteNum;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public byte[] getFirstImg() {
		return firstImg;
	}

	public void setFirstImg(byte[] firstImg) {
		this.firstImg = firstImg;
	}

	public String getFirstImgContentType() {
		return firstImgContentType;
	}

	public void setFirstImgContentType(String firstImgContentType) {
		this.firstImgContentType = firstImgContentType;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean isPublished() {
		return published;
	}

	public void setPublished(boolean published) {
		this.published = published;
	}

	public Integer getPageView() {
		return pageView;
	}

	public void setPageView(Integer pageView) {
		this.pageView = pageView;
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

	public ArticleCategory getArticleCategory() {
		return articleCategory;
	}

	public void setArticleCategory(ArticleCategory articleCategory) {
		this.articleCategory = articleCategory;
	}

	public String getPrettyTime() {
		return prettyTime;
	}

	public void setPrettyTime(String prettyTime) {
		this.prettyTime = prettyTime;
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
