package com.app.finder.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import java.time.ZonedDateTime;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * 文章类.
 */
@Entity
@Table(name = "fin_article")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "article")
public class Article implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //文章标题
    @NotNull
    @Size(max = 60)
    @Column(name = "title", length = 60, nullable = false)
    private String title;

    //显示在文章前的第一张图
    @NotNull
    @Lob
    @Column(name = "first_img", nullable = false)
    private byte[] firstImg;

    //图片类型
    @Column(name = "first_img_content_type", nullable = false)        
    private String firstImgContentType;
    
    //文章内容
    @Column(name = "content", length = 1000)
    private String content;
    
    //文章默认被发布(可能有非法不允许发布)
    @Column(nullable = false)
    private boolean published = true;
    
    //文章浏览次数
    @Column(name = "page_view")
    private Integer pageView;
    
    // 保存缩小生成第一张图片的URL
    // 压缩传输图片数据  首页显示列表图片数据用
    @Column(name = "minImgURL", length = 300)
    private String minImgURL;

    @Column(name = "created_date")
    private ZonedDateTime createdDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "article_category_id")
    private ArticleCategory articleCategory;

    @ManyToMany(fetch = FetchType.LAZY)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "fin_article_tag",
    joinColumns = @JoinColumn(name="articles_id", referencedColumnName="ID"),
    inverseJoinColumns = @JoinColumn(name="tags_id", referencedColumnName="ID"))
    private Set<Tag> tags = new HashSet<>();

    
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

    public String getMinImgURL() {
		return minImgURL;
	}

	public void setMinImgURL(String minImgURL) {
		this.minImgURL = minImgURL;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Article article = (Article) o;
        return Objects.equals(id, article.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Article{" +
            "id=" + id +
            ", title='" + title + "'" +
            ", firstImg='" + firstImg + "'" +
            ", firstImgContentType='" + firstImgContentType + "'" +
            ", content='" + content + "'" +
            ", published='" + published + "'" +
            ", pageView='" + pageView + "'" +
            ", createdDate='" + createdDate + "'" +
            ", minImgURL='" + minImgURL + "'" +
            '}';
    }
}
