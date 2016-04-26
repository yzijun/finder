package com.app.finder.service;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	private ArticleFavoriteRepository articleFavoriteRepository;

	@Inject
	private ArticleReplyRepository articleReplyRepository;

	@PersistenceContext
	private EntityManager entityManager;
	
	@Inject
	private HomeService homeService;

	/**
	 * 取得作者的详细信息
	 * 
	 * @param id
	 * @return
	 */
	@Transactional(readOnly = true)
	public ArticleAuthorDTO getAuthorDetail(Long id) {
		log.debug("Service to get getAuthorDetail UserId: {}",id);
		// 作者
		User user = userRepository.findOneById(id).get();

		if (user == null) {
			return null;
		}
		// 发表文章数 不需要在查询pageData就可以取得
//		Integer articleNum = articleRepository.findByCountArticleIsUid(id);
		// 文章分页数据  默认第一页显示10条
		Pageable pageable = new PageRequest(0, 10);
		Page<Article> pageData = pageArticleData(pageable, id);
		List<HomePageDataDTO> pageDataDTO = homeService.transPageData(pageData);
		// 评论数
		Integer commentNum = articleReplyRepository.findByCountArticleReplyUid(id);
		// 收获喜欢数
		Integer favoriteNum = articleFavoriteRepository.getCountArticleFavoriteByUser(id);
		return new ArticleAuthorDTO(user, commentNum, favoriteNum,
									pageDataDTO, pageData);
	}
	
	/**
	 * 取得作者的详细信息
	 * 
	 * @param id
	 * @return
	 */
	@Transactional(readOnly = true)
	public ArticleAuthorDTO getAuthorDetailPage(Pageable pageable, Long id, String detype) {
		log.debug("Service to get getAuthorDetailPage UserId: {} detype: {}", id, detype);
		// 作者
		User user = userRepository.findOneById(id).get();
		if (user == null) {
			return null;
		}
		// 字符串转换成枚举对象
		AuthorDetailType aType = Enum.valueOf(AuthorDetailType.class, detype.toUpperCase());
		List<HomePageDataDTO> pageDataDTO = null;
		Page<Article> pageData = null;
		// 作者文章类型
		switch (aType) {
			case ARTICLE:
				// 文章分页数据  默认第一页显示10条
				pageData = pageArticleData(pageable, id);
				pageDataDTO = homeService.transPageData(pageData);
				break;
			case COMMENT:
				
				break;
			case FAVORITE:
				
				break;
		}
		
		return new ArticleAuthorDTO(pageDataDTO, pageData);
	}
	
	 /*
     * 文章分页数据
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
		// 取得分页数据
        List<Article> articles = query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
   	    	 						  .setMaxResults(pageable.getPageSize())
   	    	 						  .getResultList();
		log.debug("get ArticleAuthor pageData size:" + articles.size());
		// 从新构造page对象
		Page<Article> page = new PageImpl<Article>(articles, pageable, totalElements); 
		return page;
	}

}
