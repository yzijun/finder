package com.app.finder.web.rest;

import java.net.URISyntaxException;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
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
import com.app.finder.service.ArticleService;
import com.app.finder.web.rest.util.PaginationUtil;
import com.codahale.metrics.annotation.Timed;

/**
 * REST controller for managing Article. 
 * 作者详细页面
 */
@RestController
@RequestMapping("/api")
public class ArticleAuthorResource {

	private final Logger log = LoggerFactory.getLogger(ArticleAuthorResource.class);

	@Inject
	private ArticleService articleService;

	/**
	 * 取得作者详细页面默认初始化数据
	 * 文章、评论、收藏
	 * uid:用户ID
	 */
	@RequestMapping(value = "/articleAuthor/{uid}", 
			method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<List<Article>> getInitArticleAuthors(@PathVariable Long uid)
					throws URISyntaxException {
        log.debug("REST request to get a init of ArticleAuthors");
        Pageable pageable = null;
        Page<Article> page = articleService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/articles");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * 取得作者详细页面分页数据
	 * 
	 * uid:用户ID
	 * type:文章、评论、收藏
	 */
	@RequestMapping(value = "/articleAuthor/{uid}", 
			method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_VALUE, 
			params = {"type"})
	@Timed
	public ResponseEntity<List<Article>> getPageArticleAuthors(Pageable pageable,
			@PathVariable Long uid, @RequestParam(value = "type", required = true) String type)
					throws URISyntaxException {
        log.debug("REST request to get a page of ArticleAuthors");
        Page<Article> page = articleService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/articles");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

}
