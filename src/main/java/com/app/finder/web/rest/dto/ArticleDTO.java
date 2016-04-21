package com.app.finder.web.rest.dto;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;

import com.app.finder.domain.Article;
import com.app.finder.domain.ArticleCategory;
import com.app.finder.domain.Tag;
import com.app.finder.domain.User;

/**
 * 文章类DTO,暂时文章详细页面使用.
 */
public class ArticleDTO {

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

	private Set<Tag> tags = new HashSet<>();

	// 该用户全部文章数
	private Integer countArticleUid;

	// 该用户收获喜欢数
    private Integer countFavorite;

	// 该文章的收藏数
	private Integer countArticleSaveAid;
	
	// 当前登录用户是否收藏过该文章
	private boolean articleFavoriteCurrentUser;
	
	// 当前登录用户是否收藏过该文章的收藏Id
	private Long favoriteId;
	
	// 右边栏 热门文章
	List<Article> hotArticles;
	
	//文章对应的分页评论
	Page<ArticleReplyDTO> articleReplies;

	public ArticleDTO(Article article, Integer countArticleUid, Integer countFavorite,
			Integer countArticleSaveAid, List<Article> hotArticles,
			Page<ArticleReplyDTO> articleReplies, boolean articleFavoriteCurrentUser, Long favoriteId) {
		this(article.getId(), article.getTitle(), article.getFirstImg(),
				article.getFirstImgContentType(), article.getContent(), article.isPublished(),
				article.getPageView(), article.getCreatedDate(), article.getUser(),
				article.getArticleCategory(), article.getTags(), countArticleUid,
				countFavorite, countArticleSaveAid, hotArticles,
				articleReplies, articleFavoriteCurrentUser, favoriteId);
	}

	public ArticleDTO(Long id, String title, byte[] firstImg, String firstImgContentType,
			String content, boolean published, Integer pageView, ZonedDateTime createdDate,
			User user, ArticleCategory articleCategory, Set<Tag> tags, Integer countArticleUid,
			Integer countFavorite, Integer countArticleSaveAid,
			List<Article> hotArticles, 
			Page<ArticleReplyDTO> articleReplies, boolean articleFavoriteCurrentUser,
			Long favoriteId) {
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
		this.tags = tags;
		this.countArticleUid = countArticleUid;
		this.countFavorite = countFavorite;
		this.countArticleSaveAid = countArticleSaveAid;
		this.hotArticles = hotArticles;
		this.articleReplies = articleReplies;
		this.articleFavoriteCurrentUser = articleFavoriteCurrentUser;
		this.favoriteId = favoriteId;
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

	public Set<Tag> getTags() {
		return tags;
	}

	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}

	public Integer getCountArticleUid() {
		return countArticleUid;
	}

	public void setCountArticleUid(Integer countArticleUid) {
		this.countArticleUid = countArticleUid;
	}

	public Integer getCountFavorite() {
		return countFavorite;
	}

	public void setCountFavorite(Integer countFavorite) {
		this.countFavorite = countFavorite;
	}

	public Integer getCountArticleSaveAid() {
		return countArticleSaveAid;
	}

	public void setCountArticleSaveAid(Integer countArticleSaveAid) {
		this.countArticleSaveAid = countArticleSaveAid;
	}

	public List<Article> getHotArticles() {
		return hotArticles;
	}

	public void setHotArticles(List<Article> hotArticles) {
		this.hotArticles = hotArticles;
	}
	
	public Page<ArticleReplyDTO> getArticleReplies() {
		return articleReplies;
	}

	public void setArticleReplies(Page<ArticleReplyDTO> articleReplies) {
		this.articleReplies = articleReplies;
	}
	
	public boolean isArticleFavoriteCurrentUser() {
		return articleFavoriteCurrentUser;
	}

	public void setArticleFavoriteCurrentUser(boolean articleFavoriteCurrentUser) {
		this.articleFavoriteCurrentUser = articleFavoriteCurrentUser;
	}
	
	public Long getFavoriteId() {
		return favoriteId;
	}

	public void setFavoriteId(Long favoriteId) {
		this.favoriteId = favoriteId;
	}

	@Override
	public String toString() {
		return "Article{" + "id=" + id + ", title='" + title + "'" + ", firstImg='" + firstImg + "'"
				+ ", firstImgContentType='" + firstImgContentType + "'" + ", content='" + content
				+ "'" + ", published='" + published + "'" + ", pageView='" + pageView + "'"
				+ ", createdDate='" + createdDate + "'" + '}';
	}
}
