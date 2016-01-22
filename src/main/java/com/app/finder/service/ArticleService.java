package com.app.finder.service;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.spring4.SpringTemplateEngine;

import com.app.finder.domain.Article;
import com.app.finder.domain.User;
import com.app.finder.repository.ArticleRepository;
import com.app.finder.repository.ForbiddenWordRepository;
import com.app.finder.repository.UserRepository;
import com.app.finder.repository.search.ArticleSearchRepository;
import com.app.finder.security.SecurityUtils;
import com.app.finder.web.rest.dto.ArticleDTO;
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
    private SpringTemplateEngine templateEngine;
    
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
        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();
        User u = new User();
        u.setId(user.getId());
        article.setUser(user);
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
	        //TODO 正式系统需要改路径
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
	        //TODO 正式系统需要改URL地址
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
	        //TODO 正式系统需要改路径
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
	        //TODO 正式系统需要改URL地址
	        urlPics.add("http://localhost:8089/" + fileName);
        	//保存缩小后的图片
        	try (BufferedOutputStream bos = new BufferedOutputStream(
        			new FileOutputStream(savePicPath))) {
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
     */
    public ArticleDTO findOne(Long id) {
        log.debug("Request to get Article : {}", id);
        Article article = articleRepository.findOne(id);
        //可能有id不存在的情况
        if (article == null) {
        	return null;
        }
        
        //取得该用户全部文章数
        int countArticleUid = articleRepository.findByCountArticleIsUid(article.getUser().getId());
        //文章的浏览数量加1
        articleRepository.updatePageView(article.getId());
        // 右边栏 热门文章
        List<Article> hotArticles = getHotArticleDetail();
    	 
        
        Integer countArticleReplyUid = 0;
		Integer countArticleSaveAid = 0;
		Integer countArticleReplyAid = 0;
		return new ArticleDTO(article, countArticleUid, 
				countArticleReplyUid, countArticleSaveAid, 
				countArticleReplyAid, hotArticles);
    }

    /*
     * 右边栏 热门文章
     * 共通处理   需要通过模板生成静态的html(暂不使用,会有显示图片的限制)
     * 用page的方式取出TOP N 的数据,原因是jpql不支持limit
     * 这里使用spring的缓存注解@Cacheable来替代模板生成静态的html
     */
    public List<Article> hotArticleDetail(String opr) {
    	log.debug("右边栏 热门文章 缓存的状态是 : {}", opr);
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
    
    /*
     * spring缓存 右边栏 热门文章
     * 使用spring缓存名叫 hotArticle  
     * 
     */
    @Cacheable("hotArticle")
	public List<Article> getHotArticleDetail() {
    	return hotArticleDetail("缓存数据");
	}

    /* 
     * 更新 右边栏 热门文章 缓存数据
     * @CachePut 注释，这个注释可以确保方法被执行，
     * 同时方法的返回值也被记录到缓存中，实现缓存与数据库的同步更新。
     */
    @CachePut("hotArticle")
    public List<Article> updateHotArticleDetail() {
    	return hotArticleDetail("更新  缓存数据");
    }
    
    /**
     * 定时 更新 右边栏 热门文章 缓存数据
     * 执行时间是在每天的凌晨1点15分 at 01:15 (am)
     */
//    @Scheduled(cron = "0 15 1 * * ?")
//    @Scheduled(cron = "0/20 * * * * ?")
    public void updateHotArticleTimer() {
//    	updateHotArticleDetail();
    }
    
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
        // TODO baseURL 正式环境时需要替换,作为模板使用
        String baseURL = "http://127.0.0.1:8080";
        context.setVariable("baseUrl", baseURL);
        String content = templateEngine.process("article/hotArticleDetail", context);
		System.out.println("----->"+content);
    }*/
}
