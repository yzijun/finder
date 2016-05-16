package com.app.finder.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

import com.app.finder.common.util.PrettyTimeUtils;
import com.app.finder.domain.Article;
import com.app.finder.domain.User;
import com.app.finder.repository.ArticleFavoriteRepository;
import com.app.finder.repository.ArticleReplyRepository;
import com.app.finder.repository.ArticleRepository;
import com.app.finder.repository.UserRepository;
import com.app.finder.security.SecurityUtils;
import com.app.finder.web.rest.dto.HomeDTO;
import com.app.finder.web.rest.dto.HomePageDataDTO;
import com.app.finder.web.rest.dto.HotAuthorDTO;
import com.app.finder.web.rest.dto.HotFavoriteDTO;
import com.app.finder.web.rest.dto.PageArticleDataDTO;

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
	
	@Inject
    private ArticleFavoriteRepository articleFavoriteRepository;
	
	@Inject
    private ArticleReplyRepository articleReplyRepository;

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
		/*
		 *  顶部的图片数据
		 *  方案一
		 */
		/*// 浏览数最多一条数据 
		Article pageViewData = pageViewOne().get(0);
		// 收获喜欢最多的一条文章数据
		Article favoriteData = favoriteOne();
		if (favoriteData == null) {
			favoriteData = pageViewOne().get(1);
		}
		// 评论最多的一条文章数据
		Article commentData = commentOne();
		if (commentData == null) {
			commentData = pageViewOne().get(2);
		}*/
		/*
		 *  顶部的图片数据
		 *  方案二
		 */
		Article pageViewData = topDataPic().get(0);
		Article favoriteData = topDataPic().get(1);
		Article commentData = topDataPic().get(2);
		/*List<SlideDTO> slidesDTOData = new ArrayList<>();
		try {
			slidesDTOData = makePicForSlide(slidesData);
		} catch (IOException e) {}*/
		// 新技术文章数据
//		List<Article> techData = technologies();
		// 文章分页数据  默认第一页显示10条
	    Pageable pageable = new PageRequest(0, 10);
		Page<Article> pageData = pageArticleData(pageable);
		List<HomePageDataDTO> pageDataDTO = transPageData(pageData);
		// 活跃作者(文章数最多)
		List<HotAuthorDTO> authorData = authors();
		// 用户收获喜欢数 
		List<HotFavoriteDTO> favoritesData = favorites();
		// 热门文章(访问最多的数据)
		List<Article> hotData = hotArticles();
		// 热门文章 不显示文章首图图片，不需要传输数据占用带宽
		List<Article> transHotData = new ArrayList<>();
		for (Article article : hotData) {
			Article a = new Article();
			a.setId(article.getId());
			a.setTitle(article.getTitle());
			a.setPageView(article.getPageView());
			transHotData.add(a);
		}
		return new HomeDTO(favoriteData, pageViewData, commentData, authorData, favoritesData, transHotData,
				 pageDataDTO, pageData.getNumber(), pageData.getTotalPages());
	}
	
	 /*
     * 浏览数最多的一条文章数据
     * 取得3条数据的原因是
     * 可能有favoriteOne()和commentOne()取不到数据的情况
     */
    @SuppressWarnings("unchecked")
	private List<Article> pageViewOne() {
        // 使用entityManager查询分页数据
		// 原因是entityManager可以用.createQuery(sql)方法,可以用迫切左外连接的方式取得数据
		Query query = entityManager.createQuery("select a from Article a left join fetch a.user left join fetch a.articleCategory where a.published = ?1 order by a.pageView desc");
		// 设置查询参数(参数索引值从1开始)
		query.setParameter(1, true);
		// 默认显示的数量
        int pageSize = 3;
		// 取得分页数据
        List<Article> article = query.setFirstResult(0)
        					   .setMaxResults(pageSize)
        					   .getResultList();
	 	return article;
	}
    
    /*
     * 首页顶部图片数据
     * 方案二：取得30条数据的打乱顺序后显示前三条数据
     */
    @SuppressWarnings("unchecked")
	private List<Article> topDataPic() {
        // 使用entityManager查询分页数据
		// 原因是entityManager可以用.createQuery(sql)方法,可以用迫切左外连接的方式取得数据
		Query query = entityManager.createQuery("select a from Article a left join fetch a.user left join fetch a.articleCategory where a.published = ?1");
		// 设置查询参数(参数索引值从1开始)
		query.setParameter(1, true);
		// 默认显示的数量
        int pageSize = 30;
		// 取得分页数据
        List<Article> article = query.setFirstResult(0)
        					   .setMaxResults(pageSize)
        					   .getResultList();
        Collections.shuffle(article);
	 	return article;
	}

    // 收获喜欢最多的一条文章数据
    @SuppressWarnings("unchecked")
    private Article favoriteOne() {
    	// createNativeQuery该方法是针对原生SQL语句
    	String sql = "SELECT COUNT(*) AS c,article_id FROM fin_article_favorite GROUP BY article_id ORDER BY c DESC";
    	Query query = entityManager.createNativeQuery(sql);
    	// 默认显示的数量
    	// 取得2个的原因是可能有文章是不可发布状态，可以取得第二条
    	int pageSize = 2;
    	// 取得分页数据
    	List<Object[]> fas = query.setFirstResult(0)
    							  .setMaxResults(pageSize)
    							  .getResultList();
    	log.debug("get home data favorites query favoriteOne list size:" + fas.size());
    	
    	Article article = null;
    	
    	for(Object[] obj: fas) {
    		// 文章ID
    		Long articleId = ((BigInteger) obj[1]).longValue();
    		// 取得文章数据
    		article = articleRepository.findByIdAndPublishedTrue(articleId);
    		if (article != null) {
    			break;
    		}
		}
    	
    	return article;
    }
    
    // 评论最多的一条文章数据
    @SuppressWarnings("unchecked")
    private Article commentOne() {
    	// createNativeQuery该方法是针对原生SQL语句
    	String sql = "SELECT COUNT(*) AS c ,article_id FROM fin_article_reply WHERE published = TRUE GROUP BY article_id ORDER BY c DESC";
    	Query query = entityManager.createNativeQuery(sql);
    	// 默认显示的数量
    	// 取得2个的原因是可能有文章是不可发布状态，可以取得第二条
    	int pageSize = 2;
    	// 取得分页数据
    	List<Object[]> fas = query.setFirstResult(0)
    							  .setMaxResults(pageSize)
    							  .getResultList();
    	log.debug("get home data favorites query commentOne list size:" + fas.size());
    	
    	Article article = null;
    	
    	for(Object[] obj: fas) {
    		// 文章ID
    		Long articleId = ((BigInteger) obj[1]).longValue();
    		// 取得文章数据
    		article = articleRepository.findByIdAndPublishedTrue(articleId);
    		if (article != null) {
    			break;
    		}
		}
    	
    	return article;
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
    
    /**
     * 转换文章分页数据为DTO，页面列表显示用
     */
    public List<HomePageDataDTO> transPageData(Page<Article> page) {
    	List<PageArticleDataDTO> list = new ArrayList<>();
    	for (Article article : page.getContent()) {
    		// 取得 文章评论数和文章收获喜欢数  需要发多条sql
    		Integer commentNum = articleReplyRepository.findByCountArticleUid(article.getId());
    		Integer favoriteNum = articleFavoriteRepository.findByCountArticleFavoriteAid(article.getId());
    		PageArticleDataDTO dto = new PageArticleDataDTO(article, commentNum, favoriteNum);
    		list.add(dto);
		}
    	// 转换DTOList
		List<HomePageDataDTO> pageList = list.stream()
		  .map(s -> new HomePageDataDTO(s.getArticle(), 
				  // result.getCreatedDate().toInstant().toEpochMilli() 取得毫秒
				  PrettyTimeUtils.timeAgo(System.currentTimeMillis() - s.getArticle().getCreatedDate().toInstant().toEpochMilli()),
				  s.getCommentNum(), s.getFavoriteNum()))
		  .collect(Collectors.toList());
		
		return pageList;
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
    
    // 用户收获喜欢数 (收获喜欢数最多的数据)
    @SuppressWarnings("unchecked")
    private List<HotFavoriteDTO> favorites() {
    	// 发送多条取得User的SQL,
    	// 先这样以后再有好的方式在修改
    	// createNativeQuery该方法是针对原生SQL语句
    	String sql = "SELECT SUM(af.num) AS favoriteNum,a.user_id AS uid FROM fin_article a, "
    				+ "(SELECT COUNT(*) AS num,article_id AS aid FROM fin_article_favorite GROUP BY article_id) af "
    				+ "WHERE a.id = af.aid GROUP BY a.user_id ORDER BY favoriteNum DESC";
    	Query query = entityManager.createNativeQuery(sql);
    	// 默认显示的数量
    	int pageSize = 5;
    	// 取得分页数据
    	List<Object[]> fas = query.setFirstResult(0)
    							  .setMaxResults(pageSize)
    							  .getResultList();
    	log.debug("get home data favorites query favoriteNum list size:" + fas.size());
    	
    	List<HotFavoriteDTO> result = new ArrayList<>();
    	for(Object[] obj: fas) {
    		// 收获喜欢数
    		Integer favoriteNum = ((BigDecimal) obj[0]).intValue();
    		// 用户ID
    		Long userId = ((BigInteger) obj[1]).longValue();
    		// 取得对应的作者信息
    		User user = userRepository.findOneById(userId).get();
    		
    		HotFavoriteDTO dto = new HotFavoriteDTO(user, favoriteNum);
    		result.add(dto);
		}
    	log.debug("get home data favorites result:" + result.toString());
    	
    	return result;
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

	/*
	 * 首页加载幻灯片的图片时为了能正常显示，
	 * 需要把文章的firstImg字节生成图片保存到服务器中。
	 * 图片的缩放比例是：宽=830  高=300
	 */
	/*private List<SlideDTO> makePicForSlide(List<Article> slidesData) 
			throws IOException{
		List<SlideDTO> slides = new ArrayList<>();
		
		for (Article article : slidesData) {
			//TODO 正式系统需要改路径
	        String savePicPath = "D:/apachePic/";
	        // 保存幻灯片图片的文件夹
	        String slidesPic = "mainSlidesPic/";
	        // 取得当前日期作为文件夹
	        String day = ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
	        // 取得图片后缀名
	        String picSuffix = article.getFirstImgContentType().split("/")[1];
			// 生成文件名路径
	        StringBuilder sb = new StringBuilder();
	        sb.append(slidesPic)
	          .append(day).append("/")
	          .append(System.currentTimeMillis())
	          .append(".").append(picSuffix);
	        
	        String fileName = sb.toString();
	        //保存图片的完整路径和文件名
	        savePicPath += fileName;
	        
	        File f = new File(savePicPath);
	        //google IO 不存在时创建父文件夹
	        Files.createParentDirs(f);
	        
	        //图片显示URL中的路径
	        //TODO 正式系统需要改URL地址
	        String urlPic = "http://localhost:8089/" + fileName;
	        
	        slides.add(new SlideDTO(article.getId(), article.getTitle(), urlPic));
	        
	    	//保存缩小后的图片
	    	try (BufferedOutputStream bos = new BufferedOutputStream(
	    			new FileOutputStream(savePicPath))) {
	    		 ByteArrayOutputStream out = new ByteArrayOutputStream();
	    	      // 等比例缩放幻灯片图片的大小
	    	     ThumbnailsUtils.resetPicture(830, 300, article.getFirstImg(), out);
				 bos.write(out.toByteArray());
			}
		}
		
		return slides;
	}*/
}
