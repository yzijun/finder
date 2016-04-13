package com.app.finder.web.rest;

import java.net.URISyntaxException;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.app.finder.domain.Article;
import com.app.finder.security.AuthoritiesConstants;
import com.app.finder.service.HomeService;
import com.app.finder.web.rest.dto.HomeDTO;
import com.app.finder.web.rest.dto.HomePageDataDTO;
import com.codahale.metrics.annotation.Timed;

/**
 * REST controller for managing Home.
 * 首页的Resource
 */
@RestController
@RequestMapping("/api")
public class HomeResource {

    private final Logger log = LoggerFactory.getLogger(HomeResource.class);
        
    @Inject
    private HomeService homeService;
    
    /**
     * 取得首页数据
     * @throws URISyntaxException 
     */
    @RequestMapping(value = "/home/all",
    		method = RequestMethod.GET,
    		produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<HomeDTO> getHomeData() 
    			throws URISyntaxException {
    	log.debug("REST request to get a page of getHomeData");
        HomeDTO data = homeService.findHomeData(); 
        // 不用HttpHeaders传递分页信息,原因是ajax请求数据时HttpHeaders不能更新
//        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(data.getPageData(), "api/home/page");
//        return new ResponseEntity<>(data, headers, HttpStatus.OK);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
    
    /**
     * 取得首页分页数据
     * @throws URISyntaxException 
     */
    @RequestMapping(value = "/home/page",
    		method = RequestMethod.GET,
    		produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<HomeDTO> getHomePageData(Pageable pageable) 
    		throws URISyntaxException {
    	Page<Article> page = homeService.pageArticleData(pageable);
		List<HomePageDataDTO> pageDataDTO = homeService.transPageData(page);
		HomeDTO homeDTO = new HomeDTO(pageDataDTO, page.getNumber(), page.getTotalPages());
        return new ResponseEntity<>(homeDTO, HttpStatus.OK);
    }
    
    /**
     * 移除首页的全部缓存数据,重新取得数据
     * @throws URISyntaxException 
     */
    @RequestMapping(value = "/home/reload",
    		method = RequestMethod.GET,
    		produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<HomeDTO> removeCacheHomeData() 
    			throws URISyntaxException {
    	log.debug("REST request to get a page of removeCacheHomeData");
        HomeDTO data = homeService.removeCacheHome(); 
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
    
    /**
     * 取得登录用户的文章数量
     */
    @RequestMapping(value = "/home/articlesum",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured(AuthoritiesConstants.USER) //用户角色可以访问
    public ResponseEntity<Integer> getArticleSumByUserId() {
    	log.debug("REST request to get getArticleSumByUserId");
        Integer articleSum = homeService.getArticleSumByUserId();
        return new ResponseEntity<>(articleSum, HttpStatus.OK);
    }
    
}
