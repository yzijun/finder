package com.app.finder.service;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.spring4.SpringTemplateEngine;

import com.app.finder.common.util.PrettyTimeUtils;
import com.app.finder.common.util.ThumbnailsUtils;
import com.app.finder.domain.Article;
import com.app.finder.domain.ArticleFavorite;
import com.app.finder.domain.ArticleReply;
import com.app.finder.domain.Tag;
import com.app.finder.domain.User;
import com.app.finder.repository.ArticleFavoriteRepository;
import com.app.finder.repository.ArticleReplyRepository;
import com.app.finder.repository.ArticleRepository;
import com.app.finder.repository.ForbiddenWordRepository;
import com.app.finder.repository.TagRepository;
import com.app.finder.repository.UserRepository;
import com.app.finder.repository.search.ArticleSearchRepository;
import com.app.finder.security.SecurityUtils;
import com.app.finder.web.rest.dto.ArticleDTO;
import com.app.finder.web.rest.dto.ArticleReplyDTO;
import com.google.common.io.Files;

/**
 * Service Implementation for managing Article.
 */
@Service
@Transactional
public class ArticleService {

    private final Logger log = LoggerFactory.getLogger(ArticleService.class);
    
    @Inject
    private ArticleRepository articleRepository;
    
    @Inject
    private ArticleSearchRepository articleSearchRepository;
    
    @Inject
    private UserRepository userRepository;
    
    @Inject
    private ForbiddenWordRepository forbiddenWordRepository;
    
    @Inject
    private TagRepository tagRepository; 
    
    @Inject
    private SpringTemplateEngine templateEngine;
    
    @Inject
    private ArticleReplyRepository articleReplyRepository;
    
    @Inject
    private ArticleFavoriteRepository articleFavoriteRepository;
    
	@PersistenceContext
	private EntityManager entityManager;
	
	// 保存文章内容的图片路径
	@Value("${apacheset.articleContent.picturePath}")
	private String savePicPath;
	
	// 文章内容图片的apache显示URL
	@Value("${apacheset.articleContent.contentPicURL}")
	private String contentPicURL;
	
    /**
     * Save a article.
     * @return the persisted entity
     * @throws IOException 
     */
    public Article save(Article article) throws IOException {
        log.debug("Request to save Article : {}", article);
        
        //设定默认值
        article.setPageView(0);
        article.setCreatedDate(ZonedDateTime.now());
        //页面参数传不过来,重新查找User
       /* User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();
        User u = new User();
        u.setId(user.getId());
        article.setUser(user);*/
        // userId可以从页面参数传过来了
        
        //注释服务端缩小上传图片的代码，改为localResizeIMG客户端浏览器缩小图片
        /*//取得上传图片的宽和高
        InputStream is = new ByteArrayInputStream(article.getFirstImg());
        int[] widthAndHeight = ThumbnailsUtils.getWidthAndHeight(is);
        int picWidth = widthAndHeight[0];
        int picHeight = widthAndHeight[1];
        //根据上传图片的宽高决定是否要缩放图片
        int width = 800;
        int height = 400;
        
         * 重新缩放上传图片的情况
         * 1.若图片宽比800大，高比400大，图片按比例缩小，宽为800或高为400
         * 2.若图片宽比800大，高比400小，宽缩小到800，图片比例不变 
         
        if ((picWidth > width && picHeight > height)
        		|| (picWidth > width && picHeight < height)) {
        	//重新缩放上传图片的大小 800 * 400
        	ByteArrayOutputStream out = new ByteArrayOutputStream();
        	ThumbnailsUtils.resetPicture(width, height, article.getFirstImg(), out);
        	article.setFirstImg(out.toByteArray());
        } 
        
         * 这两种方式不做处理保持图片原来大小
         * 1.若图片横比800小，高比400小
         * 2.若图片横比800小，高比400大
         
        //保存图片的路径
        List<String> urlPics = new ArrayList<>();
        //文章内容中有上传图片时，需要缩放图片的大小
        //正则查找base64编码后的字符串(.+)贪婪模式(.+?)非贪婪模式
        Pattern p = Pattern.compile("<img src=\"data:image/(.+?);base64,(.+?)\"");
        Matcher m = p.matcher(article.getContent());
        while (m.find()) {
        	//取得图片类型(后缀名)
        	String picType = m.group(1);
        	//取得图片base64编码后的字符串
        	String picEncode = m.group(2);
        	//Base64解码为字节数组
        	byte[] decode = Base64.getDecoder().decode(picEncode);
	    	is = new ByteArrayInputStream(decode);
	        widthAndHeight = ThumbnailsUtils.getWidthAndHeight(is);
	        picWidth = widthAndHeight[0];
	        picHeight = widthAndHeight[1];
	        // 正式系统需要改路径
	        String savePicPath = "D:/apachePic/";
	        //取得当前日期作为文件夹
	        String day = ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
			
	        String fileName = day + "/" + System.currentTimeMillis() + "." + picType;
	        //保存图片的完整路径和文件名
	        savePicPath += fileName;
	        
	        File f = new File(savePicPath);
	        //google IO 不存在时创建父文件夹
	        Files.createParentDirs(f);
	        
	        //图片显示URL中的路径
	        // 正式系统需要改URL地址
	        urlPics.add("http://localhost:8089/" + fileName);
	        
			if ((picWidth > width && picHeight > height)
            		|| (picWidth > width && picHeight < height)) {
            	//保存缩小后的图片
            	ByteArrayOutputStream out = new ByteArrayOutputStream();
            	ThumbnailsUtils.resetPicture(width, height, decode, out);
            	try (BufferedOutputStream bos = new BufferedOutputStream(
            			new FileOutputStream(savePicPath))) {
    				bos.write(out.toByteArray());
    			}
            } else {
            	//保存原大小图片
            	try (BufferedOutputStream bos = new BufferedOutputStream(
            			new FileOutputStream(savePicPath))) {
    				bos.write(decode);
    			}
            }
        }*/
        // 保存缩小生成文章内容前的图片和URL
        if (article.getFirstImg() != null) {
	        //取得当前日期作为文件夹
	        String day = ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
			// imgType:image/png
	        String fileName = day + "/" + System.currentTimeMillis() + "." + article.getFirstImgContentType().split("/")[1];
        	// minfirstpic显示在文章前的图片文件夹
	        String firstPicFolder = "minfirstimg/";
        	String minFirstPicPath = savePicPath + firstPicFolder;
        	//保存图片的完整路径和文件名
        	minFirstPicPath += fileName;
        	
        	File f = new File(minFirstPicPath);
 	        //google IO 不存在时创建父文件夹
 	        Files.createParentDirs(f);
 	        
        	//重新缩小上传图片的大小 220 * 124
        	ByteArrayOutputStream out = new ByteArrayOutputStream();
        	ThumbnailsUtils.resetPicture(220, 124, article.getFirstImg(), out);
        	//保存缩小后的图片
        	try (BufferedOutputStream bos = new BufferedOutputStream(
        			new FileOutputStream(minFirstPicPath))) {
        		bos.write(out.toByteArray());
        		// 文章第一张图片的apache显示URL
        		article.setMinImgURL(contentPicURL + firstPicFolder + fileName);
        	}
        }
    	
        //保存图片的路径
        List<String> urlPics = new ArrayList<>();
        //文章内容中有上传图片时，需要缩放图片的大小
        //正则查找base64编码后的字符串(.+)贪婪模式(.+?)非贪婪模式
        Pattern p = Pattern.compile("<img src=\"data:image/(.+?);base64,(.+?)\"");
        Matcher m = p.matcher(article.getContent());
        while (m.find()) {
        	//取得图片类型(后缀名)
        	String picType = m.group(1);
        	//取得图片base64编码后的字符串
        	String picEncode = m.group(2);
        	//Base64解码为字节数组
        	byte[] decode = Base64.getDecoder().decode(picEncode);

	        //取得当前日期作为文件夹
	        String day = ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
			
	        String fileName = day + "/" + System.currentTimeMillis() + "." + picType;
	        
	        // 临时保存图片路径，用savePicPath循环后会有字符串累加的情况导致图片路径不正确
	        // contentpic文章内容图片文件夹
	        String contentPicFolder = "contentimg/";
	        String tempPicPath = savePicPath + contentPicFolder;
	        //保存图片的完整路径和文件名
	        tempPicPath += fileName;
	        
	        File f = new File(tempPicPath);
	        //google IO 不存在时创建父文件夹
	        Files.createParentDirs(f);
	        
	        //文章内容图片的apache显示URL
	        urlPics.add(contentPicURL + contentPicFolder + fileName);
        	//保存缩小后的图片
        	try (BufferedOutputStream bos = new BufferedOutputStream(
        			new FileOutputStream(tempPicPath))) {
				bos.write(decode);
			}
        }
        
        //替换图片为保存到Apache指定路径后的url
        urlPics.forEach(s->{
        	String replace = "<img src=\"" + s + "\">";
        	String con = article.getContent().replaceFirst("<img src=\"data:image.+?\">", replace);
        	article.setContent(con);
        });
        
        //过滤敏感词汇
        forbiddenWordRepository.findAllCached().forEach(f -> {
        	String repContent = article.getContent().replaceAll(f.getWord(), "**");
        	article.setContent(repContent);
        });
        
        // 文章添加标签
        List<Tag> tags = tagRepository.findAllCached();
        // 文章内容过滤后包含的标签
        Set<Tag> filtertags = tags.stream()
				        	  // 过滤文章内容是否包含标签
				        	  .filter(t -> article.getContent().contains(t.getName()))
				        	  .collect(Collectors.toSet());

        article.setTags(filtertags);
        
        log.debug("文章内容过滤后包含的标签 : {}", filtertags);
         
        Article result = articleRepository.save(article);
        articleSearchRepository.save(result);
        return result;
    }

    /**
     *  get all the articles.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Article> findAll(Pageable pageable) {
        log.debug("Request to get all Articles");
        Page<Article> result = articleRepository.findAll(pageable); 
        return result;
    }

    /**
     *  get one article by id.
     *  @return the entity
     *  
     *  这里使用spring的缓存注解@Cacheable来替代模板生成静态的html
     *  
     *  注意：需要注意的是当一个支持缓存的方法在对象内部被调用时是不会触发缓存功能的。
     *  键的生成策略
     *  		键的生成策略有两种，一种是默认策略，一种是自定义策略。
     *  		默认策略
     *  			默认的key生成策略是通过KeyGenerator生成的，其默认策略如下：
     *  			1. 如果方法没有参数，则使用0作为key。
     *  			2. 如果只有一个参数的话则使用该参数作为key。
     *  			3. 如果参数多余一个的话则使用所有参数的hashCode作为key。
     *  
     */
    @Cacheable("articleDetail")
    public ArticleDTO findOne(Long id) {
    	log.debug("查看缓存是否执行文章ID:" + id);
        log.debug("Request to get Article : {}", id);
        Article article = articleRepository.findByIdAndPublishedTrue(id);
        // 可能有文章id不存在或是该文章不允许发布
        if (article == null) {
        	return null;
        }
        
        // 取得该用户全部文章数
        Integer countArticleUid = articleRepository.findByCountArticleIsUid(article.getUser().getId());
 		// 取得该用户全部评论数
        Integer countArticleReplyUid = articleReplyRepository.findByCountArticleReplyUid(article.getUser().getId());
        
        // 右边栏 热门文章
        List<Article> hotArticles = hotArticleDetail();
        // 右边栏 热门文章 不显示文章首图图片，不需要传输FirstImg数据占用带宽
 		List<Article> transHotData = new ArrayList<>();
 		for (Article hot : hotArticles) {
 			Article a = new Article();
 			a.setId(hot.getId());
 			a.setTitle(hot.getTitle());
 			a.setMinImgURL(hot.getMinImgURL());
 			transHotData.add(a);
 		}
        // 查询文章对应的全部评论
//        List<ArticleReply> articleReplies = articleReplyRepository.findReplyByArticleID(id, true);
//        List<ArticleReplyDTO> articleRepliesDTO = transArticleReplyDTO(articleReplies);
        
        // 默认第一页显示5条评论
        Pageable pageable = new PageRequest(0, 5);
        Page<ArticleReplyDTO> articleRepliesDTO = findPageArticleReply(pageable, id);
        
        // 取得文章的收藏数
		Integer countArticleSaveAid = articleFavoriteRepository.findByCountArticleFavoriteAid(id);
		// 当前登录用户是否收藏过该文章,默认否
		boolean isArticleFavoriteCurrentUser = false;
		// 当前用户已经收藏过该文章的收藏Id
		Long favoriteId = null;
		// 当前用户是否登录
		if (SecurityUtils.isAuthenticated()) {
			// 当前登录用是否收藏过该文章
			User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();
			ArticleFavorite f = articleFavoriteRepository.findByUserIdAndArticleId(user.getId(), id);
			// 当前用户已经收藏过该文章
			if (f != null) {
				isArticleFavoriteCurrentUser = true;
				favoriteId = f.getId();
			}
		}
		
		Integer countArticleReplyAid = 0;
		return new ArticleDTO(article, countArticleUid, 
				countArticleReplyUid, countArticleSaveAid, 
				countArticleReplyAid, transHotData,
				articleRepliesDTO, isArticleFavoriteCurrentUser,
				favoriteId);
    }
    
    /**
     * 更新文章的浏览量
     * 单独调用的原因是在文章缓存前更新文章浏览量
     * @param id 文章ID
     */
    public void updatePageView(Long id) {
    	 Article article = articleRepository.findByIdAndPublishedTrue(id);
         // 可能有文章id不存在或是该文章不允许发布
         if (article == null) {
         	return;
         }
         
         // 文章的浏览数量加1
         articleRepository.updatePageView(article.getId());
    }
    
    /**
     * 查询文章对应的分页评论
     * 带查询条件的分页
     * @param pageable
     * @param id 文章ID
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public Page<ArticleReplyDTO> findPageArticleReply(Pageable pageable, Long id) {
		// 使用entityManager查询分页数据
		// 原因是entityManager可以用.createQuery(sql)方法,可以用迫切左外连接的方式取得数据
		Query query = entityManager.createQuery("select a from ArticleReply a left join fetch a.replyer where a.article.id = ?1 and published = ?2 order by a.createdDate asc");
		// 设置查询参数
		query.setParameter(1, id);
		query.setParameter(2, true);
		// 总记录数
		int totalElements = query.getResultList().size();
		log.debug("Request to findPageArticleReply totalElements : {} ", totalElements);
		// 取得分页数据
		List articleReplies = query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
	    	 .setMaxResults(pageable.getPageSize())
	    	 .getResultList();
		
		log.debug("Request to findPageArticleReply pageable : {} id : {}", pageable, id);
		log.debug("Request to findPageArticleReply List<ArticleReply> size : {}", articleReplies.size());
		
		List<ArticleReplyDTO> alist = transArticleReplyDTO(articleReplies);
		// 从新构造page对象
		Page<ArticleReplyDTO> page = new PageImpl(alist, pageable, totalElements); 
		return page;
    }

    // 文章评论转换DTO
	private List<ArticleReplyDTO> transArticleReplyDTO(List<ArticleReply> articleReplies) {
        List<ArticleReplyDTO> articleRepliesDTO = articleReplies.stream()
        			  .map(result -> new ArticleReplyDTO(result, 
        					  // result.getCreatedDate().toInstant().toEpochMilli() 取得毫秒
        					  PrettyTimeUtils.timeAgo(System.currentTimeMillis() - result.getCreatedDate().toInstant().toEpochMilli())))
        			  .collect(Collectors.toList());
		return articleRepliesDTO;
	}

    /*
     * 右边栏 热门文章
     * 共通处理   需要通过模板生成静态的html(暂不使用,会有显示图片的限制)
     * 用page的方式取出TOP N 的数据,原因是jpql不支持limit
     * 这里使用spring的缓存注解@Cacheable来替代模板生成静态的html
     */
    public List<Article> hotArticleDetail() {
		// 默认显示的数量
        int pageSize = 5;
        Order order = new Order(Direction.DESC, "pageView");
        Sort sort = new Sort(order);
        PageRequest pageable = new PageRequest(0, pageSize, sort);
        Page<Article> page = articleRepository.findAll(pageable);
        // 当前页面的 List
    	List<Article> hotArticles = page.getContent();
		return hotArticles;
	}
    
    /**
     * 文章详细页面，新建文章评论
     * @return
     */
    // 清空articleDetail 对应的文章ID 缓存
    @CacheEvict(value = "articleDetail", key = "#articleReply.getArticle().getId()")
    public Page<ArticleReplyDTO> createArticleReply(ArticleReply articleReply) {
    	log.debug("文章详细页面，新建文章评论 : {}", articleReply);
    	// 父评论人
    	articleReply.setParentReplyer(null);
    	// 不需要从页面传参数显示评论有问题,重新查找User
    	User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();
    	articleReply.setReplyer(user);
    	
    	articleReply.setCreatedDate(ZonedDateTime.now());
    	
    	// 过滤敏感词汇
        forbiddenWordRepository.findAllCached().forEach(f -> {
        	String repContent = articleReply.getContent().replaceAll(f.getWord(), "**");
        	articleReply.setContent(repContent);
        });
    	// 保存评论
    	articleReplyRepository.save(articleReply);
    	
    	// 查询文章对应的全部评论
//    	List<ArticleReply> articleReplies =  articleReplyRepository.findReplyByArticleID(articleReply.getArticle().getId(), true);
    	
//    	return transArticleReplyDTO(articleReplies);
    	
    	// 默认第一页显示5条评论
        Pageable pageable = new PageRequest(0, 5);
        Page<ArticleReplyDTO> articleRepliesDTO = findPageArticleReply(pageable, articleReply.getArticle().getId());
    	
        return articleRepliesDTO;
    }
    
    /*
     * spring缓存 右边栏 热门文章
     * 使用spring缓存名叫 hotArticle  
     * 
     * 注意：需要注意的是当一个支持缓存的方法在对象内部被调用时是不会触发缓存功能的。
     */
   /* @Cacheable("hotArticle")
	public List<Article> getHotArticleDetail() {
    	return hotArticleDetail("缓存数据");
	}*/

    /* 
     * 更新 右边栏 热门文章 缓存数据
     * @CachePut 注释，这个注释可以确保方法被执行，
     * 同时方法的返回值也被记录到缓存中，实现缓存与数据库的同步更新。
     * 
     * 注意：需要注意的是当一个支持缓存的方法在对象内部被调用时是不会触发缓存功能的。
     */
   /* @CachePut("hotArticle")
    public List<Article> updateHotArticleDetail() {
    	return hotArticleDetail("更新  缓存数据");
    }*/
    
    /**
     * 定时 更新 右边栏 热门文章 缓存数据
     * 执行时间是在每天的凌晨1点15分 at 01:15 (am)
     */
//    @Scheduled(cron = "0 15 1 * * ?")
//    @Scheduled(cron = "0/20 * * * * ?")
   /* public void updateHotArticleTimer() {
    	updateHotArticleDetail();
    }*/
    
    /**
     *  delete the  article by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete Article : {}", id);
        articleRepository.delete(id);
        articleSearchRepository.delete(id);
    }

    /**
     * search for the article corresponding
     * to the query.
     */
    @Transactional(readOnly = true) 
    public List<Article> search(String query) {
        
        log.debug("REST request to search Articles for query {}", query);
        return StreamSupport
            .stream(articleSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

    /**
     * 设置文章禁止发布的状态
     * @param integer
     */
    // 清空articleDetail 缓存
    @CacheEvict(value="articleDetail",key="#id")
	public void updatePublished(Long id) {
		articleRepository.updatePublished(id);
	}
    
    /**
     * 文章详细页面  右边栏 热门文章
     * 通过模板生成静态的html(局部静态化)
     * 执行时间是在每天的凌晨1点15分 at 01:15 (am)
     * 暂时不使用，原因是文章的首图是数据库存储的生成静态html时不能用angular
     */
   /* @Scheduled(cron = "0 15 1 * * ?")
    public void makeFileHotArticle() {
		String time = ZonedDateTime.now()
				.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		log.debug("执行文章详细右边栏 热门文章生成静态html文件，时间是：{}", time);
		
		// 用page的方式取出TOP N 的数据,原因是jpql不支持limit
        int pageSize = 2;
        Order order = new Order(Direction.DESC, "pageView");
        Sort sort = new Sort(order);
        PageRequest pageable = new PageRequest(0, pageSize, sort);
        Page<Article> page = articleRepository.findAll(pageable);
        // 当前页面的 List
        List<Article> articles = page.getContent();

        Context context = new Context();
        context.setVariable("articles", articles);
        // baseURL 正式环境时需要替换,作为模板使用
        String baseURL = "http://127.0.0.1:8080";
        context.setVariable("baseUrl", baseURL);
        String content = templateEngine.process("article/hotArticleDetail", context);
		System.out.println("----->"+content);
    }*/
}
