package com.app.finder.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.app.finder.domain.ArticleReply;
import com.app.finder.repository.ArticleReplyRepository;
import com.app.finder.web.rest.util.HeaderUtil;
import com.app.finder.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing ArticleReply.
 */
@RestController
@RequestMapping("/api")
public class ArticleReplyResource {

    private final Logger log = LoggerFactory.getLogger(ArticleReplyResource.class);
        
    @Inject
    private ArticleReplyRepository articleReplyRepository;

    
    /**
     * POST  /articleReplys -> Create a new articleReply.
     */
    @RequestMapping(value = "/articleReplys",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ArticleReply> createArticleReply(@Valid @RequestBody ArticleReply articleReply) throws URISyntaxException {
        log.debug("REST request to save ArticleReply : {}", articleReply);
        if (articleReply.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("articleReply", "idexists", "A new articleReply cannot already have an ID")).body(null);
        }
        ArticleReply result = articleReplyRepository.save(articleReply);
        return ResponseEntity.created(new URI("/api/articleReplys/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("articleReply", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /articleReplys -> Updates an existing articleReply.
     */
    @RequestMapping(value = "/articleReplys",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ArticleReply> updateArticleReply(@Valid @RequestBody ArticleReply articleReply) throws URISyntaxException {
        log.debug("REST request to update ArticleReply : {}", articleReply);
        if (articleReply.getId() == null) {
            return createArticleReply(articleReply);
        }
        ArticleReply result = articleReplyRepository.save(articleReply);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("articleReply", articleReply.getId().toString()))
            .body(result);
    }

    /**
     * GET  /articleReplys -> get all the articleReplys.
     */
    @RequestMapping(value = "/articleReplys",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ArticleReply>> getAllArticleReplys(Pageable pageable, @RequestParam(required = false) String filter)
        throws URISyntaxException {
        if ("replyer-is-null".equals(filter)) {
            log.debug("REST request to get all ArticleReplys where replyer is null");
            return new ResponseEntity<>(StreamSupport
                .stream(articleReplyRepository.findAll().spliterator(), false)
                .filter(articleReply -> articleReply.getReplyer() == null)
                .collect(Collectors.toList()), HttpStatus.OK);
        }
        if ("parentreplyer-is-null".equals(filter)) {
            log.debug("REST request to get all ArticleReplys where parentReplyer is null");
            return new ResponseEntity<>(StreamSupport
                .stream(articleReplyRepository.findAll().spliterator(), false)
                .filter(articleReply -> articleReply.getParentReplyer() == null)
                .collect(Collectors.toList()), HttpStatus.OK);
        }
        log.debug("REST request to get a page of ArticleReplys");
        Page<ArticleReply> page = articleReplyRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/articleReplys");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /articleReplys/:id -> get the "id" articleReply.
     */
    @RequestMapping(value = "/articleReplys/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ArticleReply> getArticleReply(@PathVariable Long id) {
        log.debug("REST request to get ArticleReply : {}", id);
        ArticleReply articleReply = articleReplyRepository.findOne(id);
        return Optional.ofNullable(articleReply)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /articleReplys/:id -> delete the "id" articleReply.
     */
    @RequestMapping(value = "/articleReplys/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteArticleReply(@PathVariable Long id) {
        log.debug("REST request to delete ArticleReply : {}", id);
        articleReplyRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("articleReply", id.toString())).build();
    }

}
