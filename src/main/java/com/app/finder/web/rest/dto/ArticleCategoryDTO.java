package com.app.finder.web.rest.dto;

/**
 * 文章分类DTO,创建文章页面使用.
 */
public class ArticleCategoryDTO {

	private Long id;
    // 类别名称
    private String name;
    // 类别分组名称
    private String groupName;
    
	public ArticleCategoryDTO(Long id, String name, String groupName) {
		this.id = id;
		this.name = name;
		this.groupName = groupName;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	@Override
	public String toString() {
		return "ArticleCategoryDTO [id=" + id + ", name=" + name + ", groupName=" + groupName + "]";
	}
}
