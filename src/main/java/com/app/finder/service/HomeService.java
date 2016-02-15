package com.app.finder.service;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.finder.domain.User;
import com.app.finder.repository.ArticleRepository;
import com.app.finder.repository.UserRepository;
import com.app.finder.security.SecurityUtils;

/**
 * Service Implementation for managing Article.
 */
@Service
@Transactional
public class HomeService {

	private final Logger log = LoggerFactory.getLogger(HomeService.class);

	@Inject
	private ArticleRepository articleRepository;

	@Inject
	private UserRepository userRepository;

	/*
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
