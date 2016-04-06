package com.app.finder.web.rest.dto;

import java.util.List;

import org.springframework.data.domain.Page;

import com.app.finder.domain.Article;

/**
 * 首页数据DTO
 *
 */
public class HomeDTO {

	// 浏览数最多数据
	private List<Article> pageViewData;
	
	// 新技术文章数据
	private List<Article> techData;

	// 列表分页数据
	private Page<Article> pageData;
	
	// 列表分页数据DTO数据显示用
	List<HomePageDataDTO> pageDataDTO;

	// 活跃作者(文章数最多)
	private List<HotAuthorDTO> authors;

	// 热门文章(访问最多的数据)
	private List<Article> hotArticles;
	
	public HomeDTO(List<Article> pageViewData, List<Article> techData, Page<Article> pageData,
			List<HotAuthorDTO> authors, List<Article> hotArticles, List<HomePageDataDTO> pageDataDTO) {
		this.pageViewData = pageViewData;
		this.techData = techData;
		this.pageData = pageData;
		this.authors = authors;
		this.hotArticles = hotArticles;
		this.pageDataDTO = pageDataDTO;
	}

	public List<Article> getPageViewData() {
		return pageViewData;
	}

	public void setPageViewData(List<Article> pageViewData) {
		this.pageViewData = pageViewData;
	}

	public List<Article> getTechData() {
		return techData;
	}

	public void setTechData(List<Article> techData) {
		this.techData = techData;
	}

	public Page<Article> getPageData() {
		return pageData;
	}

	public void setPageData(Page<Article> pageData) {
		this.pageData = pageData;
	}

	public List<HotAuthorDTO> getAuthors() {
		return authors;
	}

	public void setAuthors(List<HotAuthorDTO> authors) {
		this.authors = authors;
	}

	public List<Article> getHotArticles() {
		return hotArticles;
	}

	public void setHotArticles(List<Article> hotArticles) {
		this.hotArticles = hotArticles;
	}

	public List<HomePageDataDTO> getPageDataDTO() {
		return pageDataDTO;
	}

	public void setPageDataDTO(List<HomePageDataDTO> pageDataDTO) {
		this.pageDataDTO = pageDataDTO;
	}
	
}
