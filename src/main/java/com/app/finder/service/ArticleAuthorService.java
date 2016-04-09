package com.app.finder.service;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.finder.domain.User;
import com.app.finder.repository.ArticleFavoriteRepository;
import com.app.finder.repository.ArticleReplyRepository;
import com.app.finder.repository.ArticleRepository;
import com.app.finder.repository.UserRepository;
import com.app.finder.web.rest.dto.ArticleAuthorDTO;

/**
 * 文章作者详细service
 */
@Service
@Transactional
public class ArticleAuthorService {

	private final Logger log = LoggerFactory.getLogger(ArticleAuthorService.class);

	@Inject
	private ArticleRepository articleRepository;

	@Inject
	private UserRepository userRepository;

	@Inject
	private ArticleFavoriteRepository articleFavoriteRepository;

	@Inject
	private ArticleReplyRepository articleReplyRepository;

	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * 取得作者的详细信息
	 * 
	 * @param id
	 * @return
	 */
	@Transactional(readOnly = true)
	public ArticleAuthorDTO getAuthorDetail(Long id) {
		log.debug("Service to get getAuthorDetail UserId: {}" + id);
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
		Integer favoriteNum = articleFavoriteRepository.findByCountArticleFavoriteUid(id);
		return new ArticleAuthorDTO(user, articleNum, commentNum, favoriteNum);
	}

}
