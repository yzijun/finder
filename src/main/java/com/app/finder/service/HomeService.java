package com.app.finder.service;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.finder.domain.Article;
import com.app.finder.domain.User;
import com.app.finder.repository.ArticleRepository;
import com.app.finder.repository.UserRepository;
import com.app.finder.security.SecurityUtils;
import com.app.finder.web.rest.dto.HomeDTO;
import com.app.finder.web.rest.dto.HotAuthorDTO;

/**
 * 首页service
 */
@Service
@Transactional
public class HomeService {

	private final Logger log = LoggerFactory.getLogger(HomeService.class);

	@Inject
	private ArticleRepository articleRepository;

	@Inject
	private UserRepository userRepository;

	@PersistenceContext
	private EntityManager entityManager;
	
	/**
	 * 取得首页全部数据
	 * @return
	 */
	@Cacheable("homeData")
	public HomeDTO findHomeData() {
		return getAllData();
	}
	
	/**
	 * 移除首页的全部缓存数据
	 * @CachePut 注释
	 * 这个注释可以确保方法被执行，
	 * 同时方法的返回值也被记录到缓存中，
	 * 实现缓存与数据库的同步更新。
	 */
	@CachePut("homeData")
	public HomeDTO removeCacheHome() {
		return getAllData();
	}
	
	// 取得首页全部数据
	private HomeDTO getAllData() {
		// 幻灯片数据 
		List<Article> sliderData = slides();
		// 创意数据
		List<Article> oriData = originalities();
		// 文章分页数据  默认第一页显示10条
	    Pageable pageable = new PageRequest(0, 10);
		Page<Article> pageData = pageArticleData(pageable);
		// 活跃作者(文章数最多)
		List<HotAuthorDTO> authorData = authors();
		// 热门文章(访问最多的数据)
		List<Article> hotData = hotArticles();
		return new HomeDTO(sliderData, oriData, pageData, authorData, hotData);
	}
	
	 /*
     * 幻灯片文章数据
     */
    @SuppressWarnings("unchecked")
	private List<Article> slides() {
        // 使用entityManager查询分页数据
		// 原因是entityManager可以用.createQuery(sql)方法,可以用迫切左外连接的方式取得数据
		Query query = entityManager.createQuery("select a from Article a left join fetch a.user left join fetch a.articleCategory where a.published = ?1 order by a.pageView desc");
		// 设置查询参数(参数索引值从1开始)
		query.setParameter(1, true);
		// 默认显示的数量
        int pageSize = 3;
		// 取得分页数据
        List<Article> articles = query.setFirstResult(0)
        							  .setMaxResults(pageSize)
        							  .getResultList();
		log.debug("get home data slides size:" + articles.size());
	 	return articles;
	}
    
    /*
     * 创意数据文章数据
     */
    @SuppressWarnings("unchecked")
	private List<Article> originalities() {
    	// 使用entityManager查询分页数据
		// 原因是entityManager可以用.createQuery(sql)方法,可以用迫切左外连接的方式取得数据
		Query query = entityManager.createQuery("select a from Article a left join fetch a.user left join fetch a.articleCategory where a.articleCategory.id = ?1 and a.published = ?2 order by a.pageView desc");
		// 设置查询参数(参数索引值从1开始)
		query.setParameter(1, Long.valueOf(3));
		query.setParameter(2, true);
		// 默认显示的数量
    	int pageSize = 2;
		// 取得分页数据
		List<Article> articles = query.setFirstResult(0)
									  .setMaxResults(pageSize)
									  .getResultList();
		log.debug("get home data originality size:" + articles.size());
    	return articles;
    }
    
    /*
     * 文章分页数据
     */
    @SuppressWarnings("unchecked")
	public Page<Article> pageArticleData(Pageable pageable) {
        // 使用entityManager查询分页数据
		// 原因是entityManager可以用.createQuery(sql)方法,可以用迫切左外连接的方式取得数据
		Query query = entityManager.createQuery("select a from Article a left join fetch a.user left join fetch a.articleCategory where a.published = ?1 order by a.createdDate desc");
		// 设置查询参数(参数索引值从1开始)
		query.setParameter(1, true);
		// 总记录数
		int totalElements = query.getResultList().size();
		// 取得分页数据
        List<Article> articles = query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
   	    	 						  .setMaxResults(pageable.getPageSize())
   	    	 						  .getResultList();
		log.debug("get home data pageData size:" + articles.size());
		// 从新构造page对象
		Page<Article> page = new PageImpl<Article>(articles, pageable, totalElements); 
		return page;
	}
	
    // 活跃作者(文章数最多)
    @SuppressWarnings("unchecked")
 	private List<HotAuthorDTO> authors() {
		// 用迫切左外连接取得User会出错,
    	// 不用迫切左外连会发送多条取得User的SQL,
    	// 先这样以后再有好的方式在修改
		Query query = entityManager.createQuery("select new com.app.finder.web.rest.dto.HotAuthorDTO(a.user,count(*) as num) from Article a group by a.user.id order by num desc");
		// 默认显示的数量
    	int pageSize = 5;
		// 取得分页数据
		List<HotAuthorDTO> hotAuthors = query.setFirstResult(0)
									  	     .setMaxResults(pageSize)
									         .getResultList();
		log.debug("get home data authors:" + hotAuthors);
    	return hotAuthors;
 	}
    
    // 热门文章(访问最多的数据)
    @SuppressWarnings("unchecked")
 	private List<Article> hotArticles() {
    	// 原因是entityManager可以用.createQuery(sql)方法,可以用迫切左外连接的方式取得数据
		Query query = entityManager.createQuery("select a from Article a where a.published = ?1 order by a.pageView desc");
		// 设置查询参数(参数索引值从1开始)
		query.setParameter(1, true);
		// 默认显示的数量
    	int pageSize = 5;
		// 取得分页数据
		List<Article> articles = query.setFirstResult(0)
									  .setMaxResults(pageSize)
									  .getResultList();
		log.debug("get home data hotArticles size:" + articles.size());
    	return articles;
    }
    
	/**
	 * 取得登录用户的文章数量
	 */
	public Integer getArticleSumByUserId() {
		// 查找登录User
		User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();

		// 取得该用户全部文章数
		Integer articleSum = articleRepository.findByCountArticleIsUid(user.getId());
		// 没有文章的情况
		if (articleSum == null) {
			return 0;
		}
		return articleSum;
	}

}
