package com.app.finder.web.rest.dto;

/**
 * 幻灯片数据 DTO
 *
 */
public class SlideDTO {
	
	// 文章ID
	private Long id;

	// 文章标题
	private String title;
	
	// 幻灯片图片路径
	private String urlPic;
	
	public SlideDTO(Long id, String title, String urlPic) {
		this.id = id;
		this.title = title;
		this.urlPic = urlPic;
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

	public String getUrlPic() {
		return urlPic;
	}

	public void setUrlPic(String urlPic) {
		this.urlPic = urlPic;
	}

}
