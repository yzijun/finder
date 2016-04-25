package com.app.finder.web.rest;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.finder.domain.Article;
import com.app.finder.service.ArticleAuthorService;
import com.app.finder.service.ArticleService;
import com.app.finder.web.rest.dto.ArticleAuthorDTO;
import com.app.finder.web.rest.dto.HomePageDataDTO;
import com.app.finder.web.rest.util.PaginationUtil;
import com.codahale.metrics.annotation.Timed;

/**
 * REST controller for managing ArticleAuthorResource. 
 * 文章作者详细
 */
@RestController
@RequestMapping("/api")
public class ArticleAuthorResource {

	private final Logger log = LoggerFactory.getLogger(ArticleAuthorResource.class);

	@Inject
    private ArticleAuthorService articleAuthorService;
	
	@Inject
	private ArticleService articleService;
	
	/**
	 * 取得作者详细页面默认初始化数据
	 * 文章、评论、收藏
	 * id:用户ID
	 */
    @RequestMapping(value = "/author/detail/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ArticleAuthorDTO> getArticleAuthorDetail(@PathVariable Long id) {
        ArticleAuthorDTO articleAuthorDetail = articleAuthorService.getAuthorDetail(id);
        return Optional.ofNullable(articleAuthorDetail)
                .map(result -> new ResponseEntity<>(
                    result,
                    HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

	/**
	 * 取得作者详细页面分页数据
	 * 
	 * uid:用户ID
	 * detype:文章、评论、收获喜欢
	 */
	@RequestMapping(value = "/author/detailpage/{uid}", 
			method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_VALUE, 
			params = {"detype"})
	@Timed
	public ResponseEntity<ArticleAuthorDTO> getPageArticleAuthors(
			@RequestParam(required = true) Integer page, 
			@RequestParam(required = true) Integer size,
			@PathVariable Long uid, 
			@RequestParam(value = "detype", required = true) String detype) {
		
		Pageable pageable = new PageRequest(page, size);
		ArticleAuthorDTO aDTO = articleAuthorService.getAuthorDetailPage(pageable, uid, detype);
        return Optional.ofNullable(aDTO)
        	           .map(result -> new ResponseEntity<>(
        	        		   result,
        	        		   HttpStatus.OK))
                       .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

}
