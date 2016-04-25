package com.app.finder.web.rest.enu;

/**
 * 作者详细页面 取得数据
 * 类型有文章、评论、收获喜欢的文章
 */
public enum AuthorDetailType {
	ARTICLE("文章"), COMMENT("评论"), FAVORITE("收获喜欢");

	// 描述
	private String desc;

	private AuthorDetailType(String desc) {
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}
