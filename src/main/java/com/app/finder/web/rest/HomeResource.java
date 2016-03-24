package com.app.finder.web.rest;

import java.net.URISyntaxException;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.app.finder.security.AuthoritiesConstants;
import com.app.finder.service.HomeService;
import com.app.finder.web.rest.dto.HomeDTO;
import com.app.finder.web.rest.util.PaginationUtil;
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
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(data.getPageData(), "/api/home");
        return new ResponseEntity<>(data, headers, HttpStatus.OK);
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
