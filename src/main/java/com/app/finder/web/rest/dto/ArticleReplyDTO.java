package com.app.finder.web.rest.dto;

import java.time.ZonedDateTime;

import com.app.finder.domain.ArticleReply;
import com.app.finder.domain.User;

/**
 * 文章评论DTO
 *
 */
public class ArticleReplyDTO {

	private Long id;

	// 评论内容
	private String content;

	// 新建时间
	private ZonedDateTime createdDate;

	// 美化时间 12分钟前
	private String prettyTime;

	private User replyer;

	public ArticleReplyDTO(ArticleReply articleReply, String prettyTime) {
		this(articleReply.getId(), articleReply.getContent(), articleReply.getCreatedDate(), articleReply.getReplyer(),
				prettyTime);
	}

	public ArticleReplyDTO(Long id, String content, ZonedDateTime createdDate, User replyer, String prettyTime) {
		this.id = id;
		this.content = content;
		this.createdDate = createdDate;
		this.replyer = replyer;
		this.prettyTime = prettyTime;
	}

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

	public ZonedDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(ZonedDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public String getPrettyTime() {
		return prettyTime;
	}

	public void setPrettyTime(String prettyTime) {
		this.prettyTime = prettyTime;
	}

	public User getReplyer() {
		return replyer;
	}

	public void setReplyer(User replyer) {
		this.replyer = replyer;
	}

	@Override
	public String toString() {
		return "ArticleReply{" + "id=" + id + ", content='" + content + "'" + ", prettyTime='" + prettyTime + "'"
				+ ", createdDate='" + createdDate + "'" + '}';
	}

}
