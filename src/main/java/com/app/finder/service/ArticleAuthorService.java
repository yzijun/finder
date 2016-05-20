package com.app.finder.service;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.finder.domain.Article;
import com.app.finder.domain.User;
import com.app.finder.repository.ArticleFavoriteRepository;
import com.app.finder.repository.ArticleReplyRepository;
import com.app.finder.repository.ArticleRepository;
import com.app.finder.repository.UserRepository;
import com.app.finder.web.rest.dto.ArticleAuthorDTO;
import com.app.finder.web.rest.dto.HomePageDataDTO;
import com.app.finder.web.rest.enu.AuthorDetailType;

/**
 * 文章作者详细service
 */
@Service
@Transactional
public class ArticleAuthorService {

	private final Logger log = LoggerFactory.getLogger(ArticleAuthorService.class);

	@Inject
	private UserRepository userRepository;

	@Inject
	private ArticleRepository articleRepository;
	
	@Inject
	private ArticleFavoriteRepository articleFavoriteRepository;

	@Inject
	private ArticleReplyRepository articleReplyRepository;

	@PersistenceContext
	private EntityManager entityManager;
	
	@Inject
	private HomeService homeService;

	/**
	 * 取得作者的详细信息
	 * 缓存详细数据
	 * @param id
	 * @return
	 */
	@Cacheable("authorDetail")
	@Transactional(readOnly = true)
	public ArticleAuthorDTO getAuthorDetail(Long id, String detype) {
		log.debug("缓存作者的详细信息 用户ID: {} ,detype: {}", id, detype);
		// 作者
		User user = userRepository.findOneById(id).get();

		if (user == null) {
			return null;
		}
		// 发表文章数
		Integer articleNum = articleRepository.findByCountArticleIsUid(id);
		// 评论数
		Integer commentNum = articleReplyRepository.findByCountArticleReplyUid(id);
		// 收获喜欢数
		Integer favoriteNum = articleFavoriteRepository.getCountArticleFavoriteByUser(id);
		// 文章分页数据  默认第一页显示10条
		Pageable pageable = new PageRequest(0, 10);
		// 取得作者文章类型分页数据
		Page<Article> pageData = getPageDataByType(id, detype, pageable);
		List<HomePageDataDTO> pageDataDTO = homeService.transPageData(pageData);
		return new ArticleAuthorDTO(user, articleNum, commentNum, favoriteNum,
				pageDataDTO, pageData);
	}

	/**
	 * 取得作者的分页数据
	 * 
	 * @param id
	 * @return
	 */
	@Transactional(readOnly = true)
	public ArticleAuthorDTO getAuthorDetailPage(Pageable pageable, Long id, String detype) {
		// 作者
		User user = userRepository.findOneById(id).get();
		if (user == null) {
			return null;
		}
		// 取得作者文章类型分页数据
		Page<Article> pageData = getPageDataByType(id, detype, pageable);
		
		List<HomePageDataDTO> pageDataDTO = homeService.transPageData(pageData);
		return new ArticleAuthorDTO(pageDataDTO, pageData);
	}
	
	/*
	 * 取得作者文章类型分页数据
	 */
	private Page<Article> getPageDataByType(Long id, String detype, Pageable pageable) {
		Page<Article> pageData = null;
		// 字符串转换成枚举对象
		AuthorDetailType aType = Enum.valueOf(AuthorDetailType.class, detype.toUpperCase());
		// 作者文章类型
		switch (aType) {
			case ARTICLE:
				pageData = pageArticleData(pageable, id);
				break;
			case COMMENT:
				pageData = pageReplyData(pageable, id);
				break;
			case FAVORITE:
				pageData = pageFavoriteData(pageable, id);
				break;
		}
		return pageData;
	}
	
	 /*
     * 用户文章分页数据
     */
    @SuppressWarnings("unchecked")
	public Page<Article> pageArticleData(Pageable pageable, Long id) {
        // 使用entityManager查询分页数据
		// 原因是entityManager可以用.createQuery(sql)方法,可以用迫切左外连接的方式取得数据
		Query query = entityManager.createQuery("select a from Article a left join fetch a.user left join fetch a.articleCategory where a.published = ?1 and a.user.id = ?2 order by a.createdDate desc");
		// 设置查询参数(参数索引值从1开始)
		query.setParameter(1, true);
		// 用户ID
		query.setParameter(2, id);
		// 总记录数
		int totalElements = query.getResultList().size();
		log.debug("pageArticleData total: {}", totalElements);
		// 取得分页数据
        List<Article> articles = query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
   	    	 						  .setMaxResults(pageable.getPageSize())
   	    	 						  .getResultList();
		log.debug("get ArticleAuthor pageData size:" + articles.size());
		// 从新构造page对象
		Page<Article> page = new PageImpl<Article>(articles, pageable, totalElements); 
		return page;
	}

    /*
     * 用户收获喜欢的文章分页数据
     */
    @SuppressWarnings("unchecked")
 	private Page<Article> pageFavoriteData(Pageable pageable, Long id) {
		// 原因是entityManager可以用.createQuery(sql)方法,可以用迫切左外连接的方式取得数据
    	// 以ArticleFavorite为主表 查询出article只有一条数据
//		Query query = entityManager.createQuery("select a.article from ArticleFavorite a left join fetch a.user left join fetch a.article where a.article.published = ?1 and a.article.user.id = ?2 order by a.article.createdDate desc");
    	// 以Article为主表  ,a.favorites IS NOT EMPTY 查询出收获喜欢的文章
		Query query = entityManager.createQuery("select distinct a from Article a left join fetch a.favorites where a.published = ?1 and a.user.id = ?2 and a.favorites IS NOT EMPTY order by a.createdDate desc");
		// 设置查询参数(参数索引值从1开始)
		query.setParameter(1, true);
		// 用户ID
		query.setParameter(2, id);
		// 总记录数
		int totalElements = query.getResultList().size();
		log.debug("pageFavoriteData total: {}", totalElements);
		// 取得分页数据
        List<Article> articles = query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
   	    	 						  .setMaxResults(pageable.getPageSize())
   	    	 						  .getResultList();
		log.debug("get pageUserFavorite pageData size:" + articles.size());
		// 从新构造page对象
		Page<Article> page = new PageImpl<Article>(articles, pageable, totalElements); 
		return page;
 	}
    
    
    /*
     * 用户评论的文章分页数据
     */
    @SuppressWarnings("unchecked")
 	private Page<Article> pageReplyData(Pageable pageable, Long id) {
    	// 以Article为主表  ,a.replies IS NOT EMPTY 查询有评论的文章
		Query query = entityManager.createQuery("select distinct a from Article a left join fetch a.replies where a.published = ?1 and a.user.id = ?2 and a.replies IS NOT EMPTY order by a.createdDate desc");
		// 设置查询参数(参数索引值从1开始)
		query.setParameter(1, true);
		// 用户ID
		query.setParameter(2, id);
		// 总记录数
		int totalElements = query.getResultList().size();
		log.debug("pageReplyData total: {}", totalElements);
		// 取得分页数据
        List<Article> articles = query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
   	    	 						  .setMaxResults(pageable.getPageSize())
   	    	 						  .getResultList();
		log.debug("get pageReplyData pageData size:" + articles.size());
		// 从新构造page对象
		Page<Article> page = new PageImpl<Article>(articles, pageable, totalElements); 
		return page;
 	}
}
