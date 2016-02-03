package com.app.finder.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import java.time.ZonedDateTime;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * 文章评论类.
 */
@Entity
@Table(name = "fin_article_reply")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ArticleReply implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //评论内容
    @NotNull
    @Column(name = "content", nullable = false)
    private String content;

    //默认被发布(可能有非法不允许发布)
    @Column(name = "published")
    private boolean published = true;

    @Column(name = "created_date")
    private ZonedDateTime createdDate;

    //属于哪个文章
    @ManyToOne
    @JoinColumn(name = "article_id")
    private Article article;

    //评论人
    @OneToOne()
    @JoinColumn(name = "replyer_id")
    private User replyer;

    //父评论人
    @OneToOne()
    @JoinColumn(name = "parent_replyer_id")
    private User parentReplyer;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean getPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public User getReplyer() {
        return replyer;
    }

    public void setReplyer(User user) {
        this.replyer = user;
    }

    public User getParentReplyer() {
        return parentReplyer;
    }

    public void setParentReplyer(User user) {
        this.parentReplyer = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ArticleReply articleReply = (ArticleReply) o;
        return Objects.equals(id, articleReply.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ArticleReply{" +
            "id=" + id +
            ", content='" + content + "'" +
            ", published='" + published + "'" +
            ", createdDate='" + createdDate + "'" +
            '}';
    }
}
